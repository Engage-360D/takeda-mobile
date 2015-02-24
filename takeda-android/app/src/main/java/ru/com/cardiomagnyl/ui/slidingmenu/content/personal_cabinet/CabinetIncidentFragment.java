package ru.com.cardiomagnyl.ui.slidingmenu.content.personal_cabinet;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import ru.com.cardiomagnyl.app.R;
import ru.com.cardiomagnyl.application.AppState;
import ru.com.cardiomagnyl.model.common.Dummy;
import ru.com.cardiomagnyl.model.common.Response;
import ru.com.cardiomagnyl.model.incident.Incident;
import ru.com.cardiomagnyl.model.incident.IncidentDao;
import ru.com.cardiomagnyl.model.token.Token;
import ru.com.cardiomagnyl.ui.base.BaseItemFragment;
import ru.com.cardiomagnyl.ui.slidingmenu.menu.SlidingMenuActivity;
import ru.com.cardiomagnyl.util.CallbackOne;
import ru.com.cardiomagnyl.util.Tools;
import ru.com.cardiomagnyl.widget.CustomDialogs;

public class CabinetIncidentFragment extends BaseItemFragment {
    private final View.OnClickListener mOnDoneClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            tryToSendIncident();
        }
    };

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cabinet_incident, null);
        initFragment(view);
        return view;
    }

    @Override
    public void initTopBar(ViewGroup viewGroupTopBar) {
        initTopBarDone(viewGroupTopBar, mOnDoneClickListener, false);
    }

    private void initFragment(final View view) {
        initIncidentFields(view);
    }

    private void initIncidentFields(final View view) {
        final EditText editTextComment = (EditText) view.findViewById(R.id.editTextComment);
        final RadioGroup radioGroupIncident = (RadioGroup) view.findViewById(R.id.radioGroupIncident);
        final SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();

        editTextComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setEnabledButtonDone(validateIncidentFields(editTextComment, radioGroupIncident));
            }
        });

        radioGroupIncident.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                boolean validated = validateIncidentFields(editTextComment, radioGroupIncident);
                setEnabledButtonDone(validated);
                setEnabledButtonDone(validated);
                setIncidentDescription(view, checkedId);
            }
        });
    }

    private void setEnabledButtonDone(boolean enabled) {
        SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
        ViewGroup layoutHeader = slidingMenuActivity.getHeaderLayout();
        View view = layoutHeader.findViewById(R.id.textViewDone);
        view.setEnabled(enabled);
    }

    private boolean validateIncidentFields(EditText editTextComment, RadioGroup radioGroupIncident) {
        return editTextComment.getText().length() != 0 && radioGroupIncident.getCheckedRadioButtonId() >= 0;
    }

    private void setIncidentDescription(final View view, int checkedId) {
        View layoutIncidentDescription = view.findViewById(R.id.layoutIncidentDescription);
        TextView textViewIncidentDescription = (TextView) view.findViewById(R.id.textViewIncidentDescription);

        String incident = "";
        switch (checkedId) {
            case R.id.radioButtonInfarction:
                incident = getString(R.string.infarction);
                break;
            case R.id.radioButtonApoplexy:
                incident = getString(R.string.apoplexy);
                break;
            case R.id.radioButtonShunting:
                incident = getString(R.string.shunting);
                break;
        }

        textViewIncidentDescription.setText(String.format(getString(R.string.incident_description), incident));
        layoutIncidentDescription.setVisibility(View.VISIBLE);
    }

    private void tryToSendIncident() {
        CustomDialogs.showConfirmationDialog(
                getActivity(),
                getString(R.string.report_an_incident),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tryToSendIncidentHelper();
                    }
                }
        );
    }

    private void tryToSendIncidentHelper() {
        final SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
        slidingMenuActivity.showProgressDialog();

        Incident incident = pickAllFields();
        Token token = AppState.getInsnatce().getToken();
        IncidentDao.report(
                incident,
                token,
                new CallbackOne<Dummy>() {
                    @Override
                    public void execute(Dummy dummy) {
                        // FIXME: dummy
                        Tools.showToast(getActivity(), R.string.resp_201_created, Toast.LENGTH_LONG);

                        slidingMenuActivity.hideProgressDialog();
                        slidingMenuActivity.makeContentStepBack(true);
                    }
                },
                new CallbackOne<Response>() {
                    @Override
                    public void execute(Response responseError) {
                        Tools.showToast(getActivity(), R.string.error_occurred, Toast.LENGTH_LONG);

                        slidingMenuActivity.hideProgressDialog();
                        slidingMenuActivity.makeContentStepBack(true);
                    }
                }
        );
    }

    private Incident pickAllFields(/*final View view*/) {
        Incident incident = new Incident();

        EditText editTextComment = (EditText) getActivity().findViewById(R.id.editTextComment);
        RadioGroup radioGroupIncident = (RadioGroup) getActivity().findViewById(R.id.radioGroupIncident);

        incident.setComment(editTextComment.getText().toString());
        switch (radioGroupIncident.getCheckedRadioButtonId()) {
            case R.id.radioButtonInfarction:
                incident.setName(Incident.Name.infarction.name());
                break;
            case R.id.radioButtonApoplexy:
                incident.setName(Incident.Name.apoplexy.name());
                break;
            case R.id.radioButtonShunting:
                incident.setName(Incident.Name.shunting.name());
                break;
        }

        return incident;
    }

}
