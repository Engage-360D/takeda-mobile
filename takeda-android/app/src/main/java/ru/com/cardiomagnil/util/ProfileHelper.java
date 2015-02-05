package ru.com.cardiomagnil.util;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

import ru.com.cardiomagnil.app.R;
import ru.com.cardiomagnil.application.AppState;
import ru.com.cardiomagnil.application.CardiomagnilApplication;
import ru.com.cardiomagnil.application.Constants;
import ru.com.cardiomagnil.model.common.Response;
import ru.com.cardiomagnil.model.region.Region;
import ru.com.cardiomagnil.model.region.RegionDao;
import ru.com.cardiomagnil.model.user.User;
import ru.com.cardiomagnil.social.FbApi;
import ru.com.cardiomagnil.social.FbUser;
import ru.com.cardiomagnil.social.OkApi;
import ru.com.cardiomagnil.social.OkUser;
import ru.com.cardiomagnil.social.VkApi;
import ru.com.cardiomagnil.social.VkUser;
import ru.com.cardiomagnil.ui.base.BaseStartFragment;
import ru.com.cardiomagnil.ui.slidingmenu.content.RangeSpinnerAdapter;
import ru.com.cardiomagnil.ui.slidingmenu.content.personal_cabinet.Ca_CabinetDataFargment;
import ru.com.cardiomagnil.ui.start.CustomAuthorizationListener;
import ru.com.cardiomagnil.ui.start.CustomOnDateSetListener;
import ru.com.cardiomagnil.ui.start.RegionsSpinnerAdapter;
import ru.com.cardiomagnil.ui.start.RegistrationFragment;
import ru.com.cardiomagnil.ui.start.SignInWithSocialNetwork;

public final class ProfileHelper {
    private static final int[] mRequiredEditTextCommon = new int[]{
            R.id.editTextFirstName,
            R.id.editTextLastName,
            R.id.editTextRegEmail,
            R.id.editTextPasswordFirst,
            R.id.editTextPasswordSecond
    };

    private static final int[] mRequiredCheckBoxCommon = new int[]{
            R.id.checkBoxAgreeToProcessing,
            R.id.checkBoxAgreeThatAdvises
    };

    private static final int[] mRequiredEditTextCommonDoctor = new int[]{
            R.id.editTextSpecializationName,
            R.id.editTextSpecializationInstitutionName,
            R.id.editTextSpecializationInstitutionAddress,
            R.id.editTextSpecializationInstitutionPhone
    };

    public static void initRegistrationFragment(final View view, RegistrationFragment registrationFragment) {
        intiRadioGroupDoctor(view);
        initTextViewBirthDate(view);
        initSpinnerRegion(view);
        initSpinnerExperienceYears(view);
        initSpinnerGraduationDate(view);
        initSocials(view, registrationFragment);
    }

    public static void initCabinetDataFargment(final View view, Ca_CabinetDataFargment cabinetDataFargment) {
        initSpinnerExperienceYears(view);
        initSpinnerGraduationDate(view);
        // TODO: initSocials
        // initSocials(view, registrationFragment);
        fillFields(view);
    }

    private static void intiRadioGroupDoctor(final View view) {
        RadioButton radioButtonNotDoctor = (RadioButton) view.findViewById(R.id.radioButtonNotDoctor);
        RadioButton radioButtonDoctor = (RadioButton) view.findViewById(R.id.radioButtonDoctor);
        final TextView textViewIsDoctor = (TextView) view.findViewById(R.id.textViewIsDoctor);
        final View layoutSpecializationDetailsl = view.findViewById(R.id.layoutSpecializationDetailsl);

        radioButtonNotDoctor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                textViewIsDoctor.setError(null);
                layoutSpecializationDetailsl.setVisibility(!isChecked ? View.VISIBLE : View.GONE);
            }
        });

        radioButtonDoctor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                textViewIsDoctor.setError(null);
                layoutSpecializationDetailsl.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            }
        });
    }

    private static void initTextViewBirthDate(final View parentView) {
        final TextView textViewBirthDateValue = (TextView) parentView.findViewById(R.id.textViewBirthDateValue);
        final CustomOnDateSetListener customOnDateSetListener = new CustomOnDateSetListener();
        final Calendar calendar = Calendar.getInstance();
        final DatePickerDialog dateDialog = new DatePickerDialog(
                parentView.getContext(),
                customOnDateSetListener,
                calendar.get(Calendar.YEAR) - Constants.AGE_LIMIT - 1,
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        textViewBirthDateValue.setOnTouchListener(new View.OnTouchListener() {
            private boolean datePickerDialogIsStarted = false;

            @Override
            public boolean onTouch(View paramView, MotionEvent paramMotionEvent) {
                if (!datePickerDialogIsStarted) {
                    datePickerDialogIsStarted = true;

                    Calendar calendar = (Calendar) textViewBirthDateValue.getTag();
                    if (calendar != null) {
                        dateDialog.getDatePicker().updateDate(
                                calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH));
                    }

                    dateDialog.show();

                    dateDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            if (customOnDateSetListener.isRightAge(Constants.AGE_LIMIT)) {
                                textViewBirthDateValue.setTag(customOnDateSetListener.getCalendar());
                                textViewBirthDateValue.setText(Tools.formatShortDate(customOnDateSetListener.getCalendar().getTime()));
                            } else {
                                textViewBirthDateValue.setTag(null);
                                textViewBirthDateValue.setText(textViewBirthDateValue.getContext().getString(R.string.birth_date));
                                Tools.showToast(parentView.getContext(), R.string.error_birth_date, Toast.LENGTH_LONG);
                            }

                            datePickerDialogIsStarted = false;
                        }
                    });
                }

                return false;
            }
        });
    }

    private static void initSpinnerRegion(final View spinner) {
        RegionDao.getAll(
                new CallbackOne<List<Region>>() {
                    @Override
                    public void execute(List<Region> regionsList) {
                        initSpinnerRegionHelper(spinner, regionsList);
                    }
                },
                new CallbackOne<Response>() {
                    @Override
                    public void execute(Response responseError) {
                        Log.d(CardiomagnilApplication.getInstance().getTag(), "initSpinnerRegion: " + responseError.getError().getMessage());
                    }
                }
        );
    }

    private static void initSpinnerRegionHelper(View spinner, List<Region> regionsList) {
        regionsList.add(Region.createNoRegion(spinner.getContext()));

        RegionsSpinnerAdapter regionsSpinnerAdapter = new RegionsSpinnerAdapter(spinner.getContext(), R.layout.custom_spinner_item, R.layout.ca_spinner_item_dropdown, regionsList);
        regionsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinnerRegion = (Spinner) spinner.findViewById(R.id.spinnerRegion);
        spinnerRegion.setAdapter(regionsSpinnerAdapter);
        spinnerRegion.setSelection(regionsSpinnerAdapter.getCount() - 1);
    }

    private static void initSpinnerExperienceYears(final View spinner) {
        List<Integer> range = Tools.getRange(Constants.YEARS_RANGE);
        range.add(0, null);

        RangeSpinnerAdapter rangeSpinnerAdapter = new RangeSpinnerAdapter(
                spinner.getContext(),
                R.layout.custom_spinner_item,
                R.layout.ca_spinner_item_dropdown,
                range);
        rangeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinnerExperienceYears = (Spinner) spinner.findViewById(R.id.spinnerExperienceYears);
        spinnerExperienceYears.setAdapter(rangeSpinnerAdapter);
    }

    private static void initSpinnerGraduationDate(final View spinner) {
        List<Integer> yearsRange = Tools.getYearsRange(Constants.YEARS_RANGE);
        yearsRange.add(0, null);

        RangeSpinnerAdapter rangeSpinnerAdapter = new RangeSpinnerAdapter(
                spinner.getContext(),
                R.layout.custom_spinner_item,
                R.layout.ca_spinner_item_dropdown,
                yearsRange);
        rangeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinnerGraduationDate = (Spinner) spinner.findViewById(R.id.spinnerGraduationDate);
        spinnerGraduationDate.setAdapter(rangeSpinnerAdapter);
    }

    private static void initSocials(final View parentView, BaseStartFragment parentFragment) {
        parentView.findViewById(R.id.imageViewFB).setOnClickListener(new SignInWithSocialNetwork(parentFragment, new FbApi(), new CustomAuthorizationListener(parentFragment, FbUser.class)));
        parentView.findViewById(R.id.imageViewVK).setOnClickListener(new SignInWithSocialNetwork(parentFragment, new VkApi(), new CustomAuthorizationListener(parentFragment, VkUser.class)));
        parentView.findViewById(R.id.imageViewOK).setOnClickListener(new SignInWithSocialNetwork(parentFragment, new OkApi(), new CustomAuthorizationListener(parentFragment, OkUser.class)));
    }

    public static boolean validateRegistrationFields(final View parentView) {
        View errorRadioButtons = validateRadioButtons(parentView);
        View errorEditTextCommon = validateTextViewFields(parentView, mRequiredEditTextCommon);
        View errorSpinnerRegion = validateSpinnerRegion(parentView);
        View errorTextViewBirthDate = validateTextViewBirthDate(parentView);
        View errorCheckBoxCommon = validateCheckBoxFields(parentView, mRequiredCheckBoxCommon);
        View errorEditTextCommonDoctor = validateTextViewFields(parentView, mRequiredEditTextCommonDoctor);
        View errorSpinnerExperienceYears = validateSpinnerExperienceYears(parentView);
        View errorSpinnerGraduationDate = validateSpinnerGraduationDate(parentView);
        View errorEditTextPassword = validateTextViewPassword(parentView);

        // FIXME: validate email - Tools.isValidEmail(...)

        RadioButton radioButtonDoctor = (RadioButton) parentView.findViewById(R.id.radioButtonDoctor);

        if (errorRadioButtons != null)
            errorRadioButtons.requestFocus();
        else if (errorEditTextCommon != null)
            errorEditTextCommon.requestFocus();
        else if (errorSpinnerRegion != null)
            errorSpinnerRegion.requestFocus();
        else if (errorTextViewBirthDate != null)
            errorTextViewBirthDate.requestFocus();
        else if (errorCheckBoxCommon != null)
            errorCheckBoxCommon.requestFocus();
        else if (errorEditTextCommonDoctor != null && radioButtonDoctor.isChecked())
            errorEditTextCommonDoctor.requestFocus();
        else if (errorSpinnerExperienceYears != null)
            errorSpinnerExperienceYears.requestFocus();
        else if (errorSpinnerGraduationDate != null)
            errorSpinnerGraduationDate.requestFocus();
        else if (errorEditTextPassword != null)
            errorEditTextPassword.requestFocus();
        else
            return true;

        return false;
    }

    private static View validateRadioButtons(final View parentView) {
        View errorView = null;

        TextView textViewIsDoctor = (TextView) parentView.findViewById(R.id.textViewIsDoctor);
        RadioButton radioButtonDoctor = (RadioButton) parentView.findViewById(R.id.radioButtonDoctor);
        RadioButton radioButtonNotDoctor = (RadioButton) parentView.findViewById(R.id.radioButtonNotDoctor);

        if (!radioButtonDoctor.isChecked() && !radioButtonNotDoctor.isChecked()) {
            textViewIsDoctor.setError(parentView.getContext().getString(R.string.must_be_selected));
            errorView = textViewIsDoctor;
        } else {
            textViewIsDoctor.setError(null);
        }

        return errorView;
    }

    private static View validateTextViewFields(final View parentView, int[] textViewFields) {
        View errorView = null;
        for (int fieldId : textViewFields) {
            TextView textView = (TextView) parentView.findViewById(fieldId);
            if (textView.getText().length() == 0) {
                textView.setError(parentView.getContext().getString(R.string.can_not_be_empty));
                errorView = ((errorView == null) ? textView : errorView);
            } else {
                textView.setError(null);
            }
        }
        return errorView;
    }

    private static View validateSpinnerRegion(final View parentView) {
        View errorView = null;

        TextView textViewRegion = (TextView) parentView.findViewById(R.id.textViewRegion);
        Spinner spinnerRegion = (Spinner) parentView.findViewById(R.id.spinnerRegion);

        if (spinnerRegion.getTag() == null) {
            textViewRegion.setError(parentView.getContext().getString(R.string.must_be_selected));
            errorView = textViewRegion;
        } else {
            textViewRegion.setError(null);
        }

        return errorView;
    }

    private static View validateSpinnerExperienceYears(final View parentView) {
        View errorView = null;

        TextView textViewExperienceYears = (TextView) parentView.findViewById(R.id.textViewExperienceYears);
        Spinner spinnerExperienceYears = (Spinner) parentView.findViewById(R.id.spinnerExperienceYears);

        if (spinnerExperienceYears.getTag() == null) {
            textViewExperienceYears.setError(parentView.getContext().getString(R.string.must_be_selected));
            errorView = textViewExperienceYears;
        } else {
            textViewExperienceYears.setError(null);
        }

        return errorView;
    }

    private static View validateSpinnerGraduationDate(final View parentView) {
        View errorView = null;

        TextView textViewGraduationDate = (TextView) parentView.findViewById(R.id.textViewGraduationDate);
        Spinner spinnerGraduationDate = (Spinner) parentView.findViewById(R.id.spinnerGraduationDate);

        if (spinnerGraduationDate.getTag() == null) {
            textViewGraduationDate.setError(parentView.getContext().getString(R.string.must_be_selected));
            errorView = textViewGraduationDate;
        } else {
            textViewGraduationDate.setError(null);
        }

        return errorView;
    }

    private static View validateTextViewBirthDate(final View parentView) {
        View errorView = null;

        TextView textViewBirthDate = (TextView) parentView.findViewById(R.id.textViewBirthDate);
        TextView textViewBirthDateValue = (TextView) parentView.findViewById(R.id.textViewBirthDateValue);

        if (textViewBirthDateValue.getTag() == null) {
            textViewBirthDate.setError(parentView.getContext().getString(R.string.must_be_selected));
            errorView = textViewBirthDate;
        } else {
            textViewBirthDate.setError(null);
        }

        return errorView;
    }

    private static View validateCheckBoxFields(final View parentView, int[] checkBoxFields) {
        View errorView = null;
        for (int fieldId : checkBoxFields) {
            CheckBox checkBox = (CheckBox) parentView.findViewById(fieldId);
            if (checkBox.isChecked()) {
                checkBox.setError(null);
            } else {
                checkBox.setError(parentView.getContext().getString(R.string.can_not_be_empty));
                errorView = ((errorView == null) ? checkBox : errorView);
            }
        }
        return errorView;
    }

    private static View validateTextViewPassword(final View parentView) {
        View errorView = null;

        TextView editTextPasswordFirst = (TextView) parentView.findViewById(R.id.editTextPasswordFirst);
        TextView editTextPasswordSecond = (TextView) parentView.findViewById(R.id.editTextPasswordSecond);

        if (!editTextPasswordFirst.getText().toString().equals(editTextPasswordSecond.getText().toString())) {
            editTextPasswordFirst.setError(parentView.getContext().getString(R.string.must_match));
            editTextPasswordSecond.setError(parentView.getContext().getString(R.string.must_match));
            errorView = editTextPasswordFirst;
        } else {
            editTextPasswordFirst.setError(null);
            editTextPasswordSecond.setError(null);
        }

        return errorView;
    }

    public static User pickRegistrationFields(final View parentView) {
        User newUser = new User();

        try {
            RadioButton radioButtonDoctor = (RadioButton) parentView.findViewById(R.id.radioButtonDoctor);
            EditText editTextFirstName = (EditText) parentView.findViewById(R.id.editTextFirstName);
            EditText editTextLastName = (EditText) parentView.findViewById(R.id.editTextLastName);
            EditText editTextRegEmail = (EditText) parentView.findViewById(R.id.editTextRegEmail);
            Spinner spinnerRegion = (Spinner) parentView.findViewById(R.id.spinnerRegion);
            TextView textViewBirthDateValue = (TextView) parentView.findViewById(R.id.textViewBirthDateValue);
            EditText editTextPasswordFirst = (EditText) parentView.findViewById(R.id.editTextPasswordFirst);

            EditText editTextSpecializationName = (EditText) parentView.findViewById(R.id.editTextSpecializationName);
            EditText editTextSpecializationInstitutionName = (EditText) parentView.findViewById(R.id.editTextSpecializationInstitutionName);
            EditText editTextSpecializationInstitutionAddress = (EditText) parentView.findViewById(R.id.editTextSpecializationInstitutionAddress);
            EditText editTextSpecializationInstitutionPhone = (EditText) parentView.findViewById(R.id.editTextSpecializationInstitutionPhone);
            Spinner spinnerExperienceYears = (Spinner) parentView.findViewById(R.id.spinnerExperienceYears);
            Spinner spinnerGraduationDate = (Spinner) parentView.findViewById(R.id.spinnerGraduationDate);

            CheckBox checkBoxAgreeToReceive = (CheckBox) parentView.findViewById(R.id.checkBoxAgreeToReceive);

            newUser.setIsDoctor(radioButtonDoctor.isChecked());
            newUser.setFirstname(editTextFirstName.getText().toString());
            newUser.setLastname(editTextLastName.getText().toString());
            newUser.setEmail(editTextRegEmail.getText().toString());
            newUser.setRegion(String.valueOf(spinnerRegion.getTag()));
            newUser.setBirthday(Tools.formatFullDate(((Calendar) textViewBirthDateValue.getTag()).getTime()));
            newUser.setPlainPassword(editTextPasswordFirst.getText().toString());

            newUser.setSpecializationName(editTextSpecializationName.getText().toString());
            newUser.setSpecializationInstitutionName(editTextSpecializationInstitutionName.getText().toString());
            newUser.setSpecializationInstitutionAddress(editTextSpecializationInstitutionAddress.getText().toString());
            newUser.setSpecializationInstitutionPhone(editTextSpecializationInstitutionPhone.getText().toString());
            newUser.setSpecializationExperienceYears((Integer) spinnerExperienceYears.getTag());
            newUser.setSpecializationGraduationDate(Tools.yearToDate((Integer) spinnerGraduationDate.getTag()));

            newUser.setIsSubscribed(checkBoxAgreeToReceive.isChecked());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return newUser;
    }

    private static void fillFields(final View parentView) {
        EditText editTextFirstName = (EditText) parentView.findViewById(R.id.editTextFirstName);
        EditText editTextLastName = (EditText) parentView.findViewById(R.id.editTextLastName);
        EditText editTextRegEmail = (EditText) parentView.findViewById(R.id.editTextRegEmail);

        CheckBox checkBoxAgreeToReceive = (CheckBox) parentView.findViewById(R.id.checkBoxAgreeToReceive);
        CheckBox checkBoxAgreeToProcessing = (CheckBox) parentView.findViewById(R.id.checkBoxAgreeToProcessing);
        CheckBox checkBoxAgreeThatAdvises = (CheckBox) parentView.findViewById(R.id.checkBoxAgreeThatAdvises);

        TextView textViewBirthDateValue = (TextView) parentView.findViewById(R.id.textViewBirthDateValue);
        TextView editTextPasswordFirst = (TextView) parentView.findViewById(R.id.editTextPasswordFirst);
        TextView editTextPasswordSecond = (TextView) parentView.findViewById(R.id.editTextPasswordSecond);
        Spinner spinnerRegion = (Spinner) parentView.findViewById(R.id.spinnerRegion);


        RadioButton radioButtonDoctor = (RadioButton) parentView.findViewById(R.id.radioButtonDoctor);
        View layoutSpecializationDetailsl = parentView.findViewById(R.id.layoutSpecializationDetailsl);
        EditText editTextSpecializationName = (EditText) parentView.findViewById(R.id.editTextSpecializationName);
        EditText editTextSpecializationInstitutionName = (EditText) parentView.findViewById(R.id.editTextSpecializationInstitutionName);
        EditText editTextSpecializationInstitutionAddress = (EditText) parentView.findViewById(R.id.editTextSpecializationInstitutionAddress);
        EditText editTextSpecializationInstitutionPhone = (EditText) parentView.findViewById(R.id.editTextSpecializationInstitutionPhone);
        Spinner spinnerExperienceYears = (Spinner) parentView.findViewById(R.id.spinnerExperienceYears);
        Spinner spinnerGraduationDate = (Spinner) parentView.findViewById(R.id.spinnerGraduationDate);

        User currentUser = AppState.getInstatce().getUser();

        editTextFirstName.setText(currentUser.getFirstname());
        editTextLastName.setText(currentUser.getLastname());
        editTextRegEmail.setText(currentUser.getEmail());

        checkBoxAgreeToReceive.setChecked(currentUser.isSubscribed());
        checkBoxAgreeToProcessing.setChecked(true);
        checkBoxAgreeThatAdvises.setChecked(true);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Tools.dateFromFullDate(currentUser.getBirthday()));
        textViewBirthDateValue.setTag(calendar);
        spinnerRegion.setTag(currentUser.getRegion());
        editTextPasswordFirst.setText("*");
        editTextPasswordSecond.setText("*");

        if (currentUser.isDoctor()) {
            radioButtonDoctor.setChecked(true);
            layoutSpecializationDetailsl.setVisibility(View.VISIBLE);

            editTextSpecializationName.setText(currentUser.getSpecializationName());
            editTextSpecializationInstitutionName.setText(currentUser.getSpecializationInstitutionName());
            editTextSpecializationInstitutionAddress.setText(currentUser.getSpecializationInstitutionAddress());
            editTextSpecializationInstitutionPhone.setText(currentUser.getSpecializationInstitutionPhone());
            spinnerExperienceYears.setSelection(currentUser.getSpecializationExperienceYears() + 1, true);
            spinnerExperienceYears.setTag(currentUser.getSpecializationExperienceYears());
            int graduation = Tools.getDifferenceInYears(currentUser.getSpecializationGraduationDate());
            spinnerGraduationDate.setSelection(graduation + 1, true);
            spinnerGraduationDate.setTag(spinnerGraduationDate.getAdapter().getItem(graduation));
        } else {
            radioButtonDoctor.setChecked(false);
            layoutSpecializationDetailsl.setVisibility(View.GONE);
        }
    }
}