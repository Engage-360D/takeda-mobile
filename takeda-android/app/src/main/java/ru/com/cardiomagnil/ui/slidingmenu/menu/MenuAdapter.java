package ru.com.cardiomagnil.ui.slidingmenu.menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import ru.com.cardiomagnil.app.R;
import ru.com.cardiomagnil.application.AppState;

public class MenuAdapter extends ArrayAdapter<MenuItem> {
    private final Context mContext;

    public MenuAdapter(Context context) {
        super(context, 0);
        mContext = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.ca_slidingmenu_list_item, null);
        }

        MenuItem menuItem = getItem(position);

        TextView titleTextView = (TextView) convertView.findViewById(R.id.textViewTitle);
        titleTextView.setText(mContext.getString(menuItem.getItemName()));

        boolean isResultFragment = menuItem.getItemClass().equals(MenuItem.item_analysis_results);
        boolean resultNotEmpty = AppState.getInstatce().getTestResult() != null && AppState.getInstatce().getTestResult().getId() != null;

        boolean isVisible = menuItem.isItemVisible();
        boolean isClickable = menuItem.isItemEnabled();
        if (isResultFragment && resultNotEmpty) isClickable = isVisible = true;

        convertView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        convertView.setEnabled(isClickable);
        titleTextView.setEnabled(isClickable);
        // TODO: Why !isClickable?
        convertView.setClickable(!isClickable);

        return convertView;
    }
}