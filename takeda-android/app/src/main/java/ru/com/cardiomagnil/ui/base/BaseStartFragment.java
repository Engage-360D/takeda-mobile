package ru.com.cardiomagnil.ui.base;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import ru.com.cardiomagnil.app.R;
import ru.com.cardiomagnil.application.AppSharedPreferences;
import ru.com.cardiomagnil.application.AppState;
import ru.com.cardiomagnil.ca_api.Status;
import ru.com.cardiomagnil.ca_model.common.Ca_Error;
import ru.com.cardiomagnil.ca_model.common.Ca_Response;
import ru.com.cardiomagnil.ca_model.token.Ca_Token;
import ru.com.cardiomagnil.ca_model.token.Ca_TokenDao;
import ru.com.cardiomagnil.ca_model.user.Ca_User;
import ru.com.cardiomagnil.ca_model.user.Ca_UserDao;
import ru.com.cardiomagnil.ca_model.user.Ca_UserLgnPwd;
import ru.com.cardiomagnil.social.User;
import ru.com.cardiomagnil.ui.start.StartActivity;
import ru.com.cardiomagnil.util.CallbackOne;

public abstract class BaseStartFragment extends Fragment {
    public abstract void initParent(Activity activity);

    protected void startRegistration(final Ca_User user) {
        StartActivity startActivity = (StartActivity) getActivity();
        startActivity.showProgressDialog();

        Ca_UserDao.register(
                user,
                new CallbackOne<Ca_User>() {
                    @Override
                    public void execute(Ca_User newUser) {
                        Ca_UserLgnPwd userLgnPwd = new Ca_UserLgnPwd(user.getEmail(), user.getPlainPassword());
                        startAuthorization(userLgnPwd);
                    }
                },
                new CallbackOne<Ca_Response>() {
                    @Override
                    public void execute(Ca_Response responseError) {
                        handleRegAuth(null, null, responseError);
                    }
                }
        );
    }

    protected void startAuthorization(final Ca_UserLgnPwd userLgnPwd) {
        StartActivity startActivity = (StartActivity) getActivity();
        startActivity.showProgressDialog();

        Ca_TokenDao.getByLgnPwd(
                userLgnPwd,
                new CallbackOne<Ca_Token>() {
                    @Override
                    public void execute(Ca_Token token) {
                        getUser(token);
                    }
                },
                new CallbackOne<Ca_Response>() {
                    @Override
                    public void execute(Ca_Response responseError) {
                        handleRegAuth(null, null, responseError);
                    }
                }
        );
    }

    protected void getUser(final Ca_Token token) {
        StartActivity startActivity = (StartActivity) getActivity();
        startActivity.showProgressDialog();

        Ca_UserDao.getById(
                token,
                new CallbackOne<Ca_User>() {
                    @Override
                    public void execute(Ca_User user) {
                        handleRegAuth(token, user, null);
                    }
                },
                new CallbackOne<Ca_Response>() {
                    @Override
                    public void execute(Ca_Response responseError) {
                        handleRegAuth(null, null, responseError);
                    }
                },
                false
        );
    }

    private void handleRegAuth(Ca_Token token, Ca_User user, Ca_Response responseError) {
        StartActivity startActivity = (StartActivity) getActivity();
        startActivity.hideProgressDialog();

        responseError = (responseError == null || responseError.getError() == null) ?
                new Ca_Response.Builder(new Ca_Error()).create() :
                responseError;

        if (token == null && user == null) {
            switch (responseError.getError().getCode()) {
                case Status.NO_DATA_ERROR:
                    Toast.makeText(getActivity(), getActivity().getString(R.string.error_user_not_found), Toast.LENGTH_LONG).show();
                    break;
                case Status.CONFLICT_ERROR:
                    Toast.makeText(getActivity(), getActivity().getString(R.string.error_user_already_exist), Toast.LENGTH_LONG).show();
                    break;
                default:
                    Toast.makeText(getActivity(), getActivity().getString(R.string.error_occurred), Toast.LENGTH_LONG).show();
            }

            initAppState(null, null);
        } else {
            initAppState(token, user);
            ((StartActivity) getActivity()).startSlidingMenu();
        }
    }

    private void initAppState(Ca_Token token, Ca_User user) {
        AppState.getInstatce().setToken(token);
        AppState.getInstatce().setUser(user);
        AppSharedPreferences.put(AppSharedPreferences.Preference.tokenId, token.getTokenId());
    }

    public abstract void initFieldsFromSocial(User socialUser);
}