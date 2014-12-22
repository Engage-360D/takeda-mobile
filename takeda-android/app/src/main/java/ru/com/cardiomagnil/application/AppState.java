package ru.com.cardiomagnil.application;

import ru.com.cardiomagnil.model.Authorization;
import ru.com.cardiomagnil.model.TestIncoming;
import ru.com.cardiomagnil.model.TestResult;
import ru.com.cardiomagnil.model.TestResultPage;
import ru.com.cardiomagnil.model.Token;
import ru.com.cardiomagnil.model.User;
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

    private Authorization mAuthorization;
    private TestIncoming mTestIncoming;
    private Token mToken;
    private User mUser;
    private TestResult mTestResult;
    private TestResultPage mTestResultPage;

    public Authorization getAuthorization() {
        return mAuthorization;
    }

    public void setAuthorization(Authorization authorization) {
        mAuthorization = authorization;
    }

    public TestIncoming getTestIncoming() {
        return mTestIncoming;
    }

    public void setTestIncoming(TestIncoming testIncoming) {
        mTestIncoming = testIncoming;
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

    public TestResult getTestResult() {
        return mTestResult;
    }

    public void setTestResult(TestResult testResult) {
        mTestResult = testResult;
    }

    public TestResultPage getTestResultPage() {
        return mTestResultPage;
    }

    public void setTestResultPage(TestResultPage testResultPage) {
        mTestResultPage = testResultPage;
    }
}