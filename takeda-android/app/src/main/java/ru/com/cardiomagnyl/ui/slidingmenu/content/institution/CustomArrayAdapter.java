package ru.com.cardiomagnyl.ui.slidingmenu.content.institution;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.com.cardiomagnyl.model.base.BaseModelHelper;

public class CustomArrayAdapter extends ArrayAdapter<BaseModelHelper> implements Filterable {
    private final List<BaseModelHelper> mItems;
    private final List<BaseModelHelper> mItemsAll;
    private final int mDropDownTextViewResourceId;

    public CustomArrayAdapter(Context context, int dropDownTextViewResourceId, List<BaseModelHelper> items) {
        super(context, dropDownTextViewResourceId, items);
        mItems = items;
        mItemsAll = new ArrayList<>(items);

        mDropDownTextViewResourceId = dropDownTextViewResourceId;
    }

    @Override
    public BaseModelHelper getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(mDropDownTextViewResourceId, null);
        }

        ((TextView) convertView).setText(mItems.get(position).getName());

        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            public String convertResultToString(Object resultValue) {
                return ((BaseModelHelper) resultValue).getName();
            }

            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                List<BaseModelHelper> filtered = new ArrayList<>();
                if (charSequence != null) {
                    String filterText = ((String) charSequence).toUpperCase().trim();
                    if (filterText.length() != 0) {
                        for (BaseModelHelper baseModelHelper : mItemsAll) {
                            if (baseModelHelper.getName().toUpperCase().startsWith(filterText)) {
                                filtered.add(baseModelHelper);
                            }
                        }
                    }
                }

                FilterResults result = new FilterResults();
                result.values = filtered;
                result.count = filtered.size();

                return result;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                notifyDataSetInvalidated();
                mItems.clear();
                mItems.addAll((List<BaseModelHelper>) filterResults.values);
                notifyDataSetChanged();
            }
        };
    }

    public List<BaseModelHelper> getItemsList() {
        return mItemsAll;
    }

}