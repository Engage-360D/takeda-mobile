package ru.com.cardiomagnil.application;

import com.fasterxml.jackson.core.type.TypeReference;

import ru.com.cardiomagnil.ca_model.base.BaseModel;
import ru.com.cardiomagnil.ca_model.test.Ca_TestResult;
import ru.com.cardiomagnil.ca_model.test.Ca_TestSource;
import ru.com.cardiomagnil.ca_model.token.Ca_Token;
import ru.com.cardiomagnil.ca_model.user.Ca_User;

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
    private Ca_TestSource mTestSource;
    private Ca_TestResult mTestResult;

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

    public Ca_TestSource getTestSource() {
        return mTestSource;
    }

    public void setTestSource(Ca_TestSource testSource) {
        mTestSource = testSource;
    }

    public Ca_TestResult getTestResult() {
        return mTestResult;
    }

    public void setTestResult(Ca_TestResult testResult) {
        mTestResult = testResult;
    }

    public void setTestResult(String testResult) {
        TypeReference typeReference = new TypeReference<Ca_TestResult>() {
        };
        mTestResult = (Ca_TestResult) BaseModel.stringToObject(testResult, typeReference);
    }
}