package com.vingeapp.android.apiHandling;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.util.Log;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import utils.AppLibrary;

import static com.vingeapp.android.preferences.PrefsKeyIds.FILE_NAME;

/**
 * Created by admin on 1/13/2015.
 */
public class RequestManager implements ServerKeyIDS {
    private static final String TAG = "RequestManager";

    //  private static Context mContext;
    private static SharedPreferences prefs;

    private static OkHttpClient client;

    /* request url types, should end with _REQUEST */
    public static final int USER_VERIFY_PHONE_REQUEST = 3000;


    /* object types, should end with _DETAILS or _LIST */
    public static final int USER_PHONE_VERIFIED_DETAILS = 5000;

    // Request related constants
    public static final String S3_IMAGE_BUCKET_PATH_PREFIX =
            "https://s3-ap-southeast-1.amazonaws.com/instalively.images/"; //Used for viewing images, so don't change

    private static String APP_VERSION_CODE="v1";

    public static void initialize(Context context) {
        //  mContext = context;
        prefs = context.getSharedPreferences(FILE_NAME, 0);

        PackageInfo pInfo = null;
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
//            APP_VERSION_CODE = Integer.toString(pInfo.versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        client = new OkHttpClient();
        //Optional Parameters
//      client.setConnectTimeout(10, TimeUnit.SECONDS);
//      client.setWriteTimeout(10, TimeUnit.SECONDS);
//      client.setReadTimeout(30, TimeUnit.SECONDS);
    }

    public static void makeGetRequest(Context context, ServerRequestType serverRequestType, List<NameValuePair> pairs, OnRequestFinishCallback mCallback) {
        int count = 0;
        do {
            if (Connectivity.isConnected(context)) {
                if (pairs == null)
                    pairs = new ArrayList<>();
                new GetRequestTask(serverRequestType, pairs, mCallback, context).executeOnExecutor(THREAD_POOL_EXECUTOR_API);
                return;
            } else {
                count++;
            }
        } while (count <= 4);
    }

    //    public static void makePostRequest(Context context, int url_type, int object_type, List<NameValuePair> getParams, List<NameValuePair> pairs, OnRequestFinishCallback mCallback) {
//        int count = 0;
//        do {
//            if (Connectivity.isConnected(context)) {
//                if (getParams == null)
//                    getParams = new ArrayList<>();
//                if (pairs == null)
//                    pairs = new ArrayList<>();
//                new PostRequestTask(url_type, object_type, getParams, pairs, mCallback).executeOnExecutor(THREAD_POOL_EXECUTOR_API);
//                return;
//            } else {
//                count++;
//            }
//        } while (count <= 4);
//    }
    public static void makePostRequest(Context context, ServerRequestType serverRequestType, List<NameValuePair> getParams, List<NameValuePair> pairs, OnRequestFinishCallback mCallback) {
        int count = 0;
        do {
            if (Connectivity.isConnected(context)) {
                if (getParams == null)
                    getParams = new ArrayList<>();
                if (pairs == null)
                    pairs = new ArrayList<>();
                new PostRequestTask(serverRequestType, getParams, pairs, mCallback).executeOnExecutor(THREAD_POOL_EXECUTOR_API);
                return;
            } else {
                count++;
            }
        } while (count <= 4);
    }


    private static Object NewParseResponse(JSONObject object, ServerRequestType serverRequestType) throws JSONException {
        return object;
    }


    private static String SERVER_HOST_URL = "http://52.76.209.202:3000/api/";

    private static String getUrlFromType(ServerRequestType url_type) {
        String url = SERVER_HOST_URL + APP_VERSION_CODE + "/";
        switch (url_type) {
            case CREATE_USER_REQUEST:
                url += "users/create";
            case GOOGLE_MAPS_API:
                url = "http://maps.googleapis.com/maps/api/geocode/json?";
        }
        Log.d(TAG, "getUrlFromType: " + url);
        return url;
    }

    /* hiding constructor */
    private RequestManager() {
    }

    /* helper functions */
    private static JSONObject convertJSONObject(InputStream is) throws JSONException {
        String json = convertInputStreamToString(is);
        Log.d(TAG, "response: " + json);
        JSONObject jsonObject = new JSONObject(json);
        return jsonObject;
    }

    private static String convertInputStreamToString(InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    /* scheduler & task */
    private static final BlockingQueue<Runnable> sPoolWorkQueueApi = new LinkedBlockingQueue<Runnable>(1);
    private static ThreadFactory sThreadFactoryApi = new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r);
        }
    };
    private static final int mCorePoolSize = 3;
    private static final int mMaxPoolSize = 20;
    private static final Executor THREAD_POOL_EXECUTOR_API = new ThreadPoolExecutor(mCorePoolSize, mMaxPoolSize, 1,
            TimeUnit.SECONDS, sPoolWorkQueueApi, sThreadFactoryApi, new ThreadPoolExecutor.DiscardPolicy());

    private static class PostRequestTask extends AsyncTask<Object, Void, Object> {
        //        int url_type;
//        int object_type;
        List<NameValuePair> pairs;
        List<NameValuePair> postParams;
        OnRequestFinishCallback mCallback;
        ServerRequestType serverRequestType;

//        public PostRequestTask(int url_type, int object_type, List<NameValuePair> pairs, List<NameValuePair> postParams, OnRequestFinishCallback mCallback) {
//            this.url_type = url_type;
//            this.object_type = object_type;
//            this.pairs = pairs;
//            this.postParams = postParams;
//            this.mCallback = mCallback;
//        }

        public PostRequestTask(ServerRequestType serverRequestType, List<NameValuePair> pairs, List<NameValuePair> postParams, OnRequestFinishCallback mCallback) {
            this.serverRequestType = serverRequestType;
            this.pairs = pairs;
            this.postParams = postParams;
            this.mCallback = mCallback;
        }


        @Override
        protected Object doInBackground(Object... params) {
            String url_string = getUrlFromType(serverRequestType);

            if (this.pairs == null) //If a null object is passed, create a new array list
                this.pairs = new ArrayList<>();

            if (!pairs.isEmpty()) {
                for (NameValuePair pair : pairs) {
                    url_string += "&" + pair.getName() + "=" + pair.getValue();
                }
            }

            Response response = null;
            Object parsedResponse = null;
            FormEncodingBuilder form = new FormEncodingBuilder();
            RequestBody formBody;

            if (postParams != null && !postParams.isEmpty()) {
                for (NameValuePair nvp : this.postParams) {
                    if (nvp != null) {
                        String name = nvp.getName();
                        String value = nvp.getValue();
                        form.add(name, value);
                    }
                }
                formBody = form.build();
            } else {
                formBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), ""); //Pass empty string as post params
            }

            Request request = new Request.Builder()
                    .url(url_string)
                    .post(formBody)
                    .build();

            try {
                response = client.newCall(request).execute();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

            if (response == null)
                return null;

            try {
                JSONObject Jobject = new JSONObject(response.body().string());
                parsedResponse = NewParseResponse(Jobject, serverRequestType);
            } catch (Exception e) {
                AppLibrary.error_log(TAG, "Parse error", e);
            }

            return parsedResponse;
        }

        @Override
        protected void onPostExecute(Object result) {
            if (!mCallback.isDestroyed()) {
                mCallback.onBindParams(result != null, result);
            }
        }
    }

    private static class GetRequestTask extends AsyncTask<Object, Void, Object> {
        List<NameValuePair> pairs;
        OnRequestFinishCallback mCallback;
        Context context;
        ServerRequestType serverRequestType;

        GetRequestTask(ServerRequestType serverRequestType, List<NameValuePair> pairs, OnRequestFinishCallback mCallback, Context context) {
            this.pairs = pairs;
            this.mCallback = mCallback;
            this.context = context;
        }

        @Override
        protected Object doInBackground(Object... params) {
            String url_string = getUrlFromType(serverRequestType);

            if (this.pairs == null)  //If a null object is passed, create a new array list
                this.pairs = new ArrayList<>();
            if (!pairs.isEmpty()) {
                for (NameValuePair pair : pairs) {
                    url_string += "&" + pair.getName() + "=" + pair.getValue();
                }
            }

            Response response = null;
            Object parsedResponse = null;

            Request request = new Request.Builder()
                    .url(url_string)
                    .build();

            try {
                response = client.newCall(request).execute();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

            if (response == null)
                return null;

//            if (url_type == 3046) //For RTMP remove subscriber request, don't enter NewParseResponse function
//                return true;

//            if (url_type == 3061) {
//                File downloadedFile = new File(this.context.getCacheDir(), "URL");
//
//                try {
//                    BufferedSink sink = Okio.buffer(Okio.sink(downloadedFile));
//                    sink.writeAll(response.body().source());
//                    sink.close();
//                    return downloadedFile;
//                } catch (Exception e) {
//                    return null;
//                }
//            }

            try {
                JSONObject Jobject = new JSONObject(response.body().string());
                parsedResponse = NewParseResponse(Jobject, serverRequestType);
            } catch (Exception e) {
                AppLibrary.error_log(TAG, "Parse error", e);
            }

            return parsedResponse;
        }

        @Override
        protected void onPostExecute(Object result) {
            if (!mCallback.isDestroyed()) {
                mCallback.onBindParams(result != null, result);
            }
        }
    }

    public interface OnRequestFinishCallback {
        void onBindParams(boolean success, Object response);

        boolean isDestroyed();
    }
}
