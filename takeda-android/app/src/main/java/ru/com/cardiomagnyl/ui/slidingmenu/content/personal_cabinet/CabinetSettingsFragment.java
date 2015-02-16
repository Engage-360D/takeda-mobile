package ru.com.cardiomagnyl.ui.slidingmenu.content.personal_cabinet;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ru.com.cardiomagnyl.app.R;
import ru.com.cardiomagnyl.application.CardiomagnylApplication;
import ru.com.cardiomagnyl.ui.base.BaseItemFragment;
import ru.com.cardiomagnyl.ui.slidingmenu.menu.SlidingMenuActivity;
import ru.com.cardiomagnyl.util.Tools;
import ru.com.cardiomagnyl.widget.CustomDialogLayout;
import ru.com.cardiomagnyl.widget.CustomDialogs;

public class CabinetSettingsFragment extends BaseItemFragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cabinet_settings, null);
        initFragment(view);
        return view;
    }

    @Override
    public void initTopBar(ViewGroup viewGroupTopBar) {
        initTopBarBellCabinet(viewGroupTopBar, true, true);
    }

    private void initFragment(View view) {
        final LinearLayout linearLayoutConsolidatedReport = (LinearLayout) view.findViewById(R.id.linearLayoutConsolidatedReport);
        final ImageView imageViewAddIncident = (ImageView) view.findViewById(R.id.imageViewAddIncident);
        final Button buttonExit = (Button) view.findViewById(R.id.buttonExit);

        linearLayoutConsolidatedReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryToDeleteReports();
            }
        });

        buttonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryToExit();
            }
        });

        imageViewAddIncident.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryAddIncident();
            }
        });
    }

    private void tryToDeleteReports() {
        CustomDialogLayout customDialogLayout = new CustomDialogLayout
                .Builder(getActivity())
                .setBody(R.layout.layout_enter_password)
                .addButton(R.string.cancel, CustomDialogLayout.DialogStandardAction.dismiss)
                .addButton(
                        R.string.delete_all_reports,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                View editTextPassword = Tools.findViewInParents(view, R.id.editTextPassword);
                                if (editTextPassword != null) {
                                    String str = ((EditText) editTextPassword).getText().toString();

                                    // TODO: check password adr delete reports
                                }
                            }
                        })
                .create();

        AlertDialog alertDialog = new AlertDialog
                .Builder(getActivity())
                .setView(customDialogLayout)
                .create();
        customDialogLayout.setParentDialog(alertDialog);

        alertDialog.show();
    }

    private void tryToExit() {
        CustomDialogs.showConfirmationDialog(
                getActivity(),
                getString(R.string.exit_application),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CardiomagnylApplication.getInstance().logout();
                    }
                }
        );
    }

    private void tryAddIncident() {
        if (!SlidingMenuActivity.check(getActivity())) {
            return;
        }

        Fragment fragment = new CabinetIncidentFragment();
        SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
        slidingMenuActivity.putContentOnTop(fragment, false);

//        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View bodyView = inflater.inflate(R.layout.dialog_incident, null);
//
//        CustomDialogLayout customDialogLayout = new CustomDialogLayout
//                .Builder(getActivity())
//                .setBody(bodyView)
//                .addButton(R.string.cancel, CustomDialogLayout.DialogStandardAction.dismiss)
//                .create();
//
//        AlertDialog alertDialog = new AlertDialog
//                .Builder(getActivity())
//                .setView(customDialogLayout)
//                .create();
//        customDialogLayout.setParentDialog(alertDialog);
//
//        initDialogBody(alertDialog, bodyView);
//
//        alertDialog.show();
    }

    private void initDialogBody(final AlertDialog alertDialog, final View dialogBody) {
        TextView textViewInfarction = (TextView) dialogBody.findViewById(R.id.textViewInfarction);
        TextView textViewApoplexy = (TextView) dialogBody.findViewById(R.id.textViewApoplexy);
        TextView textViewShunting = (TextView) dialogBody.findViewById(R.id.textViewShunting);

        textViewInfarction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int layoutId = R.layout.item_incident_infarction;
                onIncidentSelected(alertDialog, layoutId /*some other params*/);
            }
        });

        textViewApoplexy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int layoutId = R.layout.item_incident_apoplexy;
                onIncidentSelected(alertDialog, layoutId /*some other params*/);
            }
        });

        textViewShunting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int layoutId = R.layout.item_incident_shunting;
                onIncidentSelected(alertDialog, layoutId /*some other params*/);
            }
        });
    }

    private void onIncidentSelected(AlertDialog alertDialog, int layoutId /*some other params*/) {
        if (getActivity() == null) return;

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View incidentView = inflater.inflate(layoutId, null);

        LinearLayout linearLayoutIncidentHolder = (LinearLayout) getActivity().findViewById(R.id.linearLayoutIncidentHolder);
        linearLayoutIncidentHolder.addView(incidentView);

        alertDialog.dismiss();

        // TODO: some other actions
    }

}
