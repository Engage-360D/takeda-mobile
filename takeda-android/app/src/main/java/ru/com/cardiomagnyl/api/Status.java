package ru.com.cardiomagnyl.api;

import android.content.Context;
import android.util.Log;

import ru.com.cardiomagnyl.app.R;
import ru.com.cardiomagnyl.application.CardiomagnylApplication;

public class Status {
    public final static int LAN_ERROR = 1;
    public final static int CACHE_ERROR = 2;
    public final static int DB_ERROR = 3;
    public final static int INPUT_DATA_ERROR = 4;
    public final static int NO_DATA_ERROR = 5;

    public final static int CONFLICT_ERROR = 409;

    public static String getDescription(int errorCode) {
        Context context = CardiomagnylApplication.getAppContext();

        switch (errorCode) {
            case LAN_ERROR:
                return context.getString(R.string.resp_001_lan_error);
            case CACHE_ERROR:
                return context.getString(R.string.resp_002_cache_error);
            case DB_ERROR:
                return context.getString(R.string.resp_003_db_error);
            case INPUT_DATA_ERROR:
                return context.getString(R.string.resp_004_input_data_error);
            case NO_DATA_ERROR:
                return context.getString(R.string.resp_005_no_data_error);
            case CONFLICT_ERROR:
                return context.getString(R.string.resp_409_conflict_error);

            default:
                Log.d("Error", "Error code: " + String.valueOf(errorCode));
                return context.getString(R.string.resp_000_default_error);
        }
    }

}
