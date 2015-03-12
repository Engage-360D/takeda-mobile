package ru.com.cardiomagnyl.ui.slidingmenu.content.institution;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import ru.com.cardiomagnyl.app.R;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private final View mWindow;
    private final View mContents;

    CustomInfoWindowAdapter(Context context) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mWindow = layoutInflater.inflate(R.layout.map_custom_info_window, null);
        mContents = layoutInflater.inflate(R.layout.map_custom_info_contents, null);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        render(marker, mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        render(marker, mContents);
        return mContents;
    }

    private void render(final Marker marker, final View view) {
        TextView textViewTitle = (TextView) view.findViewById(R.id.textViewTitle);
        textViewTitle.setText(marker.getTitle().split("\n")[0]);
    }

}