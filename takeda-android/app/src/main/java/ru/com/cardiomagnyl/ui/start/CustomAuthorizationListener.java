package ru.com.cardiomagnyl.ui.start;

import android.widget.Toast;

import ru.com.cardiomagnyl.app.R;
import ru.com.cardiomagnyl.application.ExceptionsHandler;
import ru.com.cardiomagnyl.social.AuthorizationListener;
import ru.com.cardiomagnyl.social.User;
import ru.com.cardiomagnyl.ui.base.BaseStartFragment;
import ru.com.cardiomagnyl.util.Tools;

public class CustomAuthorizationListener implements AuthorizationListener {
    private BaseStartFragment mFragment;
    private Class<? extends ru.com.cardiomagnyl.social.User> mUserClass;

    public CustomAuthorizationListener(BaseStartFragment fragment, Class<? extends ru.com.cardiomagnyl.social.User> userClass) {
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
            ExceptionsHandler.getInstatce().handleException(mFragment.getActivity(), e);
            Tools.showToast(mFragment.getActivity(), R.string.authorization_error, Toast.LENGTH_LONG);
        }
    }

    @Override
    public void onAuthorizationFailed() {
        StartActivity startActivity = (StartActivity) mFragment.getActivity();
        startActivity.hideProgressDialog();
        Tools.showToast(mFragment.getActivity(), R.string.authorization_error, Toast.LENGTH_LONG);
    }

    @Override
    public void onAuthorizationCanceled() {
        StartActivity startActivity = (StartActivity) mFragment.getActivity();
        startActivity.hideProgressDialog();
    }
}
