package ru.com.cardiomagnyl.ui.slidingmenu.content.institution;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;
import java.util.List;

import ru.com.cardiomagnyl.app.R;
import ru.com.cardiomagnyl.application.Constants;
import ru.com.cardiomagnyl.model.base.BaseModelHelper;
import ru.com.cardiomagnyl.model.common.Response;
import ru.com.cardiomagnyl.model.institution.Institution;
import ru.com.cardiomagnyl.model.institution.InstitutionDao;
import ru.com.cardiomagnyl.model.specialization.Specialization;
import ru.com.cardiomagnyl.model.specialization.SpecializationDao;
import ru.com.cardiomagnyl.model.town.Town;
import ru.com.cardiomagnyl.model.town.TownDao;
import ru.com.cardiomagnyl.ui.base.BaseItemFragment;
import ru.com.cardiomagnyl.ui.slidingmenu.menu.SlidingMenuActivity;
import ru.com.cardiomagnyl.util.CallbackOne;
import ru.com.cardiomagnyl.util.Tools;
import ru.com.cardiomagnyl.widget.CustomSpinnerAdapter;

public class InstitutionsSearchFragment extends BaseItemFragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener {
    private GoogleMap mMap;
//    private final ArrayList<Marker> mMarkers = new ArrayList();

    private ClusterManager<Institution> mClusterManager;

    @Override
    public void initTopBar(ViewGroup viewGroupTopBar) {
        initTopBarBellCabinet(viewGroupTopBar, true, true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_institutions_search, null);
        preInitFragment(view);
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        setIsMapVisible(getView(), true);
        initMap(googleMap);
    }

    private void preInitFragment(final View fragmentView) {
        setIsMapVisible(fragmentView, false);

        // attempt to fix slow initialization of SupportMapFragment ("dirty hack")
        final SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
        if (slidingMenuActivity.getSlidingMenu().isMenuShowing()) {
            slidingMenuActivity.getSlidingMenu().setOnClosedListener(new SlidingMenu.OnClosedListener() {
                @Override
                public void onClosed() {
                    slidingMenuActivity.getSlidingMenu().setOnClosedListener(null);
                    initFragmentStart(fragmentView);
                    initMapFragment();
                }
            });
        } else {
            initMapFragment();
        }
    }

    private void initFragmentStart(final View fragmentView) {
        fragmentView.findViewById(R.id.fragmentContent).setVisibility(View.INVISIBLE);
        fragmentView.findViewById(R.id.textViewMessage).setVisibility(View.INVISIBLE);

        SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
        slidingMenuActivity.showProgressDialog();

        getTowns(fragmentView);
    }

    private void getTowns(final View fragmentView) {
        TownDao.getAll(
                new CallbackOne<List<Town>>() {
                    @Override
                    public void execute(List<Town> townsList) {
                        getSpecializations(fragmentView, townsList);
                    }
                },
                new CallbackOne<Response>() {
                    @Override
                    public void execute(Response responseError) {
                        initFragmentFinish(fragmentView, null, null);
                    }
                }
        );
    }

    private void getSpecializations(final View fragmentView, final List<Town> townsList) {
        SpecializationDao.getAll(
                new CallbackOne<List<Specialization>>() {
                    @Override
                    public void execute(List<Specialization> townspecializationsList) {
                        initFragmentFinish(fragmentView, townsList, townspecializationsList);
                    }
                },
                new CallbackOne<Response>() {
                    @Override
                    public void execute(Response responseError) {
                        initFragmentFinish(fragmentView, townsList, null);
                    }
                }
        );
    }

    private void initFragmentFinish(final View fragmentView, final List<Town> townsList, final List<Specialization> specializationsList) {
        View fragmentContent = fragmentView.findViewById(R.id.fragmentContent);
        View textViewMessage = fragmentView.findViewById(R.id.textViewMessage);

        SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
        slidingMenuActivity.hideProgressDialog();

        if (townsList == null || townsList.isEmpty() || specializationsList == null || specializationsList.isEmpty()) {
            fragmentContent.setVisibility(View.GONE);
            textViewMessage.setVisibility(View.VISIBLE);
        } else {
            textViewMessage.setVisibility(View.GONE);
            fragmentContent.setVisibility(View.VISIBLE);
            initFragmentFinishHelper(fragmentView, townsList, specializationsList);
        }
    }

    private void initFragmentFinishHelper(final View fragmentView, final List<Town> townsList, final List<Specialization> specializationsList) {
        Spinner spinnerTown = (Spinner) fragmentView.findViewById(R.id.spinnerTown);
        Spinner spinnerSpecialization = (Spinner) fragmentView.findViewById(R.id.spinnerSpecialization);

        townsList.add(Town.createNoTown(fragmentView.getContext()));
        specializationsList.add(Specialization.createNoSpecialization(fragmentView.getContext()));

        initSpinner(fragmentView, spinnerTown, new ArrayList<BaseModelHelper>(townsList));
        initSpinner(fragmentView, spinnerSpecialization, new ArrayList<BaseModelHelper>(specializationsList));
    }

    private void initSpinner(final View fragmentView, final Spinner spinner, final List<BaseModelHelper> itemsList) {
        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(spinner.getContext(), R.layout.custom_spinner_item, R.layout.spinner_item_dropdown, itemsList);
        customSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(customSpinnerAdapter);
        spinner.setSelection(customSpinnerAdapter.getCount() - 1, true);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                tryToGetInstitutions(fragmentView);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) { /*does nothing*/ }
        });
    }

    private void tryToGetInstitutions(final View fragmentView) {
        Spinner spinnerTown = (Spinner) fragmentView.findViewById(R.id.spinnerTown);
        Spinner spinnerSpecialization = (Spinner) fragmentView.findViewById(R.id.spinnerSpecialization);

        if (spinnerTown.getTag() != null && spinnerSpecialization.getTag() != null) {
            String town = (String) spinnerTown.getTag();
            String specialization = (String) spinnerSpecialization.getTag();
            getInstitutions(fragmentView, town, specialization);
        }
    }

    private void getInstitutions(final View fragmentView, final String town, final String specialization) {
        final SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
        slidingMenuActivity.showProgressDialog();

        InstitutionDao.getByTownAndSpec(
                town,
                specialization,
                new CallbackOne<List<Institution>>() {
                    @Override
                    public void execute(List<Institution> InstitutionsList) {
                        slidingMenuActivity.hideProgressDialog();
                        showInstitutions(fragmentView, InstitutionsList);
                    }
                },
                new CallbackOne<Response>() {
                    @Override
                    public void execute(Response responseError) {
                        slidingMenuActivity.hideProgressDialog();
                        Tools.showToast(slidingMenuActivity, R.string.error_occurred, Toast.LENGTH_LONG);
                    }
                }
        );
    }

    private void showInstitutions(final View fragmentView, final List<Institution> institutionsList) {
        mClusterManager.clearItems();
        mClusterManager.addItems(institutionsList);
    }

    private void setIsMapVisible(View parentView, boolean isMapVisible) {
        View frameLayoutMapHolder = parentView.findViewById(R.id.frameLayoutMapHolder);
        View textViewLoadingMap = parentView.findViewById(R.id.textViewLoadingMap);

        frameLayoutMapHolder.setVisibility(isMapVisible ? View.VISIBLE : View.INVISIBLE);
        textViewLoadingMap.setVisibility(isMapVisible ? View.INVISIBLE : View.VISIBLE);
    }

    private void initMapFragment() {
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();

        SupportMapFragment supportMapFragment = SupportMapFragment.newInstance();
        fragmentTransaction.replace(R.id.frameLayoutMapHolder, supportMapFragment);
        supportMapFragment.getMapAsync(this);

        fragmentTransaction.commit();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return TextUtils.isEmpty(marker.getTitle());
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        String institutionId = marker.getSnippet();

        Bundle bundle = new Bundle();
        bundle.putString(Constants.INSTITUTION_ID, institutionId);

        InstitutionDetailsFragment institutionDetailsFragment = new InstitutionDetailsFragment();
        institutionDetailsFragment.setArguments(bundle);

        SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
        slidingMenuActivity.putContentOnTop(institutionDetailsFragment, true);
    }

    private void initMap(GoogleMap googleMap) {
        mClusterManager = new ClusterManager<>(getActivity(), googleMap);
        mClusterManager.setRenderer(new CustomClusterRenderer(getActivity(), googleMap, mClusterManager));

        // Gets to GoogleMap from the MapView and does initialization stuff
        mMap = googleMap;
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setMyLocationEnabled(true);
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(getActivity()));
        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnCameraChangeListener(mClusterManager);

        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
        try {
            MapsInitializer.initialize(this.getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Updates the location and zoom of the MapView
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(55.751667, 37.617778), 10);
        mMap.animateCamera(cameraUpdate);
    }

}