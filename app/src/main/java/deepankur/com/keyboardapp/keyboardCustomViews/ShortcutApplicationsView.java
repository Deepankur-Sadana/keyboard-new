package deepankur.com.keyboardapp.keyboardCustomViews;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import deepankur.com.keyboardapp.R;
import deepankur.com.keyboardapp.adapters.ClipboardAdapter;

/**
 * Created by deepankursadana on 17/02/17.
 */

public class ShortcutApplicationsView extends FrameLayout {

    private RecyclerView mRecyclerView;
    private ClipboardAdapter mAdapter;

    public ShortcutApplicationsView(Context context) {
        super(context);
        init(context);
    }

    public ShortcutApplicationsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ShortcutApplicationsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View v = inflate(context, R.layout.keyboard_view_clipboard, null);
        this.mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        if (this.mAdapter==null){

        }
        this.removeAllViews();
        this.addView(v);
    }
}
