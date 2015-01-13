package ru.com.cardiomagnil.ui.start.registration;

import android.widget.Toast;

import ru.com.cardiomagnil.app.R;
import ru.com.cardiomagnil.application.ExeptionsHandler;
import ru.com.cardiomagnil.social.AuthorizationListener;
import ru.com.cardiomagnil.ui.start.StartActivity;

class RegisterUserOnFinish implements AuthorizationListener {
    private RegistrationFragment registrationFragment;
    private Class<? extends ru.com.cardiomagnil.social.User> mUserClass;

    public RegisterUserOnFinish(RegistrationFragment registrationFragment, Class<? extends ru.com.cardiomagnil.social.User> userClass) {
        this.registrationFragment = registrationFragment;
        mUserClass = userClass;
    }

    @Override
    public void onAuthorized(String userInfo) {
        try {
            ru.com.cardiomagnil.social.User user = mUserClass.getConstructor(String.class).newInstance(userInfo);
            registrationFragment.initFields(user);
            StartActivity startActivity = (StartActivity) registrationFragment.getActivity();
            startActivity.hideProgressDialog();
        } catch (Exception e) {
            e.printStackTrace();
            ExeptionsHandler.getInstatce().handleException(registrationFragment.getActivity(), e);
            Toast.makeText(registrationFragment.getActivity(), R.string.authorization_error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onAuthorizationFailed() {
        StartActivity startActivity = (StartActivity) registrationFragment.getActivity();
        startActivity.hideProgressDialog();
        Toast.makeText(registrationFragment.getActivity(), R.string.authorization_error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAuthorizationCanceled() {
        StartActivity startActivity = (StartActivity) registrationFragment.getActivity();
        startActivity.hideProgressDialog();
    }
}
