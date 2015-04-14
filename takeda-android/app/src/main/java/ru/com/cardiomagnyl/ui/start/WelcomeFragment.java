package ru.com.cardiomagnyl.ui.start;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.gorbin.asne.core.persons.SocialPerson;

import ru.com.cardiomagnyl.app.R;
import ru.com.cardiomagnyl.ui.base.BaseStartFragment;

public class WelcomeFragment extends BaseStartFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start_welcome, container, false);
        initFragment(view);
        return view;
    }

    @Override
    public void initParent(Activity activity) {
        ProgressBar progressBarBottomOutsideStartWork = (ProgressBar) activity.findViewById(R.id.progressBarBottomOutsideStartWork);
        TextView textViewBottomOutsideAction = (TextView) activity.findViewById(R.id.textViewBottomOutsideAction);
        View linearLayoutHeader = activity.findViewById(R.id.linearLayoutHeader);
        View textViewFooter = activity.findViewById(R.id.textViewFooter);

        progressBarBottomOutsideStartWork.setMax(5);
        progressBarBottomOutsideStartWork.setProgress(1);
        textViewBottomOutsideAction.setText(activity.getString(R.string.three_minutes));
        linearLayoutHeader.setAlpha(0);
        textViewFooter.setAlpha(0);
    }

    @Override
    public void initSocials(StartActivity startActivity) { /*does nothing*/ }

    @Override
    public void initFieldsFromSocial(int networkId, SocialPerson socialPerson) { /*does nothing*/ }

    private void initFragment(View fragmntView) {
        final ViewPager pager = (ViewPager) getActivity().findViewById(R.id.viewPagerContent);

        fragmntView.findViewById(R.id.textViewSwipe).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(1);
            }
        });
    }

}