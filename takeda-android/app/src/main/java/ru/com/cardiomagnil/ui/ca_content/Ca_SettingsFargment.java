package ru.com.cardiomagnil.ui.ca_content;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ru.com.cardiomagnil.app.R;
import ru.com.cardiomagnil.ui.ca_base.Ca_BaseItemFragment;
import ru.com.cardiomagnil.widget.CustomDialogLayout;

public class Ca_SettingsFargment extends Ca_BaseItemFragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ca_fragment_slidingmenu_settings, null);
        initFragment( view);
        return view;
    }

    @Override
    public String getMenuItemName() {
        return null;
    }

    @Override
    public View getTopView() {
        return null;
    }

    private void initFragment(View view) {
        final ImageView imageViewAddIncident = (ImageView) view.findViewById(R.id.imageViewAddIncident);

        imageViewAddIncident.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogIncident();
            }
        });
    }

    private void showDialogIncident() {
        if (getActivity() == null) return;

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View bodyView = inflater.inflate(R.layout.ca_dialog_incident, null);

        CustomDialogLayout customDialogLayout = new CustomDialogLayout
                .Builder(getActivity())
                .setBody(bodyView)
                .addButton(R.string.cancel, CustomDialogLayout.DialogStandartAction.dismiss)
                .create();

        AlertDialog alertDialog = new AlertDialog
                .Builder(getActivity())
                .setView(customDialogLayout)
                .create();
        customDialogLayout.setParentDialog(alertDialog);

        initDialogBody(alertDialog, bodyView);

        alertDialog.show();
    }

    private void initDialogBody(final AlertDialog alertDialog, final View dialogBody) {
        TextView textViewInfarction = (TextView) dialogBody.findViewById(R.id.textViewInfarction);
        TextView textViewApoplexy = (TextView) dialogBody.findViewById(R.id.textViewApoplexy);
        TextView textViewShunting = (TextView) dialogBody.findViewById(R.id.textViewShunting);

        textViewInfarction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int layoutId =  R.layout.ca_item_incident_infarction;
                onIncidentSelected(alertDialog, layoutId /*some other params*/);
            }
        });

        textViewApoplexy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int layoutId =  R.layout.ca_item_incident_apoplexy;
                onIncidentSelected(alertDialog, layoutId /*some other params*/);
            }
        });

        textViewShunting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int layoutId =  R.layout.ca_item_incident_shunting;
                onIncidentSelected(alertDialog, layoutId /*some other params*/);
            }
        });
    }

    private void onIncidentSelected(AlertDialog alertDialog, int layoutId /*some other params*/) {
        if (getActivity() == null) return;

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View incidentView = inflater.inflate(layoutId, null);

        LinearLayout linearLayoutIncidentHolder = (LinearLayout) this.getView().findViewById(R.id.linearLayoutIncidentHolder);
        linearLayoutIncidentHolder.addView(incidentView);

        alertDialog.dismiss();

        // TODO: some other actions
    }

}
