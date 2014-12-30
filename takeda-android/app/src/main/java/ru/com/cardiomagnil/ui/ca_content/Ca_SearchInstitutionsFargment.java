package ru.com.cardiomagnil.ui.ca_content;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import ru.com.cardiomagnil.app.R;
import ru.com.cardiomagnil.ui.ca_base.Ca_BaseItemFragment;

public class Ca_SearchInstitutionsFargment extends Ca_BaseItemFragment {
    GoogleMap map;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ca_fragment_slidingmenu_search_institutions, null);

        FragmentManager fm = getChildFragmentManager();
        SupportMapFragment supportMapFragment  = (SupportMapFragment)fm.findFragmentById(R.id.order_info_fragment_map);

        // Gets to GoogleMap from the MapView and does initialization stuff
        map = supportMapFragment.getMap();
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.setMyLocationEnabled(true);

        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
        try {
            MapsInitializer.initialize(this.getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Updates the location and zoom of the MapView
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(55.751667, 37.617778), 10);
        map.animateCamera(cameraUpdate);

        return view;
    }

    @Override
    public String getMenuInetmName() {
        return null;
    }

    @Override
    public View getTopView() {
        return null;
    }
}