package ru.com.cardiomagnyl.util;

import android.os.Looper;
import android.util.Log;

import ru.com.cardiomagnyl.application.CardiomagnylApplication;

public class ThreadHelper {
    public static void logThread(String where) {
        final long mainThreadId = Looper.getMainLooper().getThread().getId();
        final long currentThreadId = Thread.currentThread().getId();
        Log.d(CardiomagnylApplication.getInstance().getTag(), ""
                + "\n" + where
                + "\n" + "MainThread: " + String.valueOf(mainThreadId)
                + "\n" + "CurrentThread: " + String.valueOf(currentThreadId));
    }

    public static void startNotInMain(final Callback callback) {
        final long mainThreadId = Looper.getMainLooper().getThread().getId();
        final long currentThreadId = Thread.currentThread().getId();
        if (mainThreadId == currentThreadId) {
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
