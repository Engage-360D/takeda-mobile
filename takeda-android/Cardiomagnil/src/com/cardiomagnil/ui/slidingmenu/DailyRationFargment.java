package com.cardiomagnil.ui.slidingmenu;

import com.cardiomagnil.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class DailyRationFargment extends Fragment  {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.daily_ration_frgment, null);

        initPatientDataFargment(view);

        return view;
    }

    private void initPatientDataFargment(View view) {
        ImageView imageViewBottomInsideLeft = (ImageView)view.findViewById(R.id.imageViewBottomInsideLeft);
        TextView textViewBottomInsideAction = (TextView)view.findViewById(R.id.textViewBottomInsideAction);
        ImageView imageViewBottomInsideRight = (ImageView)view.findViewById(R.id.imageViewBottomInsideRight);

        imageViewBottomInsideLeft.setVisibility(View.INVISIBLE);
        textViewBottomInsideAction.setText(this.getString(R.string.get_results));
        imageViewBottomInsideRight.setVisibility(View.VISIBLE);
    }

}
