package ru.com.cardiomagnil.ui.slidingmenu.content.personal_cabinet;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import ru.com.cardiomagnil.app.R;
import ru.com.cardiomagnil.ui.base.BaseItemFragment;
import ru.com.cardiomagnil.ui.slidingmenu.menu.SlidingMenuActivity;

public class Ca_CabinetDataFargment extends BaseItemFragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ca_fragment_slidingmenu_cabinet_data, null);
        initFargment(view);
        return view;
    }

    @Override
    public void initTopBar(ViewGroup viewGroupTopBar) {
        initTopBarBellCabinet(viewGroupTopBar, true, false);
    }

    private void initFargment(View view) {
        if (getActivity() != null && getActivity() instanceof SlidingMenuActivity) {
            SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
            slidingMenuActivity.unselectCurrentItem();
        }

        initFargmentHelper(view);

        RadioButton radioButtonRecomendations = (RadioButton) view.findViewById(R.id.radioButtonRecomendations);
        RadioButton radioButtonSetting = (RadioButton) view.findViewById(R.id.radioButtonSetting);

        radioButtonRecomendations.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (getActivity() != null && getActivity() instanceof SlidingMenuActivity) {
                        Fragment fragment = new Ca_CabinetTestFargment();
                        SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
                        slidingMenuActivity.replaceAllContent(fragment, false);
                    }
                }
            }
        });

        radioButtonSetting.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (getActivity() != null && getActivity() instanceof SlidingMenuActivity) {
                        Fragment fragment = new Ca_CabinetSettingsFargment();
                        SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
                        slidingMenuActivity.replaceAllContent(fragment, false);
                    }
                }
            }
        });

    }

    private void initFargmentHelper(View view) {
//        editTextFirstName
//        editTextLastName
//        editTextRegEmail

//        buttonGenerate

//        imageViewVK
//        imageViewFB
//        imageViewOK

//        editTextSpecializationName
//        spinnerExperienceYears
//        editTextSpecializationInstitutionPhone
//        editTextSpecializationInstitutionName
//        editTextSpecializationInstitutionAddress
//        spinnerGraduationDate

//        buttonSave
    }
}
