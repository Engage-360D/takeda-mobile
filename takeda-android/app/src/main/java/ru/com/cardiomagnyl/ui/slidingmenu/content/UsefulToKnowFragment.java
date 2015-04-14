package ru.com.cardiomagnyl.ui.slidingmenu.content;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.gorbin.asne.core.AccessToken;
import com.github.gorbin.asne.core.persons.SocialPerson;

import java.util.ArrayList;
import java.util.List;

import ru.com.cardiomagnyl.api.Url;
import ru.com.cardiomagnyl.app.R;
import ru.com.cardiomagnyl.application.CardiomagnylApplication;
import ru.com.cardiomagnyl.application.SocialManager;
import ru.com.cardiomagnyl.ui.base.BaseItemFragment;
import ru.com.cardiomagnyl.ui.slidingmenu.menu.SlidingMenuActivity;

public class UsefulToKnowFragment extends BaseItemFragment {
    private String[] usefulDescription = CardiomagnylApplication.getAppContext().getResources().getStringArray(R.array.useful_description);
    private String[] usefulContent = CardiomagnylApplication.getAppContext().getResources().getStringArray(R.array.useful_content);
    private TypedArray usefulIcons = CardiomagnylApplication.getAppContext().getResources().obtainTypedArray(R.array.useful_icons);

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_useful_to_know, null);
        initFragment(view);
        return view;
    }

    @Override
    public void initTopBar(ViewGroup viewGroupTopBar) {
        initTopBarBellCabinet(viewGroupTopBar, true, true);
    }

    private void initFragment(final View fragmentView) {
        LinearLayout linearLayoutContent = (LinearLayout) fragmentView.findViewById(R.id.linearLayoutContent);

        SocialManager socialManager = new SocialManager((SlidingMenuActivity) getActivity());

        List<View> socialsHolders = new ArrayList<>();

        for (int counter = 0; counter < usefulDescription.length; ++counter) {
            View item = View.inflate(getActivity(), R.layout.layout_useful_to_know_item, null);
            linearLayoutContent.addView(item);

            TextView textViewDescription = (TextView) item.findViewById(R.id.textViewDescription);
            TextView textViewContent = (TextView) item.findViewById(R.id.textViewContent);

            View imageViewVk = item.findViewById(R.id.imageViewVK);
            View imageViewFb = item.findViewById(R.id.imageViewFB);
            View imageViewOk = item.findViewById(R.id.imageViewOK);
            View imageViewGp = item.findViewById(R.id.imageViewGP);

            Pair<String, String> link = new Pair<>(usefulDescription[counter], String.format(Url.SHARE_LINK, counter + 1, counter + 1));
            Pair<String, String> link_gp = new Pair<>(usefulDescription[counter], String.format(Url.SHARE_LINK_GP, counter + 1, counter + 1));
            imageViewVk.setTag(link);
            imageViewFb.setTag(link);
            imageViewOk.setTag(link);
            imageViewGp.setTag(link_gp);

            textViewDescription.setText(usefulDescription[counter]);
            textViewDescription.setCompoundDrawablesWithIntrinsicBounds(usefulIcons.getDrawable(counter), null, null, null);

            textViewContent.setText(usefulContent[counter]);

            socialsHolders.add(item);
        }

        socialManager.initSocials(this, socialsHolders, null);
    }

}
