package ru.com.cardiomagnil.ui.base;

import android.app.Activity;
import android.support.v4.app.Fragment;

import ru.com.cardiomagnil.ca_model.common.Ca_Response;
import ru.com.cardiomagnil.ca_model.token.Ca_Token;
import ru.com.cardiomagnil.ca_model.token.Ca_TokenDao;
import ru.com.cardiomagnil.ca_model.user.Ca_User;
import ru.com.cardiomagnil.ca_model.user.Ca_UserDao;
import ru.com.cardiomagnil.ca_model.user.Ca_UserLgnPwd;
import ru.com.cardiomagnil.util.CallbackOne;

public abstract class BaseStartFragment extends Fragment {
    public abstract void initParent(Activity activity);

    protected void startRegistration(final Ca_User user) {
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
                        handleRegAuth(null, null);
                    }
                }
        );
    }

    protected void startAuthorization(final Ca_UserLgnPwd userLgnPwd) {
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
                        handleRegAuth(null, null);
                    }
                }
        );
    }

    protected void getUser(final Ca_Token token) {
        Ca_UserDao.getById(
                token,
                new CallbackOne<Ca_User>() {
                    @Override
                    public void execute(Ca_User user) {
                        handleRegAuth(token, user);
                    }
                },
                new CallbackOne<Ca_Response>() {
                    @Override
                    public void execute(Ca_Response responseError) {
                        handleRegAuth(null, null);
                    }
                },
                false
        );
    }

    public abstract void handleRegAuth(Ca_Token token, Ca_User user);

}