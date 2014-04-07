package ru.evilduck.framework;

import android.app.Application;

public class SFApplicationState {
    // ///////////////////////////////////////////////////////////////
    // Singleton implementation
    // ///////////////////////////////////////////////////////////////
    private static SFApplicationState instance;

    private SFApplicationState() {
    }

    public static SFApplicationState getInstatce() {
        if (instance == null) {
            synchronized (SFApplicationState.class) {
                if (instance == null)
                    instance = new SFApplicationState();
            }
        }

        return instance;
    }

    // ///////////////////////////////////////////////////////////////

    private Application mApplication = null;
    private SFServiceHelper mServiceHelper = null;
    private boolean mIsInitialized = false;

    /**
     * Sets: Application, SFServiceHelper, IsInitialized flag
     */
    public void initialize(Application application) {
        mApplication = application;
        mServiceHelper = new SFServiceHelper(application);
        mIsInitialized = true;
    }

    public boolean isInitialized() {
        return mIsInitialized;
    }

    public Application getApplication() {
        return mApplication;
    }

    public void setServiceHelper(SFServiceHelper serviceHelper) {
        mServiceHelper = serviceHelper;
    }

    public SFServiceHelper getServiceHelper() {
        return mServiceHelper;
    }

}
