package ru.com.cardiomagnyl.widget;

import android.app.TimePickerDialog;
import android.widget.TimePicker;

import java.util.Calendar;

public class CustomOnTimeSetListener implements TimePickerDialog.OnTimeSetListener {
    private final Calendar mCalendar;

    public CustomOnTimeSetListener(Calendar calendar) {
        mCalendar = calendar;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        mCalendar.set(Calendar.MINUTE, minute);
    }

    public Calendar getCalendar() {
        return mCalendar;
    }

}
