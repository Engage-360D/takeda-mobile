package ru.com.cardiomagnil.ui.slidingmenu;

import ru.com.cardiomagnil.ui.start.CustomAnimation;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import ru.com.cardiomagnil.R;

public class ResultsWaitingFargment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_slidingmenu_start_content, null);
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
