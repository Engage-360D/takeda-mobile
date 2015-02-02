package ru.com.cardiomagnil.ui.base;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import ru.com.cardiomagnil.app.R;
import ru.com.cardiomagnil.application.AppSharedPreferences;
import ru.com.cardiomagnil.application.AppState;
import ru.com.cardiomagnil.ca_api.Status;
import ru.com.cardiomagnil.model.common.Error;
import ru.com.cardiomagnil.model.common.Response;
import ru.com.cardiomagnil.model.token.Token;
import ru.com.cardiomagnil.model.token.TokenDao;
import ru.com.cardiomagnil.model.user.User;
import ru.com.cardiomagnil.model.user.UserDao;
import ru.com.cardiomagnil.model.user.UserLgnPwd;
import ru.com.cardiomagnil.ui.start.StartActivity;
import ru.com.cardiomagnil.util.CallbackOne;
import ru.com.cardiomagnil.util.Tools;

public abstract class BaseStartFragment extends Fragment {
    public abstract void initParent(Activity activity);

    protected void startRegistration(final User user) {
        StartActivity startActivity = (StartActivity) getActivity();
        startActivity.showProgressDialog();

        UserDao.register(
                user,
                new CallbackOne<User>() {
                    @Override
                    public void execute(User newUser) {
                        UserLgnPwd userLgnPwd = new UserLgnPwd(user.getEmail(), user.getPlainPassword());
                        startAuthorization(userLgnPwd);
                    }
                },
                new CallbackOne<Response>() {
                    @Override
                    public void execute(Response responseError) {
                        handleRegAuth(null, null, responseError);
                    }
                }
        );
    }

    protected void startAuthorization(final UserLgnPwd userLgnPwd) {
        StartActivity startActivity = (StartActivity) getActivity();
        startActivity.showProgressDialog();

        TokenDao.getByLgnPwd(
                userLgnPwd,
                new CallbackOne<Token>() {
                    @Override
                    public void execute(Token token) {
                        getUser(token);
                    }
                },
                new CallbackOne<Response>() {
                    @Override
                    public void execute(Response responseError) {
                        handleRegAuth(null, null, responseError);
                    }
                }
        );
    }

    protected void getUser(final Token token) {
        StartActivity startActivity = (StartActivity) getActivity();
        startActivity.showProgressDialog();

        UserDao.getByToken(
                token,
                new CallbackOne<User>() {
                    @Override
                    public void execute(User user) {
                        handleRegAuth(token, user, null);
                    }
                },
                new CallbackOne<Response>() {
                    @Override
                    public void execute(Response responseError) {
                        handleRegAuth(null, null, responseError);
                    }
                },
                false
        );
    }

    private void handleRegAuth(Token token, User user, Response responseError) {
        StartActivity startActivity = (StartActivity) getActivity();
        startActivity.hideProgressDialog();

        responseError = (responseError == null || responseError.getError() == null) ?
                new Response.Builder(new Error()).create() :
                responseError;

        if (token == null && user == null) {
            switch (responseError.getError().getCode()) {
                case Status.NO_DATA_ERROR:
                    Tools.showToast(getActivity(), R.string.error_user_not_found, Toast.LENGTH_LONG);
                    break;
                case Status.CONFLICT_ERROR:
                    Tools.showToast(getActivity(), R.string.error_user_already_exist, Toast.LENGTH_LONG);
                    break;
                default:
                    Tools.showToast(getActivity(), R.string.error_occurred, Toast.LENGTH_LONG);
            }

            initAppState(null, null);
        } else {
            initAppState(token, user);
            ((StartActivity) getActivity()).startSlidingMenu();
        }
    }

    private void initAppState(Token token, User user) {
        String tokenId = token == null ? null : token.getTokenId();
        AppSharedPreferences.put(AppSharedPreferences.Preference.tokenId, tokenId);
        AppState.getInstatce().setToken(token);
        AppState.getInstatce().setUser(user);
        AppState.getInstatce().setTestResult((String) AppSharedPreferences.get(AppSharedPreferences.Preference.testResult));
    }

    public abstract void initFieldsFromSocial(ru.com.cardiomagnil.social.User socialUser);
}