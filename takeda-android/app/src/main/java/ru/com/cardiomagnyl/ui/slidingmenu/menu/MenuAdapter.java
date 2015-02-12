package ru.com.cardiomagnyl.ui.slidingmenu.menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import ru.com.cardiomagnyl.app.R;
import ru.com.cardiomagnyl.application.AppState;

public class MenuAdapter extends ArrayAdapter<MenuItem> {
    private final Context mContext;

    public MenuAdapter(Context context) {
        super(context, 0);
        mContext = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.slidingmenu_list_item, null);
        }

        MenuItem menuItem = getItem(position);

        TextView titleTextView = (TextView) convertView.findViewById(R.id.textViewTitle);
        titleTextView.setText(mContext.getString(menuItem.getItemName()));

        boolean isResultItem = menuItem.equals(MenuItem.item_analysis_results);
        boolean resultNotEmpty = AppState.getInsnatce().getTestResult() != null && AppState.getInsnatce().getTestResult().getId() != null;

        boolean isAnalysisItem = menuItem.equals(MenuItem.item_risk_analysis);
        boolean isDoctor = AppState.getInsnatce().getUser().isDoctor();

        boolean isVisible = menuItem.isItemVisible();
        boolean isClickable = menuItem.isItemEnabled();

        if (isResultItem && resultNotEmpty) {
            isClickable = isVisible = true;
        } else if (isAnalysisItem) {
            isVisible = true;
            isClickable = isDoctor;
        }

        convertView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        convertView.setEnabled(isClickable);
        titleTextView.setEnabled(isClickable);
        // TODO: Why !isClickable?
        convertView.setClickable(!isClickable);

        return convertView;
    }
}