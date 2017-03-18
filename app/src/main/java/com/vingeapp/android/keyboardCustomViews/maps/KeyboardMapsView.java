package com.vingeapp.android.keyboardCustomViews.maps;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.vingeapp.android.R;

/**
 * Created by deepankursadana on 18/03/17.
 */

public class KeyboardMapsView extends FrameLayout {
    View rootView;
    MapView map;
    GoogleMap googleMap;
    final String TAG = getClass().getSimpleName();

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

    private void init(final Context context) {
        rootView = inflate(context, R.layout.keyboard_view_maps, null);
        map = (MapView) rootView.findViewById(R.id.mapView);

        map.onCreate(null);
        this.addView(rootView);
        map.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                Log.d(TAG, "onMapReady: " + googleMap);

//                LatLng coordinates = new LatLng(match.match.LocationLatitude, match.match.LocationLongitude);
//                googleMap.addMarker(new MarkerOptions().position(coordinates).title(match.match.LocationAddress));
//                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 15));
                map.onResume();


//                map = googleMap.getMap();
//                map.getUiSettings().setMyLocationButtonEnabled(false);
//                map.setMyLocationEnabled(true);

                // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
                try {
                    MapsInitializer.initialize(context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
//
//        // Gets to GoogleMap from the MapView and does initialization stuff
//        map = mapView.getMap();
//        map.getUiSettings().setMyLocationButtonEnabled(false);
//        map.setMyLocationEnabled(true);
//
//        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
//        try {
//            MapsInitializer.initialize(this.getActivity());
//        } catch (GooglePlayServicesNotAvailableException e) {
//            e.printStackTrace();
//        }
//
//        // Updates the location and zoom of the MapView
//        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(43.1, -87.9), 10);
//        map.animateCamera(cameraUpdate);

    }
}
