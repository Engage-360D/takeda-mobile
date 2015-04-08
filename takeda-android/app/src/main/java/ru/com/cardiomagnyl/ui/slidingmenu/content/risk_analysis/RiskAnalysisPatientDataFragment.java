package ru.com.cardiomagnyl.ui.slidingmenu.content.risk_analysis;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
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
import ru.com.cardiomagnyl.model.test.TestResult;
import ru.com.cardiomagnyl.model.test.TestSource;
import ru.com.cardiomagnyl.model.user.User;
import ru.com.cardiomagnyl.ui.base.BaseRiskAnalysis;
import ru.com.cardiomagnyl.ui.slidingmenu.menu.SlidingMenuActivity;
import ru.com.cardiomagnyl.util.ProfileHelper;
import ru.com.cardiomagnyl.util.Tools;

public class RiskAnalysisPatientDataFragment extends BaseRiskAnalysis {
    private View parentView;
//    private String mBirthDate = null;

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

        RadioGroup radioGroupCholesterolDrugs = (RadioGroup) fragmentView.findViewById(R.id.radioGroupCholesterolDrugs);
        RadioGroup radioGroupSmoking = (RadioGroup) fragmentView.findViewById(R.id.radioGroupSmoke);
        final EditText editTextCholesterol = (EditText) parentView.findViewById(R.id.editTextCholesterol);
        CheckBox checkBoxCholesterolNotKnow = (CheckBox) parentView.findViewById(R.id.checkBoxCholesterolNotKnow);
//        final RelativeLayout relativeLayoutCholesterolDrugs = (RelativeLayout) view.findViewById(R.id.relativeLayoutCholesterolDrugs);
//        SeekBarWithValues seekBarWithValuesCholesterol = (SeekBarWithValues) view.findViewById(R.id.seekBarWithValuesCholesterol);
        ImageView imageViewBottomInsideLeft = (ImageView) fragmentView.findViewById(R.id.imageViewBottomInsideLeft);
        TextView textViewBottomInsideAction = (TextView) fragmentView.findViewById(R.id.textViewBottomInsideAction);
        ImageView imageViewBottomInsideRight = (ImageView) fragmentView.findViewById(R.id.imageViewBottomInsideRight);
        TextView textViewBirthDate = (TextView) parentView.findViewById(R.id.textViewBirthDate);
        View layoutBottomInside = fragmentView.findViewById(R.id.layoutBottomInside);

        radioGroupCholesterolDrugs.setOnCheckedChangeListener(Tools.ToggleListener);
        radioGroupSmoking.setOnCheckedChangeListener(Tools.ToggleListener);

        checkBoxCholesterolNotKnow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) editTextCholesterol.setText("");
                editTextCholesterol.setEnabled(!isChecked);
            }
        });

//        seekBarWithValuesCholesterol.setOnProgressChangedListener(new OnProgressChangedListener() {
//
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int actualProgress, boolean fromUser) {
//                if (actualProgress >= 5) {
//                    relativeLayoutCholesterolDrugs.setVisibility(View.VISIBLE);
//                } else {
//                    relativeLayoutCholesterolDrugs.setVisibility(View.GONE);
//                }
//            }
//
//        });

        imageViewBottomInsideLeft.setVisibility(View.INVISIBLE);
        textViewBottomInsideAction.setText(this.getString(R.string.step_two_patient_history));
        imageViewBottomInsideRight.setVisibility(View.VISIBLE);

        layoutBottomInside.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                trySwitchNextFragment();
            }
        });


        ProfileHelper.initTextViewBirthDate(textViewBirthDate);

        fillFields(fragmentView);
    }

    private void fillFields(final View fragmentView) {
        User user = AppState.getInsnatce().getUser();
        TestResult testResult = AppState.getInsnatce().getTestResult();


        EditText editTextHeigh = (EditText) fragmentView.findViewById(R.id.editTextHeigh);
        EditText editTextWeight = (EditText) fragmentView.findViewById(R.id.editTextWeight);

        TextView textViewBirthDate = (TextView) fragmentView.findViewById(R.id.textViewBirthDate);

        RadioButton radioButtonMale = (RadioButton) fragmentView.findViewById(R.id.radioButtonMale);
        RadioButton radioButtonFemale = (RadioButton) fragmentView.findViewById(R.id.radioButtonFemale);

        ToggleButton toggleButtonNotSmoke = (ToggleButton) fragmentView.findViewById(R.id.toggleButtonNotSmoke);
        ToggleButton toggleButtonSmoke = (ToggleButton) fragmentView.findViewById(R.id.toggleButtonSmoke);

        if (user.isDoctor() || testResult == null) {
            editTextHeigh.setText("170");
            editTextWeight.setText("70");

            String birthDate = Tools.fullDateToShortDate(user.getBirthday());
            textViewBirthDate.setText(birthDate);
            textViewBirthDate.setTag(Tools.calendarFromShortDate(birthDate));
        } else {
            editTextHeigh.setText(String.valueOf(testResult.getGrowth()));
            editTextWeight.setText(String.valueOf(testResult.getWeight()));

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
        EditText editTextHeight = (EditText) parentView.findViewById(R.id.editTextHeigh);
        EditText editTextWeight = (EditText) parentView.findViewById(R.id.editTextWeight);
        EditText editTextCholesterol = (EditText) parentView.findViewById(R.id.editTextCholesterol);
        CheckBox checkBoxCholesterolNotKnow = (CheckBox) parentView.findViewById(R.id.checkBoxCholesterolNotKnow);
        ToggleButton toggleButtonCholesterolDrugs = (ToggleButton) parentView.findViewById(R.id.toggleButtonCholesterolDrugs);
        ToggleButton toggleButtonSmoking = (ToggleButton) parentView.findViewById(R.id.toggleButtonSmoke);
        ToggleButton toggleButtonNotSmoking = (ToggleButton) parentView.findViewById(R.id.toggleButtonNotSmoke);

        String resultString = "";

        try {
            String sex = radioButtonMale.isChecked() ? "male" : "female";
            Calendar birthDate = (Calendar) textViewBirthDate.getTag();
            Integer growth = editTextHeight.length() != 0 ? Integer.parseInt(String.valueOf(editTextHeight.getText().toString())) : null;
            Integer weight = editTextWeight.length() != 0 ? Integer.parseInt(String.valueOf(editTextWeight.getText().toString())) : null;
            Boolean cholesterolNotKnow = checkBoxCholesterolNotKnow.isChecked();
            Integer cholesterolLevel = editTextCholesterol.length() != 0 && !cholesterolNotKnow ? Integer.parseInt(String.valueOf(editTextCholesterol.getText().toString())) : null;

            Boolean cholesterolDrugs = toggleButtonCholesterolDrugs.isChecked();
            Boolean smoking = toggleButtonSmoking.isChecked() || toggleButtonNotSmoking.isChecked() ? toggleButtonSmoking.isChecked() : null;

            if (birthDate == null) {
                resultString += parentView.getContext().getString(R.string.error_test_age);
            }

            if (growth < 30 || growth > 300) {
                resultString += resultString.isEmpty() ? "" : "\n";
                resultString += parentView.getContext().getString(R.string.error_test_growth);
            }

            if (weight < 30 || weight > 700) {
                resultString += resultString.isEmpty() ? "" : "\n";
                resultString += parentView.getContext().getString(R.string.error_test_weight);
            }

            if (!cholesterolNotKnow && (cholesterolLevel == null || (cholesterolLevel < 3 || cholesterolLevel > 9))) {
                resultString += resultString.isEmpty() ? "" : "\n";
                resultString += parentView.getContext().getString(R.string.error_test_cholesterol_level);
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
