package ru.com.cardiomagnil.application;

import ru.com.cardiomagnil.ca_model.test.Ca_TestSource;
import ru.com.cardiomagnil.ca_model.token.Ca_Token;
import ru.com.cardiomagnil.ca_model.user.Ca_User;
import ru.com.cardiomagnil.model.TestIncoming;
import ru.com.cardiomagnil.model.TestResult;
import ru.com.cardiomagnil.model.TestResultPage;

public class AppState {
    // ///////////////////////////////////////////////////////////////
    // Singleton implementation
    // ///////////////////////////////////////////////////////////////
    private static AppState instance;

    private AppState() {
    }

    public static AppState getInstatce() {
        if (instance == null) {
            synchronized (AppState.class) {
                if (instance == null)
                    instance = new AppState();
            }
        }

        return instance;
    }

    // ///////////////////////////////////////////////////////////////

    private Ca_Token mToken;
    private Ca_User mUser;

    public Ca_Token getToken() {
        return mToken;
    }

    public void setToken(Ca_Token token) {
        mToken = token;
    }

    public Ca_User getUser() {
        return mUser;
    }

    public void setUser(Ca_User user) {
        mUser = user;
    }


    // *****************************************************************

    private Ca_TestSource mTestSource;
    private TestResult mTestResult;
    private TestResultPage mTestResultPage;

    public Ca_TestSource getTestIncoming() {
        return mTestSource;
    }

    public void setTestSource(Ca_TestSource testSource) {
        mTestSource = testSource;
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