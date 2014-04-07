package com.cardiomagnil.ui.slidingmenu;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.cardiomagnil.R;
import com.cardiomagnil.application.AppState;
import com.cardiomagnil.application.Tools;
import com.cardiomagnil.model.TestResult;

public class PatientDataFargment extends Fragment {
    private View parentView;
    private String mBirthDate = null;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.patient_data_frgment, null);

        initPatientDataFargment(parentView);

        return parentView;
    }

    private void initPatientDataFargment(View view) {
        ImageView imageViewBottomInsideLeft = (ImageView) view.findViewById(R.id.imageViewBottomInsideLeft);
        TextView textViewBottomInsideAction = (TextView) view.findViewById(R.id.textViewBottomInsideAction);
        ImageView imageViewBottomInsideRight = (ImageView) view.findViewById(R.id.imageViewBottomInsideRight);

        View layoutBottomInside = view.findViewById(R.id.layoutBottomInside);

        imageViewBottomInsideLeft.setVisibility(View.INVISIBLE);
        textViewBottomInsideAction.setText(this.getString(R.string.step_two_patient_history));
        imageViewBottomInsideRight.setVisibility(View.VISIBLE);

        layoutBottomInside.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                trySwitchNextFragment();
            }
        });

        initTextViewBirthDate();
    }

    private void trySwitchNextFragment() {
        TestResult testResult = new TestResult();
        pickTestResultFields(testResult);

        if (testResult.validate(TestResult.RESULT_GROUPS.first)) {
            AppState.getInstatce().setTestResult(testResult);
            Fragment patientHistoryFargment = new PatientHistoryFargment();
            switchFragment(patientHistoryFargment);
        } else {
            Toast.makeText(parentView.getContext(), parentView.getContext().getString(R.string.complete_all_fields), Toast.LENGTH_LONG).show();
        }
    }

    private void pickTestResultFields(TestResult testResult) {
        RadioButton radioButtonMale = (RadioButton) parentView.findViewById(R.id.radioButtonMale);
        // EditText editTextAge = (EditText) parentView.findViewById(R.id.editTextAge);
        EditText editTextHeigh = (EditText) parentView.findViewById(R.id.editTextHeigh);
        EditText editTextWeight = (EditText) parentView.findViewById(R.id.editTextWeight);
        EditText editTextCholesterol = (EditText) parentView.findViewById(R.id.editTextCholesterol);
        Switch switchSmoking = (Switch) parentView.findViewById(R.id.switchSmoking);

        try {
            testResult.setSex(radioButtonMale.isChecked() ? "male" : "female");
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
        if (getActivity() == null)
            return;

        if (getActivity() instanceof SlidingMenuActivity) {
            SlidingMenuActivity mainActivity = (SlidingMenuActivity) getActivity();
            mainActivity.switchContent(fragment);
        }
    }

    private void initTextViewBirthDate() {
        TextView editTextAge = (TextView) parentView.findViewById(R.id.editTextAge);

        editTextAge.setOnTouchListener(new OnTouchListener() {
            private boolean datePickerDialogIsStarted = false;

            @Override
            public boolean onTouch(View paramView, MotionEvent paramMotionEvent) {
                if (!datePickerDialogIsStarted) {
                    Calendar calendar = Calendar.getInstance();

                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH);
                    int day = calendar.get(Calendar.DAY_OF_MONTH);

                    datePickerDialogIsStarted = true;
                    DatePickerDialog dateDialog = new DatePickerDialog(getActivity(), datePickerListener, year, month, day);
                    dateDialog.show();

                    dateDialog.setOnDismissListener(new OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            datePickerDialogIsStarted = false;
                        }
                    });
                }

                return true;
            }
        });
    }

    public DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Calendar calendar = Calendar.getInstance();

            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            TextView editTextAge = (TextView) parentView.findViewById(R.id.editTextAge);
            mBirthDate = Tools.formatDate(calendar.getTime());
            editTextAge.setText(mBirthDate);
        }
    };
}
