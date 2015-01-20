package ru.com.cardiomagnil.ui.start;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.List;

import ru.com.cardiomagnil.ca_model.region.Ca_Region;

public class RegionsSpinnerAdapter extends ArrayAdapter<Ca_Region> implements SpinnerAdapter {
    private final int mTextViewResourceId;
    private final int mDropDownTextViewResourceId;

    public RegionsSpinnerAdapter(Context context, int textViewResourceId, int dropDownTextViewResourceId, List<Ca_Region> regionsList) {
        super(context, textViewResourceId, regionsList);
        mTextViewResourceId = textViewResourceId;
        mDropDownTextViewResourceId = dropDownTextViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(mTextViewResourceId, null);
        }

        Ca_Region regionItem = getItem(position);
        ((TextView) convertView).setText(regionItem.getName());

        if (position == getCount() - 1) {
            parent.setTag(null);
        } else {
            parent.setTag(regionItem);
        }

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(mDropDownTextViewResourceId, null);
        }

        Ca_Region regionItem = getItem(position);
        ((TextView) convertView).setText(regionItem.getName());

        return convertView;
    }
}