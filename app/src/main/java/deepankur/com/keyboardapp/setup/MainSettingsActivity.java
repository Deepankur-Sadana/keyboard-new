/*
 * Copyright (c) 2013 Menny Even-Danan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package deepankur.com.keyboardapp.setup;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;

import net.evendanan.chauffeur.lib.permissions.PermissionsFragmentChauffeurActivity;
import net.evendanan.chauffeur.lib.permissions.PermissionsRequest;

import java.lang.ref.WeakReference;

import deepankur.com.keyboardapp.R;
import deepankur.com.keyboardapp.enums.PermissionsRequestCodes;

public class MainSettingsActivity extends PermissionsFragmentChauffeurActivity {

    public static final String EXTRA_KEY_APP_SHORTCUT_ID = "shortcut_id";
    //    private DrawerLayout mDrawerRootLayout;
//    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mTitle;
    private CharSequence mDrawerTitle;
    private SharedPreferences.OnSharedPreferenceChangeListener menuExtraUpdaterOnConfigChange = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
//            updateMenuExtraData();
        }
    };

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.main_ui);
        // Set the drawer toggle as the DrawerListener
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

//        AnyApplication.getConfig().addChangedListener(menuExtraUpdaterOnConfigChange);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
//        mDrawerToggle.syncState();
        //applying my very own Edge-Effect color
//        EdgeEffectHacker.brandGlowEffect(this, ContextCompat.getColor(this, R.color.app_accent));
//        handleAppShortcuts(getIntent());
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @NonNull
    @Override
    protected Fragment createRootFragmentInstance() {
        return new MainFragment();
    }

    @Override
    protected int getFragmentRootUiElementId() {
        return R.id.main_ui_content;
    }

    @Override
    protected void onStart() {
        super.onStart();
        //updating menu's data
//        updateMenuExtraData();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
            mAlertDialog = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        AnyApplication.getConfig().removeChangedListener(menuExtraUpdaterOnConfigChange);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
//        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
//        if (mDrawerToggle.onOptionsItemSelected(item)) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    //side menu navigation methods


    private AlertDialog mAlertDialog;

    public void startContactsPermissionRequest() {
        startPermissionsRequest(new ContactPermissionRequest(this));
    }

    @NonNull
    protected PermissionsRequest createPermissionRequestFromIntentRequest(int requestId, @NonNull String[] permissions, @NonNull Intent intent) {
        if (requestId == PermissionsRequestCodes.CONTACTS.getRequestCode()) {
            return new ContactPermissionRequest(this);
        } else {
            return super.createPermissionRequestFromIntentRequest(requestId, permissions, intent);
        }
    }


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
//            MainSettingsActivity activity = mMainSettingsActivityWeakReference.get();
//            if (activity == null) return;
//            //if the result is DENIED and the OS says "do not show rationale", it means the user has ticked "Don't ask me again".
//            final boolean userSaysDontAskAgain = !ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_CONTACTS);
//            //the user has denied us from reading the Contacts information.
//            //I'll ask them to whether they want to grant anyway, or disable ContactDictionary
//            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
//            builder.setCancelable(true);
//            builder.setIcon(R.drawable.ic_notification_contacts_permission_required);
//            builder.setTitle(R.string.notification_read_contacts_title);
//            builder.setMessage(activity.getString(R.string.contacts_permissions_dialog_message));
//            builder.setPositiveButton(activity.getString(userSaysDontAskAgain ? R.string.navigate_to_app_permissions : R.string.allow_permission), activity.mContactsDictionaryDialogListener);
//            builder.setNegativeButton(activity.getString(R.string.turn_off_contacts_dictionary), activity.mContactsDictionaryDialogListener);
//
//            if (activity.mAlertDialog != null && activity.mAlertDialog.isShowing())
//                activity.mAlertDialog.dismiss();
//            activity.mAlertDialog = builder.create();
//            activity.mAlertDialog.show();
        }
    }
}
