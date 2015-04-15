package ru.com.cardiomagnyl.ui.slidingmenu.content.risk_analysis;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import ru.com.cardiomagnyl.app.R;
import ru.com.cardiomagnyl.application.AppState;
import ru.com.cardiomagnyl.application.Constants;
import ru.com.cardiomagnyl.application.Constants.ArterialPressure;
import ru.com.cardiomagnyl.application.Constants.PhysicalActivity;
import ru.com.cardiomagnyl.model.test.TestSource;
import ru.com.cardiomagnyl.ui.base.BaseRiskAnalysis;
import ru.com.cardiomagnyl.ui.slidingmenu.menu.SlidingMenuActivity;
import ru.com.cardiomagnyl.util.ProfileHelper;
import ru.com.cardiomagnyl.util.Tools;

public class RiskAnalysisPatientHistoryFragment extends BaseRiskAnalysis {
    private View parentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_analysis_patient_history, null);
        initFragment(parentView);
        return parentView;
    }

    @Override
    public void initTopBar(ViewGroup viewGroupTopBar) {
        boolean userIsDoctor = AppState.getInsnatce().getUser().isDoctor();
        initTopBarMenuBellCabinet(viewGroupTopBar, userIsDoctor, userIsDoctor, userIsDoctor);
    }

    private void initFragment(View fragmentView) {
        initTabs(fragmentView, 1);

        TextView textViewPressure = (TextView) parentView.findViewById(R.id.textViewPressure);
        TextView textViewPhysicalActivity = (TextView) parentView.findViewById(R.id.textViewPhysicalActivity);
        RadioGroup radioGroupDiabetes = (RadioGroup) fragmentView.findViewById(R.id.radioGroupDiabetes);
        final int toggleButtonNotDiabetes = parentView.findViewById(R.id.toggleButtonNotDiabetes).getId();
        final View layoutSugarProblems = fragmentView.findViewById(R.id.layoutSugarProblems);
        RadioGroup radioGroupSugarProblems = (RadioGroup) fragmentView.findViewById(R.id.radioGroupSugarProblems);
        final View layoutDiabeticMedicines = fragmentView.findViewById(R.id.layoutDiabeticMedicines);
        RadioGroup radioGroupDiabeticMedicines = (RadioGroup) fragmentView.findViewById(R.id.radioGroupDiabeticMedicines);
        RadioGroup radioGroupHypertonicMedicines = (RadioGroup) fragmentView.findViewById(R.id.radioGroupHypertonicMedicines);
        RadioGroup radioGroupInfarctionStroke = (RadioGroup) fragmentView.findViewById(R.id.radioGroupInfarctionStroke);
        ImageView imageViewBottomInsideLeft = (ImageView) fragmentView.findViewById(R.id.imageViewBottomInsideLeft);
        TextView textViewBottomInsideAction = (TextView) fragmentView.findViewById(R.id.textViewBottomInsideAction);
        ImageView imageViewBottomInsideRight = (ImageView) fragmentView.findViewById(R.id.imageViewBottomInsideRight);
        View layoutBottomInside = fragmentView.findViewById(R.id.layoutBottomInside);

        Context context = fragmentView.getContext();
        ProfileHelper.initTextViewWithNumberPicker(textViewPressure, ArterialPressure.MIN, ArterialPressure.MAX, ArterialPressure.INIT, context.getString(R.string.arterial_pressure_hint));
        ProfileHelper.initTextViewWithNumberPicker(textViewPhysicalActivity, PhysicalActivity.MIN, PhysicalActivity.MAX, PhysicalActivity.INIT, context.getString(R.string.physical_activities_hint));

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

        radioGroupHypertonicMedicines.setOnCheckedChangeListener(Tools.ToggleListener);
        radioGroupInfarctionStroke.setOnCheckedChangeListener(Tools.ToggleListener);
        imageViewBottomInsideLeft.setVisibility(View.INVISIBLE);
        textViewBottomInsideAction.setText(this.getString(R.string.step_three_daily_ration));
        imageViewBottomInsideRight.setVisibility(View.VISIBLE);

        Bundle bundle = getArguments();
        final TestSource testSource = bundle.getParcelable(Constants.TEST_SOURCE);
        layoutBottomInside.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                trySwitchNextFragment(testSource);
            }
        });
    }

    private void trySwitchNextFragment(TestSource testSource) {
        String resultString = pickTestIncomingFields(testSource);

        if (!resultString.isEmpty()) {
            Tools.showToast(getActivity(), resultString, Toast.LENGTH_LONG);
            return;
        }

        if (testSource.validate(TestSource.RESULT_GROUPS.second)) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(Constants.TEST_SOURCE, testSource);

            Fragment dailyRationFragment = new RiskAnalysisDailyRationFragment();
            dailyRationFragment.setArguments(bundle);
            switchFragment(dailyRationFragment);
        } else {
            Tools.showToast(getActivity(), R.string.complete_all_fields, Toast.LENGTH_SHORT);
        }
    }

    private String pickTestIncomingFields(TestSource testSource) {
        TextView textViewPressure = (TextView) parentView.findViewById(R.id.textViewPressure);
        TextView textViewPhysicalActivity = (TextView) parentView.findViewById(R.id.textViewPhysicalActivity);
        ToggleButton toggleButtonDiabetes = (ToggleButton) parentView.findViewById(R.id.toggleButtonDiabetes);
        ToggleButton toggleButtonNotDiabetes = (ToggleButton) parentView.findViewById(R.id.toggleButtonNotDiabetes);
        ToggleButton toggleButtonSugarProblems = (ToggleButton) parentView.findViewById(R.id.toggleButtonSugarProblems);
        ToggleButton toggleButtonDiabeticMedicines = (ToggleButton) parentView.findViewById(R.id.toggleButtonDiabeticMedicines);
        ToggleButton toggleButtonHypertonicMedicines = (ToggleButton) parentView.findViewById(R.id.toggleButtonHypertonicMedicines);
        ToggleButton toggleButtonInfarctionStroke = (ToggleButton) parentView.findViewById(R.id.toggleButtonInfarctionStroke);
        ToggleButton toggleButtonNoInfarctionStroke = (ToggleButton) parentView.findViewById(R.id.toggleButtonNoInfarctionStroke);

        String resultString = "";

        try {
            Integer arterialPressure = (Integer) textViewPressure.getTag();
            Integer physicalActivity = (Integer) textViewPhysicalActivity.getTag();
            Boolean diabetes = toggleButtonDiabetes.isChecked() || toggleButtonNotDiabetes.isChecked() ? toggleButtonDiabetes.isChecked() : null;
            Boolean sugarProblems = toggleButtonSugarProblems.isChecked();
            Boolean sugarDrugs = toggleButtonDiabeticMedicines.isChecked();
            Boolean arterialPressureDrugs = toggleButtonHypertonicMedicines.isChecked();
            Boolean HeartAttackOrStroke = toggleButtonInfarctionStroke.isChecked() || toggleButtonNoInfarctionStroke.isChecked() ? toggleButtonInfarctionStroke.isChecked() : null;

            if (arterialPressure == null || (arterialPressure < ArterialPressure.MIN || arterialPressure > ArterialPressure.MAX)) {
                resultString += String.format(parentView.getContext().getString(R.string.error_test_pressure), ArterialPressure.MIN, ArterialPressure.MAX);
            }

            if (physicalActivity == null || (physicalActivity < PhysicalActivity.MIN || physicalActivity > PhysicalActivity.MAX)) {
                resultString += resultString.isEmpty() ? "" : "\n";
                resultString += String.format(parentView.getContext().getString(R.string.error_test_activity), PhysicalActivity.MIN, PhysicalActivity.MAX);
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
        } catch (Exception e) { /*does nothing*/ }

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
