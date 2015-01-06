package ru.com.cardiomagnil.ui.start;

import java.util.Calendar;

import ru.com.cardiomagnil.app.R;
import ru.com.cardiomagnil.application.AppState;
import ru.com.cardiomagnil.application.ExeptionsHandler;
import ru.com.cardiomagnil.application.Tools;
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
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


public class RegistrationFragment extends CustomFragment {
    private View parentView;
    private final int RUSSIA = 131;
    private String[] regionItems = null;
    private String mBirthDate = null;
    private String mCountry = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_start_registration, container, false);

        initRegistrationFragment(parentView);

        return parentView;
    }

    public void initParent() {
        TextView textViewHeader = (TextView) getActivity().findViewById(R.id.textViewHeader);

        ImageView imageViewBottomInsideLeft = (ImageView) getActivity().findViewById(R.id.imageViewBottomInsideLeft);
        TextView textViewBottomInsideAction = (TextView) getActivity().findViewById(R.id.textViewBottomInsideAction);
        ImageView imageViewBottomInsideRight = (ImageView) getActivity().findViewById(R.id.imageViewBottomInsideRight);

        ProgressBar progressBarBottomOutsideStartWork = (ProgressBar) getActivity().findViewById(R.id.progressBarBottomOutsideStartWork);
        TextView textViewBottomOutsideAction = (TextView) getActivity().findViewById(R.id.textViewBottomOutsideAction);

        textViewHeader.setText(getActivity().getString(R.string.header_registration));

        imageViewBottomInsideLeft.setVisibility(View.INVISIBLE);
        textViewBottomInsideAction.setText(getActivity().getString(R.string.bottom_registration));
        imageViewBottomInsideRight.setVisibility(View.VISIBLE);

        progressBarBottomOutsideStartWork.setMax(3);
        progressBarBottomOutsideStartWork.setProgress(2);
        textViewBottomOutsideAction.setText(getActivity().getString(R.string.two_minutes));

    }

    private void initRegistrationFragment(View view) {
        RadioGroup radioGroupDoctor = (RadioGroup) view.findViewById(R.id.radioGroupDoctor);
        radioGroupDoctor.setOnCheckedChangeListener(Tools.ToggleListener);

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
        if (newUser.validate(false)) {
            AppState.getInstatce().setUser(newUser);

            StartActivity startActivity = (StartActivity) getActivity();
            startActivity.userRegistration();
        } else {
            Toast.makeText(parentView.getContext(), parentView.getContext().getString(R.string.complete_all_fields), Toast.LENGTH_LONG).show();
        }
    }

    private User pickRegistrationFields() {
        User newUser = new User();

        try {
            ToggleButton toggleButtonDoctor = (ToggleButton) getActivity().findViewById(R.id.toggleButtonDoctor);
            ToggleButton toggleButtonNotDoctor = (ToggleButton) getActivity().findViewById(R.id.toggleButtonNotDoctor);
            EditText editTextName = (EditText) getActivity().findViewById(R.id.editTextName);
            EditText editTextRegEmail = (EditText) getActivity().findViewById(R.id.editTextRegEmail);
            // Spinner spinnerCountry = (Spinner) getActivity().findViewById(R.id.spinnerCountry);
            // TextView textViewBirthDate = (TextView) getActivity().findViewById(R.id.textViewBirthDate);
            EditText editTextPasswordFirst = (EditText) getActivity().findViewById(R.id.editTextPasswordFirst);
            EditText editTextPasswordSecond = (EditText) getActivity().findViewById(R.id.editTextPasswordSecond);
            CheckBox checkBoxAgreeToProcessing = (CheckBox) getActivity().findViewById(R.id.checkBoxAgreeToProcessing);
            CheckBox checkBoxAgreeToReceive = (CheckBox) getActivity().findViewById(R.id.checkBoxAgreeToReceive);
            CheckBox checkBoxAgreeThatAdvises = (CheckBox) getActivity().findViewById(R.id.checkBoxAgreeThatAdvises);

            newUser.setDoctor(toggleButtonDoctor.isChecked() || toggleButtonNotDoctor.isChecked() ? toggleButtonDoctor.isChecked() : null);
            newUser.setFirstName(editTextName.length() != 0 ? editTextName.getText().toString() : null);
            newUser.setEmail(editTextRegEmail.length() != 0 ? editTextRegEmail.getText().toString() : null);
            newUser.setPlainPasswordFirst(editTextPasswordFirst.length() != 0 ? editTextPasswordFirst.getText().toString() : null);
            newUser.setPlainPasswordSecond(editTextPasswordSecond.length() != 0 ? editTextPasswordSecond.getText().toString() : null);
            newUser.setConfirmPersonalization(checkBoxAgreeToProcessing.isChecked());
            newUser.setConfirmInformation(checkBoxAgreeThatAdvises.isChecked());
            newUser.setConfirmSubscription(checkBoxAgreeToReceive.isChecked());
            newUser.setBirthday(mBirthDate);
            newUser.setRegionl(mCountry);
        } catch (Exception e) {
            // do nothing
        }

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

                return true;
            }
        });
    }

    private void initSpinnerCountry() {
        Spinner spinnerCountry = (Spinner) parentView.findViewById(R.id.spinnerRegion);
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
                TextView textViewBirthDate = (TextView) parentView.findViewById(R.id.textViewBirthDate);
                mBirthDate = Tools.formatDate(calendar.getTime());
                textViewBirthDate.setText(mBirthDate);
            } else {
                Toast.makeText(parentView.getContext(), parentView.getContext().getString(R.string.error_birth_date), Toast.LENGTH_LONG).show();
            }
        }
    };

    private void initSocials() {
        parentView.findViewById(R.id.imageViewFB).setOnClickListener(new SignInWithSocialNetwork(new FbApi(), new RegisterUserOnFinish(FbUser.class)));
        parentView.findViewById(R.id.imageViewVK).setOnClickListener(new SignInWithSocialNetwork(new VkApi(), new RegisterUserOnFinish(VkUser.class)));
        parentView.findViewById(R.id.imageViewOK).setOnClickListener(new SignInWithSocialNetwork(new OkApi(), new RegisterUserOnFinish(OkUser.class)));
    }

    private class SignInWithSocialNetwork implements View.OnClickListener {
        private BaseSocialApi mApi;
        private AuthorizationListener mListener;

        public SignInWithSocialNetwork(BaseSocialApi api, AuthorizationListener listener) {
            mApi = api;
            mListener = listener;
        }

        @Override
        public void onClick(View v) {
            StartActivity startActivity = (StartActivity) getActivity();
            startActivity.showProgressDialog();

            AuthorizationDialog dialog = new AuthorizationDialog(parentView.getContext(), mApi);
            dialog.show(mListener);
        }
    }

    private class RegisterUserOnFinish implements AuthorizationListener {
        private Class<? extends ru.com.cardiomagnil.social.User> mUserClass;

        public RegisterUserOnFinish(Class<? extends ru.com.cardiomagnil.social.User> userClass) {
            mUserClass = userClass;
        }

        @Override
        public void onAuthorized(String userInfo) {
            try {
                ru.com.cardiomagnil.social.User user = mUserClass.getConstructor(String.class).newInstance(userInfo);
                initFields(user);
                StartActivity startActivity = (StartActivity) getActivity();
                startActivity.hideProgressDialog();
            } catch (Exception e) {
                e.printStackTrace();
                ExeptionsHandler.getInstatce().handleException(getActivity(), e);
                Toast.makeText(parentView.getContext(), R.string.authorization_error, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onAuthorizationFailed() {
            StartActivity startActivity = (StartActivity) getActivity();
            startActivity.hideProgressDialog();
            Toast.makeText(parentView.getContext(), R.string.authorization_error, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onAuthorizationCanceled() {
            StartActivity startActivity = (StartActivity) getActivity();
            startActivity.hideProgressDialog();
        }
    }

    @SuppressWarnings("unchecked")
    private void initFields(ru.com.cardiomagnil.social.User user) {
        EditText editTextName = (EditText) getActivity().findViewById(R.id.editTextName);
        EditText editTextRegEmail = (EditText) getActivity().findViewById(R.id.editTextRegEmail);
        Spinner spinnerCountry = (Spinner) getActivity().findViewById(R.id.spinnerRegion);
        TextView textViewBirthDate = (TextView) getActivity().findViewById(R.id.textViewBirthDate);

        if (!user.getFirstName().isEmpty()) {
            editTextName.setText(user.getFirstName());
        }

        if (!user.getEmail().isEmpty()) {
            editTextRegEmail.setText(user.getEmail());
        }

        if (!user.getCountry().isEmpty()) {
            ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinnerCountry.getAdapter();
            adapter.add(user.getCountry());
            spinnerCountry.setSelection(adapter.getCount() - 1);
            mCountry = user.getCountry();
        }

        if (!user.getBirthday().isEmpty()) {
            textViewBirthDate.setText(user.getBirthday());
            mBirthDate = user.getBirthday();
        }
    }
}