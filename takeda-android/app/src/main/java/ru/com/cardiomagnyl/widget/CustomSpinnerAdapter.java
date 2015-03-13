package ru.com.cardiomagnyl.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.List;

import ru.com.cardiomagnyl.model.base.BaseModelHelper;

public class CustomSpinnerAdapter extends ArrayAdapter<BaseModelHelper> implements SpinnerAdapter {
    private final int mTextViewResourceId;
    private final int mDropDownTextViewResourceId;

    public CustomSpinnerAdapter(Context context, int textViewResourceId, int dropDownTextViewResourceId, List<BaseModelHelper> itemsList) {
        super(context, textViewResourceId, itemsList);
        mTextViewResourceId = textViewResourceId;
        mDropDownTextViewResourceId = dropDownTextViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(mTextViewResourceId, null);
        }

        BaseModelHelper item = getItem(position);
        ((TextView) convertView).setText(item.getName());

        if (position == getCount() - 1) {
            parent.setTag(null);
        } else {
            parent.setTag(item.getId());
        }

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(mDropDownTextViewResourceId, null);
        }

        BaseModelHelper item = getItem(position);
        ((TextView) convertView).setText(item.getName());

        return convertView;
    }

}