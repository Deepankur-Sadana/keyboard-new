package deepankur.com.keyboardapp.keyboardCustomViews;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import deepankur.com.keyboardapp.adapters.FavouriteApplicationsListAdapter;

/**
 * Created by deepankur on 2/7/17.
 */

public class FavouriteApplicationView extends RelativeLayout {
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

    private void init(Context context) {
        this.setBackgroundColor(Color.BLUE);
        this.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        RecyclerView recyclerView = new RecyclerView(context);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
        recyclerView.setAdapter(new FavouriteApplicationsListAdapter(context, null));
        recyclerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        this.addView(recyclerView);
    }

}
