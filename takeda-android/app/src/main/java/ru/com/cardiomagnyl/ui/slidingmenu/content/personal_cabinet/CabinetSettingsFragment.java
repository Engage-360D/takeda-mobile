package ru.com.cardiomagnyl.ui.slidingmenu.content.personal_cabinet;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

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
        final View textViewPills = view.findViewById(R.id.textViewPills);
        final View linearLayoutConsolidatedReport = view.findViewById(R.id.linearLayoutConsolidatedReport);
        final View textViewAddIncident = view.findViewById(R.id.textViewAddIncident);
        final View buttonExit = view.findViewById(R.id.buttonExit);

        textViewPills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryToSeuUpPills();
            }
        });

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

        textViewAddIncident.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryToAddIncident();
            }
        });
    }

    private void tryToSeuUpPills() {
        if (!SlidingMenuActivity.check(getActivity())) {
            return;
        }

        Fragment fragment = new CabinetIncidentFragment();
        SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
        slidingMenuActivity.putContentOnTop(fragment, false);
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

                                    // TODO: check password for delete reports
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

    private void tryToAddIncident() {
        if (!SlidingMenuActivity.check(getActivity())) {
            return;
        }

        Fragment fragment = new CabinetIncidentFragment();
        SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
        slidingMenuActivity.putContentOnTop(fragment, false);
    }

}
