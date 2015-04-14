package ru.com.cardiomagnyl.ui.slidingmenu.content.institution;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.MarkerManager;
import com.google.maps.android.clustering.ClusterManager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;
import java.util.Collection;
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
import ru.com.cardiomagnyl.widget.CustomAutoCompleteTextView;
import ru.com.cardiomagnyl.widget.CustomExpandAnimation;
import ru.com.cardiomagnyl.widget.CustomSpinnerAdapter;

public class InstitutionsSearchFragment extends BaseItemFragment
        implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnInfoWindowClickListener,
        SwipeRefreshLayout.OnRefreshListener,
        GoogleMap.OnCameraChangeListener {
    private GoogleMap mGoogleMap;
    private final List<Institution> mInstitutionsList = new ArrayList<>();
    private final List<Town> mTownsList = new ArrayList<>();
    private final List<Specialization> mSpecializationsList = new ArrayList<>();
    private boolean mMapMode = true;
    private ClusterManager<Institution> mClusterManager;
    private CameraPosition mCameraPosition;
    private View mFragmentView;

    private static int ONE_POINT_ZOOM = 14;

    @Override
    public void initTopBar(ViewGroup viewGroupTopBar) {
        initTopBarBellCabinet(viewGroupTopBar, true, true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mFragmentView != null && mFragmentView.getParent() != null) {
            ((ViewGroup) mFragmentView.getParent()).removeView(mFragmentView);
            initFragmentFinish(mFragmentView, mTownsList, mSpecializationsList);
            initMapFragment();
            return mFragmentView;
        }

        mFragmentView = inflater.inflate(R.layout.fragment_institutions_search, null);
        preInitFragment(mFragmentView);
        return mFragmentView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        initMap(googleMap);
        setContentVisibility(getView(), mMapMode, !mMapMode);
    }

    @Override
    public void onRefresh() {
        // FIXME: implement body

        View fragmentView = getView();
//
//        fragmentView.findViewById(R.id.fragmentContent).setVisibility(View.INVISIBLE);
//        fragmentView.findViewById(R.id.textViewMessage).setVisibility(View.INVISIBLE);
//
//        SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
//        slidingMenuActivity.showProgressDialog();
//
//        getPillHttp(fragmentView);
//
        SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout) fragmentView.findViewById(R.id.customSwipeRefreshLayout);
        swipeLayout.setRefreshing(false);
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        mClusterManager.onCameraChange(cameraPosition);
        mCameraPosition = cameraPosition;
    }

    private void preInitFragment(final View fragmentView) {
        setContentVisibility(fragmentView, false, false);

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
            initFragmentFinish(mFragmentView, mTownsList, mSpecializationsList);
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
                        updateTownsList(townsList);
                        getSpecializations(fragmentView, townsList);
                    }
                },
                new CallbackOne<Response>() {
                    @Override
                    public void execute(Response responseError) {
                        updateTownsList(null);
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
                        updateSpecializationsList(townspecializationsList);
                        initFragmentFinish(fragmentView, townsList, townspecializationsList);
                    }
                },
                new CallbackOne<Response>() {
                    @Override
                    public void execute(Response responseError) {
                        updateSpecializationsList(null);
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
        Spinner spinnerSpecialization = (Spinner) fragmentView.findViewById(R.id.spinnerSpecialization);
        CustomAutoCompleteTextView autoCompleteTextViewTown = (CustomAutoCompleteTextView) fragmentView.findViewById(R.id.autoCompleteTextViewTown);
        CustomAutoCompleteTextView autoCompleteTextViewInstitution = (CustomAutoCompleteTextView) fragmentView.findViewById(R.id.autoCompleteTextViewInstitution);
        RadioGroup radioGroupCondition = (RadioGroup) fragmentView.findViewById(R.id.radioGroupCondition);


        View imageViewSearch = fragmentView.findViewById(R.id.imageViewSearch);
        final LinearLayout linearLayoutSearch = (LinearLayout) fragmentView.findViewById(R.id.linearLayoutSearch);

        initInstitutionsList(fragmentView);
        townsList.add(Town.createNoTown(fragmentView.getContext()));
        specializationsList.add(Specialization.createNoSpecialization(fragmentView.getContext()));

        initSpinner(fragmentView, spinnerSpecialization, new ArrayList<BaseModelHelper>(specializationsList), specializationsList.size() - 1);
        initAutoCompleteTextViewTown(fragmentView, autoCompleteTextViewTown, new ArrayList<BaseModelHelper>(townsList));
        if (mInstitutionsList.isEmpty()) {
            initAutoCompleteTextViewInstitution(fragmentView, autoCompleteTextViewInstitution, new ArrayList<BaseModelHelper>());
        } else {
            initAutoCompleteTextViewInstitution(fragmentView, autoCompleteTextViewInstitution, new ArrayList<BaseModelHelper>(mInstitutionsList));
            autoCompleteTextViewInstitution.setVisibility(View.VISIBLE);
        }

        CustomSpinnerAdapter customSpinnerAdapter = (CustomSpinnerAdapter) spinnerSpecialization.getAdapter();
        if (customSpinnerAdapter != null && spinnerSpecialization.getTag() == null) {
            spinnerSpecialization.setSelection(0);
            spinnerSpecialization.setTag(customSpinnerAdapter.getItem(0).getName());
            tryToGetInstitutions(mFragmentView);
        }

        radioGroupCondition.setOnCheckedChangeListener(null);
        setContentVisibility(fragmentView, mMapMode, !mMapMode);
        radioGroupCondition.check(mMapMode ? R.id.radioButtonMap : R.id.radioButtonList);
        radioGroupCondition.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                clearAutoCompleteTextViewInstitution(fragmentView);
                switch (checkedId) {
                    case R.id.radioButtonMap:
                        mMapMode = true;
                        setContentVisibility(fragmentView, true, false);
                        if (mMapMode) updateInstitutionsOnMap(fragmentView, mInstitutionsList);
                        break;
                    case R.id.radioButtonList:
                        mMapMode = false;
                        setContentVisibility(fragmentView, false, true);
                        break;
                }
            }
        });

        final int plateMedium = (int) fragmentView.getContext().getResources().getDimension(R.dimen.space_medium);
        imageViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linearLayoutSearch.getVisibility() == View.VISIBLE) {
                    CustomExpandAnimation customExpandAnimation = new CustomExpandAnimation(linearLayoutSearch, 300, CustomExpandAnimation.COLLAPSE);
                    linearLayoutSearch.startAnimation(customExpandAnimation);
                } else {
                    CustomExpandAnimation customExpandAnimation = new CustomExpandAnimation(linearLayoutSearch, 300, CustomExpandAnimation.EXPAND);
                    customExpandAnimation.setHeight(plateMedium);
                    linearLayoutSearch.startAnimation(customExpandAnimation);
                }
            }
        });
    }

    private void initInstitutionsList(final View fragmentView) {
        final ListView listViewInstitutions = (ListView) fragmentView.findViewById(R.id.listViewInstitutions);
        final SwipeRefreshLayout customSwipeRefreshLayout = (SwipeRefreshLayout) fragmentView.findViewById(R.id.customSwipeRefreshLayout);

        customSwipeRefreshLayout.setOnRefreshListener(this);

        final InstitutionsAdapter institutionsAdapter = new InstitutionsAdapter(InstitutionsSearchFragment.this.getActivity(), mInstitutionsList);
        listViewInstitutions.setAdapter(institutionsAdapter);

        listViewInstitutions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Institution institution = (Institution) institutionsAdapter.getItem(position);
                showInstitutionOnMapAndList(fragmentView, institution);
                showInstitutionDetailsFragment(institution.getId());
            }
        });
    }

    private void initAutoCompleteTextViewTown(final View fragmentView, final CustomAutoCompleteTextView autoCompleteTextView, final List<BaseModelHelper> itemsList) {
        final CustomArrayAdapter customArrayAdapter = new CustomArrayAdapter(fragmentView.getContext(), R.layout.spinner_item_dropdown, itemsList);
        autoCompleteTextView.setAdapter(customArrayAdapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Tools.hideKeyboard(autoCompleteTextView);
                autoCompleteTextView.setTag(customArrayAdapter.getItem(position).getName());
                tryToGetInstitutions(fragmentView);
            }
        });

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    autoCompleteTextView.publicPerformFiltering("", 0);
                    autoCompleteTextView.showDropDown();
                }
            }
        });
    }

    private void initAutoCompleteTextViewInstitution(final View fragmentView, final CustomAutoCompleteTextView autoCompleteTextView, final List<BaseModelHelper> itemsList) {
        final CustomArrayAdapter customArrayAdapter = new CustomArrayAdapter(fragmentView.getContext(), R.layout.spinner_item_dropdown, itemsList);
        autoCompleteTextView.setAdapter(customArrayAdapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Tools.hideKeyboard(autoCompleteTextView);
                Institution institution = (Institution) customArrayAdapter.getItem(position);
                autoCompleteTextView.setTag(institution.getName());
                showInstitutionOnMapAndList(fragmentView, institution);
            }
        });

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0 && autoCompleteTextView.getAdapter() != null) {
                    autoCompleteTextView.publicPerformFiltering("", 0);
                    autoCompleteTextView.showDropDown();
                }
            }
        });
    }

    private void showInstitutionOnMapAndList(final View fragmentView, final Institution institution) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(institution.getLat(), institution.getLng()), ONE_POINT_ZOOM);
        mGoogleMap.animateCamera(cameraUpdate);
        showInfoWindow(institution);
        scrollToListItem(fragmentView, institution);
    }

    private void initSpinner(final View fragmentView, final Spinner spinner, final List<BaseModelHelper> itemsList, int selection) {
        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(fragmentView.getContext(), R.layout.custom_spinner_item, R.layout.spinner_item_dropdown, itemsList);
        customSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(customSpinnerAdapter);
        spinner.setSelection(selection, true);

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
        AutoCompleteTextView autoCompleteTextViewTown = (AutoCompleteTextView) fragmentView.findViewById(R.id.autoCompleteTextViewTown);
        Spinner spinnerSpecialization = (Spinner) fragmentView.findViewById(R.id.spinnerSpecialization);

        if (autoCompleteTextViewTown.getTag() != null && spinnerSpecialization.getTag() != null) {
            mCameraPosition = null;
            String town = (String) autoCompleteTextViewTown.getTag();
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
                        updateInstitutions(fragmentView, InstitutionsList);
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

    private void updateTownsList(List<Town> townsList) {
        mTownsList.clear();
        if (townsList != null) mTownsList.addAll(townsList);
    }

    private void updateSpecializationsList(List<Specialization> specializationsList) {
        mSpecializationsList.clear();
        if (specializationsList != null) mSpecializationsList.addAll(specializationsList);
    }

    private void updateInstitutions(final View fragmentView, final List<Institution> institutionsList) {
        AutoCompleteTextView autoCompleteTextViewInstitution = (AutoCompleteTextView) fragmentView.findViewById(R.id.autoCompleteTextViewInstitution);
        CustomArrayAdapter customArrayAdapter = (CustomArrayAdapter) autoCompleteTextViewInstitution.getAdapter();

        autoCompleteTextViewInstitution.setVisibility(View.VISIBLE);
        autoCompleteTextViewInstitution.clearListSelection();
        autoCompleteTextViewInstitution.setText("");

        customArrayAdapter.notifyDataSetInvalidated();
        customArrayAdapter.getItemsList().clear();
        customArrayAdapter.getItemsList().addAll(institutionsList);
        customArrayAdapter.notifyDataSetChanged();

        if (institutionsList.isEmpty()) {
            Tools.showToast(fragmentView.getContext(), R.string.data_not_found, Toast.LENGTH_LONG);
        }

        updateInstitutionsInList(fragmentView, institutionsList);
        if (mMapMode) updateInstitutionsOnMap(fragmentView, institutionsList);
    }

    private void updateInstitutionsInList(final View fragmentView, final List<Institution> institutionsList) {
        ListView listViewInstitutions = (ListView) fragmentView.findViewById(R.id.listViewInstitutions);
        InstitutionsAdapter institutionsAdapter = (InstitutionsAdapter) listViewInstitutions.getAdapter();

        // not initialized yet
        if (institutionsAdapter == null) return;

        institutionsAdapter.notifyDataSetInvalidated();
        mInstitutionsList.clear();
        mInstitutionsList.addAll(institutionsList);
        institutionsAdapter.notifyDataSetChanged();
    }

    private void updateInstitutionsOnMap(final View fragmentView, final List<Institution> institutionsList) {
        mClusterManager.clearItems();
        mClusterManager.addItems(mInstitutionsList);
        mClusterManager.cluster();

        if (mCameraPosition != null) {
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(mCameraPosition.target, mCameraPosition.zoom);
            mGoogleMap.moveCamera(cameraUpdate);
        } else if (institutionsList.size() == 1) {
            LatLng latLng = new LatLng(institutionsList.get(0).getLat(), institutionsList.get(0).getLng());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, ONE_POINT_ZOOM);
            mGoogleMap.animateCamera(cameraUpdate);
        } else if (!institutionsList.isEmpty()) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (Institution institution : institutionsList) {
                builder.include(institution.getPosition());
            }
            LatLngBounds bounds = builder.build();
            int spaceMedium = (int) fragmentView.getResources().getDimension(R.dimen.space_medium);
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, spaceMedium);
            mGoogleMap.moveCamera(cameraUpdate);
        }
    }

    private void showInfoWindow(Institution institution) {
        MarkerManager.Collection markerCollection = mClusterManager.getMarkerCollection();
        Collection<com.google.android.gms.maps.model.Marker> markers = markerCollection.getMarkers();
        for (Marker marker : markers) {
            if (marker.getSnippet().equals(institution.getId())) {
                marker.showInfoWindow();
                break;
            }
        }
    }

    private void scrollToListItem(final View fragmentView, final Institution institution) {
        ListView listViewInstitutions = (ListView) fragmentView.findViewById(R.id.listViewInstitutions);
        InstitutionsAdapter institutionsAdapter = (InstitutionsAdapter) listViewInstitutions.getAdapter();

        for (int position = 0; position < institutionsAdapter.getCount(); ++position) {
            if (((Institution) institutionsAdapter.getItem(position)).getId().equals(institution.getId())) {
                listViewInstitutions.setSelection(position);
                break;
            }
        }
    }

    private void setContentVisibility(View fragmentView, boolean isMapVisible, boolean isListVisible) {
        View frameLayoutMapHolder = fragmentView.findViewById(R.id.frameLayoutMapHolder);
        View viewLoadingMap = fragmentView.findViewById(R.id.viewLoadingMap);
        View customSwipeRefreshLayout = fragmentView.findViewById(R.id.customSwipeRefreshLayout);

        frameLayoutMapHolder.setVisibility(isMapVisible ? View.VISIBLE : View.INVISIBLE);
        viewLoadingMap.setVisibility(isMapVisible || isListVisible ? View.GONE : View.VISIBLE);
        customSwipeRefreshLayout.setVisibility(isListVisible ? View.VISIBLE : View.INVISIBLE);
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
        for (Institution institution : mInstitutionsList) {
            if (institution.getId().equals(marker.getSnippet())) {
                scrollToListItem(mFragmentView, institution);
                break;
            }
        }
        clearAutoCompleteTextViewInstitution(mFragmentView);
        return TextUtils.isEmpty(marker.getTitle());
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        String institutionId = marker.getSnippet();
        showInstitutionDetailsFragment(institutionId);
    }

    private void showInstitutionDetailsFragment(String institutionId) {
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
        mGoogleMap = googleMap;
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.setMyLocationEnabled(true);
        googleMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(getActivity()));
        googleMap.setOnMarkerClickListener(this);
        googleMap.setOnInfoWindowClickListener(this);
        googleMap.setOnCameraChangeListener(this);

        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
        try {
            MapsInitializer.initialize(this.getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Updates the location and zoom of the MapView
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(new Criteria(), true);
        Location currentLocation = locationManager.getLastKnownLocation(provider);

        if (!mInstitutionsList.isEmpty() || mCameraPosition != null) {
            if (mMapMode) updateInstitutionsOnMap(mFragmentView, mInstitutionsList);
        } else if (currentLocation != null) {
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), ONE_POINT_ZOOM);
            googleMap.animateCamera(cameraUpdate);
            getCurrentTown(currentLocation);
        }
    }

    private void getCurrentTown(/*final View fragmentView,*/ Location location) {
        TownDao.getByLocation(
                location,
                new CallbackOne<Town>() {
                    @Override
                    public void execute(Town town) {
                        AutoCompleteTextView autoCompleteTextViewTown = (AutoCompleteTextView) mFragmentView.findViewById(R.id.autoCompleteTextViewTown);
                        Spinner spinnerSpecialization = (Spinner) mFragmentView.findViewById(R.id.spinnerSpecialization);
                        LinearLayout linearLayoutSearch = (LinearLayout) mFragmentView.findViewById(R.id.linearLayoutSearch);

                        autoCompleteTextViewTown.setText(town.getName());
                        autoCompleteTextViewTown.setTag(town.getName());

                        CustomSpinnerAdapter customSpinnerAdapter = (CustomSpinnerAdapter) spinnerSpecialization.getAdapter();
                        if (customSpinnerAdapter != null) {
                            spinnerSpecialization.setSelection(0);
                            spinnerSpecialization.setTag(customSpinnerAdapter.getItem(0).getName());

                            // comment if need
                            if (linearLayoutSearch.getVisibility() != View.VISIBLE) {
                                tryToGetInstitutions(mFragmentView);
                            }

                            // uncomment if need
                            //    int plateMedium = (int) mFragmentView.getContext().getResources().getDimension(R.dimen.space_medium);
                            //    if (linearLayoutSearch.getVisibility() != View.VISIBLE) {
                            //        CustomExpandAnimation customExpandAnimation = new CustomExpandAnimation(linearLayoutSearch, 300, CustomExpandAnimation.EXPAND);
                            //        customExpandAnimation.setHeight(plateMedium);
                            //        linearLayoutSearch.startAnimation(customExpandAnimation);
                            //    }
                        }
                    }
                },
                new CallbackOne<Response>() {
                    @Override
                    public void execute(Response responseError) { /* does nothing*/ }
                }
        );
    }

    private void clearAutoCompleteTextViewInstitution(final View fragmentView) {
        CustomAutoCompleteTextView autoCompleteTextViewTown = (CustomAutoCompleteTextView) fragmentView.findViewById(R.id.autoCompleteTextViewInstitution);
        autoCompleteTextViewTown.clear();
    }

}