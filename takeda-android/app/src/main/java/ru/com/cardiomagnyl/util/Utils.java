package ru.com.cardiomagnyl.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import ru.com.cardiomagnyl.application.CardiomagnylApplication;

public class Utils {
    public static void hideKeyboard(Activity currentActivity) {
        try {
            InputMethodManager inputManager = (InputMethodManager) currentActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(currentActivity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hideKeyboard(View currentFocusView) {
        if (currentFocusView != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) CardiomagnylApplication.getAppContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(currentFocusView.getWindowToken(), 0);
        }
    }

    public static String loadFileFromAssets(String absoluteFileName) {
        String str_data = "";
        byte[] buffer;
        InputStream is;
        try {
            is = CardiomagnylApplication.getAppContext().getAssets().open(absoluteFileName);
            int size = is.available();
            buffer = new byte[size];
            is.read(buffer);
            is.close();
            str_data = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str_data;
    }

    public static String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String st = Integer.toHexString(0xFF & messageDigest[i]);
                hexString.append(st.length() < 2 ? "0" + st : st);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

}

