package ru.com.cardiomagnil.ui.base;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

import ru.com.cardiomagnil.app.R;
import ru.com.cardiomagnil.application.AppSharedPreferences;
import ru.com.cardiomagnil.application.AppState;
import ru.com.cardiomagnil.ca_api.Status;
import ru.com.cardiomagnil.model.common.Error;
import ru.com.cardiomagnil.model.common.LgnPwd;
import ru.com.cardiomagnil.model.common.Response;
import ru.com.cardiomagnil.model.test.TestResult;
import ru.com.cardiomagnil.model.test.TestResultDao;
import ru.com.cardiomagnil.model.token.Token;
import ru.com.cardiomagnil.model.token.TokenDao;
import ru.com.cardiomagnil.model.user.User;
import ru.com.cardiomagnil.model.user.UserDao;
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
                        LgnPwd lgnPwd = new LgnPwd(user.getEmail(), user.getPlainPassword());
                        startAuthorization(lgnPwd);
                    }
                },
                new CallbackOne<Response>() {
                    @Override
                    public void execute(Response responseError) {
                        handleRegAuth(null, null, null, responseError);
                    }
                }
        );
    }

    protected void startAuthorization(final LgnPwd lgnPwd) {
        StartActivity startActivity = (StartActivity) getActivity();
        startActivity.showProgressDialog();

        TokenDao.getByLgnPwd(
                lgnPwd,
                new CallbackOne<Token>() {
                    @Override
                    public void execute(Token token) {
                        getUser(token);
                    }
                },
                new CallbackOne<Response>() {
                    @Override
                    public void execute(Response responseError) {
                        handleRegAuth(null, null, null, responseError);
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
                        getTestResult(token, user);
                    }
                },
                new CallbackOne<Response>() {
                    @Override
                    public void execute(Response responseError) {
                        handleRegAuth(null, null, null, responseError);
                    }
                },
                false
        );
    }

    public void getTestResult(final Token token, final User user) {
        TestResultDao.getAll(
                token,
                new CallbackOne<List<TestResult>>() {
                    @Override
                    public void execute(List<TestResult> testResultList) {
                        handleRegAuth(token, user, getNewestResult(testResultList), null);
                    }
                },
                new CallbackOne<Response>() {
                    @Override
                    public void execute(Response responseError) {
                        handleRegAuth(null, null, null, responseError);
                    }
                }
        );
    }

    private TestResult getNewestResult(List<TestResult> testResultList) {
        TestResult newestResult = (testResultList == null || testResultList.isEmpty()) ? null : testResultList.get(0);
        if (newestResult != null) {
            for (TestResult currentTestResult : testResultList) {
                Date currentDate = Tools.dateFromFullDate(currentTestResult.getCreatedAt());
                Date newestDate = Tools.dateFromFullDate(newestResult.getCreatedAt());
                newestResult = currentDate.after(newestDate) ? currentTestResult : newestResult;
            }
        }
        return newestResult;
    }

    private void handleRegAuth(Token token, User user, TestResult testResult, Response responseError) {
        StartActivity startActivity = (StartActivity) getActivity();
        startActivity.hideProgressDialog();

        responseError = (responseError == null || responseError.getError() == null) ?
                new Response.Builder(new Error()).create() :
                responseError;

        if (token == null && user == null && testResult == null) {
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

            initAppState(null, null, null);
        } else {
            initAppState(token, user, testResult);
            ((StartActivity) getActivity()).startSlidingMenu();
        }
    }

    private void initAppState(Token token, User user, TestResult testResult) {
        String tokenId = token == null ? null : token.getTokenId();
        AppSharedPreferences.put(AppSharedPreferences.Preference.tokenId, tokenId);
        AppState.getInstatce().setToken(token);
        AppState.getInstatce().setUser(user);
        AppState.getInstatce().setTestResult(testResult);
    }

    public abstract void initFieldsFromSocial(ru.com.cardiomagnil.social.User socialUser);
}