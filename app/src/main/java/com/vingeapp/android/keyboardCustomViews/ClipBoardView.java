package com.vingeapp.android.keyboardCustomViews;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.vingeapp.android.MessageEvent;
import com.vingeapp.android.R;
import com.vingeapp.android.adapters.BaseRecylerAdapter;
import com.vingeapp.android.adapters.ClipboardAdapter;
import com.vingeapp.android.firebase.FireBaseHelper;
import com.vingeapp.android.interfaces.GreenBotMessageKeyIds;
import com.vingeapp.android.interfaces.Recyclable;
import com.vingeapp.android.interfaces.RecyclerViewClickInterface;
import com.vingeapp.android.interfaces.Refreshable;
import com.vingeapp.android.models.ClipBoardItemModel;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * Created by deepankur on 2/19/17.
 */

public class ClipBoardView extends FrameLayout implements Refreshable, Recyclable, GreenBotMessageKeyIds {
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

    View rootView;
    private FrameLayout addItemFrame;

    private void init(Context context) {
        rootView = inflate(context, R.layout.keyboard_view_clipboard, null);
        FireBaseHelper.getInstance(context).addClipBoardDataListener(clipBoardDataListener);
        this.addView(rootView);
        this.context = context;
        this.mRecycler = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        this.mRecycler.setLayoutManager(new GridLayoutManager(context, 3));
        this.addItemFrame = (FrameLayout) rootView.findViewById(R.id.add_clipboard_item_dialog);
        clipboardAdapter = new ClipboardAdapter(context, getAllClipboardItems(context), clickInterface);
        mRecycler.setAdapter(clipboardAdapter);
    }

    /**
     * extras will denote the type of viewHolder clicked
     */
    private RecyclerViewClickInterface clickInterface = new RecyclerViewClickInterface() {
        @Override
        public void onItemClick(int clickType, int holderType, Object data) {
            Log.d(TAG, "onItemClick: clickType " + clickType + " holderType " + holderType + " data " + data);
            if (holderType == BaseRecylerAdapter.HEADER_HOLDER) {
                loadClipboardItemEditorView(AddEditClipboardItemView.ActionType.ADD, null);

            } else if (holderType == BaseRecylerAdapter.ITEM_HOLDER) {
                if (clickType == CLICK_TYPE_NORMAL) {
                    EventBus.getDefault().post(new MessageEvent(BROADCAST_STRING_TO_CONNECTED_APPLICATION, ((ClipBoardItemModel) data).getDescription()));
                    EventBus.getDefault().post(new MessageEvent(SWITCH_TO_QWERTY, null));

                } else if (clickType == CLICK_TYPE_LONG_PRESS) {
                    loadClipboardItemEditorView(AddEditClipboardItemView.ActionType.EDIT, (ClipBoardItemModel) data);

                }
            }
        }
    };


    private void loadClipboardItemEditorView(AddEditClipboardItemView.ActionType actionType, @Nullable ClipBoardItemModel clipBoardItemModel) {
        EventBus.getDefault().post(new MessageEvent(POPUP_KEYBOARD_FOR_IN_APP_EDITING, null));
        addItemFrame.removeAllViews();
        AddEditClipboardItemView addClipboardItemView = new AddEditClipboardItemView(context, actionType, clipBoardItemModel);
        addItemFrame.addView(addClipboardItemView);
    }



    private ArrayList<ClipBoardItemModel> getAllClipboardItems(Context context) {

        FireBaseHelper.getInstance(context).loadClipboardItems();
        return FireBaseHelper.getInstance(context).getAllClipboardModels();

    }


    private Context context;
    private final String TAG = getClass().getSimpleName();


    @Override
    public boolean doRefresh() {
        if (clipboardAdapter != null) {
            clipboardAdapter.notifyDataSetChanged();
            return true;
        }
        return false;
    }

    /**
     * we remove add/edit clipboard item when user switches to any  other option
     */
    @Override
    public void onRestInBackground() {
        if (rootView != null)
            ((FrameLayout) rootView.findViewById(R.id.add_clipboard_item_dialog)).removeAllViews();
    }


    private FireBaseHelper.ClipBoardDataListener clipBoardDataListener = new FireBaseHelper.ClipBoardDataListener() {
        @Override
        public void onItemAdded(ClipBoardItemModel clipBoardItemModel) {
            if (clipboardAdapter != null)
                clipboardAdapter.notifyDataSetChanged();
        }

        @Override
        public void onItemEdited(ClipBoardItemModel clipBoardItemModel) {
            if (clipboardAdapter != null)
                clipboardAdapter.notifyDataSetChanged();
        }

        @Override
        public void onListChanged() {
            if (clipboardAdapter != null)
                clipboardAdapter.notifyDataSetChanged();
        }
    };
}
