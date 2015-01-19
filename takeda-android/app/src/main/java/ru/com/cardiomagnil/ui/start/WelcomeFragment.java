package ru.com.cardiomagnil.ui.start;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import ru.com.cardiomagnil.app.R;
import ru.com.cardiomagnil.ui.base.BaseStartFragment;
import ru.com.cardiomagnil.util.Tools;

public class WelcomeFragment extends BaseStartFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ca_fragment_start_welcome, container, false);
        Tools.setFontSegoeWP((ViewGroup) view);
        return view;
    }

    @Override
    public void initParent(Activity activity) {
        ProgressBar progressBarBottomOutsideStartWork = (ProgressBar) activity.findViewById(R.id.progressBarBottomOutsideStartWork);
        TextView textViewBottomOutsideAction = (TextView) activity.findViewById(R.id.textViewBottomOutsideAction);
        View linearLayoutTop = activity.findViewById(R.id.linearLayoutTop);
        View textViewBottom = activity.findViewById(R.id.textViewBottom);

        progressBarBottomOutsideStartWork.setMax(5);
        progressBarBottomOutsideStartWork.setProgress(1);
        textViewBottomOutsideAction.setText(activity.getString(R.string.three_minutes));
        linearLayoutTop.setAlpha(0);
        textViewBottom.setAlpha(0);
    }
}