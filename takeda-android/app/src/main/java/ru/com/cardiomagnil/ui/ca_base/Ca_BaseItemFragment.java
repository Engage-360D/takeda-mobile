package ru.com.cardiomagnil.ui.ca_base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import ru.com.cardiomagnil.app.R;

public abstract class Ca_BaseItemFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_heart, null);
    }

    public abstract void initTopBar(ViewGroup viewGroupTopBar);

    protected void initTopBarBellCabinet(ViewGroup viewGroupTopBar, boolean isEnabled) {
        LinearLayout linearLayoutRightHolder = (LinearLayout) viewGroupTopBar.findViewById(R.id.linearLayoutRightHolder);
        linearLayoutRightHolder.removeAllViews();

        int spaceMedium = (int) viewGroupTopBar.getResources().getDimension(R.dimen.ca_space_medium);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, spaceMedium, 0);

        ImageView imageViewBell = new ImageView(viewGroupTopBar.getContext(), null, R.style.ImageViewTop);
        imageViewBell.setImageResource(R.drawable.ca_selector_button_bell);
        imageViewBell.setLayoutParams(lp);
        imageViewBell.setEnabled(isEnabled);
        linearLayoutRightHolder.addView(imageViewBell);

        if (isEnabled) {
            imageViewBell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        } else {
            imageViewBell.setOnClickListener(null);
        }

        ImageView imageViewCabinet = new ImageView(viewGroupTopBar.getContext(), null, R.style.ImageViewTop);
        imageViewCabinet.setImageResource(R.drawable.ca_selector_button_cabinet);
        imageViewCabinet.setLayoutParams(lp);
        imageViewCabinet.setEnabled(isEnabled);
        linearLayoutRightHolder.addView(imageViewCabinet);

        if (isEnabled) {
            imageViewCabinet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        } else {
            imageViewCabinet.setOnClickListener(null);
        }
    }
}