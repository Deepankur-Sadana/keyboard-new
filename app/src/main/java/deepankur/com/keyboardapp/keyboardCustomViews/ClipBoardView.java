package deepankur.com.keyboardapp.keyboardCustomViews;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import deepankur.com.keyboardapp.R;
import deepankur.com.keyboardapp.adapters.BaseRecylerAdapter;
import deepankur.com.keyboardapp.adapters.ClipboardAdapter;
import deepankur.com.keyboardapp.databases.DatabaseHelper;
import deepankur.com.keyboardapp.interfaces.RecyclerViewClickInterface;
import deepankur.com.keyboardapp.interfaces.Refreshable;
import deepankur.com.keyboardapp.models.ClipBoardItemModel;

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

    View rootView;
    private FrameLayout addItemFrame;

    private void init(Context context) {
        rootView = inflate(context, R.layout.keyboard_view_clipboard, null);
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
                onAddNewItemClicked();

            } else if (holderType == BaseRecylerAdapter.ITEM_HOLDER) {
                if (clickType == CLICK_TYPE_NORMAL) {

                } else if (clickType == CLICK_TYPE_LONG_PRESS) {

                }
            }
        }
    };

    private void onAddNewItemClicked(){
        addItemFrame.removeAllViews();
        AddEditClipboardItemView addClipboardItemView = new AddEditClipboardItemView(context);
        addItemFrame.addView(addClipboardItemView);
        addClipboardItemView.getLayoutParams().height = ViewController.KEYBOARD_HEIGHT;
    }

    private void openDialogForNewClipboardItem() {

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
}
