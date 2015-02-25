package ru.com.cardiomagnyl.ui.slidingmenu.content.pills;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Arrays;

import ru.com.cardiomagnyl.app.R;
import ru.com.cardiomagnyl.application.Constants;
import ru.com.cardiomagnyl.model.base.BaseModelHelper;
import ru.com.cardiomagnyl.model.pill.Pill;
import ru.com.cardiomagnyl.ui.base.BaseItemFragment;
import ru.com.cardiomagnyl.util.Tools;
import ru.com.cardiomagnyl.widget.CustomSpinnerAdapter;

public class PillDetailsFragment extends BaseItemFragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pill_details, null);

        Bundle bundle = this.getArguments();
        Pill pill = bundle.getParcelable(Constants.PILL);

        initFragment(view, pill);
        return view;
    }

    @Override
    public void initTopBar(ViewGroup viewGroupTopBar) {
        initTopBarMenuBellCabinet(viewGroupTopBar, true, true, true);
    }

    private void initFragment(final View fragmentView, final Pill pill) {
        EditText editTextPillName = (EditText) fragmentView.findViewById(R.id.editTextPillName);
        EditText editTextPillNumber = (EditText) fragmentView.findViewById(R.id.editTextPillNumber);
        Spinner spinnerFrequency = (Spinner) fragmentView.findViewById(R.id.spinnerPillsFrequency);
        TextView textViewPillsHoursValue = (TextView) fragmentView.findViewById(R.id.textViewPillsHoursValue);
        TextView textViewStartDateValue = (TextView) fragmentView.findViewById(R.id.textViewStartDateValue);
        TextView textViewEndDateValue = (TextView) fragmentView.findViewById(R.id.textViewEndDateValue);

        editTextPillName.setText(pill.getName());
        editTextPillName.setEnabled(false);

        editTextPillNumber.setText(String.valueOf(pill.getQuantity()));
        editTextPillNumber.setEnabled(false);

        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(
                fragmentView.getContext(),
                R.layout.custom_spinner_item,
                R.layout.spinner_item_dropdown,
                Arrays.asList((BaseModelHelper) pill.getEnumFrequency()));
        customSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFrequency.setAdapter(customSpinnerAdapter);
        spinnerFrequency.setEnabled(false);

        textViewPillsHoursValue.setText(pill.getTime());
        textViewPillsHoursValue.setEnabled(false);

        textViewStartDateValue.setText(Tools.formatShortDate(Tools.dateFromFullDate(pill.getSinceDate())));
        textViewStartDateValue.setEnabled(false);

        textViewEndDateValue.setText(Tools.formatShortDate(Tools.dateFromFullDate(pill.getTillDate())));
        textViewEndDateValue.setEnabled(false);
    }

}
