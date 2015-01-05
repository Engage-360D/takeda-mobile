package ru.com.cardiomagnil.util;

import android.os.Looper;
import android.util.Log;

import ru.com.cardiomagnil.application.CardiomagnilApplication;

public class ThtreadHelper {
    public static void logThread(String where) {
        final long mainThtreadId = Looper.getMainLooper().getThread().getId();
        final long currentThtreadId = Thread.currentThread().getId();
        Log.d(CardiomagnilApplication.getInstance().getTag(), ""
                + "\n" + where
                + "\n" + "MainThread: " + String.valueOf(mainThtreadId)
                + "\n" + "CurrentThread: " + String.valueOf(currentThtreadId));
    }

    public static void startNotInMain(final Callback callback) {
        final long mainThtreadId = Looper.getMainLooper().getThread().getId();
        final long currentThtreadId = Thread.currentThread().getId();
        if (mainThtreadId == currentThtreadId) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    callback.execute();
                }
            }).start();
        } else {
            callback.execute();
        }
    }
}
