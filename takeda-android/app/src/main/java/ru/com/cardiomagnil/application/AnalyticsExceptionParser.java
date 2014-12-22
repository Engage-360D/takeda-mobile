package ru.com.cardiomagnil.application;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.util.Log;

import com.google.analytics.tracking.android.ExceptionParser;

public class AnalyticsExceptionParser implements ExceptionParser {

    public String getDescription(String p_thread, Throwable p_throwable, String helpMessage) {
        return /*     */formatExceptionThread(p_thread) + //
                formatExceptionErrorMeaasge(Log.getStackTraceString(p_throwable)) + //
                formatExceptionHelpMeaasge(helpMessage) + //
                getExceptionModel();
    }

    @Override
    public String getDescription(String p_thread, Throwable p_throwable) {
        return /*     */formatExceptionThread(p_thread) + //
                formatExceptionErrorMeaasge(Log.getStackTraceString(p_throwable)) + //
                getExceptionModel();
    }

    private String formatExceptionErrorMeaasge(String exceptionErrorMessage) {
        return (exceptionErrorMessage == null || exceptionErrorMessage.isEmpty()) ? ("") : ("Error: " + exceptionErrorMessage.trim() + ";\n");
    }

    private String formatExceptionHelpMeaasge(String exceptionHelpMessage) {
        return (exceptionHelpMessage == null || exceptionHelpMessage.isEmpty()) ? ("") : ("Message: " + exceptionHelpMessage.trim() + ";\n");
    }

    private String formatExceptionThread(String exceptionThread) {
        return "Thread: " + exceptionThread + ";\n";
    }

    private String getExceptionModel() {
        return "Model: " + (android.os.Build.MODEL.equals("") ? "Unknown" : android.os.Build.MODEL) + ";\n";
    }

    @SuppressWarnings("unused")
    private String getExceptionTime() {
        return "Time: " + getFormatedCurrentTime() + ";\n";
    }

    private String getFormatedCurrentTime() {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");

        return dateFormat.format(date);
    }

}
