package ru.com.cardiomagnyl.application;

import com.fasterxml.jackson.core.type.TypeReference;

import ru.com.cardiomagnyl.model.base.BaseModel;
import ru.com.cardiomagnyl.model.test.TestResult;
import ru.com.cardiomagnyl.model.test.TestSource;
import ru.com.cardiomagnyl.model.token.Token;
import ru.com.cardiomagnyl.model.user.User;

public class AppState {
    // ///////////////////////////////////////////////////////////////
    // Singleton implementation
    // ///////////////////////////////////////////////////////////////
    private static AppState instance;

    private AppState() {
    }

    public static AppState getInsnatce() {
        if (instance == null) {
            synchronized (AppState.class) {
                if (instance == null)
                    instance = new AppState();
            }
        }

        return instance;
    }

    // ///////////////////////////////////////////////////////////////

    private Token mToken;
    private User mUser;
    private TestSource mTestSource;
    private TestResult mTestResult;

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

    public TestSource getTestSource() {
        return mTestSource;
    }

    public void setTestSource(TestSource testSource) {
        mTestSource = testSource;
    }

    public TestResult getTestResult() {
        return mTestResult;
    }

    public void setTestResult(TestResult testResult) {
        mTestResult = testResult;
    }

    public void setTestResult(String testResult) {
        TypeReference typeReference = new TypeReference<TestResult>() {
        };
        mTestResult = (TestResult) BaseModel.stringToObject(testResult, typeReference);
    }
}