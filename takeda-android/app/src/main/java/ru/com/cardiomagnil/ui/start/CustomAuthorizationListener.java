package ru.com.cardiomagnil.ui.start;

import android.widget.Toast;

import ru.com.cardiomagnil.app.R;
import ru.com.cardiomagnil.application.ExeptionsHandler;
import ru.com.cardiomagnil.social.AuthorizationListener;
import ru.com.cardiomagnil.social.User;
import ru.com.cardiomagnil.ui.base.BaseStartFragment;

public class CustomAuthorizationListener implements AuthorizationListener {
    private BaseStartFragment mFragment;
    private Class<? extends ru.com.cardiomagnil.social.User> mUserClass;

    public CustomAuthorizationListener(BaseStartFragment fragment, Class<? extends ru.com.cardiomagnil.social.User> userClass) {
        mFragment = fragment;
        mUserClass = userClass;
    }

    @Override
    public void onAuthorized(String userInfo) {
        try {
            User user = mUserClass.getConstructor(String.class).newInstance(userInfo);
            mFragment.initFieldsFromSocial(user);
            StartActivity startActivity = (StartActivity) mFragment.getActivity();
            startActivity.hideProgressDialog();
        } catch (Exception e) {
            e.printStackTrace();
            ExeptionsHandler.getInstatce().handleException(mFragment.getActivity(), e);
            Toast.makeText(mFragment.getActivity(), R.string.authorization_error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onAuthorizationFailed() {
        StartActivity startActivity = (StartActivity) mFragment.getActivity();
        startActivity.hideProgressDialog();
        Toast.makeText(mFragment.getActivity(), R.string.authorization_error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAuthorizationCanceled() {
        StartActivity startActivity = (StartActivity) mFragment.getActivity();
        startActivity.hideProgressDialog();
    }
}
