package ru.com.cardiomagnyl.util.schedule;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;

import ru.com.cardiomagnyl.app.R;
import ru.com.cardiomagnyl.application.Constants;
import ru.com.cardiomagnyl.model.pill.Pill;
import ru.com.cardiomagnyl.util.Tools;

public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "wake_lock");
        // осуществляем блокировку
        wakeLock.acquire();

        Bundle extras = intent.getExtras();
        if (extras != null) {
            Pill pill = (Pill) extras.getParcelable(Constants.PILL);
            if (pill == null) return;
            showNotification(context, (Pill) pill);
            vibroSund(context);
        }

        // разблокируем
        wakeLock.release();
    }

    private void showNotification(Context context, Pill pill) {
        Intent notificationIntent = new Intent(context, NotificationBroadcastReceiver.class);
        notificationIntent.setAction("NotificationReceiverIntent");
        PendingIntent contentIntent = PendingIntent.getBroadcast(context, Tools.generateMessageId(), notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        Resources res = context.getResources();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.ic_launcher))
                .setTicker(res.getString(R.string.time_to_take_pills))
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentTitle(res.getString(R.string.time_to_take_pills))
                .setContentText(pill.getName());

        Notification notification = builder.build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(Tools.generateMessageId(), notification);
    }

    private void vibroSund(final Context context) {
        Thread myThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    vibroSundHelper(context);
                    Thread.sleep(200);
                    vibroSundHelper(context);

                    Thread.sleep(350);

                    vibroSundHelper(context);
                    Thread.sleep(200);
                    vibroSundHelper(context);

                    Thread.sleep(350);

                    vibroSundHelper(context);
                    Thread.sleep(200);
                    vibroSundHelper(context);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        myThread.start();
    }

    private void vibroSundHelper(Context context) {
        Vibrator vibrator = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);
        vibrator.vibrate(100);

        final ToneGenerator toneGenerator = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
        toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP);

        vibrator.vibrate(100);
    }

}

