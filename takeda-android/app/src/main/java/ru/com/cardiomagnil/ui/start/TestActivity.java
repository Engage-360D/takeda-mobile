package ru.com.cardiomagnil.ui.start;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
        View.OnClickListener onYesClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Yes!",
                        Toast.LENGTH_LONG).show();
            }
        };

        CustomDialogLayout customDialogLayout = new CustomDialogLayout
                .Builder(this)
                .setBodyText("test")
                .addButton(R.string.no, CustomDialogLayout.DialogStandartAction.dismiss)
                .addButton(R.string.yes, onYesClickListener)
                .create();
        AlertDialog alertDialog = new AlertDialog
                .Builder(this)
                .setView(customDialogLayout)
                .create();
        customDialogLayout.setParentDialog(alertDialog);
        alertDialog.show();
    }
}
