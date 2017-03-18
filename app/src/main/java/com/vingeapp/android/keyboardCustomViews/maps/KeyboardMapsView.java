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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.vingeapp.android.R;

/**
 * Created by deepankursadana on 18/03/17.
 */

public class KeyboardMapsView extends FrameLayout {
    private View rootView;
    private MapView map;
    private final String TAG = getClass().getSimpleName();

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
                map.onResume();
                try {
                    MapsInitializer.initialize(context);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                final LatLng MELBOURNE = new LatLng(-37.813, 144.962);
                Marker melbourne = googleMap.addMarker(new MarkerOptions()
                        .position(MELBOURNE));
            }
        });
//
    }
}
