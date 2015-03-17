package ru.com.cardiomagnyl.ui.slidingmenu.content;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import ru.com.cardiomagnyl.app.R;
import ru.com.cardiomagnyl.ui.base.BaseItemFragment;

public class UsefulToKnowFragment extends BaseItemFragment implements View.OnClickListener {
    private enum Socials {vk, fb, ok, gp}

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_useful_to_know, null);
        initFragment(view);
        return view;
    }

    @Override
    public void initTopBar(ViewGroup viewGroupTopBar) {
        initTopBarBellAddPill(viewGroupTopBar, true, true);
    }

    @Override
    public void onClick(View v) {

    }

    private void initFragment(final View fragmentView) {
        LinearLayout linearLayoutContent = (LinearLayout) fragmentView.findViewById(R.id.linearLayoutContent);

        String[] usefulDescription = getResources().getStringArray(R.array.useful_description);
        String[] usefulContent = getResources().getStringArray(R.array.useful_content);
        TypedArray usefulIcons = getResources().obtainTypedArray(R.array.useful_icons);

        for (int counter = 0; counter < usefulDescription.length; ++counter) {
            View item = View.inflate(getActivity(), R.layout.layout_useful_to_know_item, null);
            linearLayoutContent.addView(item);

            TextView textViewDescription = (TextView) item.findViewById(R.id.textViewDescription);
            TextView textViewContent = (TextView) item.findViewById(R.id.textViewContent);

            View imageViewVk = item.findViewById(R.id.imageViewVk);
            View imageViewFb = item.findViewById(R.id.imageViewFb);
            View imageViewOk = item.findViewById(R.id.imageViewOk);
            View imageViewGp = item.findViewById(R.id.imageViewGp);

            textViewDescription.setText(usefulDescription[counter]);
            textViewDescription.setCompoundDrawablesWithIntrinsicBounds(usefulIcons.getDrawable(counter), null, null, null);

            textViewContent.setText(usefulContent[counter]);

            imageViewVk.setTag(Socials.vk);
            imageViewVk.setOnClickListener(this);

            imageViewFb.setTag(Socials.fb);
            imageViewFb.setOnClickListener(this);

            imageViewOk.setTag(Socials.ok);
            imageViewOk.setOnClickListener(this);

            imageViewGp.setTag(Socials.gp);
            imageViewGp.setOnClickListener(this);
        }
    }

}
