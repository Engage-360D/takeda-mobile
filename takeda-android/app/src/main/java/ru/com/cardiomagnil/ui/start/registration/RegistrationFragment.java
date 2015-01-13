package ru.com.cardiomagnil.ui.start.registration;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

import ru.com.cardiomagnil.app.R;
import ru.com.cardiomagnil.application.AppState;
import ru.com.cardiomagnil.ca_model.common.Ca_Response;
import ru.com.cardiomagnil.ca_model.region.Ca_Region;
import ru.com.cardiomagnil.ca_model.region.Ca_RegionDao;
import ru.com.cardiomagnil.model.User;
import ru.com.cardiomagnil.social.AuthorizationDialog;
import ru.com.cardiomagnil.social.AuthorizationListener;
import ru.com.cardiomagnil.social.BaseSocialApi;
import ru.com.cardiomagnil.social.FbApi;
import ru.com.cardiomagnil.social.FbUser;
import ru.com.cardiomagnil.social.OkApi;
import ru.com.cardiomagnil.social.OkUser;
import ru.com.cardiomagnil.social.VkApi;
import ru.com.cardiomagnil.social.VkUser;
import ru.com.cardiomagnil.ui.start.Ca_RegionAdapter;
import ru.com.cardiomagnil.ui.start.CustomFragment;
import ru.com.cardiomagnil.ui.start.StartActivity;
import ru.com.cardiomagnil.util.CallbackOne;
import ru.com.cardiomagnil.util.ThtreadHelper;
import ru.com.cardiomagnil.util.Tools;

public class RegistrationFragment extends CustomFragment {
    private String mBirthDate = null;

    private final int[] mRequiredEditTextCommon = new int[]{
            R.id.editTextFirstName,
            R.id.editTextLastName,
            R.id.editTextRegEmail,
            R.id.editTextPasswordFirst,
            R.id.editTextPasswordSecond,
            R.id.textViewBirthDate
    };

    private final int[] mRequiredCheckBoxCommon = new int[]{
            R.id.checkBoxAgreeToProcessing,
            R.id.checkBoxAgreeToReceive,
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
    public void initParent() {
        TextView textViewHeader = (TextView) getActivity().findViewById(R.id.textViewHeader);

        ProgressBar progressBarBottomOutsideStartWork = (ProgressBar) getActivity().findViewById(R.id.progressBarBottomOutsideStartWork);
        TextView textViewBottomOutsideAction = (TextView) getActivity().findViewById(R.id.textViewBottomOutsideAction);

        textViewHeader.setText(getActivity().getString(R.string.header_registration));

        progressBarBottomOutsideStartWork.setMax(3);
        progressBarBottomOutsideStartWork.setProgress(2);
        textViewBottomOutsideAction.setText(getActivity().getString(R.string.two_minutes));

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
        TextView textViewBirthDate = (TextView) parentView.findViewById(R.id.textViewBirthDate);

        textViewBirthDate.setOnTouchListener(new OnTouchListener() {
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
                    dateDialog.show();

                    dateDialog.setOnDismissListener(new OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
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
                        ThtreadHelper.logThread("doWhatYouNeed->error");
                    }
                }
        );
    }

    private void initSpinnerRegionHelper(View parentView, List<Ca_Region> regionsList) {
        Spinner spinnerRegion = (Spinner) parentView.findViewById(R.id.spinnerRegion);
        Ca_RegionAdapter adapter = new Ca_RegionAdapter(this.getActivity(), R.layout.custom_spinner_item, regionsList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRegion.setAdapter(adapter);
        // FIXME
//        spinnerRegion.setSelection(RUSSIA);
        spinnerRegion.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // FIXME
//                mCountry = regionItems[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void initSocials(final View parentView) {
        parentView
                .findViewById(R.id.imageViewFB)
                .setOnClickListener(
                        new SignInWithSocialNetwork(
                                this.getActivity(),
                                new FbApi(),
                                new RegisterUserOnFinish(this, FbUser.class)
                        )
                );
        parentView
                .findViewById(R.id.imageViewVK)
                .setOnClickListener(
                        new SignInWithSocialNetwork(
                                this.getActivity(),
                                new VkApi(),
                                new RegisterUserOnFinish(this, VkUser.class))
                );
        parentView
                .findViewById(R.id.imageViewOK)
                .setOnClickListener(
                        new SignInWithSocialNetwork(
                                this.getActivity(),
                                new OkApi(),
                                new RegisterUserOnFinish(this, OkUser.class)
                        )
                );
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
        if (!validateAllFields()) {
            return;
        }

        User newUser = pickRegistrationFields();
        if (newUser.validate(false)) {
            AppState.getInstatce().setUser(newUser);

            StartActivity startActivity = (StartActivity) getActivity();
            startActivity.userRegistration();
        } else {
            Toast.makeText(parentView.getContext(), parentView.getContext().getString(R.string.complete_all_fields), Toast.LENGTH_LONG).show();
        }
    }

    private boolean validateAllFields() {
        View errorRadioButton = validateRadioButton();
        View errorEditTextCommon = validateTextViewFields(mRequiredEditTextCommon);
        View errorEditTextCommonDoctor = validateTextViewFields(mRequiredEditTextCommonDoctor);
        View errorCheckBoxCommon = validateCheckBoxFields(mRequiredCheckBoxCommon);

        RadioButton radioButtonDoctor = (RadioButton) getActivity().findViewById(R.id.radioButtonDoctor);

        if (errorRadioButton != null)
            errorRadioButton.requestFocus();
        else if (errorEditTextCommon != null)
            errorEditTextCommon.requestFocus();
        else if (errorEditTextCommonDoctor != null && radioButtonDoctor.isChecked())
            errorEditTextCommonDoctor.requestFocus();
        else if (errorCheckBoxCommon != null)
            errorCheckBoxCommon.requestFocus();
        else
            return true;

        return false;
    }

    private View validateRadioButton() {
        View errorView = null;

        TextView textViewIsDoctor = (TextView) getActivity().findViewById(R.id.textViewIsDoctor);
        RadioButton radioButtonDoctor = (RadioButton) getActivity().findViewById(R.id.radioButtonDoctor);
        RadioButton radioButtonNotDoctor = (RadioButton) getActivity().findViewById(R.id.radioButtonNotDoctor);

        if (!radioButtonDoctor.isChecked() && !radioButtonNotDoctor.isChecked()) {
            textViewIsDoctor.setError(getString(R.string.must_be_selected));
            errorView = textViewIsDoctor;
        } else {
            textViewIsDoctor.setError(null);
        }

        return errorView;
    }

    private View validateTextViewFields(int[] textViewFields) {
        View errorView = null;
        for (int fieldId : textViewFields) {
            TextView textView = (TextView) this.getActivity().findViewById(fieldId);
            if (textView.getText().length() == 0) {
                textView.setError(getString(R.string.can_not_be_empty));
                errorView = ((errorView == null) ? textView : errorView);
            } else {
                textView.setError(null);
            }
        }
        return errorView;
    }

    private View validateCheckBoxFields(int[] checkBoxFields) {
        View errorView = null;
        for (int fieldId : checkBoxFields) {
            CheckBox checkBox = (CheckBox) this.getActivity().findViewById(fieldId);
            if (checkBox.isChecked()) {
                checkBox.setError(null);
            } else {
                checkBox.setError(getString(R.string.can_not_be_empty));
                errorView = ((errorView == null) ? checkBox : errorView);
            }
        }
        return errorView;
    }

    private User pickRegistrationFields() {
        User newUser = new User();

        RadioButton radioButtonDoctor = (RadioButton) getActivity().findViewById(R.id.radioButtonDoctor);
        RadioButton radioButtonNotDoctor = (RadioButton) getActivity().findViewById(R.id.radioButtonNotDoctor);
        EditText editTextFirstName = (EditText) getActivity().findViewById(R.id.editTextFirstName);
        EditText editTextLastName = (EditText) getActivity().findViewById(R.id.editTextLastName);
        EditText editTextRegEmail = (EditText) getActivity().findViewById(R.id.editTextRegEmail);
        Spinner spinnerRegion = (Spinner) getActivity().findViewById(R.id.spinnerRegion);
        TextView textViewBirthDate = (TextView) getActivity().findViewById(R.id.textViewBirthDate);
        EditText editTextPasswordFirst = (EditText) getActivity().findViewById(R.id.editTextPasswordFirst);
        EditText editTextPasswordSecond = (EditText) getActivity().findViewById(R.id.editTextPasswordSecond);

        EditText editTextSpecializationName = (EditText) getActivity().findViewById(R.id.editTextSpecializationName);
        EditText editTextSpecializationInstitutionName = (EditText) getActivity().findViewById(R.id.editTextSpecializationInstitutionName);
        EditText editTextSpecializationInstitutionAddress = (EditText) getActivity().findViewById(R.id.editTextSpecializationInstitutionAddress);
        EditText editTextSpecializationInstitutionPhone = (EditText) getActivity().findViewById(R.id.editTextSpecializationInstitutionPhone);
        EditText editTextSpecializationGraduationDate = (EditText) getActivity().findViewById(R.id.editTextSpecializationGraduationDate);
        EditText editTextSpecializationExperienceYears = (EditText) getActivity().findViewById(R.id.editTextSpecializationExperienceYears);

        CheckBox checkBoxAgreeToProcessing = (CheckBox) getActivity().findViewById(R.id.checkBoxAgreeToProcessing);
        CheckBox checkBoxAgreeToReceive = (CheckBox) getActivity().findViewById(R.id.checkBoxAgreeToReceive);
        CheckBox checkBoxAgreeThatAdvises = (CheckBox) getActivity().findViewById(R.id.checkBoxAgreeThatAdvises);

        newUser.setDoctor(radioButtonDoctor.isChecked() || radioButtonNotDoctor.isChecked() ? radioButtonDoctor.isChecked() : null);
        newUser.setFirstName(editTextFirstName.length() != 0 ? editTextFirstName.getText().toString() : null);
        newUser.setEmail(editTextRegEmail.length() != 0 ? editTextRegEmail.getText().toString() : null);
        newUser.setPlainPassword(editTextPasswordFirst.length() != 0 ? editTextPasswordFirst.getText().toString() : null);
        newUser.setConfirmPersonalization(checkBoxAgreeToProcessing.isChecked());
        newUser.setConfirmInformation(checkBoxAgreeThatAdvises.isChecked());
        newUser.setConfirmSubscription(checkBoxAgreeToReceive.isChecked());
        newUser.setBirthday(mBirthDate);
        // FIXME
//            newUser.setRegionl(mCountry);

        return newUser;
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
                TextView textViewBirthDate = (TextView) RegistrationFragment.this.getView().findViewById(R.id.textViewBirthDate);
                mBirthDate = Tools.formatDate(calendar.getTime());
                textViewBirthDate.setText(mBirthDate);
            } else {
                Toast.makeText(view.getContext(), view.getContext().getString(R.string.error_birth_date), Toast.LENGTH_LONG).show();
            }
        }
    };

    private class SignInWithSocialNetwork implements View.OnClickListener {
        private final Context mContext;
        private final BaseSocialApi mApi;
        private final AuthorizationListener mListener;

        public SignInWithSocialNetwork(Context context, BaseSocialApi api, AuthorizationListener listener) {
            mContext = context;
            mApi = api;
            mListener = listener;
        }

        @Override
        public void onClick(View v) {
            StartActivity startActivity = (StartActivity) getActivity();
            startActivity.showProgressDialog();

            AuthorizationDialog dialog = new AuthorizationDialog(mContext, mApi);
            dialog.show(mListener);
        }
    }

    // FIXME!!!
    @SuppressWarnings("unchecked")
    public void initFields(ru.com.cardiomagnil.social.User user) {
        // FIXME!!!
        EditText editTextName = (EditText) getActivity().findViewById(R.id.editTextFirstName);


        EditText editTextRegEmail = (EditText) getActivity().findViewById(R.id.editTextRegEmail);
        Spinner spinnerRegion = (Spinner) getActivity().findViewById(R.id.spinnerRegion);
        TextView textViewBirthDate = (TextView) getActivity().findViewById(R.id.textViewBirthDate);

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
            mBirthDate = user.getBirthday();
        }
    }
}
