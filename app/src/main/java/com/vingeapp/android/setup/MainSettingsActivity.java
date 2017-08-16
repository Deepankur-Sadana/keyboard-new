package com.vingeapp.android.setup;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
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
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.vingeapp.android.R;
import com.vingeapp.android.activities.BaseActivity;
import com.vingeapp.android.apiHandling.RequestManager;
import com.vingeapp.android.apiHandling.ServerRequestType;
import com.vingeapp.android.enums.PermissionsRequestCodes;
import com.vingeapp.android.ftue.ChildFirstRunIntroFragment;
import com.vingeapp.android.ftue.ParentFirstRunIntroFragment;
import com.vingeapp.android.preferences.PrefsKeyIds;

import net.evendanan.chauffeur.lib.permissions.PermissionsRequest;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import utils.AppLibrary;

public class MainSettingsActivity extends BaseActivity implements PrefsKeyIds {


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
        SharedPreferences prefs = MainSettingsActivity.this.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        Log.d(TAG, "onCreate: access token " + prefs.getString(AppLibrary.FACEBOOK_ACCESS_TOKEN, null));

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.vingeapp.android",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {

        }

        callbackManager = CallbackManager.Factory.create();

        callFacebookLogout(this);
        addCallBack();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main_ui);
        mTitle = mDrawerTitle = getTitle();
        SharedPreferences preferences = this.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        Map<String, ?> all = preferences.getAll();
        preferences.registerOnSharedPreferenceChangeListener(mOnSharedPreferenceChangeListener);
        preferences.unregisterOnSharedPreferenceChangeListener(mOnSharedPreferenceChangeListener);

        boolean firstIntroFragment = checkAndLaunchFirstIntroFragment();
        if (!firstIntroFragment) {
            boolean launchSetUpFragment = checkAndLaunchSetUpFragment(this);
            if (!launchSetUpFragment){
                WizardSelectTabsFragment fragment = new WizardSelectTabsFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.main_ui_content, fragment, SetUpKeyboardWizardFragment.class.getSimpleName());
                fragmentTransaction.addToBackStack(SetUpKeyboardWizardFragment.class.getSimpleName());
                fragmentTransaction.commitAllowingStateLoss();
            }
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

    @Override
    protected void onPermissionsGranted(String[] permissions) {
//        finish();
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

        boolean b = SetupSupport.isThisKeyboardSetAsDefaultIME(context) && AppLibrary.allPermissionsGranted(this);
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


    /**
     * We declare our first intro to be done only when the user has done facebook login
     */
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

    private CallbackManager callbackManager;

    void addCallBack() {
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @SuppressLint("CommitPrefEdits")
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        if (loginResult != null) {
                            String accessToken = loginResult.getAccessToken().getToken();
                            Log.d(TAG, "Permissions granted -" + AccessToken.getCurrentAccessToken().getPermissions());
                            Log.d(TAG, "OnSuccess, Facebook Access Token - " + accessToken);
                            Log.d("OnSuccess, FACEBOOK_ID", loginResult.getAccessToken().getUserId());
                            final SharedPreferences prefs = MainSettingsActivity.this.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);

                            prefs.edit().putString(AppLibrary.FACEBOOK_ACCESS_TOKEN, accessToken).commit();
                            prefs.edit().putBoolean(AppLibrary.FACEBOOK_LOGIN_STATE, true).commit();
                            prefs.edit().putString(AppLibrary.FACEBOOK_ID, loginResult.getAccessToken().getUserId()).commit();
                            prefs.edit().putBoolean(AppLibrary.BIRTHDAY_PERMISSION, true).commit();

                            GraphRequest request = GraphRequest.newMeRequest(
                                    loginResult.getAccessToken(),
                                    new GraphRequest.GraphJSONObjectCallback() {
                                        @Override
                                        public void onCompleted(JSONObject object, GraphResponse response) {
                                            onFirstIntroDone();
                                            Log.v("LoginActivity", response.toString());
                                            // Application code
                                            try {
                                                String email = object.getString("email");
                                                String birthday = object.getString("birthday"); // 01/31/1980 format
                                                Log.d(TAG, "onCompleted: email " + email + " birthday " + birthday);
                                                prefs.edit().putString(AppLibrary.FACEBOOK_EMAIL, email).commit();
                                                postFacebookLoginRequest();

                                            } catch (JSONException e) {
                                                Log.d(TAG, "onCompleted: " + e);
                                                e.printStackTrace();
                                                postFacebookLoginRequest();
                                            }
                                        }
                                    });
                            Bundle parameters = new Bundle();
                            parameters.putString("fields", "id,name,email,gender,birthday");
                            request.setParameters(parameters);
                            request.executeAsync();

                        } else {
                            Log.d(TAG, "On Success, Login result not found");
                            Toast.makeText(MainSettingsActivity.this, "Sorry! Something went wrong", Toast.LENGTH_SHORT).show();
                            enableLoginButton();
                        }
                    }

                    @Override
                    public void onCancel() {
                        Log.d(TAG, "On Cancel");
                        Toast.makeText(MainSettingsActivity.this, "Sorry! Something went wrong", Toast.LENGTH_SHORT).show();
//                        findViewById(R.id.facebookLayout).setClickable(true);
                        enableLoginButton();

                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.d(TAG, "On Error");
//                        findViewById(R.id.facebookLayout).setClickable(true);
                        enableLoginButton();
                        Toast.makeText(MainSettingsActivity.this, "Sorry! Something went wrong", Toast.LENGTH_SHORT).show();
                        exception.printStackTrace();
                    }
                });
    }

    private ChildFirstRunIntroFragment childIntroFragment;

    public void setChildIntroFragment(ChildFirstRunIntroFragment childIntroFragment) {
        this.childIntroFragment = childIntroFragment;
    }

    public void onLoginButtonClicked() {
        doFacebookLogin();
    }


    private void enableLoginButton() {
        if (childIntroFragment != null) childIntroFragment.enableFacebookButton();
    }

    private void doFacebookLogin() {
        LoginManager.getInstance().logInWithReadPermissions(this,
                Arrays.asList("public_profile", "email", "user_friends", "user_birthday"));
    }

    /**
     * Logout From Facebook
     */
    public static void callFacebookLogout(Context context) {
        LoginManager.getInstance().logOut();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @SuppressWarnings("deprecation")
    private void postFacebookLoginRequest() {
        SharedPreferences prefs = MainSettingsActivity.this.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);

        List<NameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("fb_token", prefs.getString(AppLibrary.FACEBOOK_ACCESS_TOKEN, "")));
        pairs.add(new BasicNameValuePair("email", prefs.getString(AppLibrary.FACEBOOK_EMAIL, "")));
        RequestManager.makePostRequest(this, ServerRequestType.CREATE_USER_REQUEST, null, pairs, postLoginCallback);
    }

    private RequestManager.OnRequestFinishCallback postLoginCallback = new RequestManager.OnRequestFinishCallback() {
        @Override
        public void onBindParams(boolean success, Object response) {
            if (success)
                onFirstIntroDone();
            Log.d(TAG, "onBindParams: " + success + " response " + response);
        }

        @Override
        public boolean isDestroyed() {
            return false;
        }
    };

}
