package ru.com.cardiomagnil.ui.start;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import ru.com.cardiomagnil.R;
import ru.com.cardiomagnil.application.Tools;

public class WelcomeFragment extends CustomFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start_welcome, container, false);
        Tools.setFontSegoeWP((ViewGroup) view);
        return view;
    }

    public void initParent() {
        ImageView imageViewBottomInsideLeft = (ImageView) getActivity().findViewById(R.id.imageViewBottomInsideLeft);
        TextView textViewBottomInsideAction = (TextView) getActivity().findViewById(R.id.textViewBottomInsideAction);
        ImageView imageViewBottomInsideRight = (ImageView) getActivity().findViewById(R.id.imageViewBottomInsideRight);

        ProgressBar progressBarBottomOutsideStartWork = (ProgressBar) getActivity().findViewById(R.id.progressBarBottomOutsideStartWork);
        TextView textViewBottomOutsideAction = (TextView) getActivity().findViewById(R.id.textViewBottomOutsideAction);

        imageViewBottomInsideLeft.setVisibility(View.VISIBLE);
        textViewBottomInsideAction.setText(getActivity().getString(R.string.swipe));
        imageViewBottomInsideRight.setVisibility(View.INVISIBLE);

        progressBarBottomOutsideStartWork.setMax(5);
        progressBarBottomOutsideStartWork.setProgress(1);
        textViewBottomOutsideAction.setText(getActivity().getString(R.string.three_minutes));
    }
}