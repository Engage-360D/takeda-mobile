package ru.com.cardiomagnil.commands;

import ru.com.cardiomagnil.api.Api;
import ru.com.cardiomagnil.application.AppState;
import ru.com.cardiomagnil.application.CardiomagnilApplication;
import ru.com.cardiomagnil.application.ExeptionsHandler;
import ru.com.cardiomagnil.model.TestResult;
import ru.evilduck.framework.handlers.SFBaseCommand;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.ResultReceiver;

import ru.com.cardiomagnil.app.R;
import com.google.gson.JsonObject;

public class GetTestResults extends SFBaseCommand {

    @Override
    public void doExecute(Intent intent, Context context, ResultReceiver callback) {
        Bundle data = new Bundle();

        String message = getTestResults(context);
        if (message.isEmpty()) {
            notifySuccess(data);
        } else {
            data.putString("error", message);
            notifyFailure(data);
        }
    }

    // FIXME
    private String getTestResults(Context context) {
        String result = context.getString(R.string.error_get_test_results);

//        try {
//            Api api = new Api();
//            JsonObject jsonObjectTestResult = api.testResults(AppState.getInstatce().getTestIncoming(), AppState.getInstatce().getToken());
//
//            TestResult testResult = new TestResult(jsonObjectTestResult);
//            if (testResult.isInitialized()) {
//                AppState.getInstatce().setTestResult(testResult);
//                result = "";
//                // TODO: убрать костыль: попросить коллег на том конце переделать API - нужны коды ошибок
//            } else if (jsonObjectTestResult.toString().equals("{\"_form\":[\"Test already passed by user.\"]}")) {
//                result = context.getString(R.string.error_tets_already_passed);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            ExeptionsHandler.getInstatce().handleException(CardiomagnilApplication.getAppContext(), e);
//        }

        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public static final Parcelable.Creator<GetTestResults> CREATOR = new Parcelable.Creator<GetTestResults>() {
        public GetTestResults createFromParcel(Parcel in) {
            return new GetTestResults(in);
        }

        public GetTestResults[] newArray(int size) {
            return new GetTestResults[size];
        }
    };

    private GetTestResults(Parcel in) {
    }

    public GetTestResults() {
    }

}