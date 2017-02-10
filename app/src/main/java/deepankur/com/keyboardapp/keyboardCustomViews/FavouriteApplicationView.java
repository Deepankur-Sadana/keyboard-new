package deepankur.com.keyboardapp.keyboardCustomViews;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import deepankur.com.keyboardapp.adapters.FavouriteApplicationsListAdapter;
import deepankur.com.keyboardapp.interfaces.RecyclerViewClickInterface;
import deepankur.com.keyboardapp.interfaces.Refreshable;

/**
 * Created by deepankur on 2/7/17.
 */

public class FavouriteApplicationView extends RelativeLayout implements Refreshable {
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
        RecyclerView recyclerView = new RecyclerView(context);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
        favouriteApplicationsListAdapter = new FavouriteApplicationsListAdapter(context, new RecyclerViewClickInterface() {
            @Override
            public void onItemClick(int extras, Object data) {
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
            favouriteApplicationsListAdapter.refresh();
            favouriteApplicationsListAdapter.notifyDataSetChanged();
            return true;
        }
        return false;
    }
}
