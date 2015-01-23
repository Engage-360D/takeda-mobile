package ru.com.cardiomagnil.ui.ca_menu;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import ru.com.cardiomagnil.app.R;
import ru.com.cardiomagnil.application.AppState;
import ru.com.cardiomagnil.ui.ca_content.Ca_MenuItem;
import ru.com.cardiomagnil.ui.slidingmenu.SlidingMenuActivity;

public class Ca_MenuAdapter extends ArrayAdapter<Ca_MenuItem> {
    private final Context mContext;

    public Ca_MenuAdapter(Context context) {
        super(context, 0);
        mContext = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.ca_slidingmenu_list_item, null);
        }

        Ca_MenuItem menuItem = getItem(position);

        Class itemClass = menuItem.getItemClass();
        String itemTitle = mContext.getString(menuItem.getItemName()) ;
        boolean isVisible = menuItem.isItemVisible();
        boolean isClickable = menuItem.isItemEnabled();

        TextView titleTextView = (TextView) convertView.findViewById(R.id.textViewTitle);
        titleTextView.setText(itemTitle);

        boolean isResultFragment = menuItem.getItemClass().equals(Ca_MenuItem.item_analysis_results);
        boolean resultNotEmpty = AppState.getInstatce().getTestResult() != null && AppState.getInstatce().getTestResult().isInitialized();

        if (isResultFragment && resultNotEmpty) {
            isClickable = true;
            isVisible = true;
        }

        convertView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        if (isClickable) {
            // TODO: Why !isClickable?
            convertView.setClickable(false);
            convertView.setEnabled(true);
        } else {
            // TODO: Why !isClickable?
            convertView.setClickable(true);
            convertView.setEnabled(false);
            titleTextView.setTextColor(mContext.getResources().getColor(R.color.ca_text_menu_disabled));
        }

        if (mContext != null && mContext instanceof SlidingMenuActivity) {
            SlidingMenuActivity mainActivity = (SlidingMenuActivity) mContext;
            // FIXME!!! getCurrentFragment must be protected
            Fragment currentFragment = mainActivity.getCurrentFragment();
            // FIXME: uncomment
//            if (currentFragment != null && currentFragment.getClass().getName().equals(itemClass.getName()) && mPreviousSelectedItem == null) {
//                setSelectedItem(convertView);
//            }
        }

        return convertView;
    }
}