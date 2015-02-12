package ru.com.cardiomagnyl.ui.slidingmenu.content;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import ru.com.cardiomagnyl.app.R;
import ru.com.cardiomagnyl.ui.base.BaseItemFragment;
import ru.com.cardiomagnyl.ui.slidingmenu.menu.SlidingMenuActivity;

public class SearchInstitutionsFragment extends BaseItemFragment implements OnMapReadyCallback {
    GoogleMap map;

    @Override
    public void initTopBar(ViewGroup viewGroupTopBar) {
        initTopBarBellCabinet(viewGroupTopBar, true, true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_search_institutions, null);
        setIsMapVisible(view, false);
        initMapFragment();
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        setIsMapVisible(getView(), true);
        initMap(googleMap);
    }

    private void initMapFragment() {
        // attempt to fix slow initialization of SupportMapFragment ("dirty hack")
        final SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
        if (slidingMenuActivity.getSlidingMenu().isMenuShowing()) {
            slidingMenuActivity.getSlidingMenu().setOnClosedListener(new SlidingMenu.OnClosedListener() {
                @Override
                public void onClosed() {
                    slidingMenuActivity.getSlidingMenu().setOnClosedListener(null);
                    initMapFragmentHelper();
                }
            });
        } else {
            initMapFragmentHelper();
        }
    }

    private void setIsMapVisible(View parentView, boolean isMapVisible) {
        View frameLayoutMapHolder = parentView.findViewById(R.id.frameLayoutMapHolder);
        View textViewLoadingMap = parentView.findViewById(R.id.textViewLoadingMap);

        frameLayoutMapHolder.setVisibility(isMapVisible ? View.VISIBLE : View.INVISIBLE);
        textViewLoadingMap.setVisibility(isMapVisible ? View.INVISIBLE : View.VISIBLE);
    }

    private void initMapFragmentHelper() {
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();

        SupportMapFragment supportMapFragment = SupportMapFragment.newInstance();
        fragmentTransaction.replace(R.id.frameLayoutMapHolder, supportMapFragment);
        supportMapFragment.getMapAsync(this);

        fragmentTransaction.commit();
    }

    private void initMap(GoogleMap googleMap) {
        // Gets to GoogleMap from the MapView and does initialization stuff
        map = googleMap;
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
    }
}
