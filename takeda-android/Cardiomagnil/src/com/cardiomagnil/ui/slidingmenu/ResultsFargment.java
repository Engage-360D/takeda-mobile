package com.cardiomagnil.ui.slidingmenu;

import com.cardiomagnil.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ResultsFargment extends Fragment  {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.empty_content_view, null);

        TextView textViewTestText = (TextView)view.findViewById(R.id.textViewTestText);
        if (textViewTestText != null) {
            textViewTestText.setText("ResultsFargment");
        }

        return view;
    }

}
