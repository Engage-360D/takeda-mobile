package ru.com.cardiomagnil.commands;

import ru.com.cardiomagnil.R;
import ru.com.cardiomagnil.api.Api;
import ru.com.cardiomagnil.application.CardiomagnilApplication;
import ru.com.cardiomagnil.application.ExeptionsHandler;
import ru.evilduck.framework.handlers.SFBaseCommand;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.ResultReceiver;

import com.google.gson.JsonObject;

public class RestorePassword extends SFBaseCommand {
    String mEmail;

    @Override
    public void doExecute(Intent intent, Context context, ResultReceiver callback) {
        Bundle data = new Bundle();

        String message = restorePassword(context);
        if (message.isEmpty()) {
            notifySuccess(data);
        } else {
            data.putString("error", message);
            notifyFailure(data);
        }
    }

    private String restorePassword(Context context) {
        String result = context.getString(R.string.error_restoring);

        try {
            Api api = new Api();
            JsonObject jsonObjectToken = api.restorePassword(mEmail);

            // TODO: убрать костыль: попросить коллег на том конце переделать API - нужны коды ошибок
            if (jsonObjectToken != null && jsonObjectToken.toString().equals("{}")) {
                result = "";
            } else if (jsonObjectToken != null && jsonObjectToken.toString().equals("{\"error\":\"User not found\"}")) {
                result = context.getString(R.string.error_user_not_found);
            } else if (jsonObjectToken != null && jsonObjectToken.toString().equals("{\"error\":\"Password already requested\"}")) {
                result = context.getString(R.string.error_password_already_requested);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ExeptionsHandler.getInstatce().handleException(CardiomagnilApplication.getAppContext(), e);
        }

        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mEmail);
    }

    public static final Parcelable.Creator<RestorePassword> CREATOR = new Parcelable.Creator<RestorePassword>() {
        public RestorePassword createFromParcel(Parcel in) {
            return new RestorePassword(in);
        }

        public RestorePassword[] newArray(int size) {
            return new RestorePassword[size];
        }
    };

    private RestorePassword(Parcel in) {
        mEmail = in.readString();
    }

    public RestorePassword(String email) {
        mEmail = email;
    }

}