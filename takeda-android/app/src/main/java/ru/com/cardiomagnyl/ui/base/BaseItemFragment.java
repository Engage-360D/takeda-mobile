package ru.com.cardiomagnyl.ui.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import ru.com.cardiomagnyl.app.R;
import ru.com.cardiomagnyl.ui.slidingmenu.content.personal_cabinet.CabinetDataFragment;
import ru.com.cardiomagnyl.ui.slidingmenu.menu.SlidingMenuActivity;

public abstract class BaseItemFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_heart, null);
    }

    public abstract void initTopBar(ViewGroup viewGroupTopBar);

    protected void initTopBarBellCabinet(ViewGroup viewGroupTopBar, boolean isBellEnabled, boolean isCabinetEnabled) {
        LinearLayout linearLayoutRightHolder = (LinearLayout) viewGroupTopBar.findViewById(R.id.linearLayoutRightHolder);
        linearLayoutRightHolder.removeAllViews();

        int spaceMedium = (int) viewGroupTopBar.getResources().getDimension(R.dimen.space_medium);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, spaceMedium, 0);

        ImageView imageViewBell = new ImageView(viewGroupTopBar.getContext(), null, R.style.ImageViewTop);
        imageViewBell.setImageResource(R.drawable.selector_button_bell);
        imageViewBell.setLayoutParams(lp);
        imageViewBell.setEnabled(isBellEnabled);
        linearLayoutRightHolder.addView(imageViewBell);

        if (isBellEnabled) {
            imageViewBell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        } else {
            imageViewBell.setOnClickListener(null);
        }

        ImageView imageViewCabinet = new ImageView(viewGroupTopBar.getContext(), null, R.style.ImageViewTop);
        imageViewCabinet.setImageResource(R.drawable.selector_button_cabinet);
        imageViewCabinet.setLayoutParams(lp);
        imageViewCabinet.setEnabled(isCabinetEnabled);
        linearLayoutRightHolder.addView(imageViewCabinet);

        if (isCabinetEnabled) {
            imageViewCabinet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getActivity() != null && getActivity() instanceof SlidingMenuActivity) {
                        Fragment fragment = new CabinetDataFragment();
                        SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
                        slidingMenuActivity.replaceAllContent(fragment, false);
                    }
                }
            });
        } else {
            imageViewCabinet.setClickable(false);
        }
    }

}