package ru.com.cardiomagnyl.ui.start;

import android.support.v4.app.Fragment;
import android.view.View;

import ru.com.cardiomagnyl.social.AuthorizationDialog;
import ru.com.cardiomagnyl.social.AuthorizationListener;
import ru.com.cardiomagnyl.social.BaseSocialApi;

public class SignInWithSocialNetwork implements View.OnClickListener {
    private final Fragment mFragment;
    private final BaseSocialApi mApi;
    private final AuthorizationListener mListener;

    public SignInWithSocialNetwork(Fragment fragment, BaseSocialApi api, AuthorizationListener listener) {
        mFragment = fragment;
        mApi = api;
        mListener = listener;
    }

    @Override
    public void onClick(View v) {
        StartActivity startActivity = (StartActivity) mFragment.getActivity();
        startActivity.showProgressDialog();

        AuthorizationDialog dialog = new AuthorizationDialog(startActivity, mApi);
        dialog.show(mListener);
    }
}
