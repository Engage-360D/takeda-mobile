package ru.com.cardiomagnil.application;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;

import ru.com.cardiomagnil.BuildConfig;
import android.content.Context;

public class ExeptionsHandler {
    // ///////////////////////////////////////////////////////////////
    // Singleton implementation
    // ///////////////////////////////////////////////////////////////
    private static ExeptionsHandler instance;

    private ExeptionsHandler() {
    }

    public static ExeptionsHandler getInstatce() {
        if (instance == null) {
            synchronized (ExeptionsHandler.class) {
                if (instance == null)
                    instance = new ExeptionsHandler();
            }
        }

        return instance;
    }

    // ///////////////////////////////////////////////////////////////

    public void handleException(Context context, String message) {
        handleException(context, null, message);
    }

    public void handleException(Context context, Exception e) {
        handleException(context, e, "");
    }

    public void handleException(Context context, Exception e, String message) {
        handleExceptionGoogle(context, e, message);

        if (e != null && BuildConfig.DEBUG) {
            e.printStackTrace();
        }
    }

    private void handleExceptionGoogle(Context context, Exception e, String message) {
        EasyTracker easyTracker = EasyTracker.getInstance(context);

        if (easyTracker != null) {
            AnalyticsExceptionParser analyticsExceptionParser = new AnalyticsExceptionParser();
            String exceptionDescription = (e == null) ? message : analyticsExceptionParser.getDescription(Thread.currentThread().getName(), e, message);
            Boolean isFatal = false;

            easyTracker.send(MapBuilder.createException(exceptionDescription, isFatal).build());
        }
    }
}