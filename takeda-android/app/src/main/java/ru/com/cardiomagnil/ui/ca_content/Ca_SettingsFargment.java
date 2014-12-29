package ru.com.cardiomagnil.ui.ca_content;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import ru.com.cardiomagnil.app.R;
import ru.com.cardiomagnil.ui.ca_base.Ca_BaseItemFragment;

public class Ca_SettingsFargment extends Ca_BaseItemFragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ca_fragment_slidingmenu_settings, null);

        initFragment(inflater, view);
        return view;
    }

    @Override
    public String getMenuInetmName() {
        return null;
    }

    @Override
    public View getTopView() {
        return null;
    }

    private void initFragment(LayoutInflater inflater, View view) {
        ImageView imageViewAddIncident = (ImageView) view.findViewById(R.id.imageViewAddIncident);

        imageViewAddIncident.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                View dialogView = inflater.inflate(R.layout.ca_fragment_slidingmenu_settings, null);
//                ca_dialog_incident
            }
        });
    }
}
