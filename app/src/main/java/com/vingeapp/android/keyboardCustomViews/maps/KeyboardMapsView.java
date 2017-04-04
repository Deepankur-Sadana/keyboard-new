package com.vingeapp.android.keyboardCustomViews.maps;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.IndoorBuilding;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.vingeapp.android.MessageEvent;
import com.vingeapp.android.R;
import com.vingeapp.android.googleLocationApiResponse.Result;
import com.vingeapp.android.interfaces.GreenBotMessageKeyIds;
import com.vingeapp.android.interfaces.Refreshable;

import java.util.ArrayList;
import java.util.HashMap;

import de.greenrobot.event.EventBus;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by deepankursadana on 18/03/17.
 */

@SuppressWarnings({"FieldCanBeLocal", "SimplifiableIfStatement"})
public class KeyboardMapsView extends FrameLayout implements GreenBotMessageKeyIds, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, Refreshable {
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters
    private final String TAG = getClass().getSimpleName();
    private GoogleMap mGoogleMap;
    private View mapContainerView;
    private MapView map;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Context context;

    @Override
    public boolean doRefresh() {
        if (searchView != null) {
            return searchView.doRefresh();
        }
        return false;
    }

    public enum View_State {MAPS, SEARCH}

    private View_State mCurrentViewState = View_State.SEARCH;


    public KeyboardMapsView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public KeyboardMapsView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    public KeyboardMapsView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private SearchMapsView searchView;
    private RelativeLayout locationRl1, locationRl2;
    String lat = "", lng = "";

    void updateLocationInTheView(Result result) {
        if (result != null) {
            Log.e(TAG, "updateLocationInTheView: result is null returning");

            if (result.getAddress_components().size() >= 0)
                ((TextView) locationRl1.findViewById(R.id.TextView1)).setText(result.getAddress_components().get(0).getLong_name());
            ((TextView) locationRl2.findViewById(R.id.TextView2)).setText(result.getFormatted_address());
            lat = String.valueOf(result.getGeometry().getLocation().getLat());
            lng = String.valueOf(result.getGeometry().getLocation().getLng());
        } else {
            ((TextView) locationRl1.findViewById(R.id.TextView1)).setText("My location");
            ((TextView) locationRl2.findViewById(R.id.TextView2)).setText("My location");

            if (mLastLocation != null) {
                lat = String.valueOf(mLastLocation.getLatitude());
                lng = String.valueOf(mLastLocation.getLongitude());
            }
        }
    }

    void initializeLocationSpecificViews(View mapContainerView) {
        locationRl1 = (RelativeLayout) mapContainerView.findViewById(R.id.locationRL1);
        locationRl2 = (RelativeLayout) mapContainerView.findViewById(R.id.locationRL2);
        locationRl1.findViewById(R.id.backIV).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleViews(false);

            }
        });

        locationRl2.findViewById(R.id.shareTV).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = "http://maps.google.com/?q=" + lat + "," + lng + "&hl=en&gl=us";
                EventBus.getDefault().post(new MessageEvent(BROADCAST_STRING_TO_CONNECTED_APPLICATION, s));
                EventBus.getDefault().post(new MessageEvent(SWITCH_TO_QWERTY, null));

            }
        });
    }

    private void init(final Context context) {
        this.context = context;
        mapContainerView = inflate(context, R.layout.keyboard_view_maps, null);
        initializeLocationSpecificViews(mapContainerView);
        map = (MapView) mapContainerView.findViewById(R.id.mapView);

        map.onCreate(null);
        this.addView(mapContainerView);
        searchView = getSearchView(context);
        this.addView(searchView);
        map.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                Log.d(TAG, "onMapReady: " + googleMap);
                map.onResume();
                try {
                    MapsInitializer.initialize(context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mGoogleMap = googleMap;
                mGoogleMap.setOnCameraIdleListener(onCameraIdleListener);
                mGoogleMap.setOnCameraMoveStartedListener(onCameraMoveStartedListener);
                mGoogleMap.setOnCameraMoveListener(onCameraMoveListener);
                mGoogleMap.setOnCameraMoveCanceledListener(onCameraMoveCanceledListener);

                if (checkPlayServices()) {
                    buildGoogleApiClient();
                    createLocationRequest();
                }
                if (mGoogleApiClient != null) {
                    mGoogleApiClient.connect();
                }

                if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                    startLocationUpdates();
                }
                mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker arg0) {
                        Log.d(TAG, "onMarkerClick: " + arg0.getTitle());
                        updateLocationInTheView(markerResultHashMap.get(arg0));
                        return true;
                    }
                });

            }
        });
//
    }

    private GoogleMap.OnCameraMoveListener onCameraMoveListener = new GoogleMap.OnCameraMoveListener() {
        @Override
        public void onCameraMove() {
//            Log.d(TAG, "onCameraMove: ");
        }
    };

    private GoogleMap.OnCameraIdleListener onCameraIdleListener = new GoogleMap.OnCameraIdleListener() {
        @Override
        public void onCameraIdle() {
            Log.d(TAG, "onCameraIdle: ");
            toggleMapView(false);
        }
    };

    private GoogleMap.OnCameraMoveCanceledListener onCameraMoveCanceledListener = new GoogleMap.OnCameraMoveCanceledListener() {
        @Override
        public void onCameraMoveCanceled() {
            Log.d(TAG, "onCameraMoveCanceled: ");
            toggleMapView(false);
        }
    };

    private GoogleMap.OnCameraMoveStartedListener onCameraMoveStartedListener = new GoogleMap.OnCameraMoveStartedListener() {
        @Override
        public void onCameraMoveStarted(int i) {
            Log.d(TAG, "onCameraMoveStarted: ");
            toggleMapView(true);
        }
    };

    private GoogleMap.OnIndoorStateChangeListener onIndoorStateChangeListener = new GoogleMap.OnIndoorStateChangeListener() {
        @Override
        public void onIndoorBuildingFocused() {

        }

        @Override
        public void onIndoorLevelActivated(IndoorBuilding indoorBuilding) {

        }
    };


    private void toggleMapView(boolean hideTheViews) {
        if (hideTheViews) {
            locationRl1.animate().alpha(0f).setDuration(300).start();
            locationRl2.animate().alpha(0f).setDuration(300).start();
        } else {
            locationRl1.animate().alpha(1f).setDuration(300).start();
            locationRl2.animate().alpha(1f).setDuration(300).start();

        }
    }

    private HashMap<Marker, Result> markerResultHashMap = new HashMap<>();

    private void makeEntry(Marker marker, Result result) {
        markerResultHashMap.put(marker, result);
    }

    private void toggleViews(boolean hideSearchView) {
        if (hideSearchView) {
            mapContainerView.setVisibility(VISIBLE);
            searchView.setVisibility(GONE);
            EventBus.getDefault().post(new MessageEvent(ON_IN_APP_EDITING_FINISHED, null));

        } else {
            mapContainerView.setVisibility(GONE);
            searchView.setVisibility(VISIBLE);
            searchView.doRefresh();
            EventBus.getDefault().post(new MessageEvent(POPUP_KEYBOARD_FOR_IN_APP_EDITING, null));

        }
        mCurrentViewState = hideSearchView ? View_State.MAPS : View_State.SEARCH;
    }


    public View_State getCurrentViewState() {
        return mCurrentViewState;
    }

    private SearchMapsView getSearchView(Context context) {
        SearchMapsView searchMapsView = new SearchMapsView(context);

        searchMapsView.setLocationItemClickedListener(new SearchMapsView.LocationItemClickedListener() {
            @Override
            public void onItemClicked(Result locationModel) {
                clickedResults.add(locationModel);
                toggleViews(true);
                updateLocationInTheView(locationModel);
                addLocationOnMap(locationModel.getGeometry().getLocation().getLat(), locationModel.getGeometry().getLocation().getLng(), locationModel);
            }
        });
        return searchMapsView;
    }

    private ArrayList<Result> clickedResults = new ArrayList<>();

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getContext());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
//                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(), "This device is not supported.", Toast.LENGTH_LONG).show();
            }
            Log.d(TAG, "checkPlayServices: returning false");
            return false;
        }
        Log.d(TAG, "checkPlayServices: returning true");
        return true;
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }


    private void addLocationOnMap(double lat, double lng, Result result) {

        LatLng latLng = new LatLng(lat, lng);
        MarkerOptions position = new MarkerOptions().position(latLng);

        if (result != null)
            position.title(result.getFormatted_address());
        else
            position.title("!!!!!!!!!");

        Marker marker = mGoogleMap.addMarker(position);

        makeEntry(marker, result);
    }

    private Location mLastLocation;


    @SuppressWarnings("MissingPermission")
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null)
            addLocationOnMap(mLastLocation.getLatitude(), mLastLocation.getLongitude(), null);
    }

    @SuppressWarnings("MissingPermission")
    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (LocationListener) this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mLastLocation != null)
            addLocationOnMap(mLastLocation.getLatitude(), mLastLocation.getLongitude(), null);
    }


}
