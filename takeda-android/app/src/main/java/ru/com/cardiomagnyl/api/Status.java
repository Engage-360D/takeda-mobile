package ru.com.cardiomagnyl.api;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;

import ru.com.cardiomagnyl.app.R;
import ru.com.cardiomagnyl.application.CardiomagnylApplication;
import ru.com.cardiomagnyl.widget.CustomDialogLayout;

public class Status {
    // TODO: remove after test
    public final static int OK = 0;
    //

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

//    public static byte[] createData(int status) {
//        //  {
//        //      "code" : XXX,
//        //      "status" : "YYY",
//        //      "message" : "ZZZ",
//        //      "data" : {}
//        //  }
//        byte[] data = new byte[]{};
//        String dataString = String.format("{\"code\" : %d, \"status\" : \"%s\", \"message\" : \"%s\", \"data\" : {} }", status, getDescription(status), getDescription(status));
//        try {
//            data = dataString.getBytes("UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        return data;
//    }

    public static void showErrorDialog(Context context, int errorStringId) {
        showErrorDialog(context, context.getString(errorStringId));
    }

    public static void showErrorDialog(Context context, String error) {
        CustomDialogLayout customDialogLayout = new CustomDialogLayout
                .Builder(context)
                .setBody(error)
                .addButton(R.string.close, CustomDialogLayout.DialogStandardAction.dismiss)
                .create();

        AlertDialog alertDialog = new AlertDialog
                .Builder(context)
                .setView(customDialogLayout)
                .create();
        customDialogLayout.setParentDialog(alertDialog);

        alertDialog.show();
    }

    public static void showConfirmationDialog(Context context, int messageStringId, View.OnClickListener onAccept) {
        showConfirmationDialog(context, context.getString(messageStringId), onAccept);
    }

    public static void showConfirmationDialog(Context context, String message, View.OnClickListener onAccept) {
        CustomDialogLayout customDialogLayout = new CustomDialogLayout
                .Builder(context)
                .setBody(message)
                .addButton(R.string.no, CustomDialogLayout.DialogStandardAction.dismiss)
                .addButton(R.string.yes, onAccept)
                .create();

        AlertDialog alertDialog = new AlertDialog
                .Builder(context)
                .setView(customDialogLayout)
                .create();
        customDialogLayout.setParentDialog(alertDialog);

        alertDialog.show();
    }
}
