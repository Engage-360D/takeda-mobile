package ru.com.cardiomagnyl.ui.slidingmenu.content.institution;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.android.gms.maps.model.Marker;

import java.util.List;

import ru.com.cardiomagnyl.app.R;
import ru.com.cardiomagnyl.model.institution.Institution;

public class InstitutionsAdapter extends BaseAdapter {
    private final Context mContext;
    private final List<Institution> mInstitutionList;

    public InstitutionsAdapter(Context context, List<Institution> institutionsList) {
        mContext = context;
        mInstitutionList = institutionsList;
    }

    @Override
    public int getCount() {
        return mInstitutionList.size();
    }

    @Override
    public Object getItem(int position) {
        return mInstitutionList.get(position);
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

        textViewName.setText(mInstitutionList.get(position).getName());
        textViewSubname.setText(mInstitutionList.get(position).getAddress());

        return view;
    }

}
