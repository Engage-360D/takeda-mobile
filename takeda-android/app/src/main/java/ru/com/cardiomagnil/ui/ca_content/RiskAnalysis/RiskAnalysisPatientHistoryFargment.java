package ru.com.cardiomagnil.ui.ca_content.RiskAnalysis;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import ru.com.cardiomagnil.app.R;
import ru.com.cardiomagnil.application.AppState;
import ru.com.cardiomagnil.model.TestIncoming;
import ru.com.cardiomagnil.ui.slidingmenu.SlidingMenuActivity;
import ru.com.cardiomagnil.util.Tools;

public class RiskAnalysisPatientHistoryFargment extends Fragment {
    private View parentView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_slidingmenu_test_patient_history, null);

        initPatientDataFargment(parentView);

        return parentView;
    }

    private void initPatientDataFargment(View view) {
        RadioGroup radioGroupDiabetes = (RadioGroup) view.findViewById(R.id.radioGroupDiabetes);
        final int toggleButtonDiabetesId = parentView.findViewById(R.id.toggleButtonDiabetes).getId();
        final RelativeLayout relativeLayoutDiabeticMedicines = (RelativeLayout) view.findViewById(R.id.relativeLayoutDiabeticMedicines);
        RadioGroup radioGroupDiabeticMedicines = (RadioGroup) view.findViewById(R.id.radioGroupDiabeticMedicines);
        final RelativeLayout relativeLayoutHypertonicMedicines = (RelativeLayout) view.findViewById(R.id.relativeLayoutHypertonicMedicines);
        RadioGroup radioGroupHypertonicMedicines = (RadioGroup) view.findViewById(R.id.radioGroupHypertonicMedicines);
        RadioGroup radioGroupInfarctionStroke = (RadioGroup) view.findViewById(R.id.radioGroupInfarctionStroke);
        ImageView imageViewBottomInsideLeft = (ImageView) view.findViewById(R.id.imageViewBottomInsideLeft);
        TextView textViewBottomInsideAction = (TextView) view.findViewById(R.id.textViewBottomInsideAction);
        ImageView imageViewBottomInsideRight = (ImageView) view.findViewById(R.id.imageViewBottomInsideRight);
        View layoutBottomInside = view.findViewById(R.id.layoutBottomInside);

        radioGroupDiabetes.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Tools.onCheckedChangedHelper(group, checkedId);
                if (checkedId == toggleButtonDiabetesId) {
                    relativeLayoutDiabeticMedicines.setVisibility(View.VISIBLE);
                } else {
                    relativeLayoutDiabeticMedicines.setVisibility(View.GONE);
                }

            }
        });

        radioGroupDiabeticMedicines.setOnCheckedChangeListener(Tools.ToggleListener);
        radioGroupHypertonicMedicines.setOnCheckedChangeListener(Tools.ToggleListener);
        radioGroupInfarctionStroke.setOnCheckedChangeListener(Tools.ToggleListener);
        imageViewBottomInsideLeft.setVisibility(View.INVISIBLE);
        textViewBottomInsideAction.setText(this.getString(R.string.step_three_daily_ration));
        imageViewBottomInsideRight.setVisibility(View.VISIBLE);

        layoutBottomInside.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                trySwitchNextFragment();
            }
        });
    }

    private void trySwitchNextFragment() {
        TestIncoming testIncoming = AppState.getInstatce().getTestIncoming();
        String resultString = pickTestIncomingFields(testIncoming);

        if (!resultString.isEmpty()) {
            Toast toast = Toast.makeText(parentView.getContext(), resultString, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER | Gravity.CENTER, 0, 0);
            toast.show();
            return;
        }

        if (testIncoming.validate(TestIncoming.RESULT_GROUPS.second)) {
            Fragment dailyRationFargment = new RiskAnalysisDailyRationFargment();
            switchFragment(dailyRationFargment);
        } else {
            Toast toast = Toast.makeText(parentView.getContext(), parentView.getContext().getString(R.string.complete_all_fields), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER | Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    private String pickTestIncomingFields(TestIncoming testIncoming) {
        ToggleButton toggleButtonDiabetes = (ToggleButton) parentView.findViewById(R.id.toggleButtonDiabetes);
        ToggleButton toggleButtonNotDiabetes = (ToggleButton) parentView.findViewById(R.id.toggleButtonNotDiabetes);
        ToggleButton toggleButtonDiabeticMedicines = (ToggleButton) parentView.findViewById(R.id.toggleButtonDiabeticMedicines);
        EditText editTextPressure = (EditText) parentView.findViewById(R.id.editTextPressure);
        ToggleButton toggleButtonHypertonicMedicines = (ToggleButton) parentView.findViewById(R.id.toggleButtonHypertonicMedicines);
        EditText editTextPhysicalActivity = (EditText) parentView.findViewById(R.id.editTextPhysicalActivity);
        ToggleButton toggleButtonInfarctionStroke = (ToggleButton) parentView.findViewById(R.id.toggleButtonInfarctionStroke);
        ToggleButton toggleButtonNoInfarctionStroke = (ToggleButton) parentView.findViewById(R.id.toggleButtonNoInfarctionStroke);

        String resultString = "";

        try {
            Boolean diabetes = toggleButtonDiabetes.isChecked() || toggleButtonNotDiabetes.isChecked() ? toggleButtonDiabetes.isChecked() : null;
            Boolean sugarDrugs = toggleButtonDiabeticMedicines.isChecked();
            Integer arterialPressure = editTextPressure.length() != 0 ? Integer.parseInt(String.valueOf(editTextPressure.getText().toString())) : null;
            Boolean arterialPressureDrugs = toggleButtonHypertonicMedicines.isChecked();
            Integer physicalActivity = editTextPhysicalActivity.length() != 0 ? Integer.parseInt(String.valueOf(editTextPhysicalActivity.getText().toString())) : null;
            Boolean HeartAttackOrStroke = toggleButtonInfarctionStroke.isChecked() || toggleButtonNoInfarctionStroke.isChecked() ? toggleButtonInfarctionStroke.isChecked() : null;

            if (arterialPressure < 80 || arterialPressure > 200) {
                resultString += parentView.getContext().getString(R.string.error_test_pressure);
            }

            if (physicalActivity < 80 || physicalActivity > 200) {
                resultString += resultString.isEmpty() ? "" : "\n";
                resultString += parentView.getContext().getString(R.string.error_test_activity);
            }

            if (resultString.isEmpty()) {
                testIncoming.setDiabetes(diabetes);
                testIncoming.setSugarDrugs(sugarDrugs);
                testIncoming.setArterialPressure(arterialPressure);
                testIncoming.setArterialPressureDrugs(arterialPressureDrugs);
                testIncoming.setPhysicalActivity(physicalActivity);
                testIncoming.setHeartAttackOrStroke(HeartAttackOrStroke);
            }
        } catch (Exception e) {
            // do nothing
        }

        return resultString;
    }

    // the meat of switching the above fragment
    private void switchFragment(Fragment fragment) {
        if (getActivity() == null)
            return;

        if (getActivity() instanceof SlidingMenuActivity) {
            SlidingMenuActivity mainActivity = (SlidingMenuActivity) getActivity();
            // FIXME!!! switchContent
//            mainActivity.switchContent(fragment);
        }
    }
}
