package deepankur.com.keyboardapp.keyboardCustomViews;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import deepankur.com.keyboardapp.R;
import deepankur.com.keyboardapp.enums.KeyBoardOptions;

/**
 * Created by deepankur on 2/7/17.
 */

public class ViewController {
    private TabStripView tabStripView;
    private Context context;
    private View rootView;

    public ViewController(View rootView, TabStripView tabStripView, Context context) {
        this.rootView = rootView;
        this.tabStripView = tabStripView;
        this.context = context;
        init(context, rootView);
    }


    private void init(Context context, View rootView) {
        FrameLayout frameLayout = (FrameLayout) rootView.findViewById(R.id.containerFrame);
        FavouriteApplicationView favouriteApplicationView = new FavouriteApplicationView(context);
        frameLayout.addView(favouriteApplicationView);
        tabStripView.setOnOptionClickedListener(new TabStripView.OnOptionClickedListener() {
            @Override
            public void onOptionClicked(KeyBoardOptions keyBoardOptions) {
                switch (keyBoardOptions) {
                    case QWERTY:
                        break;
                    case FAVORITE_APPS:
                        break;
                }
            }
        });
    }

}
