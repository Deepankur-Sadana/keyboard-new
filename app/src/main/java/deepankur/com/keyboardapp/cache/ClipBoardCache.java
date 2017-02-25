package deepankur.com.keyboardapp.cache;

import java.util.ArrayList;

import deepankur.com.keyboardapp.models.ClipBoardItemModel;

/**
 * Created by deepankursadana on 24/02/17.
 */

public class ClipBoardCache {
    private static ClipBoardCache ourInstance;
    private ArrayList<ClipBoardDataListener> clipBoardDataListeners;

    public synchronized static ClipBoardCache getInstance() {
        if (ourInstance == null)
            ourInstance = new ClipBoardCache();
        return ourInstance;
    }

    private ArrayList<ClipBoardItemModel> clipBoardItemModels;

    public void addItem(ClipBoardItemModel clipBoardItemModel) {
        clipBoardItemModels.add(clipBoardItemModel);

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

    private ClipBoardCache() {
        clipBoardDataListeners = new ArrayList<>();
        clipBoardItemModels = new ArrayList<>();
    }

    public ArrayList<ClipBoardItemModel> getAllClipboardModels() {
        return clipBoardItemModels;
    }

    public void addListener(ClipBoardDataListener clipBoardDataListener) {
        clipBoardDataListeners.add(clipBoardDataListener);
    }


   public interface ClipBoardDataListener {
        void onItemAdded(ClipBoardItemModel clipBoardItemModel);

        void onItemEdited(ClipBoardItemModel clipBoardItemModel);
    }
}
