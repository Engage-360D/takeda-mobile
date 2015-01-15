package ru.com.cardiomagnil.ui.start.registration;

import android.content.Context;
import android.view.View;

import ru.com.cardiomagnil.social.AuthorizationDialog;
import ru.com.cardiomagnil.social.AuthorizationListener;
import ru.com.cardiomagnil.social.BaseSocialApi;
import ru.com.cardiomagnil.ui.start.StartActivity;

class SignInWithSocialNetwork implements View.OnClickListener {
    private RegistrationFragment registrationFragment;
    private final Context mContext;
    private final BaseSocialApi mApi;
    private final AuthorizationListener mListener;

    public SignInWithSocialNetwork(RegistrationFragment registrationFragment, Context context, BaseSocialApi api, AuthorizationListener listener) {
        this.registrationFragment = registrationFragment;
        mContext = context;
        mApi = api;
        mListener = listener;
    }

    @Override
    public void onClick(View v) {
        StartActivity startActivity = (StartActivity) registrationFragment.getActivity();
        startActivity.showProgressDialog();

        AuthorizationDialog dialog = new AuthorizationDialog(mContext, mApi);
        dialog.show(mListener);
    }
}
