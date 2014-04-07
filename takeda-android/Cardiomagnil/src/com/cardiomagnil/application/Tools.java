package com.cardiomagnil.application;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.inputmethod.InputMethodManager;

public class Tools {
    public static void hideKeyboard(Activity currentActivity) {
        try {
            InputMethodManager inputManager = (InputMethodManager) currentActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(currentActivity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            // do nothing
        }
    }

    public static JsonElement jsonElementByMemberName(JsonObject jsonObject, String memberName) {
        if (jsonObject == null || !jsonObject.isJsonObject() || memberName == null || memberName.isEmpty()) {
            return null;
        }

        JsonElement jsonElement = null;

        if (jsonObject.has(memberName)) {
            jsonElement = jsonObject.get(memberName);
            jsonElement = jsonElement.isJsonNull() ? null : jsonElement;
        }

        return jsonElement;
    }

    public static void showAlertDialog(final Activity activity, String message, final boolean fnish) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setMessage(message)
            /**/.setCancelable(false)
            /**/.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    if (fnish) {
                        activity.finish();
                    }
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        } catch (Exception e) {
            // TODO: ExeptionsHandler
            e.printStackTrace();
            // ExeptionsHandler.getInstatce().handleException(TheHillApplication.getAppContext(), e);
        }
    }

    public static String formatDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String formatedDate = dateFormat.format(date);
        formatedDate = Character.toUpperCase(formatedDate.charAt(0)) + formatedDate.substring(1);

        return formatedDate;
    }

}
