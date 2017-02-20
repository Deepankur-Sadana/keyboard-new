package deepankur.com.keyboardapp.keyboardCustomViews;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import deepankur.com.keyboardapp.R;
import deepankur.com.keyboardapp.adapters.ClipboardAdapter;
import deepankur.com.keyboardapp.interfaces.RecyclerViewClickInterface;
import deepankur.com.keyboardapp.interfaces.Refreshable;

/**
 * Created by deepankur on 2/19/17.
 */

public class ClipBoardView extends FrameLayout implements Refreshable {
    private RecyclerView mRecycler;
    private ClipboardAdapter clipboardAdapter;

    public ClipBoardView(Context context) {
        super(context);
        init(context);
    }

    public ClipBoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ClipBoardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View rootView = inflate(context, R.layout.keyboard_view_clipboard, null);
        this.addView(rootView);
        mRecycler = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        mRecycler.setLayoutManager(new GridLayoutManager(context, 3));
        clipboardAdapter = new ClipboardAdapter(context, null, clickInterface);
        mRecycler.setAdapter(clipboardAdapter);
    }

    private RecyclerViewClickInterface clickInterface = new RecyclerViewClickInterface() {
        @Override
        public void onItemClick(int extras, Object data) {

        }
    };

    @Override
    public boolean doRefresh() {
        if (clipboardAdapter!=null){
            clipboardAdapter.notifyDataSetChanged();
            return true;
        }
        return false;
    }
}
