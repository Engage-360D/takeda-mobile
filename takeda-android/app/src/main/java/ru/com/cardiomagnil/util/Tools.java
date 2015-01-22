package ru.com.cardiomagnil.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.widget.RadioGroup;
import android.widget.ToggleButton;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import ru.com.cardiomagnil.application.CardiomagnilApplication;
import ru.com.cardiomagnil.application.ExeptionsHandler;

public class Tools {
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
            e.printStackTrace();
            ExeptionsHandler.getInstatce().handleException(CardiomagnilApplication.getAppContext(), e);
        }
    }

    public static String formatShortDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String formatedDate = dateFormat.format(date);
        formatedDate = Character.toUpperCase(formatedDate.charAt(0)) + formatedDate.substring(1);

        return formatedDate;
    }

    public static String formatFullDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        String formatedDate = dateFormat.format(date);
        return formatedDate;
    }

    public static Calendar calendarFromShort(String shortDate) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();

        try {
            date = dateFormat.parse(shortDate);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        calendar.setTime(date);

        return calendar;
    }

    public static RadioGroup.OnCheckedChangeListener ToggleListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(final RadioGroup radioGroup, final int i) {
            onCheckedChangedHelper(radioGroup, i);
        }
    };

    public static void onCheckedChangedHelper(final RadioGroup radioGroup, final int i) {
        for (int j = 0; j < radioGroup.getChildCount(); j++) {
            final ToggleButton view = (ToggleButton) radioGroup.getChildAt(j);
            view.setChecked(view.getId() == i);
        }
    }

    public static String getAppKeyHashB64() {
        String keyHashB64 = "";

        try {
            PackageInfo info = CardiomagnilApplication.getAppContext().getPackageManager().getPackageInfo("ru.com.cardiomagnil", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                byte[] digest = md.digest();
                keyHashB64 = Base64.encodeToString(digest, Base64.DEFAULT);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ExeptionsHandler.getInstatce().handleException(CardiomagnilApplication.getAppContext(), e);
        }

        return keyHashB64;
    }

    public static boolean isValidEmail(CharSequence target) {
        if (target == null || target.length() == 0) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
}
