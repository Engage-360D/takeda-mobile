package ru.com.cardiomagnil.ui.ca_content.RiskAnalysis;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import ru.com.cardiomagnil.app.R;
import ru.com.cardiomagnil.application.AppState;
import ru.com.cardiomagnil.ca_model.common.Ca_Response;
import ru.com.cardiomagnil.ca_model.test.Ca_TestResult;
import ru.com.cardiomagnil.ca_model.test.Ca_TestResultDao;
import ru.com.cardiomagnil.ca_model.test.Ca_TestSource;
import ru.com.cardiomagnil.ui.slidingmenu.SlidingMenuActivity;
import ru.com.cardiomagnil.util.CallbackOne;
import ru.com.cardiomagnil.util.Tools;

public class RiskAnalysisDailyRationFargment extends Fragment {
    private View parentView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_slidingmenu_test_daily_ration, null);

        initPatientDataFargment(parentView);

        return parentView;
    }

    private void initPatientDataFargment(View view) {
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
        Ca_TestSource testSource = AppState.getInstatce().getTestIncoming();
        pickTestIncomingFields(testSource);

        if (testSource.validate(Ca_TestSource.RESULT_GROUPS.third)) {
            tryGetTestResultHelper(testSource);
        } else {
            Toast toast = Toast.makeText(parentView.getContext(), parentView.getContext().getString(R.string.complete_all_fields), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER | Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    private void pickTestIncomingFields(Ca_TestSource testSource) {
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

    private void tryGetTestResultHelper(Ca_TestSource testSource) {
        SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
        slidingMenuActivity.showProgressDialog();

        Ca_TestResultDao.sendTestSource(
                testSource,
                AppState.getInstatce().getToken(),
                new CallbackOne<Ca_TestResult>() {
                    @Override
                    public void execute(Ca_TestResult testResult) {
                        handleTestResult(testResult, null);
                    }
                },
                new CallbackOne<Ca_Response>() {
                    @Override
                    public void execute(Ca_Response responseError) {
                        handleTestResult(null, responseError);
                    }
                }
        );
    }

    private void handleTestResult(Ca_TestResult testResult, Ca_Response responseError) {
        SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
        slidingMenuActivity.hideProgressDialog();

        if (testResult != null) {
            // TODO: show result
        } else if (responseError != null) {
            Toast.makeText(getActivity(), getActivity().getString(R.string.error_occurred), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getActivity(), getActivity().getString(R.string.error_occurred), Toast.LENGTH_LONG).show();
        }
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
