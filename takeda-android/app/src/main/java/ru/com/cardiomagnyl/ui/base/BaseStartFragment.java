package ru.com.cardiomagnyl.ui.base;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import ru.com.cardiomagnyl.api.Status;
import ru.com.cardiomagnyl.app.R;
import ru.com.cardiomagnyl.application.AppSharedPreferences;
import ru.com.cardiomagnyl.application.AppState;
import ru.com.cardiomagnyl.model.common.Error;
import ru.com.cardiomagnyl.model.common.LgnPwd;
import ru.com.cardiomagnyl.model.common.Response;
import ru.com.cardiomagnyl.model.test.TestResult;
import ru.com.cardiomagnyl.model.test.TestResultDao;
import ru.com.cardiomagnyl.model.test_diet.TestDietResult;
import ru.com.cardiomagnyl.model.test_diet.TestDietResultDao;
import ru.com.cardiomagnyl.model.token.Token;
import ru.com.cardiomagnyl.model.token.TokenDao;
import ru.com.cardiomagnyl.model.user.User;
import ru.com.cardiomagnyl.model.user.UserDao;
import ru.com.cardiomagnyl.ui.start.StartActivity;
import ru.com.cardiomagnyl.util.CallbackOne;
import ru.com.cardiomagnyl.util.Tools;

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
                        handleRegAuth(null, null, null, null, responseError);
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
                        handleRegAuth(null, null, null, null, responseError);
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
                        handleRegAuth(null, null, null, null, responseError);
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
                    public void execute(List<TestResult> testResultsList) {
                        TestResult testResult = TestResultDao.getNewestResult(testResultsList);
                        if (testResult == null) handleRegAuth(token, user, null, null, null);
                        else getDietTestResult(token, user, testResult);
                    }
                },
                new CallbackOne<Response>() {
                    @Override
                    public void execute(Response responseError) {
                        handleRegAuth(null, null, null, null, responseError);
                    }
                }
        );
    }

    public void getDietTestResult(final Token token, final User user, final TestResult testResult) {
        TestDietResultDao.getByTestId(
                testResult.getId(),
                new CallbackOne<TestDietResult>() {
                    @Override
                    public void execute(TestDietResult testDietResult) {
                        handleRegAuth(token, user, testResult, testDietResult, null);
                    }
                },
                new CallbackOne<Response>() {
                    @Override
                    public void execute(Response responseError) {
                        handleRegAuth(token, user, testResult, null, responseError);
                    }
                }
        );
    }

    private void handleRegAuth(Token token, User user, TestResult testResult, final TestDietResult testDietResult, Response responseError) {
        StartActivity startActivity = (StartActivity) getActivity();
        startActivity.hideProgressDialog();

        if (token == null && user == null) {
            responseError = (responseError == null || responseError.getError() == null) ?
                    new Response.Builder(new Error()).create() :
                    responseError;

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

            initAppState(null, null, null, null);
        } else {
            initAppState(token, user, testResult, testDietResult);
            ((StartActivity) getActivity()).startSlidingMenu();
        }
    }

    private void initAppState(final Token token, final User user, final TestResult testResult, final TestDietResult testDietResult) {
        String tokenId = token == null ? null : token.getTokenId();
        AppSharedPreferences.put(AppSharedPreferences.Preference.tokenId, tokenId);
        AppState.getInsnatce().setToken(token);
        AppState.getInsnatce().setUser(user);
        AppState.getInsnatce().setTestResult(testResult);
        AppState.getInsnatce().setTestDietResult(testDietResult);
    }

    public abstract void initFieldsFromSocial(ru.com.cardiomagnyl.social.User socialUser);
}