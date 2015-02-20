package ru.com.cardiomagnyl.ui.slidingmenu.content.risk_analysis;

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
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Calendar;

import ru.com.cardiomagnyl.app.R;
import ru.com.cardiomagnyl.application.AppState;
import ru.com.cardiomagnyl.application.Constants;
import ru.com.cardiomagnyl.model.test.TestSource;
import ru.com.cardiomagnyl.ui.base.BaseRiskAnalysis;
import ru.com.cardiomagnyl.ui.slidingmenu.menu.SlidingMenuActivity;
import ru.com.cardiomagnyl.util.Tools;

public class RiskAnalysisPatientDataFragment extends BaseRiskAnalysis {
    private View parentView;
    private String mBirthDate = null;

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

    private void initPatientDataFragment(View view) {
        initTabs(view, 0);

        RadioGroup radioGroupCholesterolDrugs = (RadioGroup) view.findViewById(R.id.radioGroupCholesterolDrugs);
        RadioGroup radioGroupSmoking = (RadioGroup) view.findViewById(R.id.radioGroupSmoke);
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
        TestSource testSource = new TestSource();
        String resultString = pickTestIncomingFields(testSource);

        if (!resultString.isEmpty()) {
            Tools.showToast(getActivity(), resultString, Toast.LENGTH_LONG);
            return;
        }

        if (testSource.validate(TestSource.RESULT_GROUPS.first)) {
            AppState.getInsnatce().setTestSource(testSource);
            Fragment patientHistoryFragment = new RiskAnalysisPatientHistoryFragment();
            switchFragment(patientHistoryFragment);
        } else {
            Tools.showToast(getActivity(), R.string.complete_all_fields, Toast.LENGTH_SHORT);
        }
    }

    private String pickTestIncomingFields(TestSource testSource) {
        RadioButton radioButtonMale = (RadioButton) parentView.findViewById(R.id.radioButtonMale);
        EditText editTextAge = (EditText) parentView.findViewById(R.id.editTextAge);
        EditText editTextHeight = (EditText) parentView.findViewById(R.id.editTextHeigh);
        EditText editTextWeight = (EditText) parentView.findViewById(R.id.editTextWeight);
        EditText editTextCholesterol = (EditText) parentView.findViewById(R.id.editTextCholesterol);
        ToggleButton toggleButtonCholesterolDrugs = (ToggleButton) parentView.findViewById(R.id.toggleButtonCholesterolDrugs);
        ToggleButton toggleButtonSmoking = (ToggleButton) parentView.findViewById(R.id.toggleButtonSmoke);
        ToggleButton toggleButtonNotSmoking = (ToggleButton) parentView.findViewById(R.id.toggleButtonNotSmoke);

        String resultString = "";

        try {
            String sex = radioButtonMale.isChecked() ? "male" : "female";
            Integer age = editTextAge.length() != 0 ? Integer.parseInt(String.valueOf(editTextAge.getText().toString())) : null;
            Integer growth = editTextHeight.length() != 0 ? Integer.parseInt(String.valueOf(editTextHeight.getText().toString())) : null;
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

            if (years > Constants.AGE_LIMIT) {
                TextView editTextAge = (TextView) parentView.findViewById(R.id.editTextAge);
                mBirthDate = Tools.formatFullDate(calendar.getTime());
                editTextAge.setText(String.valueOf(years));
            } else {
                Tools.showToast(getActivity(), R.string.error_birth_date, Toast.LENGTH_LONG);
            }
        }
    };
}
