package deepankur.com.keyboardapp.keyboardCustomViews;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import deepankur.com.keyboardapp.R;
import deepankur.com.keyboardapp.enums.KeyBoardOptions;
import deepankur.com.keyboardapp.interfaces.Refreshable;

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
        init(context, rootView);
    }

    public ViewController(final Context context, final View rootView, TabStripView tabStripView) {
        this.rootView = rootView;
        this.tabStripView = tabStripView;
        this.context = context;
        init(context, rootView);
    }


    private void init(final Context context, final View rootView) {
        tabStripView.setOnOptionClickedListener(new TabStripView.OnOptionClickedListener() {
            @Override
            public void onOptionClicked(KeyBoardOptions keyBoardOptions) {

                toggleKeyboardViews(keyBoardOptions == KeyBoardOptions.QWERTY);
                if (keyBoardOptions != KeyBoardOptions.QWERTY)
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

    /**
     * As we have keyboard and fragments in a different prent groups we can control their
     * visibility from here
     */
    private void toggleKeyboardViews(boolean showKeyboard) {
        rootView.findViewById(R.id.keyboardContainer).setVisibility(showKeyboard ? View.VISIBLE : View.GONE);
        rootView.findViewById(R.id.fragmentContainerFrame).setVisibility(!showKeyboard ? View.VISIBLE : View.GONE);

    }

    private void checkAndAddViewIfNotPresent(Context context, View rootView, KeyBoardOptions keyBoardOptions) {
        KeyBoardOptions[] values = KeyBoardOptions.values();
        for (KeyBoardOptions option : values) {
            if (option == keyBoardOptions) {
                boolean exists = false;
                FrameLayout frameLayout = (FrameLayout) rootView.findViewById(R.id.fragmentContainerFrame);
                for (int i = 0; i < frameLayout.getChildCount(); i++) {

                    if (keyBoardOptions == frameLayout.getChildAt(i).getTag()) {
                        exists = true;
                        break;
                    }

                }
                if (!exists) {
                    addView(frameLayout, keyBoardOptions);
                } else {
                    View viewWithTag = frameLayout.findViewWithTag(keyBoardOptions);
                    frameLayout.bringChildToFront(viewWithTag);
                    if (viewWithTag instanceof Refreshable)
                        ((Refreshable) viewWithTag).doRefresh();
                }
                break;
            }
        }
    }

    private void addView(FrameLayout frameLayout, KeyBoardOptions keyBoardOptions) {
        if (keyBoardOptions == KeyBoardOptions.FAVORITE_APPS) {
            final FavouriteApplicationView favouriteApplicationView = new FavouriteApplicationView(context);
            favouriteApplicationView.setTag(keyBoardOptions);
            frameLayout.addView(favouriteApplicationView);
            Log.d(TAG, "addView: " + rootView.getHeight());
            favouriteApplicationView.getLayoutParams().height = getKeyboardHeight();
        } else if (keyBoardOptions == KeyBoardOptions.CLIP_BOARD) {
            final ClipBoardView clipBoardView = new ClipBoardView(context);
            clipBoardView.setTag(keyBoardOptions);
            frameLayout.addView(clipBoardView);
            clipBoardView.getLayoutParams().height = getKeyboardHeight();
        }
    }

    private int getKeyboardHeight() {
        KEYBOARD_HEIGHT = rootView.findViewById(R.id.keyboard).getHeight();
        return KEYBOARD_HEIGHT;
    }

    static int KEYBOARD_HEIGHT;

}
