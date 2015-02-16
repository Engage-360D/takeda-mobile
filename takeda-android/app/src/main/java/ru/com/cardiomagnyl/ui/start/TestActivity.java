package ru.com.cardiomagnyl.ui.start;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import ru.com.cardiomagnyl.app.R;
import ru.com.cardiomagnyl.util.Tools;
import ru.com.cardiomagnyl.widget.CustomDialogLayout;

public class TestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        showDialog();
    }

    private void showDialog() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View bodyView = inflater.inflate(R.layout.dialog_incident, null);

        CustomDialogLayout customDialogLayout = new CustomDialogLayout
                .Builder(this)
                .setBody(bodyView)
                .addButton(R.string.cancel, CustomDialogLayout.DialogStandardAction.dismiss)
                .create();

        AlertDialog alertDialog = new AlertDialog
                .Builder(this)
                .setView(customDialogLayout)
                .create();
        customDialogLayout.setParentDialog(alertDialog);

        initDialogBody(alertDialog, bodyView);

        alertDialog.show();
    }

    private void initDialogBody(final AlertDialog alertDialog, final View dialogBody) {
        final TextView textViewInfarction = (TextView) dialogBody.findViewById(R.id.textViewInfarction);
        final TextView textViewApoplexy = (TextView) dialogBody.findViewById(R.id.textViewApoplexy);
        final TextView textViewShunting = (TextView) dialogBody.findViewById(R.id.textViewShunting);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.equals(textViewInfarction)) {
                    Tools.showToast(getApplicationContext(), "textViewInfarction", Toast.LENGTH_SHORT);
                    alertDialog.dismiss();
                } else if (view.equals(textViewApoplexy)) {
                    Tools.showToast(getApplicationContext(), "textViewApoplexy", Toast.LENGTH_SHORT);
                    alertDialog.dismiss();
                } else if (view.equals(textViewShunting)) {
                    Tools.showToast(getApplicationContext(), "textViewShunting", Toast.LENGTH_SHORT);
                    alertDialog.dismiss();
                }
            }
        };

        textViewInfarction.setOnClickListener(onClickListener);
        textViewApoplexy.setOnClickListener(onClickListener);
        textViewShunting.setOnClickListener(onClickListener);
    }
}
