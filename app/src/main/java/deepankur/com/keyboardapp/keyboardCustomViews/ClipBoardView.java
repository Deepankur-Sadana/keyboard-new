package deepankur.com.keyboardapp.keyboardCustomViews;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import deepankur.com.keyboardapp.MessageEvent;
import deepankur.com.keyboardapp.R;
import deepankur.com.keyboardapp.adapters.BaseRecylerAdapter;
import deepankur.com.keyboardapp.adapters.ClipboardAdapter;
import deepankur.com.keyboardapp.cache.ClipBoardCache;
import deepankur.com.keyboardapp.databases.DatabaseHelper;
import deepankur.com.keyboardapp.interfaces.GreenBotMessageKeyIds;
import deepankur.com.keyboardapp.interfaces.Recyclable;
import deepankur.com.keyboardapp.interfaces.RecyclerViewClickInterface;
import deepankur.com.keyboardapp.interfaces.Refreshable;
import deepankur.com.keyboardapp.models.ClipBoardItemModel;

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
        ClipBoardCache.getInstance().addListener(clipBoardDataListener);
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
            if (holderType == BaseRecylerAdapter.HEADER_HOLDER) {
                loadClipboardItemEditorView(AddEditClipboardItemView.ADD, null);

            } else if (holderType == BaseRecylerAdapter.ITEM_HOLDER) {
                if (clickType == CLICK_TYPE_NORMAL) {

                } else if (clickType == CLICK_TYPE_LONG_PRESS) {
                    loadClipboardItemEditorView(AddEditClipboardItemView.EDIT, (ClipBoardItemModel) data);

                }
            }
        }
    };


    private void loadClipboardItemEditorView(int actionType, @Nullable ClipBoardItemModel clipBoardItemModel) {
        addItemFrame.removeAllViews();
        AddEditClipboardItemView addClipboardItemView = new AddEditClipboardItemView(context);
        addClipboardItemView.setACTION_TYPE(actionType);
        if (clipBoardItemModel != null)
            addClipboardItemView.setClipBoardItemModel(clipBoardItemModel);
        addItemFrame.addView(addClipboardItemView);
        EventBus.getDefault().post(new MessageEvent(POPUP_KEYBOARD_FOR_IN_APP_EDITING, null));
    }


    private ArrayList<ClipBoardItemModel> clipboardItemsList;

    private ArrayList<ClipBoardItemModel> getAllClipboardItems(Context context) {
        if (clipboardItemsList != null)
            return clipboardItemsList;
        List<ClipBoardItemModel> allToDos = DatabaseHelper.getDataBaseInstance(context).getAllToDos();
        clipboardItemsList = new ArrayList<>(allToDos);
        return clipboardItemsList;
    }

    private DatabaseHelper databaseHelper;

    private DatabaseHelper getDatabaseHelper() {
        if (databaseHelper == null)
            databaseHelper = DatabaseHelper.getDataBaseInstance(context);
        return databaseHelper;
    }

    private Context context;
    private final String TAG = getClass().getSimpleName();


    private void requestAddItem(ClipBoardItemModel keyValueShortcutModel) {
        long toDo = getDatabaseHelper().createToDo(keyValueShortcutModel, new long[0]);
        Log.d(TAG, "requestAddItem: " + toDo);
        clipboardItemsList.add(keyValueShortcutModel);
        clipboardAdapter.notifyItemInserted(clipboardItemsList.size() - 1);
    }

    private void updateItem(int index, ClipBoardItemModel keyValueShortcutModel) {
        int i = getDatabaseHelper().updateToDo(keyValueShortcutModel);
        Log.d(TAG, "updateItem: " + i);
        clipboardAdapter.notifyItemChanged(index);
    }

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

    private ClipBoardCache.ClipBoardDataListener clipBoardDataListener = new ClipBoardCache.ClipBoardDataListener() {
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
    };
}
