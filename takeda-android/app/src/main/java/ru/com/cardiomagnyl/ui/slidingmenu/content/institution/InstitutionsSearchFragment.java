package ru.com.cardiomagnyl.ui.slidingmenu.content.institution;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
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
import ru.com.cardiomagnyl.util.Utils;
import ru.com.cardiomagnyl.widget.CustomSpinnerAdapter;

public class InstitutionsSearchFragment extends BaseItemFragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener, SwipeRefreshLayout.OnRefreshListener {
    private GoogleMap mGoogleMap;
    private List<Institution> mInstitutionsList = new ArrayList<>();
    private ClusterManager<Institution> mClusterManager;
    private View mFragmentView;

    private static int ONE_POINT_ZOOM = 14;

    @Override
    public void initTopBar(ViewGroup viewGroupTopBar) {
        initTopBarBellCabinet(viewGroupTopBar, true, true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mFragmentView != null) {
            ViewGroup parent = (ViewGroup) mFragmentView.getParent();
            parent.removeView(mFragmentView);
            return mFragmentView;
        }

        mFragmentView = inflater.inflate(R.layout.fragment_institutions_search, null);
        preInitFragment(mFragmentView);
        return mFragmentView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        setContentVisiblity(getView(), true, false);
        initMap(googleMap);
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

    private void preInitFragment(final View fragmentView) {
        setContentVisiblity(fragmentView, false, false);

        // attempt to fix slow initialization of SupportMapFragment ("dirty hack")
        final SlidingMenuActivity slidingMenuActivity = (SlidingMenuActivity) getActivity();
        if (slidingMenuActivity.getSlidingMenu().isMenuShowing()) {
            slidingMenuActivity.getSlidingMenu().setOnClosedListener(new SlidingMenu.OnClosedListener() {
                @Override
                public void onClosed() {
                    slidingMenuActivity.getSlidingMenu().setOnClosedListener(null);
                    initInstitutionsList(fragmentView);
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
        AutoCompleteTextView autoCompleteTextViewTown = (AutoCompleteTextView) fragmentView.findViewById(R.id.autoCompleteTextViewTown);
        Spinner spinnerSpecialization = (Spinner) fragmentView.findViewById(R.id.spinnerSpecialization);
        RadioGroup radioGroupCondition = (RadioGroup) fragmentView.findViewById(R.id.radioGroupCondition);

        townsList.add(Town.createNoTown(fragmentView.getContext()));
        specializationsList.add(Specialization.createNoSpecialization(fragmentView.getContext()));

        initAutoCompleteTextView(fragmentView, autoCompleteTextViewTown, new ArrayList<BaseModelHelper>(townsList));
        initSpinner(fragmentView, spinnerSpecialization, new ArrayList<BaseModelHelper>(specializationsList), specializationsList.size() - 1);

        radioGroupCondition.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButtonMap:
                        setContentVisiblity(fragmentView, true, false);
                        break;
                    case R.id.radioButtonList:
                        setContentVisiblity(fragmentView, false, true);
                        break;
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
                String institutionId = ((Institution) institutionsAdapter.getItem(position)).getId();
                showInstitutionDetailsFragment(institutionId);
            }
        });
    }

    private void initAutoCompleteTextView(final View fragmentView, final AutoCompleteTextView autoCompleteTextView, final List<BaseModelHelper> itemsList) {
        final CustomArrayAdapter customArrayAdapter = new CustomArrayAdapter(fragmentView.getContext(), R.layout.spinner_item_dropdown, itemsList);
        autoCompleteTextView.setAdapter(customArrayAdapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Utils.hideKeyboard(autoCompleteTextView);
                autoCompleteTextView.setTag(customArrayAdapter.getItem(position).getName());
                tryToGetInstitutions(fragmentView);
            }
        });
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

    private void updateInstitutions(final View fragmentView, final List<Institution> institutionsList) {
        final ListView listViewInstitutions = (ListView) fragmentView.findViewById(R.id.listViewInstitutions);
        InstitutionsAdapter institutionsAdapter = (InstitutionsAdapter) listViewInstitutions.getAdapter();
        institutionsAdapter.notifyDataSetInvalidated();
        mInstitutionsList.clear();
        mInstitutionsList.addAll(institutionsList);
        institutionsAdapter.notifyDataSetChanged();

        mClusterManager.clearItems();
        mClusterManager.addItems(institutionsList);
        mClusterManager.cluster();

        if (institutionsList.isEmpty()) {
            Tools.showToast(fragmentView.getContext(), R.string.data_not_found, Toast.LENGTH_LONG);
        } else if (institutionsList.size() == 1) {
            LatLng latLng = new LatLng(institutionsList.get(0).getLat(), institutionsList.get(0).getLng());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, ONE_POINT_ZOOM);
            mGoogleMap.animateCamera(cameraUpdate);
        } else {
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

    private void setContentVisiblity(View parentView, boolean isMapVisible, boolean isListVisible) {
        View frameLayoutMapHolder = parentView.findViewById(R.id.frameLayoutMapHolder);
        View viewLoadingMap = parentView.findViewById(R.id.viewLoadingMap);
        View customSwipeRefreshLayout = parentView.findViewById(R.id.customSwipeRefreshLayout);

        frameLayoutMapHolder.setVisibility(isMapVisible ? View.VISIBLE : View.GONE);
        viewLoadingMap.setVisibility(isMapVisible || isListVisible ? View.GONE : View.VISIBLE);
        customSwipeRefreshLayout.setVisibility(isListVisible ? View.VISIBLE : View.GONE);
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
        googleMap.setOnCameraChangeListener(mClusterManager);

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

        if (currentLocation != null) {
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), ONE_POINT_ZOOM);
            googleMap.animateCamera(cameraUpdate);
        }
    }

}