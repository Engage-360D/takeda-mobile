package ru.com.cardiomagnyl.ui.slidingmenu.content.personal_cabinet;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ru.com.cardiomagnyl.app.R;
import ru.com.cardiomagnyl.application.AppState;
import ru.com.cardiomagnyl.application.CardiomagnylApplication;
import ru.com.cardiomagnyl.model.common.Dummy;
import ru.com.cardiomagnyl.model.common.LgnPwd;
import ru.com.cardiomagnyl.model.common.Response;
import ru.com.cardiomagnyl.model.incidents.Incidents;
import ru.com.cardiomagnyl.model.token.Token;
import ru.com.cardiomagnyl.model.user.UserDao;
import ru.com.cardiomagnyl.ui.base.BaseItemFragment;
import ru.com.cardiomagnyl.ui.slidingmenu.content.pills.PillsFragment;
import ru.com.cardiomagnyl.ui.slidingmenu.menu.SlidingMenuActivity;
import ru.com.cardiomagnyl.ui.start.SplashActivity;
import ru.com.cardiomagnyl.util.CallbackOne;
import ru.com.cardiomagnyl.util.Tools;
import ru.com.cardiomagnyl.util.schedule.PillsScheduler;
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
        final View layoutIncidentDescription = view.findViewById(R.id.layoutIncidentDescription);
        final TextView textViewIncidentDescription = (TextView) view.findViewById(R.id.textViewIncidentDescription);

        textViewPills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryToSetUpPills();
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

        Incidents incidents = AppState.getInsnatce().getIncidents();
        if (!incidents.isEmpty()) {
            String incident = "";
            if (incidents.isHadBypassSurgery()) {
                incident = getString(R.string.shunting);
            } else if (incidents.isHadHeartAttackOrStroke()) {
                incident = getString(R.string.infarction_or_apoplexy);
            } else {
                incident = getString(R.string.incident);
            }

            textViewIncidentDescription.setText(String.format(getString(R.string.incident_description), incident));
            layoutIncidentDescription.setVisibility(View.VISIBLE);
        }
    }

    private void tryToSetUpPills() {
        if (!SlidingMenuActivity.check(getActivity())) {
            return;
        }

        Fragment fragment = new PillsFragment();
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
                                    String email = AppState.getInsnatce().getUser().getEmail();
                                    String password = ((EditText) editTextPassword).getText().toString();
                                    LgnPwd lgnPwd = new LgnPwd(email, password);
                                    Token token = AppState.getInsnatce().getToken();
                                    deleteReports(lgnPwd, token);
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

    private void deleteReports(LgnPwd lgnPwd, Token token) {
        final SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
        slidingMenuActivity.showProgressDialog();

        UserDao.resetProfile(
                lgnPwd,
                token,
                new CallbackOne<Dummy>() {
                    @Override
                    public void execute(Dummy dummy) {
                        slidingMenuActivity.hideProgressDialog();
                        AppState.getInsnatce().setTimelineEvents(0);
                        Intent intent = new Intent(slidingMenuActivity, SplashActivity.class);
                        startActivity(intent);
                        slidingMenuActivity.finish();
                    }
                },
                new CallbackOne<Response>() {
                    @Override
                    public void execute(Response responseError) {
                        slidingMenuActivity.hideProgressDialog();
                        Tools.showToast(slidingMenuActivity, R.string.error_occurred, Toast.LENGTH_LONG);

                    }
                }
        );
    }

    private void tryToExit() {
        CustomDialogs.showConfirmationDialog(
                getActivity(),
                getString(R.string.exit_application),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PillsScheduler.cancelAll();
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