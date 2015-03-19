package ru.com.cardiomagnyl.util.schedule;

import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import ru.com.cardiomagnyl.application.AppSharedPreferences;
import ru.com.cardiomagnyl.model.pill.Pill;
import ru.com.cardiomagnyl.model.pill.PillFrequency;
import ru.com.cardiomagnyl.util.Tools;

public class PillsScheduler {
    public synchronized static void setAll(List<Pill> pillsList) {
        Set<String> intentActions = (Set<String>) AppSharedPreferences.get(AppSharedPreferences.Preference.alarmActions);
        AlarmManagerBroadcastReceiver alarm = new AlarmManagerBroadcastReceiver();
        alarm.cancelAlarm(intentActions);

        if (pillsList == null || pillsList.isEmpty()) return;

        intentActions.clear();
        Calendar currentCalendar = Calendar.getInstance();
        for (Pill pill : pillsList) {
            if (isActual(pill, currentCalendar)) {
                setHelper(pill, pill.getId());
                intentActions.add(pill.getId());
            }
        }
        AppSharedPreferences.put(AppSharedPreferences.Preference.alarmActions, intentActions);
    }

    private static void setHelper(Pill pill, String intentAction) {
        Log.d("Test", "Added: " + pill.getName());
        AlarmManagerBroadcastReceiver alarm = new AlarmManagerBroadcastReceiver();
        alarm.setAlarm(pill, intentAction);
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
