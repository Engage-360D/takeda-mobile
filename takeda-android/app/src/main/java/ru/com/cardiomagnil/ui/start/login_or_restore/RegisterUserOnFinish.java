package ru.com.cardiomagnil.ui.start.login_or_restore;

import android.widget.Toast;

import ru.com.cardiomagnil.app.R;
import ru.com.cardiomagnil.application.ExeptionsHandler;
import ru.com.cardiomagnil.social.AuthorizationListener;
import ru.com.cardiomagnil.ui.start.StartActivity;

class RegisterUserOnFinish implements AuthorizationListener {
    private LoginOrRestoreFragment loginOrRestoreFragment;
    private Class<? extends ru.com.cardiomagnil.social.User> mUserClass;

    public RegisterUserOnFinish(LoginOrRestoreFragment loginOrRestoreFragment, Class<? extends ru.com.cardiomagnil.social.User> userClass) {
        this.loginOrRestoreFragment = loginOrRestoreFragment;
        mUserClass = userClass;
    }

    @Override
    public void onAuthorized(String userInfo) {
        try {
            ru.com.cardiomagnil.social.User user = mUserClass.getConstructor(String.class).newInstance(userInfo);
            // FIXME
//            loginOrRestoreFragment.initFields(user);
            StartActivity startActivity = (StartActivity) loginOrRestoreFragment.getActivity();
            startActivity.hideProgressDialog();
        } catch (Exception e) {
            e.printStackTrace();
            ExeptionsHandler.getInstatce().handleException(loginOrRestoreFragment.getActivity(), e);
            // FIXME
//            Toast.makeText(loginOrRestoreFragment.parentView.getContext(), R.string.authorization_error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onAuthorizationFailed() {
        StartActivity startActivity = (StartActivity) loginOrRestoreFragment.getActivity();
        startActivity.hideProgressDialog();
        // FIXME
//        Toast.makeText(loginOrRestoreFragment.parentView.getContext(), R.string.authorization_error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAuthorizationCanceled() {
        StartActivity startActivity = (StartActivity) loginOrRestoreFragment.getActivity();
        startActivity.hideProgressDialog();
    }
}
