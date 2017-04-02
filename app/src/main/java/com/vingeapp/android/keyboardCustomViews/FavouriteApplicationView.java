package com.vingeapp.android.keyboardCustomViews;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.vingeapp.android.MessageEvent;
import com.vingeapp.android.activities.MyActivity;
import com.vingeapp.android.adapters.FavouriteApplicationsListAdapter;
import com.vingeapp.android.interfaces.GreenBotMessageKeyIds;
import com.vingeapp.android.interfaces.RecyclerViewClickInterface;
import com.vingeapp.android.interfaces.Refreshable;

import de.greenrobot.event.EventBus;


/**
 * Created by deepankur on 2/7/17.
 */

public class FavouriteApplicationView extends RelativeLayout implements Refreshable, GreenBotMessageKeyIds {
    public FavouriteApplicationView(Context context) {
        super(context);
        init(context);
    }

    public FavouriteApplicationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FavouriteApplicationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private FavouriteApplicationsListAdapter favouriteApplicationsListAdapter;

    private void init(final Context context) {
        this.setBackgroundColor(Color.WHITE);
        EventBus.getDefault().register(this);
        RecyclerView recyclerView = new RecyclerView(context);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
        favouriteApplicationsListAdapter = new FavouriteApplicationsListAdapter(context, new RecyclerViewClickInterface() {
            @Override
            public void onItemClick(int clickType, int extras, Object data) {
                String packageName = (String) data;
                Intent LaunchIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
                context.startActivity(LaunchIntent);
            }
        });
        recyclerView.setAdapter(favouriteApplicationsListAdapter);
        recyclerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        this.addView(recyclerView);
    }


    @Override
    public boolean doRefresh() {
        if (favouriteApplicationsListAdapter != null) {
            favouriteApplicationsListAdapter.setPreferredApps();
            favouriteApplicationsListAdapter.notifyDataSetChanged();
            return true;
        }
        return false;
    }

    private final String TAG = getClass().getSimpleName();

    /**
     * Called by {@link MyActivity#onPause()}
     *
     * @param messageEvent
     */
    @SuppressWarnings("unused")
    public void onEvent(MessageEvent messageEvent) {
        Log.d(TAG, "onEvent: " + messageEvent.getMessageType());
        if (messageEvent.getMessageType() == FAVOURITE_APP_PREFERRED_LIST_CHANGED) {
            if (favouriteApplicationsListAdapter != null) {
                favouriteApplicationsListAdapter.setPreferredApps();
                favouriteApplicationsListAdapter.notifyDataSetChanged();
            }
        }
    }
}
