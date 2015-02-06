package ru.com.cardiomagnyl.ui.slidingmenu.content.risk_analysis;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import ru.com.cardiomagnyl.app.R;
import ru.com.cardiomagnyl.application.AppState;
import ru.com.cardiomagnyl.model.common.Response;
import ru.com.cardiomagnyl.model.test.TestResult;
import ru.com.cardiomagnyl.model.test.TestResultDao;
import ru.com.cardiomagnyl.model.test.TestSource;
import ru.com.cardiomagnyl.ui.base.BaseRiskAnalysis;
import ru.com.cardiomagnyl.ui.slidingmenu.menu.SlidingMenuActivity;
import ru.com.cardiomagnyl.util.CallbackOne;
import ru.com.cardiomagnyl.util.Tools;

public class RiskAnalysisDailyRationFragment extends BaseRiskAnalysis {
    private View parentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_slidingmenu_test_daily_ration, null);

        initPatientDataFragment(parentView);

        return parentView;
    }

    @Override
    public void initTopBar(ViewGroup viewGroupTopBar) {
        initTopBarBellCabinet(viewGroupTopBar, false, false);
    }

    private void initPatientDataFragment(View view) {
        initTabs(view, 2);

        RadioGroup radioGroupExtraSalt = (RadioGroup) view.findViewById(R.id.radioGroupExtraSalt);
        RadioGroup radioGroupAspirin = (RadioGroup) view.findViewById(R.id.radioGroupAspirin);
        ImageView imageViewBottomInsideLeft = (ImageView) view.findViewById(R.id.imageViewBottomInsideLeft);
        TextView textViewBottomInsideAction = (TextView) view.findViewById(R.id.textViewBottomInsideAction);
        ImageView imageViewBottomInsideRight = (ImageView) view.findViewById(R.id.imageViewBottomInsideRight);
        View layoutBottomInside = view.findViewById(R.id.layoutBottomInside);

        radioGroupExtraSalt.setOnCheckedChangeListener(Tools.ToggleListener);
        radioGroupAspirin.setOnCheckedChangeListener(Tools.ToggleListener);
        imageViewBottomInsideLeft.setVisibility(View.INVISIBLE);
        textViewBottomInsideAction.setText(this.getString(R.string.get_results));
        imageViewBottomInsideRight.setVisibility(View.VISIBLE);

        layoutBottomInside.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                tryGetResults();
            }
        });
    }

    private void tryGetResults() {
        TestSource testSource = AppState.getInsnatce().getTestSource();
        pickTestIncomingFields(testSource);

        if (testSource.validate(TestSource.RESULT_GROUPS.third)) {
            tryGetTestResultHelper(testSource);
        } else {
            Tools.showToast(parentView.getContext(), R.string.complete_all_fields, Toast.LENGTH_SHORT);
        }
    }

    private void pickTestIncomingFields(TestSource testSource) {
        ToggleButton toggleButtonExtraSalt = (ToggleButton) parentView.findViewById(R.id.toggleButtonExtraSalt);
        ToggleButton toggleButtonNoExtraSalt = (ToggleButton) parentView.findViewById(R.id.toggleButtonNoExtraSalt);
        ToggleButton toggleButtonAspirin = (ToggleButton) parentView.findViewById(R.id.toggleButtonAspirin);
        ToggleButton toggleButtonNoAspirin = (ToggleButton) parentView.findViewById(R.id.toggleButtonNoAspirin);

        try {
            Boolean extraSalt = toggleButtonExtraSalt.isChecked() || toggleButtonNoExtraSalt.isChecked() ? toggleButtonExtraSalt.isChecked() : null;
            Boolean aspirin = toggleButtonAspirin.isChecked() || toggleButtonNoAspirin.isChecked() ? toggleButtonAspirin.isChecked() : null;

            testSource.setIsAddingExtraSalt(extraSalt);
            testSource.setIsAcetylsalicylicDrugsConsumer(aspirin);
        } catch (Exception e) {
            // do nothing
        }
    }

    private void tryGetTestResultHelper(TestSource testSource) {
        SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
        slidingMenuActivity.showProgressDialog();

        TestResultDao.sendTestSource(
                testSource,
                AppState.getInsnatce().getToken(),
                new CallbackOne<TestResult>() {
                    @Override
                    public void execute(TestResult testResult) {
                        handleTestResult(testResult, null);
                    }
                },
                new CallbackOne<Response>() {
                    @Override
                    public void execute(Response responseError) {
                        handleTestResult(null, responseError);
                    }
                }
        );
    }

    private void handleTestResult(TestResult testResult, Response responseError) {
        SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
        slidingMenuActivity.hideProgressDialog();

        if (testResult != null) {
            AppState.getInsnatce().setTestResult(testResult);
            slidingMenuActivity.unLockMenu();
        } else if (responseError != null) {
            // TODO: show message according to error
            Tools.showToast(getActivity(), R.string.error_occurred, Toast.LENGTH_LONG);
        } else {
            Tools.showToast(getActivity(), R.string.error_occurred, Toast.LENGTH_LONG);
        }

        slidingMenuActivity.replaceContentOnTop(new RiskAnalysisResultsFragment(), false);
    }

    // the meat of switching the above fragment
    private void switchFragment(Fragment fragment) {
        if (getActivity() == null)
            return;

        if (getActivity() instanceof SlidingMenuActivity) {
            SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
            slidingMenuActivity.replaceContentOnTop(fragment, false);
        }
    }
}
