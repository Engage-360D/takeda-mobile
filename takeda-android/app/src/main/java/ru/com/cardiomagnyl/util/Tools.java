package ru.com.cardiomagnyl.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Base64;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ru.com.cardiomagnyl.app.R;
import ru.com.cardiomagnyl.application.CardiomagnylApplication;
import ru.com.cardiomagnyl.application.Constants;
import ru.com.cardiomagnyl.application.ExceptionsHandler;

public class Tools {
    public static JsonElement jsonElementByMemberName(JsonObject jsonObject, String memberName) {
        if (jsonObject == null || !jsonObject.isJsonObject() || memberName == null || memberName.isEmpty()) {
            return null;
        }

        JsonElement jsonElement = null;

        if (jsonObject.has(memberName)) {
            jsonElement = jsonObject.get(memberName);
            jsonElement = jsonElement.isJsonNull() ? null : jsonElement;
        }

        return jsonElement;
    }

    public static String formatShortTime(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        String formattedDate = dateFormat.format(date);
        return formattedDate;
    }

    public static String formatMediumTime(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        String formattedDate = dateFormat.format(date);
        return formattedDate;
    }

    public static String formatShortDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(date);
        return formattedDate;
    }

    public static Date dateFromShortDate(String dateString) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String formatShortHintedDate(Date date) {
        if (DateUtils.isToday(date.getTime())) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(" (EEEE)");
            String today = CardiomagnylApplication.getAppContext().getString(R.string.today);
            return today + simpleDateFormat.format(date).toLowerCase();
        } else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy (EEEE)");
            return simpleDateFormat.format(date).toLowerCase();
        }
    }

    public static String formatFullDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        String formattedDate = dateFormat.format(date);
        return formattedDate;
    }

    public static Date dateFromFullDate(String dateString) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String yearToDate(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return (formatFullDate(calendar.getTime()));
    }

    public static Calendar resetCalendar(Calendar calendar) {
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    public static Calendar calendarFromMediumTime(String mediumTime) {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();

        try {
            date = dateFormat.parse(mediumTime);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        calendar.setTime(date);

        return calendar;
    }

    public static Calendar calendarFromShortDate(String shortDate) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();

        try {
            date = dateFormat.parse(shortDate);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        calendar.setTime(date);

        return calendar;
    }

    public static Calendar calendarFromFullDate(String longDate) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();

        try {
            date = dateFormat.parse(longDate);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        calendar.setTime(date);

        return calendar;
    }

    public static int getDifferenceInYears(String dateString) {
        if (TextUtils.isEmpty(dateString)) return 0;

        Calendar calendarCurrent = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateFromFullDate(dateString));
        return calendarCurrent.get(Calendar.YEAR) - calendar.get(Calendar.YEAR);
    }

    public static Pair<String, String> getCurrentWeekDateRange() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        String weekStart = simpleDateFormat.format(calendar.getTime()).toLowerCase();
        calendar.add(Calendar.DAY_OF_MONTH, 4);
        String weekEnd = simpleDateFormat.format(calendar.getTime()).toLowerCase();
        return new Pair<>(weekStart, weekEnd);
    }

    public static List<Integer> getRange(final Integer range) {
        final List<Integer> rangeList = new ArrayList<Integer>();
        for (Integer counter = 0; counter < Constants.YEARS_RANGE; ++counter) {
            rangeList.add(counter);
        }
        return rangeList;
    }

    public static List<Integer> getYearsRange(final Integer range) {
        final List<Integer> yearsRangeList = new ArrayList<Integer>();
        final Calendar calendar = Calendar.getInstance();
        final Integer currentYear = calendar.get(Calendar.YEAR);
        for (Integer counter = 0; counter < Constants.YEARS_RANGE; ++counter) {
            yearsRangeList.add(currentYear - counter);
        }
        return yearsRangeList;
    }

    public static RadioGroup.OnCheckedChangeListener ToggleListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(final RadioGroup radioGroup, final int i) {
            onCheckedChangedHelper(radioGroup, i);
        }
    };

    public static void onCheckedChangedHelper(final RadioGroup radioGroup, final int i) {
        for (int j = 0; j < radioGroup.getChildCount(); j++) {
            final ToggleButton view = (ToggleButton) radioGroup.getChildAt(j);
            view.setChecked(view.getId() == i);
        }
    }

    public static String getAppKeyHashB64() {
        String keyHashB64 = "";

        try {
            PackageInfo info = CardiomagnylApplication.getAppContext().getPackageManager().getPackageInfo("ru.com.cardiomagnyl", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                byte[] digest = md.digest();
                keyHashB64 = Base64.encodeToString(digest, Base64.DEFAULT);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionsHandler.getInstatce().handleException(CardiomagnylApplication.getAppContext(), e);
        }

        return keyHashB64;
    }

    public static boolean isValidEmail(CharSequence target) {
        return TextUtils.isEmpty(target) ? false : android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static void showToast(Context context, int textId, int length) {
        showToast(context, context.getString(textId), length);
    }

    public static void showToast(Context context, String text, int length) {
        Toast toast = Toast.makeText(context, text, length);
        toast.setGravity(Gravity.CENTER | Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static View findViewInParents(View currentView, int seekingViewId) {
        View viewParent = (View) currentView.getParent();
        if (viewParent != null) {
            View seekingView = viewParent.findViewById(seekingViewId);
            return seekingView != null ? seekingView : findViewInParents(viewParent, seekingViewId);
        } else {
            return null;
        }
    }
}
