package deepankur.com.keyboardapp.keyboardCustomViews;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
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
    private String TAG = getClass().getSimpleName();

    public ViewController(final Context context, final View rootView) {
        this.rootView = rootView;
        this.tabStripView = (TabStripView) rootView.findViewById(R.id.tabsStrip);
        this.context = context;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
            }
        }, 3000);
        init(context, rootView);
    }


    private void init(final Context context, final View rootView) {
        tabStripView.setOnOptionClickedListener(new TabStripView.OnOptionClickedListener() {
            @Override
            public void onOptionClicked(KeyBoardOptions keyBoardOptions) {
                checkAndAddViewIfNotPresent(context, rootView, keyBoardOptions);
                switch (keyBoardOptions) {
                    case QWERTY:
                        break;
                    case FAVORITE_APPS:
                        break;
                }
            }
        });
    }


    private void checkAndAddViewIfNotPresent(Context context, View rootView, KeyBoardOptions keyBoardOptions) {
        KeyBoardOptions[] values = KeyBoardOptions.values();
        for (KeyBoardOptions option : values) {
            if (option == keyBoardOptions) {
                boolean exists = false;
                FrameLayout frameLayout = (FrameLayout) rootView.findViewById(R.id.containerFrame);
                for (int i = 0; i < frameLayout.getChildCount(); i++) {
                    if (keyBoardOptions == frameLayout.getChildAt(i).getTag()) {
                        exists = true;
                        break;
                    }

                }
                if (!exists) {
                    addView(frameLayout, keyBoardOptions);
                } else {
                    frameLayout.bringChildToFront(frameLayout.findViewWithTag(keyBoardOptions));
                }
                break;
            }
        }
    }

    private void addView(FrameLayout frameLayout, KeyBoardOptions keyBoardOptions) {
        if (keyBoardOptions == KeyBoardOptions.FAVORITE_APPS) {
            final FavouriteApplicationView favouriteApplicationView = new FavouriteApplicationView(context);
            frameLayout.addView(favouriteApplicationView);
            Log.d(TAG, "addView: " + rootView.getHeight());
//            favouriteApplicationView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, rootView.getHeight()));
            favouriteApplicationView.getLayoutParams().height = rootView.findViewById(R.id.keyboardView).getHeight();

        }
    }

}
