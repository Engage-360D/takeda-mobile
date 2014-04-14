package ru.com.cardiomagnil.commands;

import ru.com.cardiomagnil.R;
import com.google.gson.JsonObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.ResultReceiver;
import ru.com.cardiomagnil.api.Api;
import ru.com.cardiomagnil.application.AppState;
import ru.com.cardiomagnil.model.Token;
import ru.evilduck.framework.handlers.SFBaseCommand;

public class UserAuthorization extends SFBaseCommand {

    @Override
    public void doExecute(Intent intent, Context context, ResultReceiver callback) {
        Bundle data = new Bundle();

        String message = userAuthorization(context);
        if (message.isEmpty()) {
            notifySuccess(data);
        } else {
            data.putString("error", message);
            notifyFailure(data);
        }
    }

    private String userAuthorization(Context context) {
        String result = context.getString(R.string.error_authorization);

        try {
            Api api = new Api();
            JsonObject jsonObjectToken = api.userAuthorization(AppState.getInstatce().getAuthorization());
            Token token = new Token(jsonObjectToken);
            if (token.isInitialized()) {
                AppState.getInstatce().setToken(token);
                result =  "";
            }
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

    public static final Parcelable.Creator<UserAuthorization> CREATOR = new Parcelable.Creator<UserAuthorization>() {
        public UserAuthorization createFromParcel(Parcel in) {
            return new UserAuthorization(in);
        }

        public UserAuthorization[] newArray(int size) {
            return new UserAuthorization[size];
        }
    };

    private UserAuthorization(Parcel in) {
    }

    public UserAuthorization() {
    }

}