package ru.com.cardiomagnyl.widget;

import android.app.DatePickerDialog;
import android.widget.DatePicker;

import java.util.Calendar;

public class CustomOnDateSetListener implements DatePickerDialog.OnDateSetListener {
    private final Calendar mCalendar;
    private final int mCurrentYear;

    public CustomOnDateSetListener(Calendar calndar) {
        mCalendar = calndar;
        mCurrentYear = Calendar.getInstance().get(Calendar.YEAR);
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
