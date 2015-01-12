package ru.com.cardiomagnil.ui.start;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import ru.com.cardiomagnil.app.R;
import ru.com.cardiomagnil.util.Tools;

public class WelcomeFragment extends CustomFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start_welcome, container, false);
        Tools.setFontSegoeWP((ViewGroup) view);
        return view;
    }

    public void initParent() {
        ProgressBar progressBarBottomOutsideStartWork = (ProgressBar) getActivity().findViewById(R.id.progressBarBottomOutsideStartWork);
        TextView textViewBottomOutsideAction = (TextView) getActivity().findViewById(R.id.textViewBottomOutsideAction);

        progressBarBottomOutsideStartWork.setMax(5);
        progressBarBottomOutsideStartWork.setProgress(1);
        textViewBottomOutsideAction.setText(getActivity().getString(R.string.three_minutes));
    }
}