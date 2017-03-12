package utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.login.LoginManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.vingeapp.android.MasterClass;

/**
 * Created by admin on 1/12/2015.
 */
public class AppLibrary {

    public static final String FACEBOOK_ACCESS_TOKEN = "facebook_access_token";

    public static final String FACEBOOK_ID = "facebook_id";
    public static final String FACEBOOK_BIO_INFO = "facebook_bio_info";
    public static final String BIRTHDAY_PERMISSION = "birthday_permission";
    public static final String FACEBOOK_LOGIN_STATE = "facebook_login_state";


    static final SimpleDateFormat outFormatD = new SimpleDateFormat("MMM d, yyyy", Locale.US);
    static final SimpleDateFormat outFormatT = new SimpleDateFormat("h:mma", Locale.US);
    static SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    private static int mWidthPixels;
    private static int mHeightPixels;


    public static void error_log(String TAG, String message, Exception e) {
        if (e != null) {
            String msg = message + e.getClass().toString();
            msg += Log.getStackTraceString(e);
            Log.e(TAG, msg);
        } else {
            Log.e(TAG, message + " ");
        }
    }

    public static void error_log(String TAG, String message, Error e) {
        if (e != null) {
            String msg = message + e.getClass().toString();
            msg += Log.getStackTraceString(e);
            Log.e(TAG, msg);
        } else {
            Log.e(TAG, message + " ");
        }
    }


    public static boolean isNetworkAvailable(Context c) {
        if (c == null) return false;
        ConnectivityManager connectivityManager = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private static String TAG = "AppLibrary";

    public static void makeViewVisible(final View v) {
        AlphaAnimation a = new AlphaAnimation(0, 1f);
        a.setDuration(100);
        a.setFillAfter(true);
        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                v.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        if (v.getVisibility() == View.GONE) {
            Log.d(TAG, "View is invisible, showing it");
            v.startAnimation(a);
        }
    }

    public static void makeViewInvisible(final View v) {
        AlphaAnimation a = new AlphaAnimation(1f, 0);
        a.setDuration(100);
        a.setFillAfter(true);
        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                v.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        if (v.getVisibility() == View.VISIBLE) {
            Log.d(TAG, "View is visible, hiding it");
            v.startAnimation(a);
        }
    }

    public static void showNoNetworkDialog(final Context mContext) {
        /*AlertDialog.Builder builder = new AlertDialog.Builder(mContext, AlertDialog.THEME_HOLO_DARK);
        builder.setTitle("No network Available");
        builder.setPositiveButton()
        builder.setMessage("");
        builder.create().show();*/
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext, AlertDialog.THEME_HOLO_DARK);

        // Setting Dialog Title
        alertDialog.setTitle("Bad Network");

        // Setting Dialog Message
        alertDialog.setMessage("Oops! Your internet is too slow. Please try with a different network source.");

        // On pressing Settings button
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    public static void showAgreementDialog(Context mContext) {
        /*AlertDialog.Builder builder = new AlertDialog.Builder(mContext, AlertDialog.THEME_HOLO_DARK);
        builder.setTitle("No network Available");
        builder.setPositiveButton()
        builder.setMessage("");
        builder.create().show();*/
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext, AlertDialog.THEME_HOLO_DARK);

        // Setting Dialog Title
        alertDialog.setTitle("Oops");

        // Setting Dialog Message
        alertDialog.setMessage("You have to agree to continue");
        alertDialog.setCancelable(false);
        // On pressing Settings button
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    public static void showServerDownDialog(Context mContext) {
        /*AlertDialog.Builder builder = new AlertDialog.Builder(mContext, AlertDialog.THEME_HOLO_DARK);
        builder.setTitle("No network Available");
        builder.setPositiveButton()
        builder.setMessage("");
        builder.create().show();*/
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext, AlertDialog.THEME_HOLO_DARK);

        // Setting Dialog Title
        alertDialog.setTitle("Oops");

        // Setting Dialog Message
        alertDialog.setMessage("Youtube server is down. Please try again later!");
        alertDialog.setCancelable(false);
        // On pressing Settings button
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    /* services */
    public static boolean isMyServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /* fonts */
    private static final Hashtable<String, Typeface> typefaces = new Hashtable<String, Typeface>();

    public static Typeface getTypeface(Context c, String name) {
        synchronized (typefaces) {
            if (!typefaces.containsKey(name)) {
                /*if (name.equals(Regular)) {
                    Typeface t =  Typeface.SANS_SERIF;
                    typefaces.put(name, t);
                    return t;

                } else if (name.equals(Bold)) {
                    Typeface t =  Typeface.SERIF;
                    typefaces.put(name, t);
                    return t;
                }*/

                try {
                    InputStream inputStream = c.getAssets().open(name);
                    File file = createFileFromInputStream(inputStream);
                    if (file == null) {
                        return Typeface.DEFAULT;
                    }
                    Typeface t = Typeface.createFromFile(file);
                    typefaces.put(name, t);
                } catch (Exception e) {
                    e.printStackTrace();
                    return Typeface.DEFAULT;
                }
            }
            return typefaces.get(name);
        }
    }

    public static boolean fileExists(String value) {
        return new File(value).exists();
    }

    private static File createFileFromInputStream(InputStream inputStream) {

        try {
            File f = File.createTempFile("font", null);
            OutputStream outputStream = new FileOutputStream(f);
            byte buffer[] = new byte[1024];
            int length = 0;

            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.close();
            inputStream.close();
            return f;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void hideKeyboard(Context context, EditText et) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
    }

    public static void showKeyboard(Context context, EditText et) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.showSoftInput(et, 0);
    }

    public static void toggleKeyboardVisibility(boolean isVisible, Context context, EditText et) {
        if (isVisible)
            hideKeyboard(context, et);
        else
            showKeyboard(context, et);
    }


    public static String capsFirstLetter(String str) {
        if (str != null & !str.isEmpty()) {
            Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(str.substring(0, 1));
            String upperString = str;

            if (!m.find())
                upperString = str.substring(0, 1).toUpperCase() + str.substring(1);

            return upperString;
        } else
            return ""; //Return empty string to prevent crashes
    }


    public static String getFirstName(String str) {
        if (str != null && !str.isEmpty()) {
            String trimmedName = str.trim();
            String[] items = trimmedName.split(" ");
            return items[0];
        } else
            return ""; //Return empty string to prevent crashes
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp      A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px      A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

    public static boolean isPackageInstalled(String targetPackage, Context context) {
        List<ApplicationInfo> packages;
        packages = context.getPackageManager().getInstalledApplications(0);
        for (ApplicationInfo packageInfo : packages) {
            if (packageInfo.packageName.equals(targetPackage))
                return true;
        }
        return false;
    }

    public static String getPackageBasedOnPriority(Context context) {
        if (isPackageInstalled("com.whatsapp", context)) {
            return "com.whatsapp";
        } else if (isPackageInstalled("com.facebook.orca", context)) {
            return "com.facebook.orca";
        }/*else if(isPackageInstalled("com.bsb.hike", context)){
            return "com.bsb.hike";
        }else if(isPackageInstalled("jp.naver.line.android", context)){
            return "jp.naver.line.android";
        }else if(isPackageInstalled("com.tencent.mm", context)){
            return "com.tencent.mm";
        }*/ else if (isPackageInstalled("com.google.android.talk", context)) {
            return "com.google.android.talk";
        } else if (isPackageInstalled("com.android.mms", context)) {
            return "com.android.mms";
        }
        return null;
    }

//    public static int getPackageImg(String packageName) {
//        if (packageName.equalsIgnoreCase("com.whatsapp")) {
//            return R.drawable.icon_whatsapp;
//        } else if (packageName.equalsIgnoreCase("com.facebook.orca")) {
//            return R.drawable.icon_messenger;
//        } else if (packageName.equalsIgnoreCase("com.bsb.hike")) {
//            return R.drawable.icon_hike;
//        } else if (packageName.equalsIgnoreCase("jp.naver.line.android")) {
//            return R.drawable.icon_line;
//        } else if (packageName.equalsIgnoreCase("com.tencent.mm")) {
//            return R.drawable.icon_wechat;
//        } else if (packageName.equalsIgnoreCase("com.google.android.talk")) {
//            return R.drawable.icon_hangout;
//        } else if (packageName.equalsIgnoreCase("com.android.mms")) {
//            return R.drawable.icon_sms;
//        }
//        return 0;
//    }

    @SuppressLint("NewApi")
    public static int getSoftbuttonsbarHeight(Activity activity) {
        // getRealMetrics is only available with API 17 and +
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            DisplayMetrics metrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int usableHeight = metrics.heightPixels;
            activity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
            int realHeight = metrics.heightPixels;
            if (realHeight > usableHeight)
                return realHeight - usableHeight;
            else
                return 0;
        }
        return 0;
    }

    public static int getDeviceParams(Activity activity, String params) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        if (params.contains("width"))
            return metrics.widthPixels;
        else
            return metrics.heightPixels;
    }

    private static int sDeviceWidth;

    public static int getDeviceWidth(Activity activity) {
        if (sDeviceWidth != 0) return sDeviceWidth;
        sDeviceWidth = getDeviceParams(activity, "width");
        return sDeviceWidth;
    }

    public static float getDeviceAspectRatio(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels / metrics.heightPixels;
    }

    public static int[] getDeviceParams(Activity activity) {
        if ((mWidthPixels == 0) || (mHeightPixels == 0)) {
            DisplayMetrics metrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            mWidthPixels = metrics.widthPixels;
            mHeightPixels = metrics.heightPixels;
        }
        int[] params = new int[2];
        params[0] = mWidthPixels;
        params[1] = mHeightPixels;
        return params;
    }


    public static String getRealPathFromURI(Uri contentUri, Context context) {
        String[] proj = {MediaStore.Images.Media.DATA};
//        CursorLoader loader = new CursorLoader(context, contentUri, proj, null, null, null);
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
//        Cursor cursor = loader.loadInBackground();
        if (cursor == null) return null;

        String result = null;
        try {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            result = cursor.getString(column_index);
        } catch (Exception e) {
            e.printStackTrace();
        }

        cursor.close();

        return result;
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;

        if (model == null || manufacturer == null) return null;

        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    public static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    public static String checkStringObject(String value) {
        if (value == null || value.equals("null") || value.equals(""))
            return null;
        else
            return value.trim();
    }

    // given a integer input returns the one decimal rounded output
    public static String getStringCount(int value) {
        String stringValue = "";
        DecimalFormat df = new DecimalFormat("0.0");
        if (value <= 1000) {
            stringValue = Integer.toString(value);
        } else if (value > 1000 && value < 100000) {
            stringValue = df.format((value / 1000.0)) + "k";
        } else if (value >= 100000) {
            stringValue = df.format((value / 100000.0)) + "l";
        }
        return stringValue;
    }

    public static void copyLink(Context context, String link) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", link);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(context, "Link copied!", Toast.LENGTH_SHORT).show();
    }

    public static void doFacebookLogOut() {
        LoginManager.getInstance().logOut();
    }

    public static int convertDpToPixels(Context context, int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                context.getResources().getDisplayMetrics()
        );
    }

    public static void showShortToast(String s) {
        Toast.makeText(MasterClass.getGlobalContext(), s, Toast.LENGTH_SHORT).show();
    }

}
