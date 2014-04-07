package com.cardiomagnil.commands;

import ru.evilduck.framework.handlers.SFBaseCommand;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.ResultReceiver;

import com.cardiomagnil.R;
import com.cardiomagnil.api.Api;
import com.cardiomagnil.application.AppState;
import com.google.gson.JsonObject;

public class UserRegistration extends SFBaseCommand {

    @Override
    public void doExecute(Intent intent, Context context, ResultReceiver callback) {
        Bundle data = new Bundle();

        String message = userRegistration(context);
        if (message.isEmpty()) {
            notifySuccess(data);
        } else {
            data.putString("error", message);
            notifyFailure(data);
        }
    }

    private String userRegistration(Context context) {
        String result = context.getString(R.string.error_registration);

        try {
            Api api = new Api();
            JsonObject jsonObjectUser = api.userRegistration(AppState.getInstatce().getUser());

            if (jsonObjectUser != null && jsonObjectUser.has("id")) {
                result =  "";
            }

//            TODO: USER
//            User user = new User(jsonObjectUser);
//            if (user.isInitialized()) {
//                AppState.getInstatce().setUser(user);
//                result =  "";
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public static final Parcelable.Creator<UserRegistration> CREATOR = new Parcelable.Creator<UserRegistration>() {
        public UserRegistration createFromParcel(Parcel in) {
            return new UserRegistration(in);
        }

        public UserRegistration[] newArray(int size) {
            return new UserRegistration[size];
        }
    };

    private UserRegistration(Parcel in) {
    }

    public UserRegistration() {
    }

}