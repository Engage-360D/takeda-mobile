package ru.com.cardiomagnil.ui.slidingmenu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import ru.com.cardiomagnil.app.R;
import ru.com.cardiomagnil.ui.start.CustomAnimation;

public class ResultsWaitingFargment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ca_slidingmenu_fragment_dummy, null);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View imageViewHeart = view.findViewById(R.id.imageViewHeart);
        startAnimation(imageViewHeart);
        tryGetTestResult();
    }

    private void startAnimation(View view) {
        new CustomAnimation
        /**/.Builder(AnimationUtils.loadAnimation(getActivity(), R.anim.splash_repeat), view)
        /**/.build()
        /**/.startAnimation();
    }

    private void tryGetTestResult() {
        SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity)getActivity();
        slidingMenuActivity.getTestResult();
    }
}
