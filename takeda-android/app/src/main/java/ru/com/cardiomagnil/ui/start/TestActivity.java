package ru.com.cardiomagnil.ui.start;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import ru.com.cardiomagnil.app.R;
import ru.com.cardiomagnil.widget.CustomDialogLayout;

public class TestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        showDialog();
    }

    private void showDialog() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View bodyView = inflater.inflate(R.layout.ca_dialog_incident, null);

        CustomDialogLayout customDialogLayout = new CustomDialogLayout
                .Builder(this)
                .setBody(bodyView)
                .addButton(R.string.cancel, CustomDialogLayout.DialogStandartAction.dismiss)
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
                    Toast.makeText(getApplicationContext(), "textViewInfarction",
                            Toast.LENGTH_LONG).show();
                    alertDialog.dismiss();
                } else if (view.equals(textViewApoplexy)) {
                    Toast.makeText(getApplicationContext(), "textViewApoplexy",
                            Toast.LENGTH_LONG).show();
                    alertDialog.dismiss();
                } else if (view.equals(textViewShunting)) {
                    Toast.makeText(getApplicationContext(), "textViewShunting",
                            Toast.LENGTH_LONG).show();
                    alertDialog.dismiss();
                }
            }
        };

        textViewInfarction.setOnClickListener(onClickListener);
        textViewApoplexy.setOnClickListener(onClickListener);
        textViewShunting.setOnClickListener(onClickListener);
    }
}
