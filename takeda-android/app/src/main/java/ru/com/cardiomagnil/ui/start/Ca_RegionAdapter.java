package ru.com.cardiomagnil.ui.start;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.List;

import ru.com.cardiomagnil.app.R;
import ru.com.cardiomagnil.ca_model.region.Ca_Region;

public class Ca_RegionAdapter extends ArrayAdapter<Ca_Region> implements SpinnerAdapter {
    private final int mTextViewResourceId;

    public Ca_RegionAdapter(Context context, int textViewResourceId, List<Ca_Region> regionsList) {
        super(context, textViewResourceId, regionsList);
        mTextViewResourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(mTextViewResourceId, null);
        }

        Ca_Region regionItem = getItem(position);
        ((TextView) convertView).setText(regionItem.getName());


        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.ca_spinner_item_dropdown, null);
        }

        Ca_Region regionItem = getItem(position);
        ((TextView) convertView).setText(regionItem.getName());

        return convertView;
    }
}