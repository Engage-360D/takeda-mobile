package ru.com.cardiomagnil.ui.start;

import ru.com.cardiomagnil.R;
import ru.com.cardiomagnil.application.AppState;
import ru.com.cardiomagnil.application.ExeptionsHandler;
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
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LoginOrRestoreFragment extends CustomFragment {
    private View parentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_start_login_or_restore, container, false);

        initLoginOrRestoreFragment(parentView);

        return parentView;
    }

    @Override
    public void initParent() {
        ImageView imageViewBottomInsideLeft = (ImageView) getActivity().findViewById(R.id.imageViewBottomInsideLeft);
        TextView textViewBottomInsideAction = (TextView) getActivity().findViewById(R.id.textViewBottomInsideAction);
        ImageView imageViewBottomInsideRight = (ImageView) getActivity().findViewById(R.id.imageViewBottomInsideRight);

        ProgressBar progressBarBottomOutsideStartWork = (ProgressBar) getActivity().findViewById(R.id.progressBarBottomOutsideStartWork);
        TextView textViewBottomOutsideAction = (TextView) getActivity().findViewById(R.id.textViewBottomOutsideAction);

        imageViewBottomInsideLeft.setVisibility(View.INVISIBLE);
        textViewBottomInsideAction.setText(getActivity().getString(R.string.bottom_registration));
        imageViewBottomInsideRight.setVisibility(View.VISIBLE);

        progressBarBottomOutsideStartWork.setMax(5);
        progressBarBottomOutsideStartWork.setProgress(2);
        textViewBottomOutsideAction.setText(getActivity().getString(R.string.two_minutes));
    }

    private void initLoginOrRestoreFragment(View view) {
        final EditText editTextEmailLogin = (EditText) view.findViewById(R.id.editTextEmailLogin);
        final EditText editTextPassword = (EditText) view.findViewById(R.id.editTextPassword);
        final EditText editTextEmailRestore = (EditText) view.findViewById(R.id.editTextEmailRestore);
        final Button buttonEnter = (Button) view.findViewById(R.id.buttonEnter);
        final Button buttonRestore = (Button) view.findViewById(R.id.buttonRestore);

        editTextEmailLogin.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                buttonEnter.setEnabled(isValidEmail(s) && editTextPassword.getText().length() != 0);
            }

        });

        editTextPassword.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                buttonEnter.setEnabled(s.length() != 0 && isValidEmail(editTextEmailLogin.getText()));
            }

        });

        editTextEmailRestore.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                buttonRestore.setEnabled(isValidEmail(s));
            }

        });

        buttonEnter.setEnabled(false);
        buttonEnter.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                tryAuthorization();
            }
        });

        buttonRestore.setEnabled(false);
        buttonRestore.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                tryRestore();
            }
        });

        initSocials();
        initLoginOrRestore();
    }

    private void initLoginOrRestore() {
        LinearLayout linearLayoutLogin = (LinearLayout) parentView.findViewById(R.id.linearLayoutLogin);
        LinearLayout linearLayoutRestore = (LinearLayout) parentView.findViewById(R.id.linearLayoutRestore);

        final RelativeLayout relativeLayoutLogin = (RelativeLayout) parentView.findViewById(R.id.relativeLayoutLogin);
        final RelativeLayout relativeLayoutRestore = (RelativeLayout) parentView.findViewById(R.id.relativeLayoutRestore);

        linearLayoutLogin.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                relativeLayoutLogin.setVisibility(View.VISIBLE);
                relativeLayoutRestore.setVisibility(View.GONE);
            }
        });

        linearLayoutRestore.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                relativeLayoutRestore.setVisibility(View.VISIBLE);
                relativeLayoutLogin.setVisibility(View.GONE);
            }
        });
    }

    private void tryAuthorization() {
        User user = pickAuthorizationFields();
        if (user.validate(true)) {
            AppState.getInstatce().setUser(user);

            StartActivity startActivity = (StartActivity) getActivity();
            startActivity.userAuthorization();
        } else {
            Toast.makeText(parentView.getContext(), parentView.getContext().getString(R.string.complete_all_fields), Toast.LENGTH_LONG).show();
        }
    }

    private void tryRestore() {
        String email = pickRestoreFields();
        if (isValidEmail(email)) {
            StartActivity startActivity = (StartActivity) getActivity();
            startActivity.restorePassword(email);
        } else {
            Toast.makeText(parentView.getContext(), parentView.getContext().getString(R.string.complete_all_fields), Toast.LENGTH_LONG).show();
        }
    }

    private User pickAuthorizationFields() {
        User user = new User();

        try {
            EditText editTextEmailLogin = (EditText) getActivity().findViewById(R.id.editTextEmailLogin);
            EditText editTextPassword = (EditText) getActivity().findViewById(R.id.editTextPassword);

            user.setEmail(editTextEmailLogin.getText().toString());
            user.setPlainPasswordFirst(editTextPassword.getText().toString());
            user.setPlainPasswordSecond(editTextPassword.getText().toString());
        } catch (Exception e) {
            // do nothing
        }

        return user;
    }

    private String pickRestoreFields(){
        EditText editTextEmailRestore = (EditText) getActivity().findViewById(R.id.editTextEmailRestore);
        return editTextEmailRestore.getText().toString();
    }

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

    private void initFields(ru.com.cardiomagnil.social.User user) {
        final EditText editTextEmail = (EditText) parentView.findViewById(R.id.editTextEmailLogin);

        if (!user.getEmail().isEmpty()) {
            editTextEmail.setText(user.getEmail());
        }
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
}