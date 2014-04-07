package com.cardiomagnil.ui.start;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.cardiomagnil.R;
import com.cardiomagnil.application.AppState;
import com.cardiomagnil.application.Tools;
import com.cardiomagnil.model.User;

public class RegistrationFragment extends CustomFragment {
    private View parentView;
    private final int RUSSIA = 131;
    private String[] regionItems = null;
    private String mBirthDate = null;
    private String mCountry = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_registration, container, false);

        initRegistrationFragment();

        return parentView;
    }

    public void initParent() {
        View layoutTop = getActivity().findViewById(R.id.linearLayoutTopContent);
        TextView textViewHeader = (TextView) getActivity().findViewById(R.id.textViewHeader);

        ImageView imageViewBottomInsideLeft = (ImageView) getActivity().findViewById(R.id.imageViewBottomInsideLeft);
        TextView textViewBottomInsideAction = (TextView) getActivity().findViewById(R.id.textViewBottomInsideAction);
        ImageView imageViewBottomInsideRight = (ImageView) getActivity().findViewById(R.id.imageViewBottomInsideRight);

        ProgressBar progressBarBottomOutsideStartWork = (ProgressBar) getActivity().findViewById(R.id.progressBarBottomOutsideStartWork);
        TextView textViewBottomOutsideAction = (TextView) getActivity().findViewById(R.id.textViewBottomOutsideAction);

        layoutTop.setVisibility(View.VISIBLE);
        textViewHeader.setText(getActivity().getString(R.string.header_registration));

        imageViewBottomInsideLeft.setVisibility(View.INVISIBLE);
        textViewBottomInsideAction.setText(getActivity().getString(R.string.bottom_registration));
        imageViewBottomInsideRight.setVisibility(View.VISIBLE);

        progressBarBottomOutsideStartWork.setMax(3);
        progressBarBottomOutsideStartWork.setProgress(2);
        textViewBottomOutsideAction.setText(getActivity().getString(R.string.two_minutes));

    }

    private void initRegistrationFragment() {
        regionItems = getResources().getStringArray(R.array.region_items);
        mCountry = regionItems[RUSSIA];

        initRegistrationButton();
        initTextViewBirthDate();
        initSpinnerCountry();
        initSocials();
    }

    private void initRegistrationButton() {
        View layoutBottomInside = getActivity().findViewById(R.id.layoutBottomInside);
        layoutBottomInside.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View paramView) {
                tryRegistration();
            }
        });
    }

    private void tryRegistration() {
        User newUser = pickRegistrationFields();
        if (newUser.validate()) {
            AppState.getInstatce().setUser(newUser);

            StartActivity startActivity = (StartActivity)getActivity();
            startActivity.userRegistration();
        } else {
            Toast.makeText(parentView.getContext(), parentView.getContext().getString(R.string.complete_all_fields), Toast.LENGTH_LONG).show();
        }
    }

    private User pickRegistrationFields() {
        User newUser = new User();

        Switch switchIsADoctor = (Switch) getActivity().findViewById(R.id.switchIsADoctor);
        EditText editTextName = (EditText) getActivity().findViewById(R.id.editTextName);
        EditText editTextRegEmail = (EditText) getActivity().findViewById(R.id.editTextRegEmail);
        // Spinner spinnerCountry = (Spinner) getActivity().findViewById(R.id.spinnerCountry);
        // TextView textViewBirthDate = (TextView) getActivity().findViewById(R.id.textViewBirthDate);
        EditText editTextPasswordFirst = (EditText) getActivity().findViewById(R.id.editTextPasswordFirst);
        EditText editTextPasswordSecond = (EditText) getActivity().findViewById(R.id.editTextPasswordSecond);
        CheckBox checkBoxAgreeToProcessing = (CheckBox) getActivity().findViewById(R.id.checkBoxAgreeToProcessing);
        CheckBox checkBoxAgreeToReceive = (CheckBox) getActivity().findViewById(R.id.checkBoxAgreeToReceive);
        CheckBox checkBoxAgreeThatAdvises = (CheckBox) getActivity().findViewById(R.id.checkBoxAgreeThatAdvises);

        newUser.setDoctor(switchIsADoctor.isChecked());
        newUser.setFirstName(editTextName.length() != 0 ? editTextName.getText().toString() : null);
        newUser.setEmail(editTextRegEmail.length() != 0 ? editTextRegEmail.getText().toString() : null);
        newUser.setPlainPasswordFirst(editTextPasswordFirst.length() != 0 ? editTextPasswordFirst.getText().toString() : null);
        newUser.setPlainPasswordSecond(editTextPasswordSecond.length() != 0 ? editTextPasswordSecond.getText().toString() : null);
        newUser.setConfirmPersonalization(checkBoxAgreeToProcessing.isChecked());
        newUser.setConfirmInformation(checkBoxAgreeThatAdvises.isChecked());
        newUser.setConfirmSubscription(checkBoxAgreeToReceive.isChecked());
        newUser.setBirthday(mBirthDate);
        newUser.setRegionl(mCountry);

        return newUser;
    }

    private void initTextViewBirthDate() {
        TextView textViewBirthDate = (TextView) parentView.findViewById(R.id.textViewBirthDate);

        textViewBirthDate.setOnTouchListener(new OnTouchListener() {
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

    private void initSpinnerCountry() {
        Spinner spinnerCountry = (Spinner) parentView.findViewById(R.id.spinnerCountry);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, regionItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCountry.setAdapter(adapter);
        spinnerCountry.setSelection(RUSSIA);
        spinnerCountry.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mCountry = regionItems[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void initSocials() {
        View imageViewVK = parentView.findViewById(R.id.imageViewVK);
        View imageViewFB = parentView.findViewById(R.id.imageViewFB);
        View imageViewOK = parentView.findViewById(R.id.imageViewOK);

        imageViewVK.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View paramView) {
                tryRegistration();
            }
        });

        imageViewFB.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View paramView) {
                tryRegistration();
            }
        });

        imageViewOK.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View paramView) {
                tryRegistration();
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

            TextView textViewBirthDate = (TextView) parentView.findViewById(R.id.textViewBirthDate);
            mBirthDate = Tools.formatDate(calendar.getTime());
            textViewBirthDate.setText(mBirthDate);
        }
    };
}