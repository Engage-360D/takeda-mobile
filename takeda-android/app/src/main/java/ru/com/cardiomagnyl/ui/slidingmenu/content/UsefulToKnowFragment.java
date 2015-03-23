package ru.com.cardiomagnyl.ui.slidingmenu.content;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import ru.com.cardiomagnyl.app.R;
import ru.com.cardiomagnyl.application.CardiomagnylApplication;
import ru.com.cardiomagnyl.ui.base.BaseItemFragment;

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

    private View.OnClickListener onClickListenerVk = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int position = (Integer) view.getTag();

            String description = usefulDescription[position];
            String content = usefulContent[position];

            // TODO: share by VK
        }
    };

    private View.OnClickListener onClickListenerFb = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int position = (Integer) view.getTag();

            String description = usefulDescription[position];
            String content = usefulContent[position];

            // TODO: share by FB
        }
    };

    private View.OnClickListener onClickListenerOk = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int position = (Integer) view.getTag();

            String description = usefulDescription[position];
            String content = usefulContent[position];

            // TODO: share by OK
        }
    };

    private View.OnClickListener onClickListenerGp = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int position = (Integer) view.getTag();

            String description = usefulDescription[position];
            String content = usefulContent[position];

            // TODO: share by GP
        }
    };

    private void initFragment(final View fragmentView) {
        LinearLayout linearLayoutContent = (LinearLayout) fragmentView.findViewById(R.id.linearLayoutContent);

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

            imageViewVk.setTag(counter);
            imageViewVk.setOnClickListener(onClickListenerVk);

            imageViewFb.setTag(counter);
            imageViewFb.setOnClickListener(onClickListenerFb);

            imageViewOk.setTag(counter);
            imageViewOk.setOnClickListener(onClickListenerOk);

            imageViewGp.setTag(counter);
            imageViewGp.setOnClickListener(onClickListenerGp);
        }
    }

}
