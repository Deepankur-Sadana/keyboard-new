package deepankur.com.keyboardapp.cache;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import deepankur.com.keyboardapp.MasterClass;
import deepankur.com.keyboardapp.databases.DatabaseHelper;
import deepankur.com.keyboardapp.models.ClipBoardItemModel;

/**
 * Created by deepankursadana on 24/02/17.
 */

public class ClipBoardCache {
    private static ClipBoardCache ourInstance;
    private final String TAG = getClass().getSimpleName();
    private ArrayList<ClipBoardDataListener> clipBoardDataListeners;
    private ArrayList<ClipBoardItemModel> clipBoardItemModels;

    private ClipBoardCache() {
        clipBoardDataListeners = new ArrayList<>();
        clipBoardItemModels = new ArrayList<>();
        warmUpFromDataBase();
    }

    public synchronized static ClipBoardCache getInstance() {
        if (ourInstance == null)
            ourInstance = new ClipBoardCache();
        return ourInstance;
    }

    public void addItem(ClipBoardItemModel clipBoardItemModel) {
        clipBoardItemModels.add(clipBoardItemModel);
//        clipBoardItemModel.setId(System.currentTimeMillis());
        long clipboardItem = getDatabaseHelper().createClipboardItem(clipBoardItemModel, new long[0]);
        Log.d(TAG, "addItem: " + clipboardItem);

        for (ClipBoardDataListener l : clipBoardDataListeners) {
            l.onItemAdded(clipBoardItemModel);
        }
    }

    public void update(ClipBoardItemModel c) {
        for (ClipBoardDataListener l : clipBoardDataListeners) {
            l.onItemEdited(c);
        }
    }

    private void removeItem(ClipBoardItemModel clipBoardItemModel) {
        clipBoardItemModels.remove(clipBoardItemModel);
    }

    public ArrayList<ClipBoardItemModel> getAllClipboardModels() {
        return clipBoardItemModels;
    }

    private synchronized void warmUpFromDataBase() {
        List<ClipBoardItemModel> allToDos = getDatabaseHelper().getAllToDos();
        clipBoardItemModels.clear();
        clipBoardItemModels.addAll(allToDos);
        awakeAllListenersOnCompleteListChanged();
        allToDos.clear();
    }

    /**
     * This method calls {@link ClipBoardDataListener#onListChanged()},
     * compelling the whole list to be redrawn in the view.
     * Use it as seldom as possible due to its unoptimized behaviour.
     */
    private DatabaseHelper getDatabaseHelper() {
        Context globalContext = MasterClass.getGlobalContext();
        if (globalContext == null)
            throw new NullPointerException("Master Class Context is null");
        return DatabaseHelper.getDataBaseHelper(globalContext);
    }

    private void awakeAllListenersOnCompleteListChanged() {
        if (clipBoardDataListeners != null)
            for (ClipBoardDataListener l : clipBoardDataListeners) {
                l.onListChanged();
            }
    }

    public void addListener(ClipBoardDataListener clipBoardDataListener) {
        clipBoardDataListeners.add(clipBoardDataListener);
    }

    public interface ClipBoardDataListener {
        void onItemAdded(ClipBoardItemModel clipBoardItemModel);

        void onItemEdited(ClipBoardItemModel clipBoardItemModel);

        void onListChanged();
    }
}