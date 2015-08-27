package ru.com.cardiomagnyl.ui.base;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.widget.Toast;

import com.github.gorbin.asne.core.persons.SocialPerson;

import java.util.List;

import ru.com.cardiomagnyl.api.Status;
import ru.com.cardiomagnyl.app.R;
import ru.com.cardiomagnyl.application.AppSharedPreferences;
import ru.com.cardiomagnyl.application.AppState;
import ru.com.cardiomagnyl.model.common.Error;
import ru.com.cardiomagnyl.model.common.Response;
import ru.com.cardiomagnyl.model.incidents.Incidents;
import ru.com.cardiomagnyl.model.incidents.IncidentsDao;
import ru.com.cardiomagnyl.model.social.Social;
import ru.com.cardiomagnyl.model.test.TestResult;
import ru.com.cardiomagnyl.model.test.TestResultDao;
import ru.com.cardiomagnyl.model.test_diet.TestDietResult;
import ru.com.cardiomagnyl.model.test_diet.TestDietResultDao;
import ru.com.cardiomagnyl.model.token.Token;
import ru.com.cardiomagnyl.model.token.TokenDao;
import ru.com.cardiomagnyl.model.user.Isr;
import ru.com.cardiomagnyl.model.user.LgnPwd;
import ru.com.cardiomagnyl.model.user.User;
import ru.com.cardiomagnyl.model.user.UserDao;
import ru.com.cardiomagnyl.ui.start.StartActivity;
import ru.com.cardiomagnyl.util.CallbackOne;
import ru.com.cardiomagnyl.util.Tools;

public abstract class BaseStartFragment extends Fragment {
    public abstract void initParent(Activity activity);

    public abstract void initSocials(StartActivity startActivity);

    public abstract void initFieldsFromSocial(int networkId, SocialPerson socialPerson);

    protected void startRegistration(final User user) {
        StartActivity startActivity = (StartActivity) getActivity();
        if (startActivity == null) return;
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
                        handleRegAuth(null, null, null, null, null, null, responseError);
                    }
                }
        );
    }

    protected void startAuthorization(final LgnPwd lgnPwd) {
        StartActivity startActivity = (StartActivity) getActivity();
        if (startActivity == null) return;
        startActivity.showProgressDialog();

        TokenDao.getByLgnPwd(
                lgnPwd,
                new CallbackOne<Token>() {
                    @Override
                    public void execute(Token token) {
                        getUserWeb(token);
                    }
                },
                new CallbackOne<Response>() {
                    @Override
                    public void execute(Response responseError) {
                        handleRegAuth(null, null, null, null, null, null, responseError);
                    }
                }
        );
    }

    protected void startAuthorization(final int networkId, final Social social) {
        StartActivity startActivity = (StartActivity) getActivity();
        if (startActivity == null) return;
        startActivity.showProgressDialog();

        TokenDao.getBySocial(
                networkId,
                social,
                new CallbackOne<Token>() {
                    @Override
                    public void execute(Token token) {
                        getUserWeb(token);
                    }
                },
                new CallbackOne<Response>() {
                    @Override
                    public void execute(Response responseError) {
                        handleRegAuth(null, null, null, null, null, null, responseError);
                    }
                }
        );
    }

    protected void getUserWeb(final Token token) {
        StartActivity startActivity = (StartActivity) getActivity();
        if (startActivity == null) return;
        startActivity.showProgressDialog();

        UserDao.getByToken(
                token,
                new CallbackOne<User>() {
                    @Override
                    public void execute(User user) {
                        getIsr(token, user);
                    }
                },
                new CallbackOne<Response>() {
                    @Override
                    public void execute(Response responseError) {
                        handleRegAuth(token, null, null, null, null, null, responseError);
                    }
                },
                UserDao.Source.web
        );
    }

    private void getIsr(final Token token, final User user) {
        UserDao.getIsr(
                token,
                new CallbackOne<Isr>() {
                    @Override
                    public void execute(Isr isr) {
                        getIncidents(token, user, isr);
                    }
                },
                new CallbackOne<Response>() {
                    @Override
                    public void execute(Response responseError) {
                        String isrId = (String) AppSharedPreferences.get(AppSharedPreferences.Preference.isr);
                        Isr isr = new Isr();
                        isr.setId(TextUtils.isEmpty(isrId) ? "0" : isrId);
                        getIncidents(token, user, isr);
                    }
                }
        );
    }

    private void getIncidents(final Token token, final User user, final Isr isr) {
        IncidentsDao.getByToken(
                token,
                new CallbackOne<Incidents>() {
                    @Override
                    public void execute(Incidents incidents) {
                        getTestResult(token, user, isr, incidents);
                    }
                },
                new CallbackOne<Response>() {
                    @Override
                    public void execute(Response responseError) {
                        if (responseError.getError().getCode() == Status.CONFLICT_ERROR) {
                            getTestResult(token, user, isr, new Incidents());
                        } else {
                            handleRegAuth(token, user, isr, null, null, null, responseError);
                        }
                    }
                }
        );
    }

    public void getTestResult(final Token token, final User user, final Isr isr, final Incidents incidents) {
        TestResultDao.getAll(
                token,
                new CallbackOne<List<TestResult>>() {
                    @Override
                    public void execute(List<TestResult> testResultsList) {
                        TestResult testResult = TestResultDao.getNewestResult(testResultsList);
                        if (testResult == null)
                            handleRegAuth(token, user, isr, incidents, null, null, null);
                        else getDietTestResult(token, user, isr, incidents, testResult);
                    }
                },
                new CallbackOne<Response>() {
                    @Override
                    public void execute(Response responseError) {
                        handleRegAuth(null, null, null, null, null, null, responseError);
                    }
                }
        );
    }

    public void getDietTestResult(final Token token, final User user, final Isr isr, final Incidents incidents, final TestResult testResult) {
        TestDietResultDao.getByTestId(
                testResult.getId(),
                new CallbackOne<TestDietResult>() {
                    @Override
                    public void execute(TestDietResult testDietResult) {
                        handleRegAuth(token, user, isr, incidents, testResult, testDietResult, null);
                    }
                },
                new CallbackOne<Response>() {
                    @Override
                    public void execute(Response responseError) {
                        handleRegAuth(token, user, isr, incidents, testResult, null, responseError);
                    }
                }
        );
    }

    private void handleRegAuth(final Token token,
                               final User user,
                               final Isr isr,
                               final Incidents incidents,
                               final TestResult testResult,
                               final TestDietResult testDietResult,
                               Response responseError) {
        StartActivity startActivity = (StartActivity) getActivity();
        if (startActivity == null) return;
        startActivity.hideProgressDialog();

        if (token == null || user == null || incidents == null) {
            responseError = (responseError == null || responseError.getError() == null) ?
                    new Response.Builder(new Error()).create() :
                    responseError;

            switch (responseError.getError().getCode()) {
                case Status.NOT_FOUND_ERROR:
                    Tools.showToast(getActivity(), R.string.error_user_not_found, Toast.LENGTH_LONG);
                    break;
                case Status.NO_DATA_ERROR:
                    Tools.showToast(getActivity(), R.string.error_user_not_found, Toast.LENGTH_LONG);
                    break;
                case Status.CONFLICT_ERROR:
                    Tools.showToast(getActivity(), R.string.error_user_already_exist, Toast.LENGTH_LONG);
                    break;
                default:
                    Tools.showToast(getActivity(), R.string.error_occurred, Toast.LENGTH_LONG);
            }

            initAppState(null, null, null, null, null, null);
        } else {
            initAppState(token, user, isr, incidents, testResult, testDietResult);
            ((StartActivity) getActivity()).startSlidingMenu();
        }
    }

    private void initAppState(final Token token,
                              final User user,
                              final Isr isr,
                              final Incidents incidents,
                              final TestResult testResult,
                              final TestDietResult testDietResult) {
        String tokenId = token == null ? null : token.getTokenId();
        String isrId = (isr == null || TextUtils.isEmpty(isr.getId())) ? "0" : isr.getId();
        AppSharedPreferences.put(AppSharedPreferences.Preference.tokenId, tokenId);
        AppSharedPreferences.put(AppSharedPreferences.Preference.isr, isrId);
        AppState.getInsnatce().setToken(token);
        AppState.getInsnatce().setUser(user);
        AppState.getInsnatce().setIsr(isr);
        AppState.getInsnatce().setIncidents(incidents);
        AppState.getInsnatce().setTestResult(testResult);
        AppState.getInsnatce().setTestDietResult(testDietResult);
    }

}