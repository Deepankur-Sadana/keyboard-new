package deepankur.com.keyboardapp.keyboardCustomViews;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import de.greenrobot.event.EventBus;
import deepankur.com.keyboardapp.MessageEvent;
import deepankur.com.keyboardapp.R;
import deepankur.com.keyboardapp.enums.KeyBoardOptions;
import deepankur.com.keyboardapp.interfaces.GreenBotMessageKeyIds;
import deepankur.com.keyboardapp.interfaces.Recyclable;
import deepankur.com.keyboardapp.interfaces.Refreshable;

/**
 * Created by deepankur on 2/7/17.
 */

public class ViewController implements GreenBotMessageKeyIds {
    private TabStripView tabStripView;
    private Context context;
    private View rootView;
    private String TAG = getClass().getSimpleName();

    public TabStripView getTabStripView() {
        return tabStripView;
    }

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
        EventBus.getDefault().register(this);
        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        tabStripView.setOnOptionClickedListener(new TabStripView.OnOptionClickedListener() {
            @Override
            public void onOptionClicked(KeyBoardOptions keyBoardOptions) {

                EventBus.getDefault().post(new MessageEvent(ON_IN_APP_EDITING_FINISHED, null));

                if (keyBoardOptions != KeyBoardOptions.QWERTY)
                    checkAndAddViewIfNotPresent(context, rootView, keyBoardOptions);

                adjustViews(keyBoardOptions);

                switch (keyBoardOptions) {
                    case QWERTY:
                        break;
                    case FAVORITE_APPS:
                        break;
                }
            }
        });
    }

    private void adjustViews(KeyBoardOptions keyBoardOptions) {
        RelativeLayout.LayoutParams keyBoardFrameParams = (RelativeLayout.LayoutParams) rootView.findViewById(R.id.keyboardContainer).getLayoutParams();
        int frameToShow;
        keyBoardFrameParams.addRule(RelativeLayout.BELOW, R.id.tabsStrip);
        if (keyBoardOptions == KeyBoardOptions.QWERTY) {
//            keyBoardFrameParams.addRule(RelativeLayout.BELOW, R.id.tabsStrip);
            frameToShow = R.id.keyboardContainer;
        } else {//non qwerty
//            if (keyBoardOptions == KeyBoardOptions.CLIP_BOARD)
//                keyBoardFrameParams.addRule(RelativeLayout.BELOW, R.id.fragmentContainerFrame);
//            else
//                keyBoardFrameParams.addRule(RelativeLayout.BELOW, R.id.tabsStrip);
            frameToShow = R.id.fragmentContainerFrame;
        }
        pauseAllViewsOtherThanQwerty();
        ((RelativeLayout) rootView).bringChildToFront(rootView.findViewById(frameToShow));
    }


    private void pauseAllViewsOtherThanQwerty(){
        FrameLayout frameLayout = (FrameLayout) rootView.findViewById(R.id.fragmentContainerFrame);
        for (int i = 0; i < frameLayout.getChildCount(); i++) {
            // pause everything else
                if (frameLayout.getChildAt(i) instanceof Recyclable)
                    ((Recyclable) frameLayout.getChildAt(i)).onRestInBackground();
            }
    }

    public void onEvent(MessageEvent messageEvent) {
        if (messageEvent.getMessageType() == POPUP_KEYBOARD_FOR_IN_APP_EDITING)
            bringKeyboardToFrameContainerBottom();
        else if (messageEvent.getMessageType() == ON_IN_APP_EDITING_FINISHED)
            enterNormalKeyboardMode();
    }

    private void bringKeyboardToFrameContainerBottom() {
        RelativeLayout.LayoutParams keyBoardFrameParams = (RelativeLayout.LayoutParams) rootView.findViewById(R.id.keyboardContainer).getLayoutParams();
        keyBoardFrameParams.addRule(RelativeLayout.BELOW, R.id.fragmentContainerFrame);
    }

    private void enterNormalKeyboardMode() {
        RelativeLayout.LayoutParams keyBoardFrameParams = (RelativeLayout.LayoutParams) rootView.findViewById(R.id.keyboardContainer).getLayoutParams();
        keyBoardFrameParams.addRule(RelativeLayout.BELOW, R.id.tabsStrip);
    }


    private void checkAndAddViewIfNotPresent(Context context, View rootView, KeyBoardOptions keyBoardOptions) {
        if (keyBoardOptions == KeyBoardOptions.QWERTY) {//if qwerty is selected set all the views to normal height
            setHeightToMatchKeyboard(rootView.findViewById(R.id.fragmentContainerFrame));
        }
        KeyBoardOptions[] values = KeyBoardOptions.values();
        for (KeyBoardOptions option : values) {
            if (option == keyBoardOptions) {
                boolean exists = false;
                FrameLayout frameLayout = (FrameLayout) rootView.findViewById(R.id.fragmentContainerFrame);
                for (int i = 0; i < frameLayout.getChildCount(); i++) {
                    if (keyBoardOptions == frameLayout.getChildAt(i).getTag()) {//the one we are looking for
                        exists = true;
                    } else {// pause everything else
                        if (frameLayout.getChildAt(i) instanceof Recyclable)
                            ((Recyclable) frameLayout.getChildAt(i)).onRestInBackground();
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
        } else if (keyBoardOptions == KeyBoardOptions.CONTACTS) {
            final SearchContactsView contactsView = new SearchContactsView(context);
            contactsView.setTag(keyBoardOptions);
            frameLayout.addView(contactsView);
            contactsView.getLayoutParams().height = getKeyboardHeight();
        }
    }


    private void setHeightToMatchKeyboard(View v) {
        v.getLayoutParams().height = getKeyboardHeight();
    }

    private int getKeyboardHeight() {
        KEYBOARD_HEIGHT = rootView.findViewById(R.id.keyboard).getHeight();
        return KEYBOARD_HEIGHT;
    }

    static int KEYBOARD_HEIGHT;

}
