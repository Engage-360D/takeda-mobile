package ru.com.cardiomagnyl.ui.slidingmenu.content.risk_analysis;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import ru.com.cardiomagnyl.app.R;
import ru.com.cardiomagnyl.application.AppState;
import ru.com.cardiomagnyl.model.test.TestSource;
import ru.com.cardiomagnyl.ui.base.BaseRiskAnalysis;
import ru.com.cardiomagnyl.ui.slidingmenu.menu.SlidingMenuActivity;
import ru.com.cardiomagnyl.util.Tools;

public class RiskAnalysisPatientHistoryFragment extends BaseRiskAnalysis {
    private View parentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_slidingmenu_test_patient_history, null);
        initPatientDataFragment(parentView);
        return parentView;
    }

    @Override
    public void initTopBar(ViewGroup viewGroupTopBar) {
        initTopBarBellCabinet(viewGroupTopBar, false, false);
    }

    private void initPatientDataFragment(View view) {
        initTabs(view, 1);

        RadioGroup radioGroupDiabetes = (RadioGroup) view.findViewById(R.id.radioGroupDiabetes);
        final int toggleButtonNotDiabetes = parentView.findViewById(R.id.toggleButtonNotDiabetes).getId();
        final View layoutSugarProblems = view.findViewById(R.id.layoutSugarProblems);
        RadioGroup radioGroupSugarProblems = (RadioGroup) view.findViewById(R.id.radioGroupSugarProblems);
        final View layoutDiabeticMedicines = view.findViewById(R.id.layoutDiabeticMedicines);
        RadioGroup radioGroupDiabeticMedicines = (RadioGroup) view.findViewById(R.id.radioGroupDiabeticMedicines);
//        final RelativeLayout relativeLayoutHypertonicMedicines = (RelativeLayout) view.findViewById(R.id.relativeLayoutHypertonicMedicines);
//        SeekBarWithValues seekBarWithValuesPressure = (SeekBarWithValues) parentView.findViewById(R.id.seekBarWithValuesPressure);
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
                if (checkedId == toggleButtonNotDiabetes) {
                    layoutSugarProblems.setVisibility(View.VISIBLE);
                    layoutDiabeticMedicines.setVisibility(View.GONE);
                } else {
                    layoutSugarProblems.setVisibility(View.GONE);
                    layoutDiabeticMedicines.setVisibility(View.VISIBLE);
                }

            }
        });

        radioGroupSugarProblems.setOnCheckedChangeListener(Tools.ToggleListener);
        radioGroupDiabeticMedicines.setOnCheckedChangeListener(Tools.ToggleListener);

//        seekBarWithValuesPressure.setOnProgressChangedListener(new OnProgressChangedListener() {
//
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int actualProgress, boolean fromUser) {
//                if (actualProgress >= 140) {
//                    relativeLayoutHypertonicMedicines.setVisibility(View.VISIBLE);
//                } else {
//                    relativeLayoutHypertonicMedicines.setVisibility(View.GONE);
//                }
//            }
//
//        });

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
        TestSource testSource = AppState.getInsnatce().getTestSource();
        String resultString = pickTestIncomingFields(testSource);

        if (!resultString.isEmpty()) {
            Tools.showToast(getActivity(), resultString, Toast.LENGTH_LONG);
            return;
        }

        if (testSource.validate(TestSource.RESULT_GROUPS.second)) {
            Fragment dailyRationFragment = new RiskAnalysisDailyRationFragment();
            switchFragment(dailyRationFragment);
        } else {
            Tools.showToast(getActivity(), R.string.complete_all_fields, Toast.LENGTH_SHORT);
        }
    }

    private String pickTestIncomingFields(TestSource testSource) {
        ToggleButton toggleButtonDiabetes = (ToggleButton) parentView.findViewById(R.id.toggleButtonDiabetes);
        ToggleButton toggleButtonNotDiabetes = (ToggleButton) parentView.findViewById(R.id.toggleButtonNotDiabetes);
        ToggleButton toggleButtonSugarProblems = (ToggleButton) parentView.findViewById(R.id.toggleButtonSugarProblems);
        ToggleButton toggleButtonDiabeticMedicines = (ToggleButton) parentView.findViewById(R.id.toggleButtonDiabeticMedicines);
        EditText editTextPressure = (EditText) parentView.findViewById(R.id.editTextPressure);
        ToggleButton toggleButtonHypertonicMedicines = (ToggleButton) parentView.findViewById(R.id.toggleButtonHypertonicMedicines);
        EditText editTextPhysicalActivity = (EditText) parentView.findViewById(R.id.editTextPhysicalActivity);
        ToggleButton toggleButtonInfarctionStroke = (ToggleButton) parentView.findViewById(R.id.toggleButtonInfarctionStroke);
        ToggleButton toggleButtonNoInfarctionStroke = (ToggleButton) parentView.findViewById(R.id.toggleButtonNoInfarctionStroke);

        String resultString = "";

        try {
            Boolean diabetes = toggleButtonDiabetes.isChecked() || toggleButtonNotDiabetes.isChecked() ? toggleButtonDiabetes.isChecked() : null;
            Boolean sugarProblems = toggleButtonSugarProblems.isChecked();
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
                testSource.setHasDiabetes(diabetes);
                testSource.setHadSugarProblems(sugarProblems);
                testSource.setIsSugarDrugsConsumer(sugarDrugs);
                testSource.setArterialPressure(arterialPressure);
                testSource.setIsArterialPressureDrugsConsumer(arterialPressureDrugs);
                testSource.setPhysicalActivityMinutes(physicalActivity);
                testSource.setHadHeartAttackOrStroke(HeartAttackOrStroke);
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
            SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
            slidingMenuActivity.replaceContentOnTop(fragment, false);
        }
    }
}
