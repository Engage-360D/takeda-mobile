package ru.com.cardiomagnyl.ui.start;

import android.app.DatePickerDialog;
import android.widget.DatePicker;

import java.util.Calendar;

public class CustomOnDateSetListener implements DatePickerDialog.OnDateSetListener {
    private final Calendar mCalendar;
    private final int mCurrentYear;

    public CustomOnDateSetListener() {
        mCalendar = Calendar.getInstance();
        mCurrentYear = mCalendar.get(Calendar.YEAR);

        mCalendar.set(Calendar.HOUR, 0);
        mCalendar.set(Calendar.MINUTE, 0);
        mCalendar.set(Calendar.SECOND, 0);
        mCalendar.set(Calendar.MILLISECOND, 0);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, monthOfYear);
        mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
    }

    public Calendar getCalendar() {
        return mCalendar;
    }

    public boolean isRightAge(int ageLimit) {
        return (mCurrentYear - mCalendar.get(Calendar.YEAR)) > ageLimit;
    }
}
