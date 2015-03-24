package ru.com.cardiomagnyl.util.schedule;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ru.com.cardiomagnyl.application.AppSharedPreferences;
import ru.com.cardiomagnyl.application.CardiomagnylApplication;
import ru.com.cardiomagnyl.application.Constants;
import ru.com.cardiomagnyl.model.pill.Pill;
import ru.com.cardiomagnyl.model.pill.PillFrequency;
import ru.com.cardiomagnyl.util.Tools;

public class PillsScheduler {
    private static Object mSynchroObjectc = new Object();

    public synchronized static void setAll(List<Pill> pillsList) {
        synchronized (mSynchroObjectc) {
            cancelAllHelper();

            if (pillsList == null || pillsList.isEmpty()) return;

            Set<String> intentActions = new HashSet<>();
            Calendar currentCalendar = Calendar.getInstance();
            for (Pill pill : pillsList) {
                if (isActual(pill, currentCalendar)) {
                    int messageId = Tools.generateMessageId();
                    setHelper(pill, String.valueOf(messageId));
                    intentActions.add(String.valueOf(messageId));
                }
            }
            AppSharedPreferences.put(AppSharedPreferences.Preference.alarmActions, intentActions);
        }
    }

    public synchronized static void cancelAll() {
        synchronized (mSynchroObjectc) {
            cancelAllHelper();
        }
    }

    private static void cancelAllHelper() {
        Set<String> intentActions = (Set<String>) AppSharedPreferences.get(AppSharedPreferences.Preference.alarmActions);

        if (intentActions == null || intentActions.isEmpty()) return;

        AppSharedPreferences.put(AppSharedPreferences.Preference.alarmActions, new HashSet<String>());

        Context context = CardiomagnylApplication.getAppContext();
        for (String action : intentActions) {
            Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, Integer.parseInt(action), intent, PendingIntent.FLAG_CANCEL_CURRENT);

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);
        }
    }

    private static void setHelper(Pill pill, String intentAction) {
        Context context = CardiomagnylApplication.getAppContext();

        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        intent.putExtra(Constants.PILL, pill);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, Integer.parseInt(intentAction), intent, 0);

        Calendar timeCalendar = Tools.calendarFromMediumTime(pill.getTime());
        Calendar sinceDadaTimeCalendar = Tools.calendarFromFullDate(pill.getSinceDate());
        sinceDadaTimeCalendar.set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY));
        sinceDadaTimeCalendar.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE));
        sinceDadaTimeCalendar.set(Calendar.SECOND, timeCalendar.get(Calendar.SECOND));

        long requiredTime = calculateStarTime(sinceDadaTimeCalendar, pill.getEnumFrequency().getPeriod());

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, requiredTime, pill.getEnumFrequency().getPeriod(), pendingIntent);
    }

    // the easiest way to calculate StarTime
    private static long calculateStarTime(Calendar sinceCalendar, long period) {
        final long currentTime = new Date().getTime();
        long requiredTime = sinceCalendar.getTimeInMillis();
        while (requiredTime < currentTime) {
            requiredTime += period;
        }
        return requiredTime;
    }

    private static boolean isActual(Pill pill, Calendar currentCalendar) {
        Calendar timeCalendar = Tools.calendarFromMediumTime(pill.getTime());
        Calendar tillDadaTimeCalendar = Tools.calendarFromFullDate(pill.getTillDate());
        tillDadaTimeCalendar.set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY));
        tillDadaTimeCalendar.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE));
        tillDadaTimeCalendar.set(Calendar.SECOND, timeCalendar.get(Calendar.SECOND));

        boolean isTwoDaysPeriod = pill.getEnumFrequency().getPeriod() == PillFrequency.every_2_days.getPeriod();
        if (isTwoDaysPeriod) {
            long timeDifference = tillDadaTimeCalendar.getTime().getTime() - currentCalendar.getTimeInMillis();
            boolean isLastDayInTwoDaysPeriod = timeDifference > 0 && timeDifference < 24 * 60 * 60 * 1000;
            boolean isTakingPillsDay = (Tools.calendarFromFullDate(pill.getTillDate()).getTimeInMillis() - Tools.calendarFromFullDate(pill.getSinceDate()).getTimeInMillis())
                    % PillFrequency.every_2_days.getPeriod() == 0;
            return isLastDayInTwoDaysPeriod ? isTakingPillsDay && tillDadaTimeCalendar.after(currentCalendar) : tillDadaTimeCalendar.after(currentCalendar);
        } else {
            return tillDadaTimeCalendar.after(currentCalendar);
        }
    }

}
