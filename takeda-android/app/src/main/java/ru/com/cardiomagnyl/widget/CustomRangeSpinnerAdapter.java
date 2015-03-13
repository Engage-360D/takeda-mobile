package ru.com.cardiomagnyl.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.List;

import ru.com.cardiomagnyl.app.R;

public class CustomRangeSpinnerAdapter extends ArrayAdapter<Integer> implements SpinnerAdapter {
    private final  Context mContext;
    private final int mTextViewResourceId;
    private final int mDropDownTextViewResourceId;

    public CustomRangeSpinnerAdapter(Context context, int textViewResourceId, int dropDownTextViewResourceId, List<Integer> range) {
        super(context, textViewResourceId, range);
        mContext = context;
        mTextViewResourceId = textViewResourceId;
        mDropDownTextViewResourceId = dropDownTextViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(mTextViewResourceId, null);
        }

        Integer range = getItem(position);
        if (range == null) {
            ((TextView) convertView).setText(mContext.getString(R.string.not_specified));
            parent.setTag(null);
        }else {
            ((TextView) convertView).setText(String.valueOf(range));
            parent.setTag(range);
        }

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(mDropDownTextViewResourceId, null);
        }

        Integer range = getItem(position);
        if (range == null) {
            ((TextView) convertView).setText(mContext.getString(R.string.not_specified));
        }else {
            ((TextView) convertView).setText(String.valueOf(range));
        }

        return convertView;
    }
}
