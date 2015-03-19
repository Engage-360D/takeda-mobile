package ru.com.cardiomagnyl.util.schedule;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import ru.com.cardiomagnyl.application.CardiomagnylApplication;
import ru.com.cardiomagnyl.application.Constants;
import ru.com.cardiomagnyl.model.pill.Pill;
import ru.com.cardiomagnyl.util.Tools;

public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {
    private final static Object sLockObject = new Object();

    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "YOUR TAG");
        // осуществляем блокировку
        wakeLock.acquire();

        Bundle extras = intent.getExtras();
        if (extras != null) {
            Pill pill = extras.getParcelable(Constants.PILL);
            if (pill == null) {
                Log.d("Test", "getParcelable == null");
                return;
            }
            Log.d("Test", "getParcelable = " + pill);
        }

//        AlarmMana ger alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
//        PendingIntent intent123 = alarmManager..getNextAlarmClock().getShowIntent();

        // разблокируем
        wakeLock.release();
    }

    public void setAlarm(Pill pill, String intentAction) {
        synchronized (sLockObject) {
            Context context = CardiomagnylApplication.getAppContext();

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
//            intent.setAction(intentAction);
            intent.putExtra(Constants.PILL, pill);
//            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, Integer.parseInt(pill.getId()), intent, 0);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, Integer.parseInt(intentAction), intent, 0);

            Calendar timeCalendar = Tools.calendarFromMediumTime(pill.getTime());
            Calendar sinceDadaTimeCalendar = Tools.calendarFromFullDate(pill.getSinceDate());
            sinceDadaTimeCalendar.set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY));
            sinceDadaTimeCalendar.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE));
            sinceDadaTimeCalendar.set(Calendar.SECOND, timeCalendar.get(Calendar.SECOND));

            // alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, Tools.dateFromFullDate(pill.getSinceDate()).getTime(), pill.getEnumFrequency().getPeriod(), pendingIntent);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, new Date().getTime() + 1000 * 10, 1000 * 10 /*pill.getEnumFrequency().getPeriod()*/, pendingIntent);

//        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
//        intent.setAction(pill.getId());
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
//        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        alarmManager.cancel(pendingIntent);
        }
    }

    public void cancelAlarm(Set<String> intentActions) {
        synchronized (sLockObject) {
            if (intentActions == null || intentActions.isEmpty()) return;

            Context context = CardiomagnylApplication.getAppContext();
            for (String action : intentActions) {
                Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
                // PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
//                intent.setAction(action);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, Integer.parseInt(action), intent, PendingIntent.FLAG_CANCEL_CURRENT);
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                alarmManager.cancel(pendingIntent);
            }
        }
    }

}
