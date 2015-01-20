package ru.com.cardiomagnil.ui.start.registration;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

import ru.com.cardiomagnil.app.R;
import ru.com.cardiomagnil.application.CardiomagnilApplication;
import ru.com.cardiomagnil.ca_model.common.Ca_Response;
import ru.com.cardiomagnil.ca_model.region.Ca_Region;
import ru.com.cardiomagnil.ca_model.region.Ca_RegionDao;
import ru.com.cardiomagnil.ca_model.token.Ca_Token;
import ru.com.cardiomagnil.ca_model.user.Ca_User;
import ru.com.cardiomagnil.ca_model.user.Ca_UserDao;
import ru.com.cardiomagnil.social.FbApi;
import ru.com.cardiomagnil.social.FbUser;
import ru.com.cardiomagnil.social.OkApi;
import ru.com.cardiomagnil.social.OkUser;
import ru.com.cardiomagnil.social.VkApi;
import ru.com.cardiomagnil.social.VkUser;
import ru.com.cardiomagnil.ui.base.BaseStartFragment;
import ru.com.cardiomagnil.ui.start.CustomAuthorizationListener;
import ru.com.cardiomagnil.ui.start.SignInWithSocialNetwork;
import ru.com.cardiomagnil.util.CallbackOne;
import ru.com.cardiomagnil.util.Tools;

public class RegistrationFragment extends BaseStartFragment {
    private final int[] mRequiredEditTextCommon = new int[]{
            R.id.editTextFirstName,
            R.id.editTextLastName,
            R.id.editTextRegEmail,
            R.id.editTextPasswordFirst,
            R.id.editTextPasswordSecond
    };

    private final int[] mRequiredCheckBoxCommon = new int[]{
            R.id.checkBoxAgreeToProcessing,
            R.id.checkBoxAgreeThatAdvises
    };

    private final int[] mRequiredEditTextCommonDoctor = new int[]{
            R.id.editTextSpecializationName,
            R.id.editTextSpecializationInstitutionName,
            R.id.editTextSpecializationInstitutionAddress,
            R.id.editTextSpecializationInstitutionPhone,
            R.id.editTextSpecializationGraduationDate,
            R.id.editTextSpecializationExperienceYears
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ca_fragment_start_registration, container, false);
        initRegistrationFragment(view);
        return view;
    }

    @Override
    public void initParent(Activity activity) {
        TextView textViewHeader = (TextView) activity.findViewById(R.id.textViewHeader);

        ProgressBar progressBarBottomOutsideStartWork = (ProgressBar) activity.findViewById(R.id.progressBarBottomOutsideStartWork);
        TextView textViewBottomOutsideAction = (TextView) activity.findViewById(R.id.textViewBottomOutsideAction);

        textViewHeader.setText(activity.getString(R.string.header_registration));

        progressBarBottomOutsideStartWork.setMax(3);
        progressBarBottomOutsideStartWork.setProgress(2);
        textViewBottomOutsideAction.setText(activity.getString(R.string.two_minutes));
    }

    // FIXME!!! remove getActivity!!!
    @Override
    public void initFields(ru.com.cardiomagnil.social.User user) {
        // FIXME!!!
        EditText editTextName = (EditText) getActivity().findViewById(R.id.editTextFirstName);


        EditText editTextRegEmail = (EditText) getActivity().findViewById(R.id.editTextRegEmail);
        Spinner spinnerRegion = (Spinner) getActivity().findViewById(R.id.spinnerRegion);
        TextView textViewBirthDate = (TextView) getActivity().findViewById(R.id.textViewBirthDateValue);

        if (!user.getFirstName().isEmpty()) {
            editTextName.setText(user.getFirstName());
        }

        if (!user.getEmail().isEmpty()) {
            editTextRegEmail.setText(user.getEmail());
        }

        if (!user.getCountry().isEmpty()) {
            ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinnerRegion.getAdapter();
            adapter.add(user.getCountry());
            spinnerRegion.setSelection(adapter.getCount() - 1);
            // FIXME
//            mCountry = user.getCountry();
        }

        if (!user.getBirthday().isEmpty()) {
            textViewBirthDate.setText(user.getBirthday());
            // FIXME
//            mBirthDate = user.getBirthday();
        }
    }

    @Override
    protected void handleRegAuth(Ca_Token token, Ca_User user) {

    }

    private void initRegistrationFragment(final View view) {
        intiRadioGroupDoctor(view);
        initTextViewBirthDate(view);
        initSpinnerRegion(view);
        initSocials(view);
        initSignUpButton(view);
    }

    private void intiRadioGroupDoctor(final View view) {
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

    private void initTextViewBirthDate(final View parentView) {
        final int ageLimt = 21;
        final TextView textViewBirthDate = (TextView) parentView.findViewById(R.id.textViewBirthDateValue);
        final CustomOnDateSetListener customOnDateSetListener = new CustomOnDateSetListener();
        final Calendar calendar = Calendar.getInstance();
        final DatePickerDialog dateDialog = new DatePickerDialog(
                parentView.getContext(),
                customOnDateSetListener,
                calendar.get(Calendar.YEAR) - ageLimt,
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        textViewBirthDate.setOnTouchListener(new OnTouchListener() {
            private boolean datePickerDialogIsStarted = false;

            @Override
            public boolean onTouch(View paramView, MotionEvent paramMotionEvent) {
                if (!datePickerDialogIsStarted) {
                    datePickerDialogIsStarted = true;
                    dateDialog.show();

                    dateDialog.setOnDismissListener(new OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            if (customOnDateSetListener.isRightAge(ageLimt)) {
                                textViewBirthDate.setTag(customOnDateSetListener.getCalendar());
                                textViewBirthDate.setText(Tools.formatShortDate(customOnDateSetListener.getCalendar().getTime()));
                            } else {
                                textViewBirthDate.setTag(null);
                                textViewBirthDate.setText(textViewBirthDate.getContext().getString(R.string.birth_date));
                                Toast.makeText(textViewBirthDate.getContext(), textViewBirthDate.getContext().getString(R.string.error_birth_date), Toast.LENGTH_LONG).show();
                            }

                            datePickerDialogIsStarted = false;
                        }
                    });
                }

                return false;
            }
        });
    }

    private void initSpinnerRegion(final View parentView) {
        Ca_RegionDao.getAll(
                new CallbackOne<List<Ca_Region>>() {
                    @Override
                    public void execute(List<Ca_Region> regionsList) {
                        initSpinnerRegionHelper(parentView, regionsList);
                    }
                },
                new CallbackOne<Ca_Response>() {
                    @Override
                    public void execute(Ca_Response responseError) {
                        Log.d(CardiomagnilApplication.getInstance().getTag(), "initSpinnerRegion: " + responseError.getError().getMessage());
                    }
                }
        );
    }

    private void initSpinnerRegionHelper(View parentView, List<Ca_Region> regionsList) {
        regionsList.add(Ca_Region.createNoRegion(parentView.getContext()));

        RegionsSpinnerAdapter regionsSpinnerAdapter = new RegionsSpinnerAdapter(parentView.getContext(), R.layout.custom_spinner_item, R.layout.ca_spinner_item_dropdown, regionsList);
        regionsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinnerRegion = (Spinner) parentView.findViewById(R.id.spinnerRegion);
        spinnerRegion.setAdapter(regionsSpinnerAdapter);
        spinnerRegion.setSelection(regionsSpinnerAdapter.getCount() - 1);
    }

    private void initSocials(final View view) {
        view.findViewById(R.id.imageViewFB).setOnClickListener(new SignInWithSocialNetwork(this, new FbApi(), new CustomAuthorizationListener(this, FbUser.class)));
        view.findViewById(R.id.imageViewVK).setOnClickListener(new SignInWithSocialNetwork(this, new VkApi(), new CustomAuthorizationListener(this, VkUser.class)));
        view.findViewById(R.id.imageViewOK).setOnClickListener(new SignInWithSocialNetwork(this, new OkApi(), new CustomAuthorizationListener(this, OkUser.class)));
    }

    private void initSignUpButton(final View parentView) {
        Button buttonSignUp = (Button) parentView.findViewById(R.id.buttonSignUp);
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View paramView) {
                tryRegistration(parentView);
            }
        });
    }

    private void tryRegistration(final View parentView) {
        if (!validateRegistrationFields(parentView)) {
            Toast.makeText(parentView.getContext(), parentView.getContext().getString(R.string.complete_required_fields), Toast.LENGTH_LONG).show();
            return;
        }

        Ca_User newUser = pickRegistrationFields(parentView);

        Ca_UserDao.register(
                newUser,
                new CallbackOne<Ca_User>() {
                    @Override
                    public void execute(Ca_User user) {
                        int t = 1;
                        t++;
                    }
                },
                new CallbackOne<Ca_Response>() {
                    @Override
                    public void execute(Ca_Response responseError) {
                        int t = 1;
                        t++;
                    }
                }
        );

//        AppState.getInstatce().set User(newUser);
//        StartActivity startActivity = (StartActivity) getActivity();
//        startActivity.userRegistration();
    }

    private boolean validateRegistrationFields(final View parentView) {
        View errorRadioButtons = validateRadioButtons(parentView);
        View errorEditTextCommon = validateTextViewFields(parentView, mRequiredEditTextCommon);
        View errorSpinner = validateSpinner(parentView);
        View errorTextViewBirthDate = validateTextViewBirthDate(parentView);
        View errorCheckBoxCommon = validateCheckBoxFields(parentView, mRequiredCheckBoxCommon);
        View errorEditTextCommonDoctor = validateTextViewFields(parentView, mRequiredEditTextCommonDoctor);
        View errorEditTextPassword = validateTextViewPassword(parentView);

        // FIXME: validate email - Tools.isValidEmail(...)

        RadioButton radioButtonDoctor = (RadioButton) parentView.findViewById(R.id.radioButtonDoctor);

        if (errorRadioButtons != null)
            errorRadioButtons.requestFocus();
        else if (errorEditTextCommon != null)
            errorEditTextCommon.requestFocus();
        else if (errorSpinner != null)
            errorSpinner.requestFocus();
        else if (errorTextViewBirthDate != null)
            errorTextViewBirthDate.requestFocus();
        else if (errorCheckBoxCommon != null)
            errorCheckBoxCommon.requestFocus();
        else if (errorEditTextCommonDoctor != null && radioButtonDoctor.isChecked())
            errorEditTextCommonDoctor.requestFocus();
        else if (errorEditTextPassword != null)
            errorEditTextPassword.requestFocus();
        else
            return true;

        return false;
    }

    private View validateRadioButtons(final View parentView) {
        View errorView = null;

        TextView textViewIsDoctor = (TextView) parentView.findViewById(R.id.textViewIsDoctor);
        RadioButton radioButtonDoctor = (RadioButton) parentView.findViewById(R.id.radioButtonDoctor);
        RadioButton radioButtonNotDoctor = (RadioButton) parentView.findViewById(R.id.radioButtonNotDoctor);

        if (!radioButtonDoctor.isChecked() && !radioButtonNotDoctor.isChecked()) {
            textViewIsDoctor.setError(getString(R.string.must_be_selected));
            errorView = textViewIsDoctor;
        } else {
            textViewIsDoctor.setError(null);
        }

        return errorView;
    }

    private View validateTextViewFields(final View parentView, int[] textViewFields) {
        View errorView = null;
        for (int fieldId : textViewFields) {
            TextView textView = (TextView) parentView.findViewById(fieldId);
            if (textView.getText().length() == 0) {
                textView.setError(getString(R.string.can_not_be_empty));
                errorView = ((errorView == null) ? textView : errorView);
            } else {
                textView.setError(null);
            }
        }
        return errorView;
    }

    private View validateSpinner(final View parentView) {
        View errorView = null;

        TextView textViewRegion = (TextView) parentView.findViewById(R.id.textViewRegion);
        Spinner spinnerRegion = (Spinner) parentView.findViewById(R.id.spinnerRegion);

        if (spinnerRegion.getTag() == null) {
            textViewRegion.setError(getString(R.string.must_be_selected));
            errorView = textViewRegion;
        } else {
            textViewRegion.setError(null);
        }

        return errorView;
    }

    private View validateTextViewBirthDate(final View parentView) {
        View errorView = null;

        TextView textViewBirthDate = (TextView) parentView.findViewById(R.id.textViewBirthDate);
        TextView textViewBirthDateValue = (TextView) parentView.findViewById(R.id.textViewBirthDateValue);

        if (textViewBirthDateValue.getTag() == null) {
            textViewBirthDate.setError(getString(R.string.must_be_selected));
            errorView = textViewBirthDate;
        } else {
            textViewBirthDate.setError(null);
        }

        return errorView;
    }

    private View validateCheckBoxFields(final View parentView, int[] checkBoxFields) {
        View errorView = null;
        for (int fieldId : checkBoxFields) {
            CheckBox checkBox = (CheckBox) parentView.findViewById(fieldId);
            if (checkBox.isChecked()) {
                checkBox.setError(null);
            } else {
                checkBox.setError(getString(R.string.can_not_be_empty));
                errorView = ((errorView == null) ? checkBox : errorView);
            }
        }
        return errorView;
    }

    private View validateTextViewPassword(final View parentView) {
        View errorView = null;

        TextView editTextPasswordFirst = (TextView) parentView.findViewById(R.id.editTextPasswordFirst);
        TextView editTextPasswordSecond = (TextView) parentView.findViewById(R.id.editTextPasswordSecond);

        if (!editTextPasswordFirst.getText().toString().equals(editTextPasswordSecond.getText().toString())) {
            editTextPasswordFirst.setError(getString(R.string.must_match));
            editTextPasswordSecond.setError(getString(R.string.must_match));
            errorView = editTextPasswordFirst;
        } else {
            editTextPasswordFirst.setError(null);
            editTextPasswordSecond.setError(null);
        }

        return errorView;
    }

    private Ca_User pickRegistrationFields(final View parentView) {
        Ca_User newUser = new Ca_User();

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
            EditText editTextSpecializationGraduationDate = (EditText) parentView.findViewById(R.id.editTextSpecializationGraduationDate);
            EditText editTextSpecializationExperienceYears = (EditText) parentView.findViewById(R.id.editTextSpecializationExperienceYears);

            CheckBox checkBoxAgreeToReceive = (CheckBox) parentView.findViewById(R.id.checkBoxAgreeToReceive);

            newUser.setIsDoctor(radioButtonDoctor.isChecked());
            newUser.setFirstname(editTextFirstName.getText().toString());
            newUser.setLastname(editTextLastName.getText().toString());
            newUser.setEmail(editTextRegEmail.getText().toString());
            newUser.setRegion(String.valueOf(((Ca_Region) spinnerRegion.getTag()).getId()));
            newUser.setBirthday(Tools.formatFullDate(((Calendar) textViewBirthDateValue.getTag()).getTime()));
            newUser.setPlainPassword(editTextPasswordFirst.getText().toString());

            newUser.setSpecializationName(editTextSpecializationName.getText().toString());
            newUser.setSpecializationInstitutionName(editTextSpecializationInstitutionName.getText().toString());
            newUser.setSpecializationInstitutionAddress(editTextSpecializationInstitutionAddress.getText().toString());
            newUser.setSpecializationInstitutionPhone(editTextSpecializationInstitutionPhone.getText().toString());
            String specializationGraduationDate = editTextSpecializationGraduationDate.getText().toString();
            newUser.setSpecializationGraduationDate(specializationGraduationDate.isEmpty() ? null : specializationGraduationDate);
            newUser.setSpecializationExperienceYears(Integer.parseInt(editTextSpecializationExperienceYears.getText().toString()));

            newUser.setisSubscribed(checkBoxAgreeToReceive.isChecked());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return newUser;
    }
}
