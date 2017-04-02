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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.vingeapp.android.MessageEvent;
import com.vingeapp.android.R;
import com.vingeapp.android.apiHandling.RequestManager;
import com.vingeapp.android.apiHandling.ServerRequestType;
import com.vingeapp.android.interfaces.GreenBotMessageKeyIds;
import com.vingeapp.android.interfaces.Refreshable;
import com.vingeapp.android.models.LocationModel;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

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

    private void init(final Context context) {
        this.context = context;
        mapContainerView = inflate(context, R.layout.keyboard_view_maps, null);
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
//                        if (arg0.getTitle().equals("10009 - Bt Merah Ctrl"))
                        Log.d(TAG, "onMarkerClick: " + arg0);
                        return true;
                    }
                });

            }
        });
//
    }

    private void toggleViews(boolean hideSearchView) {
        if (hideSearchView) {
            mapContainerView.setVisibility(VISIBLE);
            searchView.setVisibility(GONE);
            EventBus.getDefault().post(new MessageEvent(ON_IN_APP_EDITING_FINISHED, null));

        } else {
            mapContainerView.setVisibility(GONE);
            searchView.setVisibility(VISIBLE);
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
            public void onItemClicked(LocationModel locationModel) {
                toggleViews(true);
                makeLocationRequest(locationModel.displayName);

            }
        });
        return searchMapsView;
    }

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

    private void updateMyLocationOnMap(Location location) {
        LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
        mGoogleMap.addMarker(new MarkerOptions().position(myLocation));

    }

    private Location mLastLocation;


    @SuppressWarnings("MissingPermission")
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        updateMyLocationOnMap(mLastLocation);
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
        updateMyLocationOnMap(location);
    }

    @SuppressWarnings("deprecation")
    private void makeLocationRequest(@NonNull String location) {
        List<NameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("address", location));
        RequestManager.makeGetRequest(getContext(), ServerRequestType.GOOGLE_MAPS_API, pairs, onRequestFinishCallback);
    }

    RequestManager.OnRequestFinishCallback onRequestFinishCallback = new RequestManager.OnRequestFinishCallback() {
        @Override
        public void onBindParams(boolean success, Object response) {
            Log.d(TAG, "onBindParams: success ? " + success + " " + response);

        }

        @Override
        public boolean isDestroyed() {
            return false;
        }
    };
}
