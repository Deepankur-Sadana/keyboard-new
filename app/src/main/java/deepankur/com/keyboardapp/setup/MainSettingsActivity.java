package deepankur.com.keyboardapp.setup;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.SharedPreferencesCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MenuItem;

import net.evendanan.chauffeur.lib.permissions.PermissionsRequest;

import java.lang.ref.WeakReference;
import java.util.Map;

import deepankur.com.keyboardapp.R;
import deepankur.com.keyboardapp.enums.PermissionsRequestCodes;
import deepankur.com.keyboardapp.ftue.ParentFirstRunIntroFragment;
import deepankur.com.keyboardapp.preferences.PrefsKeyIds;

public class MainSettingsActivity extends FragmentActivity implements PrefsKeyIds {


    public static final String EXTRA_KEY_APP_SHORTCUT_ID = "shortcut_id";
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mTitle;
    private CharSequence mDrawerTitle;
    private SharedPreferences.OnSharedPreferenceChangeListener menuExtraUpdaterOnConfigChange = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        }
    };

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        Log.d(TAG, "onCreate: ");
        setContentView(R.layout.main_ui);
        mTitle = mDrawerTitle = getTitle();
        SharedPreferences preferences = this.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        Map<String, ?> all = preferences.getAll();
        preferences.registerOnSharedPreferenceChangeListener(mOnSharedPreferenceChangeListener);
        preferences.unregisterOnSharedPreferenceChangeListener(mOnSharedPreferenceChangeListener);

        boolean firstIntroFragment = checkAndLaunchFirstIntroFragment();
        if (!firstIntroFragment) {
            boolean launchSetUpFragment = checkAndLaunchSetUpFragment(this);
        }

    }

    SharedPreferences.OnSharedPreferenceChangeListener mOnSharedPreferenceChangeListener
            = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        }
    };

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        EdgeEffectHacker.brandGlowEffect(this, ContextCompat.getColor(this, android.R.color.holo_red_dark));
        handleAppShortcuts(getIntent());
    }

    private void handleAppShortcuts(Intent intent) {
        if (intent != null && Intent.ACTION_VIEW.equals(intent.getAction()) && intent.hasExtra(EXTRA_KEY_APP_SHORTCUT_ID)) {
            final String shortcutId = intent.getStringExtra(EXTRA_KEY_APP_SHORTCUT_ID);
            intent.removeExtra(EXTRA_KEY_APP_SHORTCUT_ID);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleAppShortcuts(intent);
    }


    final String TAG = getClass().getSimpleName();

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
            mAlertDialog = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
//        getSupportActionBar().setTitle(mTitle);
    }


    /**
     * Will set the title in the hosting Activity's title.
     * Will only set the title if the fragment is hosted by the Activity's manager, and not inner one.
     */
    public static void setActivityTitle(Fragment fragment, CharSequence title) {
        FragmentActivity activity = fragment.getActivity();
        if (activity.getSupportFragmentManager() == fragment.getFragmentManager()) {
            activity.setTitle(title);
        }
    }

    private final DialogInterface.OnClickListener mContactsDictionaryDialogListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, final int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    if (ActivityCompat.shouldShowRequestPermissionRationale(MainSettingsActivity.this, Manifest.permission.READ_CONTACTS)) {
                        startContactsPermissionRequest();
                    } else {
//                        startAppPermissionsActivity();
                    }
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    final SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putBoolean(getString(R.string.settings_key_use_contacts_dictionary), false);
                    SharedPreferencesCompat.EditorCompat.getInstance().apply(editor);
                    break;
            }
        }
    };

    private AlertDialog mAlertDialog;

    public void startContactsPermissionRequest() {
//        startPermissionsRequest(new ContactPermissionRequest(this));
    }

//    @NonNull
//    protected PermissionsRequest createPermissionRequestFromIntentRequest(int requestId, @NonNull String[] permissions, @NonNull Intent intent) {
//        if (requestId == PermissionsRequestCodes.CONTACTS.getRequestCode()) {
//            return new ContactPermissionRequest(this);
//        } else {
//            return super.createPermissionRequestFromIntentRequest(requestId, permissions, intent);
//        }
//    }


    private static class ContactPermissionRequest extends PermissionsRequest.PermissionsRequestBase {

        private final WeakReference<MainSettingsActivity> mMainSettingsActivityWeakReference;

        public ContactPermissionRequest(MainSettingsActivity activity) {
            super(PermissionsRequestCodes.CONTACTS.getRequestCode(), Manifest.permission.READ_CONTACTS);
            mMainSettingsActivityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void onPermissionsGranted() {
            /*
            nothing to do here, it will re-load the contact dictionary next time the
            input-connection will start.
            */
        }

        @Override
        public void onPermissionsDenied(@NonNull String[] grantedPermissions, @NonNull String[] deniedPermissions, @NonNull String[] declinedPermissions) {
            MainSettingsActivity activity = mMainSettingsActivityWeakReference.get();
            if (activity == null) return;
            //if the result is DENIED and the OS says "do not show rationale", it means the user has ticked "Don't ask me again".
            final boolean userSaysDontAskAgain = !ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_CONTACTS);
            //the user has denied us from reading the Contacts information.
            //I'll ask them to whether they want to grant anyway, or disable ContactDictionary
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setCancelable(true);
            builder.setIcon(R.drawable.ic_notification_contacts_permission_required);
            builder.setTitle(R.string.notification_read_contacts_title);
            builder.setMessage(activity.getString(R.string.contacts_permissions_dialog_message));
            builder.setPositiveButton(activity.getString(userSaysDontAskAgain ? R.string.navigate_to_app_permissions : R.string.allow_permission), activity.mContactsDictionaryDialogListener);
            builder.setNegativeButton(activity.getString(R.string.turn_off_contacts_dictionary), activity.mContactsDictionaryDialogListener);

            if (activity.mAlertDialog != null && activity.mAlertDialog.isShowing())
                activity.mAlertDialog.dismiss();
            activity.mAlertDialog = builder.create();
            activity.mAlertDialog.show();
        }
    }


    static boolean loaded;

    /**
     * @return true if the fragment was launched,false otherwise
     */
    private boolean checkAndLaunchFirstIntroFragment() {
        // parse Preference file
        SharedPreferences preferences = this.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        boolean introDone = preferences.getBoolean(APP_INTRO_DONE, false);
        if (introDone) return false;
        ParentFirstRunIntroFragment fragment = new ParentFirstRunIntroFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.main_ui_content, fragment, ParentFirstRunIntroFragment.class.getSimpleName());
        fragmentTransaction.commitAllowingStateLoss();

        return true;
    }

    /**
     * @param context
     * @return true if this fragment was launched,false otherwisw
     */
    private boolean checkAndLaunchSetUpFragment(Context context) {

        boolean b = SetupSupport.isThisKeyboardSetAsDefaultIME(context);
        if (b)
            return false;
        SetUpKeyboardWizardFragment fragment = new SetUpKeyboardWizardFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.main_ui_content, fragment, SetUpKeyboardWizardFragment.class.getSimpleName());
        fragmentTransaction.addToBackStack(SetUpKeyboardWizardFragment.class.getSimpleName());
        fragmentTransaction.commitAllowingStateLoss();
        return true;
    }


    public void onFirstIntroDone() {
        SharedPreferences preferences = this.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(APP_INTRO_DONE, true);
        editor.commit();
        removeFirstIntroFragment();
        checkAndLaunchSetUpFragment(this);
    }

    public void removeFirstIntroFragment() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(ParentFirstRunIntroFragment.class.getSimpleName());
        try {
            if (fragment != null)
                getSupportFragmentManager().beginTransaction().remove(fragment).commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (getFragmentInBackStack(ParentFirstRunIntroFragment.class.getSimpleName()) != null) {
            boolean popped = false;
            try {
                popped = getSupportFragmentManager().popBackStackImmediate();
            } catch (Exception e) {
                e.printStackTrace();
            }

            Log.d(TAG, "removeFirstIntroFragment: popped " + popped);
        } else
            Log.d(TAG, "removeFirstIntroFragment: fragment is null");
    }

    /**
     * @param fragmentTag the String supplied while fragment transaction
     * @return
     */
    private Fragment getFragmentInBackStack(String fragmentTag) {
        return getSupportFragmentManager().findFragmentByTag(fragmentTag);

    }

}
