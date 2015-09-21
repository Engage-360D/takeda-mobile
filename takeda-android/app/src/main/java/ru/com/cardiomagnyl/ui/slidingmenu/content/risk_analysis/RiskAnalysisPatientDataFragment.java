package ru.com.cardiomagnyl.ui.slidingmenu.content.risk_analysis;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Calendar;

import ru.com.cardiomagnyl.app.R;
import ru.com.cardiomagnyl.application.AppState;
import ru.com.cardiomagnyl.application.Constants;
import ru.com.cardiomagnyl.application.Constants.CholesterolLevel;
import ru.com.cardiomagnyl.application.Constants.Growth;
import ru.com.cardiomagnyl.application.Constants.Weight;
import ru.com.cardiomagnyl.model.test.TestResult;
import ru.com.cardiomagnyl.model.test.TestSource;
import ru.com.cardiomagnyl.model.user.User;
import ru.com.cardiomagnyl.ui.base.BaseRiskAnalysis;
import ru.com.cardiomagnyl.ui.slidingmenu.menu.SlidingMenuActivity;
import ru.com.cardiomagnyl.util.ProfileHelper;
import ru.com.cardiomagnyl.util.Tools;

public class RiskAnalysisPatientDataFragment extends BaseRiskAnalysis {
    private View parentView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_analysis_patient_data, null);
        initPatientDataFragment(parentView);
        return parentView;
    }

    @Override
    public void initTopBar(ViewGroup viewGroupTopBar) {
        boolean userIsDoctor = AppState.getInsnatce().getUser().isDoctor();
        initTopBarMenuBellCabinet(viewGroupTopBar, userIsDoctor, userIsDoctor, userIsDoctor);
    }

    private void initPatientDataFragment(View fragmentView) {
        initTabs(fragmentView, 0);

        TextView textViewScoreProcam = (TextView) fragmentView.findViewById(R.id.textViewScoreProcam);
        RadioGroup radioGroupCholesterolDrugs = (RadioGroup) fragmentView.findViewById(R.id.radioGroupCholesterolDrugs);
        RadioGroup radioGroupSmoking = (RadioGroup) fragmentView.findViewById(R.id.radioGroupSmoke);
        ImageView imageViewBottomInsideLeft = (ImageView) fragmentView.findViewById(R.id.imageViewBottomInsideLeft);
        TextView textViewBottomInsideAction = (TextView) fragmentView.findViewById(R.id.textViewBottomInsideAction);
        ImageView imageViewBottomInsideRight = (ImageView) fragmentView.findViewById(R.id.imageViewBottomInsideRight);
        TextView textViewBirthDate = (TextView) parentView.findViewById(R.id.textViewBirthDate);
        TextView textViewHeigh = (TextView) parentView.findViewById(R.id.textViewHeigh);
        TextView textViewWeight = (TextView) fragmentView.findViewById(R.id.textViewWeight);
        final TextView textViewCholesterol = (TextView) parentView.findViewById(R.id.textViewCholesterol);
        CheckBox checkBoxCholesterolNotKnow = (CheckBox) parentView.findViewById(R.id.checkBoxCholesterolNotKnow);
        View layoutBottomInside = fragmentView.findViewById(R.id.layoutBottomInside);

        textViewScoreProcam.setText(Html.fromHtml(fragmentView.getContext().getString(R.string.score_procam)));
        textViewScoreProcam.setMovementMethod(LinkMovementMethod.getInstance());

        radioGroupCholesterolDrugs.setOnCheckedChangeListener(Tools.ToggleListener);
        radioGroupSmoking.setOnCheckedChangeListener(Tools.ToggleListener);

        imageViewBottomInsideLeft.setVisibility(View.INVISIBLE);
        textViewBottomInsideAction.setText(this.getString(R.string.step_two_patient_history));
        imageViewBottomInsideRight.setVisibility(View.VISIBLE);

        layoutBottomInside.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                trySwitchNextFragment();
            }
        });

        Context context = fragmentView.getContext();
        ProfileHelper.initTextViewBirthDate(textViewBirthDate);
        ProfileHelper.initTextViewWithNumberPicker(textViewHeigh, Growth.MIN, Growth.MAX, Growth.INIT, context.getString(R.string.data_height_hint));
        ProfileHelper.initTextViewWithNumberPicker(textViewWeight, Weight.MIN, Weight.MAX, Weight.INIT, context.getString(R.string.data_weight_hint));
        ProfileHelper.initTextViewWithNumberPicker(textViewCholesterol, CholesterolLevel.MIN, CholesterolLevel.MAX, CholesterolLevel.INIT, context.getString(R.string.data_cholesterol_hint));
        checkBoxCholesterolNotKnow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) textViewCholesterol.setText("");
                textViewCholesterol.setEnabled(!isChecked);
            }
        });

        fillFields(fragmentView);
    }

    private void fillFields(final View fragmentView) {
        User user = AppState.getInsnatce().getUser();
        TestResult testResult = AppState.getInsnatce().getTestResult();

        TextView textViewHeigh = (TextView) fragmentView.findViewById(R.id.textViewHeigh);
        TextView textViewWeight = (TextView) fragmentView.findViewById(R.id.textViewWeight);
        TextView textViewBirthDate = (TextView) fragmentView.findViewById(R.id.textViewBirthDate);

        RadioButton radioButtonMale = (RadioButton) fragmentView.findViewById(R.id.radioButtonMale);
        RadioButton radioButtonFemale = (RadioButton) fragmentView.findViewById(R.id.radioButtonFemale);

        ToggleButton toggleButtonNotSmoke = (ToggleButton) fragmentView.findViewById(R.id.toggleButtonNotSmoke);
        ToggleButton toggleButtonSmoke = (ToggleButton) fragmentView.findViewById(R.id.toggleButtonSmoke);

        Context context = fragmentView.getContext();
        if (user.isDoctor() || testResult == null) {
            textViewHeigh.setText(String.valueOf(Growth.INIT) + " " + context.getString(R.string.data_height_hint));
            textViewHeigh.setTag(Growth.INIT);

            textViewWeight.setText(String.valueOf(Weight.INIT) + " " + context.getString(R.string.data_weight_hint));
            textViewWeight.setTag(Weight.INIT);

            String birthDate = Tools.fullDateToShortDate(user.getBirthday());
            textViewBirthDate.setText(birthDate);
            textViewBirthDate.setTag(Tools.calendarFromShortDate(birthDate));
        } else {
            textViewHeigh.setText(String.valueOf(testResult.getGrowth()));
            textViewHeigh.setTag(String.valueOf(testResult.getGrowth()));

            textViewWeight.setText(String.valueOf(testResult.getWeight()));
            textViewWeight.setTag(String.valueOf(testResult.getWeight()));

            String birthDate = Tools.fullDateToShortDate(user.getBirthday());
            textViewBirthDate.setText(birthDate);
            textViewBirthDate.setTag(Tools.calendarFromShortDate(birthDate));

            if (testResult.isIsSmoker()) toggleButtonSmoke.setChecked(true);
            else toggleButtonNotSmoke.setChecked(true);

            if (testResult.getSex().equals("male")) radioButtonMale.setChecked(true);
            else radioButtonFemale.setChecked(true);
        }
    }

    private void trySwitchNextFragment() {
        TestSource testSource = new TestSource();
        String resultString = pickTestIncomingFields(testSource);

        if (!resultString.isEmpty()) {
            Tools.showToast(getActivity(), resultString, Toast.LENGTH_LONG);
            return;
        }

        if (testSource.validate(TestSource.RESULT_GROUPS.first)) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(Constants.TEST_SOURCE, testSource);

            Fragment patientHistoryFragment = new RiskAnalysisPatientHistoryFragment();
            patientHistoryFragment.setArguments(bundle);
            switchFragment(patientHistoryFragment);
        } else {
            Tools.showToast(getActivity(), R.string.complete_all_fields, Toast.LENGTH_SHORT);
        }
    }

    private String pickTestIncomingFields(TestSource testSource) {
        RadioButton radioButtonMale = (RadioButton) parentView.findViewById(R.id.radioButtonMale);
        TextView textViewBirthDate = (TextView) parentView.findViewById(R.id.textViewBirthDate);
        TextView textViewHeigh = (TextView) parentView.findViewById(R.id.textViewHeigh);
        TextView textViewWeight = (TextView) parentView.findViewById(R.id.textViewWeight);
        TextView textViewCholesterol = (TextView) parentView.findViewById(R.id.textViewCholesterol);
        CheckBox checkBoxCholesterolNotKnow = (CheckBox) parentView.findViewById(R.id.checkBoxCholesterolNotKnow);
        ToggleButton toggleButtonCholesterolDrugs = (ToggleButton) parentView.findViewById(R.id.toggleButtonCholesterolDrugs);
        ToggleButton toggleButtonSmoking = (ToggleButton) parentView.findViewById(R.id.toggleButtonSmoke);
        ToggleButton toggleButtonNotSmoking = (ToggleButton) parentView.findViewById(R.id.toggleButtonNotSmoke);

        String resultString = "";

        try {
            String sex = radioButtonMale.isChecked() ? "male" : "female";
            Calendar birthDate = (Calendar) textViewBirthDate.getTag();
            Integer growth = (Integer) textViewHeigh.getTag();
            Integer weight = (Integer) textViewWeight.getTag();
            Boolean cholesterolNotKnow = checkBoxCholesterolNotKnow.isChecked();
            Integer cholesterolLevel = textViewCholesterol.getTag() != null && !cholesterolNotKnow ?
                    (Integer) textViewCholesterol.getTag() : null;

            Boolean cholesterolDrugs = toggleButtonCholesterolDrugs.isChecked();
            Boolean smoking = toggleButtonSmoking.isChecked() || toggleButtonNotSmoking.isChecked() ? toggleButtonSmoking.isChecked() : null;

            if (birthDate == null) {
                resultString += parentView.getContext().getString(R.string.error_test_age);
            }

            if (growth == null || (growth < Constants.Growth.MIN || growth > Constants.Growth.MAX)) {
                resultString += resultString.isEmpty() ? "" : "\n";
                resultString += String.format(parentView.getContext().getString(R.string.error_test_growth), Constants.Growth.MIN, Constants.Growth.MAX);
            }

            if (weight == null || (weight < Weight.MIN || weight > Weight.MAX)) {
                resultString += resultString.isEmpty() ? "" : "\n";
                resultString += String.format(parentView.getContext().getString(R.string.error_test_weight), Weight.MIN, Weight.MAX);
            }

            if (!cholesterolNotKnow && (cholesterolLevel == null || (cholesterolLevel < CholesterolLevel.MIN || cholesterolLevel > CholesterolLevel.MAX))) {
                resultString += resultString.isEmpty() ? "" : "\n";
                resultString += String.format(parentView.getContext().getString(R.string.error_test_cholesterol_level), CholesterolLevel.MIN, CholesterolLevel.MAX);
            }

            testSource.setSex(sex);
            testSource.setBirthday(Tools.formatFullDate(birthDate.getTime()));
            testSource.setGrowth(growth);
            testSource.setWeight(weight);
            testSource.setCholesterolLevel(cholesterolLevel);
            testSource.setIsCholesterolDrugsConsumer(cholesterolDrugs);
            testSource.setIsSmoker(smoking);
        } catch (Exception e) { /*does nothing*/ }

        return resultString;
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
