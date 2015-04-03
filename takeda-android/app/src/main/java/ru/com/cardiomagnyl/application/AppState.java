package ru.com.cardiomagnyl.application;

import com.fasterxml.jackson.core.type.TypeReference;

import ru.com.cardiomagnyl.model.base.BaseModel;
import ru.com.cardiomagnyl.model.incidents.Incidents;
import ru.com.cardiomagnyl.model.test.TestResult;
import ru.com.cardiomagnyl.model.test.TestSource;
import ru.com.cardiomagnyl.model.test_diet.TestDietResult;
import ru.com.cardiomagnyl.model.token.Token;
import ru.com.cardiomagnyl.model.user.Isr;
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
    private Isr mIsr;
    private Incidents mIncidents;
    private TestResult mTestResult;
    private TestDietResult mTestDietResult;
    private int mTimelineEvents;

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

    public Isr getIsr() {
        return mIsr;
    }

    public void setIsr(Isr isr) {
        mIsr = isr;
    }

    public void setIncidents(Incidents incidents) {
        mIncidents = incidents;
    }

    public Incidents getIncidents() {
        return mIncidents;
    }

    public TestResult getTestResult() {
        return mTestResult;
    }

    public void setTestResult(TestResult testResult) {
        mTestResult = testResult;
    }

    public TestDietResult getTestDietResult() {
        return mTestDietResult;
    }

    public void setTestDietResult(TestDietResult testDietResult) {
        mTestDietResult = testDietResult;
    }

    public int getTimelineEvents() {
        return mTimelineEvents;
    }

    public void setTimelineEvents(int timelineEvents) {
        mTimelineEvents = timelineEvents;
    }

    public boolean isInitialized() {
        return mToken != null && mUser != null && mTestResult != null && mIncidents != null;
    }

}