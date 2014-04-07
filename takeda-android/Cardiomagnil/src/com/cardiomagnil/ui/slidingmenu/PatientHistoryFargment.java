package com.cardiomagnil.ui.slidingmenu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.cardiomagnil.R;
import com.cardiomagnil.application.AppState;
import com.cardiomagnil.model.TestResult;

public class PatientHistoryFargment extends Fragment {
    private View parentView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.patient_history_frgment, null);

        initPatientDataFargment(parentView);

        return parentView;
    }

    private void initPatientDataFargment(View view) {
        ImageView imageViewBottomInsideLeft = (ImageView) view.findViewById(R.id.imageViewBottomInsideLeft);
        TextView textViewBottomInsideAction = (TextView) view.findViewById(R.id.textViewBottomInsideAction);
        ImageView imageViewBottomInsideRight = (ImageView) view.findViewById(R.id.imageViewBottomInsideRight);
        View layoutBottomInside = view.findViewById(R.id.layoutBottomInside);

        imageViewBottomInsideLeft.setVisibility(View.INVISIBLE);
        textViewBottomInsideAction.setText(this.getString(R.string.step_three_daily_ration));
        imageViewBottomInsideRight.setVisibility(View.VISIBLE);

        layoutBottomInside.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                trySwitchNextFragment();
            }
        });
    }

    private void trySwitchNextFragment() {
        TestResult testResult = AppState.getInstatce().getTestResult();
        pickTestResultFields(testResult);

        if (testResult.validate(TestResult.RESULT_GROUPS.first)) {
            Fragment patientHistoryFargment = new PatientHistoryFargment();
            switchFragment(patientHistoryFargment);
        } else {
            Toast.makeText(parentView.getContext(), parentView.getContext().getString(R.string.complete_all_fields), Toast.LENGTH_LONG).show();
        }
    }

    private void pickTestResultFields(TestResult testResult) {
 //        switchDiabeticMedicines
 //        editTextArterialPressure
 //        switchHypertonicMedicines
 //        editTextAerobicActivity
 //        editTextAnaerobicActivity
 //        switchInfarctionStroke

        Switch switchDiabetes = (Switch) parentView.findViewById(R.id.switchDiabetes);
        Switch switchDiabeticMedicines = (Switch) parentView.findViewById(R.id.switchDiabeticMedicines);
        EditText editTextArterialPressure = (EditText) parentView.findViewById(R.id.editTextArterialPressure);
        Switch switchHypertonicMedicines = (Switch) parentView.findViewById(R.id.switchHypertonicMedicines);
        EditText editTextAerobicActivity = (EditText) parentView.findViewById(R.id.editTextAerobicActivity);
        EditText editTextAnaerobicActivity = (EditText) parentView.findViewById(R.id.editTextAnaerobicActivity);
        Switch switchInfarctionStroke = (Switch) parentView.findViewById(R.id.switchInfarctionStroke);


//        RadioButton radioButtonMale = (RadioButton) parentView.findViewById(R.id.radioButtonMale);
//        // EditText editTextAge = (EditText) parentView.findViewById(R.id.editTextAge);
//        EditText editTextHeigh = (EditText) parentView.findViewById(R.id.editTextHeigh);
//        EditText editTextWeight = (EditText) parentView.findViewById(R.id.editTextWeight);
//        EditText editTextCholesterol = (EditText) parentView.findViewById(R.id.editTextCholesterol);
//        Switch switchSmoking = (Switch) parentView.findViewById(R.id.switchSmoking);
//
        try {

            testResult.setDiabetes(switchDiabetes.isChecked());
            testResult.set(switchDiabeticMedicines.isChecked());

            ///
            testResult.setBirthday(mBirthDate);
            testResult.setGrowth(editTextHeigh.length() != 0 ? Integer.parseInt(String.valueOf(editTextHeigh.getText().toString())) : null);
            testResult.setWeight(editTextWeight.length() != 0 ? Integer.parseInt(String.valueOf(editTextWeight.getText().toString())) : null);
            testResult.setCholesterolLevel(editTextCholesterol.length() != 0 ? Integer.parseInt(String.valueOf(editTextCholesterol.getText().toString())) : null);
            testResult.setSmoking(switchSmoking.isChecked());
        } catch (Exception e) {
            // do nothing
        }
    }

    // the meat of switching the above fragment
    private void switchFragment(Fragment fragment) {
//        Fragment dailyRationFargment = new DailyRationFargment();
//        switchFragment(dailyRationFargment);

        if (getActivity() == null)
            return;

        if (getActivity() instanceof SlidingMenuActivity) {
            SlidingMenuActivity mainActivity = (SlidingMenuActivity) getActivity();
            mainActivity.switchContent(fragment);
        }
    }
}
