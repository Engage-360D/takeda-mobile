package ru.com.cardiomagnyl.ui.slidingmenu.content.pills;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ru.com.cardiomagnyl.app.R;
import ru.com.cardiomagnyl.model.pill.Pill;

public class PillsAdapter extends BaseAdapter {
    private final Context mContext;
    private final List<Pill> mPillsList;

    public PillsAdapter(Context context, List<Pill> pillsList) {
        mContext = context;
        mPillsList = pillsList;
    }

    @Override
    public int getCount() {
        return mPillsList.size();
    }

    @Override
    public Object getItem(int position) {
        return mPillsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View view = (convertView != null ? convertView : View.inflate(mContext, R.layout.list_item_two_lines, null));

        final TextView textViewName = (TextView) view.findViewById(R.id.textViewName);
        final TextView textViewSubname = (TextView) view.findViewById(R.id.textViewSubname);

        textViewName.setText(mPillsList.get(position).getName());
        textViewSubname.setText(String.valueOf(mPillsList.get(position).getQuantity()) + mContext.getString(R.string.pcs));

        return view;
    }

}
