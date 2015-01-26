package ru.com.cardiomagnil.ui.ca_content.RiskAnalysis;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
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
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Calendar;

import ru.com.cardiomagnil.app.R;
import ru.com.cardiomagnil.application.AppState;
import ru.com.cardiomagnil.ca_model.test.Ca_TestSource;
import ru.com.cardiomagnil.ui.ca_base.Ca_BaseItemFragment;
import ru.com.cardiomagnil.ui.slidingmenu.SlidingMenuActivity;
import ru.com.cardiomagnil.util.Tools;

public class RiskAnalysisPatientDataFargment extends Ca_BaseItemFragment {
    private View parentView;
    private String mBirthDate = null;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_slidingmenu_test_patient_data, null);

        initPatientDataFargment(parentView);

        return parentView;
    }

    @Override
    public void initTopBar(ViewGroup viewGroupTopBar) {
    }

    private void initPatientDataFargment(View view) {
        RadioGroup radioGroupCholesterolDrugs = (RadioGroup) view.findViewById(R.id.radioGroupCholesterolDrugs);
        RadioGroup radioGroupSmoking = (RadioGroup) view.findViewById(R.id.radioGroupSmoking);
//        final RelativeLayout relativeLayoutCholesterolDrugs = (RelativeLayout) view.findViewById(R.id.relativeLayoutCholesterolDrugs);
//        SeekBarWithValues seekBarWithValuesCholesterol = (SeekBarWithValues) view.findViewById(R.id.seekBarWithValuesCholesterol);
        ImageView imageViewBottomInsideLeft = (ImageView) view.findViewById(R.id.imageViewBottomInsideLeft);
        TextView textViewBottomInsideAction = (TextView) view.findViewById(R.id.textViewBottomInsideAction);
        ImageView imageViewBottomInsideRight = (ImageView) view.findViewById(R.id.imageViewBottomInsideRight);
        View layoutBottomInside = view.findViewById(R.id.layoutBottomInside);

        radioGroupCholesterolDrugs.setOnCheckedChangeListener(Tools.ToggleListener);
        radioGroupSmoking.setOnCheckedChangeListener(Tools.ToggleListener);

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

        initTextViewBirthDate();
    }

    private void trySwitchNextFragment() {
        Ca_TestSource testSource = new Ca_TestSource();
        String resultString = pickTestIncomingFields(testSource);

        if (!resultString.isEmpty()) {
            Toast toast = Toast.makeText(parentView.getContext(), resultString, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER | Gravity.CENTER, 0, 0);
            toast.show();
            return;
        }

        if (testSource.validate(Ca_TestSource.RESULT_GROUPS.first)) {
            AppState.getInstatce().setTestSource(testSource);
            Fragment patientHistoryFargment = new RiskAnalysisPatientHistoryFargment();
            switchFragment(patientHistoryFargment);
        } else {
            Toast toast = Toast.makeText(parentView.getContext(), parentView.getContext().getString(R.string.complete_all_fields), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER | Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    private String pickTestIncomingFields(Ca_TestSource testSource) {
        RadioButton radioButtonMale = (RadioButton) parentView.findViewById(R.id.radioButtonMale);
        EditText editTextAge = (EditText) parentView.findViewById(R.id.editTextAge);
        EditText editTextHeigh = (EditText) parentView.findViewById(R.id.editTextHeigh);
        EditText editTextWeight = (EditText) parentView.findViewById(R.id.editTextWeight);
        EditText editTextCholesterol = (EditText) parentView.findViewById(R.id.editTextCholesterol);
        ToggleButton toggleButtonCholesterolDrugs = (ToggleButton) parentView.findViewById(R.id.toggleButtonCholesterolDrugs);
        ToggleButton toggleButtonSmoking = (ToggleButton) parentView.findViewById(R.id.toggleButtonSmoking);
        ToggleButton toggleButtonNotSmoking = (ToggleButton) parentView.findViewById(R.id.toggleButtonNotSmoking);

        String resultString = "";

        try {
            String sex = radioButtonMale.isChecked() ? "male" : "female";
            Integer age = editTextAge.length() != 0 ? Integer.parseInt(String.valueOf(editTextAge.getText().toString())) : null;
            Integer growth = editTextHeigh.length() != 0 ? Integer.parseInt(String.valueOf(editTextHeigh.getText().toString())) : null;
            Integer weight = editTextWeight.length() != 0 ? Integer.parseInt(String.valueOf(editTextWeight.getText().toString())) : null;
            Integer cholesterolLevel = editTextWeight.length() != 0 ? Integer.parseInt(String.valueOf(editTextCholesterol.getText().toString())) : null;
            Boolean cholesterolDrugs = toggleButtonCholesterolDrugs.isChecked();
            Boolean smoking = toggleButtonSmoking.isChecked() || toggleButtonNotSmoking.isChecked() ? toggleButtonSmoking.isChecked() : null;

            if (age < 22) {
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

            if (cholesterolLevel < 3 || cholesterolLevel > 9) {
                resultString += resultString.isEmpty() ? "" : "\n";
                resultString += parentView.getContext().getString(R.string.error_test_cholesterol_level);
            }

            testSource.setSex(sex);
            testSource.setBirthday(mBirthDate);
            testSource.setGrowth(growth);
            testSource.setWeight(weight);
            testSource.setCholesterolLevel(cholesterolLevel);
            testSource.setIsCholesterolDrugsConsumer(cholesterolDrugs);
            testSource.setIsSmoker(smoking);
        } catch (Exception e) {
            // do nothing
        }

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

    private void initTextViewBirthDate() {
        TextView editTextAge = (TextView) parentView.findViewById(R.id.editTextAge);

        editTextAge.setOnTouchListener(new OnTouchListener() {
            private boolean datePickerDialogIsStarted = false;

            @Override
            public boolean onTouch(View paramView, MotionEvent paramMotionEvent) {
                if (!datePickerDialogIsStarted) {
                    Calendar calendar = Calendar.getInstance();

                    int year = calendar.get(Calendar.YEAR) - 22;
                    int month = calendar.get(Calendar.MONTH);
                    int day = calendar.get(Calendar.DAY_OF_MONTH);

                    datePickerDialogIsStarted = true;
                    DatePickerDialog dateDialog = new DatePickerDialog(getActivity(), datePickerListener, year, month, day);
                    dateDialog.setTitle(parentView.getResources().getString(R.string.enter_birth_date));
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

            int currentYear = calendar.get(Calendar.YEAR);

            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            int years = currentYear - year;

            if (years > 21) {
                TextView editTextAge = (TextView) parentView.findViewById(R.id.editTextAge);
                mBirthDate = Tools.formatFullDate(calendar.getTime());
                editTextAge.setText(String.valueOf(years));
            } else {
                Toast toast = Toast.makeText(parentView.getContext(), parentView.getContext().getString(R.string.error_birth_date), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
    };
}
