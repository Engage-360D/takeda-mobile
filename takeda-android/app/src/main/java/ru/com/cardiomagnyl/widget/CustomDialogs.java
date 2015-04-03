package ru.com.cardiomagnyl.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;

import ru.com.cardiomagnyl.app.R;

public class CustomDialogs {
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

    // FIXME: use instead toasts
    public static void showAlertDialog(Context context, int alertStringId) {
        showAlertDialog(context, context.getString(alertStringId));
    }

    public static void showAlertDialog(Context context, String alert) {
        CustomDialogLayout customDialogLayout = new CustomDialogLayout
                .Builder(context)
                .setBody(alert)
                .addButton(R.string.close, CustomDialogLayout.DialogStandardAction.dismiss)
                .create();

        AlertDialog alertDialog = new AlertDialog
                .Builder(context)
                .setView(customDialogLayout)
                .create();
        customDialogLayout.setParentDialog(alertDialog);

        alertDialog.show();
    }

}
