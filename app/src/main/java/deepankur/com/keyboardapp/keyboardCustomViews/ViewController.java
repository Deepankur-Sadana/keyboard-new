package deepankur.com.keyboardapp.keyboardCustomViews;

import android.content.Context;
import android.view.View;

import deepankur.com.keyboardapp.keyboardCustomViews.TabStripView;

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
    }

}
