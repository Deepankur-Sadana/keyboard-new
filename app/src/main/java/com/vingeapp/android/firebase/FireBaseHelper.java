package com.vingeapp.android.firebase;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.vingeapp.android.BuildConfig;
import com.vingeapp.android.MasterClass;
import com.vingeapp.android.models.ClipBoardItemModel;
import com.vingeapp.android.utils.DeviceUuidFactory;

import java.util.ArrayList;
import java.util.LinkedHashSet;

import utils.AppLibrary;

import static utils.AppLibrary.FACEBOOK_ID;

/**
 * Created by deepankursadana on 13/03/17.
 */

public class FireBaseHelper implements FireBaseKEYIDS {
    private static FireBaseHelper sFireBaseHelper = new FireBaseHelper();
    private Context mContext;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    public static synchronized FireBaseHelper getInstance(Context context) {
        String TAG = "FireBaseHelper";
        if (sFireBaseHelper == null) {
            sFireBaseHelper = new FireBaseHelper(context.getApplicationContext());
            Log.i(TAG, "initialized fireBase ");
        }
        sFireBaseHelper.checkdebug();
        return sFireBaseHelper;
    }

    private DatabaseReference mFireBase;


    private FireBaseHelper() {
    }

    private static boolean initializationDone;

    private FireBaseHelper(Context context) {
        this.mContext = context;
        Log.d(TAG, "FireBaseHelper: ");
        if (mFireBase == null) {

            if (!initializationDone) {
                if (!AppLibrary.PRODUCTION_MODE)
                    FirebaseDatabase.getInstance().setLogLevel(Logger.Level.DEBUG);

                FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            }

            mFireBase = FirebaseDatabase.getInstance().getReferenceFromUrl(FireBaseKEYIDS.fireBaseURL);
            initializationDone = true;
        }

        if (mFirebaseRemoteConfig == null) {
            mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
            if (!AppLibrary.PRODUCTION_MODE) {
                FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                        .setDeveloperModeEnabled(BuildConfig.DEBUG)
                        .build();
                mFirebaseRemoteConfig.setConfigSettings(configSettings);
            }
//            mFirebaseRemoteConfig.setDefaults(R.xml.app_defaults);
//            fetchServerValues();
        }

//        if (firebaseHelperThread == null) {
//            firebaseHelperThread = new HandlerThread("cameraStickerThread");
//            firebaseHelperThread.start();
//        }
//        if (firebaseHelperHandler == null)
//            firebaseHelperHandler = new Handler(firebaseHelperThread.getLooper());
//
//        if (getMyUserId() == null) return;
//
//
//        loadData();
        loadPrefferedApplications();
    }

    private void checkdebug() {
        Log.d(TAG, "FireBaseHelper: 11111 ");
        getNewFireBase("test_exception", null).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "FireBaseHelper: 3333  ");
                if (dataSnapshot.getValue(String.class).equals("test_exception"))
                throw new NullPointerException();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * @param anchor   the outer  FireBase node ie. socials,moments,users,media
     * @param children subsequent nodes to query upon; supply null If you need the anchor nodes.
     *                 instead of children
     * @return the resolved FireBase
     */

    private DatabaseReference getNewFireBase(String anchor, String[] children) {
        if (mFireBase == null)
            mFireBase = FirebaseDatabase.getInstance().getReferenceFromUrl(FireBaseKEYIDS.fireBaseURL);
        if (anchor == null) throw new NullPointerException("anchor cannot be null");
        DatabaseReference fireBase = this.mFireBase.child(anchor);
        if (children != null && children.length > 0) {
            for (int i = 0; i < children.length; i++) {
                fireBase = fireBase.child(children[i]);
            }
        }
        return fireBase;
    }

    private ArrayList<ClipBoardItemModel> clipBoardItemModelArrayList;

    private final String TAG = FireBaseHelper.class.getSimpleName();

    public void loadClipboardItems() {
        if (clipBoardItemModelArrayList != null) {
            Log.d(TAG, "loadClipboardItems: only one call allowed to this method");
        }
        clipBoardItemModelArrayList = new ArrayList<>();
        this.getNewFireBase(ANCHOR_CLIPBOARD, new String[]{getUserId()}).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ClipBoardItemModel model = dataSnapshot.getValue(ClipBoardItemModel.class);
                model.id = dataSnapshot.getKey();
                clipBoardItemModelArrayList.add(model);
                awakeAllListeners();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                for (int i = 0; i < clipBoardItemModelArrayList.size(); i++) {
                    if (clipBoardItemModelArrayList.get(i).id.equals(dataSnapshot.getKey())) {
                        ClipBoardItemModel model = dataSnapshot.getValue(ClipBoardItemModel.class);
                        model.id = dataSnapshot.getKey();
                        clipBoardItemModelArrayList.set(i, model);
                        awakeAllListeners();
                        break;
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                for (int i = 0; i < clipBoardItemModelArrayList.size(); i++) {
                    if (clipBoardItemModelArrayList.get(i).id.equals(dataSnapshot.getKey())) {
                        clipBoardItemModelArrayList.remove(i);
                        awakeAllListeners();
                        break;
                    }
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void addNewClipBoardItemToDb(ClipBoardItemModel c) {
        DatabaseReference newFireBase = getNewFireBase(ANCHOR_CLIPBOARD, new String[]{getUserId()});
        DatabaseReference push = newFireBase.push();
        push.setValue(c);
    }


    ArrayList<ClipBoardDataListener> clipBoardDataListeners;

    public void addClipBoardDataListener(ClipBoardDataListener clipBoardDataListener) {
        if (clipBoardDataListeners == null)
            clipBoardDataListeners = new ArrayList<>();
        clipBoardDataListeners.add(clipBoardDataListener);
    }

    public void removeClipBoardDataListener(ClipBoardDataListener c) {
        if (clipBoardDataListeners != null)
            clipBoardDataListeners.remove(c);
    }

    public ArrayList<ClipBoardItemModel> getAllClipboardModels() {
        return clipBoardItemModelArrayList;
    }

    public void updateClipBoardItem(ClipBoardItemModel clipBoardItemModel) {
        getNewFireBase(ANCHOR_CLIPBOARD, new String[]{getUserId(), clipBoardItemModel.id}).setValue(clipBoardItemModel);
    }

    public void deleteClipboardItem(ClipBoardItemModel clipBoardItemModel) {
        getNewFireBase(ANCHOR_CLIPBOARD, new String[]{getUserId(), clipBoardItemModel.id}).setValue(null);

    }

    public interface ClipBoardDataListener {
        void onItemAdded(ClipBoardItemModel clipBoardItemModel);

        void onItemEdited(ClipBoardItemModel clipBoardItemModel);

        void onListChanged();
    }

    /**
     * bad call, needs to be optimized
     */
    private void awakeAllListeners() {
        if (clipBoardDataListeners != null)
            for (int i = 0; i < clipBoardDataListeners.size(); i++) {
                ClipBoardDataListener clipBoardDataListener = clipBoardDataListeners.get(i);
                clipBoardDataListener.onListChanged();
            }

    }


    private String getUserId() {
        String facebookId = getFacebookId();
        if (facebookId != null)
            return facebookId;
        return new DeviceUuidFactory(MasterClass.getGlobalContext()).getDeviceUuid().toString();
    }

    private String getFacebookId() {
        SharedPreferences defaultSharePrefs = AppLibrary.getDefaultSharePrefs();
        return defaultSharePrefs.getString(FACEBOOK_ID, null);
    }


    //We are using linked hashset as we don't want duplicate entries and also we want the items to remain ordered.
    private LinkedHashSet<String> mAllPackageNames = new LinkedHashSet<>();

    private void loadPrefferedApplications() {
        Log.d(TAG, "loadPrefferedApplications: ");
        getNewFireBase(ANCHOR_SHORTCUT_APPS, new String[]{getUserId()}).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "loadPrefferedApplications: " + dataSnapshot);
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    mAllPackageNames.add(DecodeString(dataSnapshot1.getKey()));
                }
                if (onShortCutAppChangedListener != null)
                    onShortCutAppChangedListener.onListUpdated(mAllPackageNames);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void deleteAppFromShortcut(String packageName) {
        packageName = EncodeString(packageName);
        getNewFireBase(ANCHOR_SHORTCUT_APPS, new String[]{getUserId(), packageName}).setValue(null);

    }

    public void addPackageNameToPrefs(String packageName) {
        packageName = EncodeString(packageName);
        getNewFireBase(ANCHOR_SHORTCUT_APPS, new String[]{getUserId(), packageName}).setValue(1);
    }


    private OnShortCutAppChangedListener onShortCutAppChangedListener;

    public void setOnShortCutAppChangedListener(@NonNull OnShortCutAppChangedListener onShortCutAppChangedListener) {
        this.onShortCutAppChangedListener = onShortCutAppChangedListener;
        onShortCutAppChangedListener.onListUpdated(mAllPackageNames);
    }

    public interface OnShortCutAppChangedListener {
        void onListUpdated(LinkedHashSet<String> newList);

    }

    private static String EncodeString(String string) {
        return string.replace(".", ",");
    }

    private static String DecodeString(String string) {
        return string.replace(",", ".");
    }
}
