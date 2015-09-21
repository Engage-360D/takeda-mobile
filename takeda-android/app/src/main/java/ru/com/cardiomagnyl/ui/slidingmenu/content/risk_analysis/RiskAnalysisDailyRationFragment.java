package ru.com.cardiomagnyl.ui.slidingmenu.content.risk_analysis;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import ru.com.cardiomagnyl.api.Status;
import ru.com.cardiomagnyl.app.R;
import ru.com.cardiomagnyl.application.AppState;
import ru.com.cardiomagnyl.application.Constants;
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
        parentView = inflater.inflate(R.layout.fragment_analysis_daily_ration, null);
        initFragment(parentView);
        return parentView;
    }

    @Override
    public void initTopBar(ViewGroup viewGroupTopBar) {
        boolean userIsDoctor = AppState.getInsnatce().getUser().isDoctor();
        initTopBarMenuBellCabinet(viewGroupTopBar, userIsDoctor, userIsDoctor, userIsDoctor);
    }

    private void initFragment(View view) {
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

        Bundle bundle = getArguments();
        final TestSource testSource = bundle.getParcelable(Constants.TEST_SOURCE);
        layoutBottomInside.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                tryGetResults(testSource);
            }
        });

        hideAspirin(view);
    }

    private void hideAspirin(View view) {
        View relativeLayoutAspirin = view.findViewById(R.id.relativeLayoutAspirin);
        ToggleButton toggleButtonNoAspirin = (ToggleButton) view.findViewById(R.id.toggleButtonNoAspirin);

        relativeLayoutAspirin.setVisibility(View.GONE);
        toggleButtonNoAspirin.setChecked(true);
    }

    private void tryGetResults(TestSource testSource) {
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
        } catch (Exception e) { /*does nothing*/ }
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

        if (testResult == null) {
            responseError = (responseError == null || responseError.getError() == null) ?
                    new Response.Builder(new ru.com.cardiomagnyl.model.common.Error()).create() :
                    responseError;

            switch (responseError.getError().getCode()) {
                case Status.CONFLICT_ERROR:
                    Tools.showToast(getActivity(), R.string.error_tets_already_passed, Toast.LENGTH_LONG);
                    break;
                default:
                    Tools.showToast(getActivity(), R.string.error_occurred, Toast.LENGTH_LONG);
            }
        } else {
            AppState.getInsnatce().setTestResult(testResult);
        }

        // set up menu items according to testResult
        slidingMenuActivity.refreshMenuItems();

        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.IS_NOT_PASSED, testResult == null);
        RiskAnalysisResultsFragment riskAnalysisResultsFragment = new RiskAnalysisResultsFragment();
        riskAnalysisResultsFragment.setArguments(bundle);

        slidingMenuActivity.replaceContentOnTop(riskAnalysisResultsFragment, false);
    }

}
