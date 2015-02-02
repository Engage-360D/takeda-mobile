package ru.com.cardiomagnil.ui.slidingmenu.content;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.com.cardiomagnil.app.R;
import ru.com.cardiomagnil.ui.base.BaseItemFragment;
import ru.com.cardiomagnil.util.Tools;

public class Ca_JournalFargment extends BaseItemFragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ca_fragment_slidingmenu_journal, null);
        initFargment(view);
        return view;
    }

    @Override
    public void initTopBar(ViewGroup viewGroupTopBar) {
    }

    private void initFargment(View view) {
        View item1 = view.findViewById(R.id.item1);
        View item2 = view.findViewById(R.id.item2);
        View item3 = view.findViewById(R.id.item3);

        TextView textViewDate1 = (TextView) item1.findViewById(R.id.textViewDate);
        TextView textViewDate2 = (TextView) item2.findViewById(R.id.textViewDate);
        TextView textViewDate3 = (TextView) item3.findViewById(R.id.textViewDate);

        textViewDate1.setText(Tools.getDayOfWeek(0));
        textViewDate2.setText(Tools.getDayOfWeek(-1));
        textViewDate3.setText(Tools.getDayOfWeek(-2));


//        View linearLayoutTime1 = view.findViewById(R.id.linearLayoutTime1);
//        View linearLayoutTime2 = view.findViewById(R.id.linearLayoutTime1);
//        View linearLayoutTime3 = view.findViewById(R.id.linearLayoutTime1);
//
//        View.OnClickListener onClickListener = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (getActivity() != null && getActivity() instanceof SlidingMenuActivity) {
//                    SlidingMenuActivity mainActivity = (SlidingMenuActivity) getActivity();
//                    Fragment fragment = new Ca_AddPillsFargment();
//                    mainActivity.switchContent(fragment);
//                }
//            }
//        };
//
//        linearLayoutTime1.setOnClickListener(onClickListener);
//        linearLayoutTime2.setOnClickListener(onClickListener);
//        linearLayoutTime3.setOnClickListener(onClickListener);
    }
}
