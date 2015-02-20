package ru.com.cardiomagnyl.ui.slidingmenu.content;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import ru.com.cardiomagnyl.app.R;
import ru.com.cardiomagnyl.ui.base.BaseItemFragment;
import ru.com.cardiomagnyl.ui.slidingmenu.menu.SlidingMenuActivity;

public class TakingPillsFragment extends BaseItemFragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_taking_pills, null);
        initFragment(view);
        return view;
    }

    @Override
    public void initTopBar(ViewGroup viewGroupTopBar) {
        LinearLayout linearLayoutRightHolder = (LinearLayout) viewGroupTopBar.findViewById(R.id.linearLayoutRightHolder);
        linearLayoutRightHolder.removeAllViews();

        ImageView imageViewBell = new ImageView(viewGroupTopBar.getContext(), null, R.style.ImageViewTop);
        imageViewBell.setImageResource(R.drawable.ic_button_bell_unpressed);

        ImageView imageViewPlus = new ImageView(viewGroupTopBar.getContext(), null, R.style.ImageViewTop);
        imageViewPlus.setImageResource(R.drawable.ic_button_add_pill_unpressed);

        int space_small = (int) viewGroupTopBar.getResources().getDimension(R.dimen.space_small);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(space_small, 0, 0, 0);

        imageViewPlus.setLayoutParams(lp);

        linearLayoutRightHolder.addView(imageViewBell);
        linearLayoutRightHolder.addView(imageViewPlus);
    }

    private void initFragment(View view) {
        View linearLayoutTime1 = view.findViewById(R.id.linearLayoutTime1);
        View linearLayoutTime2 = view.findViewById(R.id.linearLayoutTime2);
        View linearLayoutTime3 = view.findViewById(R.id.linearLayoutTime3);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null && getActivity() instanceof SlidingMenuActivity) {
                    SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
                    Fragment fragment = new AddPillFragment();
                    slidingMenuActivity.putContentOnTop(fragment, false);
                }
            }
        };

        linearLayoutTime1.setOnClickListener(onClickListener);
        linearLayoutTime2.setOnClickListener(onClickListener);
        linearLayoutTime3.setOnClickListener(onClickListener);
    }
}
