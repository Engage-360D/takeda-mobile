package ru.com.cardiomagnil.util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.IOException;
import java.io.InputStream;

import ru.com.cardiomagnil.application.CardiomagnilApplication;

public class Utils {
    public static void hideKeyboard(View currentFocusView) {
        if (currentFocusView != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) CardiomagnilApplication.getAppContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(currentFocusView.getWindowToken(), 0);
        }
    }

    public static String loadFileFromAssets(String absoluteFileName) {
        String str_data = "";
        byte[] buffer;
        InputStream is;
        try {
            is = CardiomagnilApplication.getAppContext().getAssets().open(absoluteFileName);
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
}

