package com.cardiomagnil.application;

import com.cardiomagnil.model.Authorization;
import com.cardiomagnil.model.TestResult;
import com.cardiomagnil.model.Token;
import com.cardiomagnil.model.User;

import ru.evilduck.framework.SFApplicationState;

public class AppState {
    // ///////////////////////////////////////////////////////////////
    // Singleton implementation
    // ///////////////////////////////////////////////////////////////
    private static AppState instance;

    private AppState() {
    }

    public static AppState getInstatce() {
        if (instance == null) {
            synchronized (SFApplicationState.class) {
                if (instance == null)
                    instance = new AppState();
            }
        }

        return instance;
    }

    // ///////////////////////////////////////////////////////////////

    private AppSharedPreferences mAppSharedPreferences = new AppSharedPreferences(CardiomagnilApplication.getAppContext());
    private Authorization mAuthorization;
    private TestResult mTestResult;
    private Token mToken;
    private User mUser;

    public AppSharedPreferences getAppSharedPreferences() {
        return mAppSharedPreferences;
    }

    public Authorization getAuthorization() {
        return mAuthorization;
    }

    public void setAuthorization(Authorization authorization) {
        mAuthorization = authorization;
    }

    public TestResult getTestResult() {
        return mTestResult;
    }

    public void setTestResult(TestResult testResult) {
        mTestResult = testResult;
    }

    public Token getToken() {
        return mToken;
    }

    public void setToken(Token token) {
        mToken = token;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }
}