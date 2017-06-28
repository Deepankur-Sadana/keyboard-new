package com.vingeapp.android.keyboardCustomViews.maps;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.vingeapp.android.InAppEditingController;
import com.vingeapp.android.R;
import com.vingeapp.android.googleLocationApiResponse.Result;
import com.vingeapp.android.adapters.SearchMapsAdapter;
import com.vingeapp.android.apiHandling.RequestManager;
import com.vingeapp.android.apiHandling.ServerRequestType;
import com.vingeapp.android.interfaces.GreenBotMessageKeyIds;
import com.vingeapp.android.interfaces.RecyclerViewClickInterface;
import com.vingeapp.android.interfaces.Refreshable;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by deepankursadana on 02/04/17.
 */

public class SearchMapsView extends FrameLayout implements GreenBotMessageKeyIds, Refreshable {
    private View rootView;
    private Context context;
    private RecyclerView mRecycler;
    private EditText mEditText;
    private SearchMapsAdapter searchLocationAdapter;
    ArrayList<Result> suggestedLocationModels;

    String TAG = getClass().getSimpleName();

    public SearchMapsView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public SearchMapsView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SearchMapsView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    public boolean doRefresh() {
        if (mEditText != null) {
            mEditText.setText("");
            if (!mEditText.hasFocus())
                mEditText.requestFocus();
            return true;
        }
        return false;
    }


    Handler queryHandler = new Handler();
    Runnable queryRunnable = new Runnable() {
        @Override
        public void run() {
            if (mEditText != null)
                makeLocationRequest(mEditText.getText().toString());
        }
    };

    private void init(final Context context) {
        rootView = inflate(context, R.layout.keyboard_view_maps, null);
        ((ImageView) rootView.findViewById(R.id.contactIV)).setImageResource(R.drawable.ic_maps);
        this.context = context;
        this.mRecycler = (RecyclerView) rootView.findViewById(R.id.contactsRecycler);
        this.mRecycler.setLayoutManager(new LinearLayoutManager(context));
        this.mEditText = (EditText) rootView.findViewById(R.id.searchET);
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                queryHandler.removeCallbacksAndMessages(null);
                queryHandler.postDelayed(queryRunnable, 800);
            }
        });

        suggestedLocationModels = new ArrayList<>();

        searchLocationAdapter = new SearchMapsAdapter(suggestedLocationModels, context, new RecyclerViewClickInterface() {
            @Override
            public void onItemClick(int clickType, int extras, Object data) {
                Result locationModel = (Result) data;
//                EventBus.getDefault().post(new MessageEvent(SWITCH_TO_QWERTY, null));
//                EventBus.getDefault().post(new MessageEvent(BROADCAST_STRING_TO_CONNECTED_APPLICATION, locationModel.displayName));
                if (locationItemClickedListener != null)
                    locationItemClickedListener.onItemClicked(locationModel);
            }
        });
        mRecycler.setAdapter(searchLocationAdapter);
        mEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    InAppEditingController.getInstance().setEditText((EditText) v);
            }
        });
        this.addView(rootView);
        mEditText.requestFocus();
    }

    private View.OnFocusChangeListener focusChangeListener = new OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            Log.d(TAG, "onFocusChange: ");
        }
    };

    private LocationItemClickedListener locationItemClickedListener;

    public void setLocationItemClickedListener(LocationItemClickedListener locationItemClickedListener) {
        this.locationItemClickedListener = locationItemClickedListener;
    }

    public interface LocationItemClickedListener {
        void onItemClicked(Result locationModel);
    }

    @SuppressWarnings("deprecation")
    private void makeLocationRequest( String location) {
        List<NameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("address", location));
        RequestManager.makeGetRequest(getContext(), ServerRequestType.GOOGLE_MAPS_API, pairs, onRequestFinishCallback);
    }

    RequestManager.OnRequestFinishCallback onRequestFinishCallback = new RequestManager.OnRequestFinishCallback() {
        @Override
        public void onBindParams(boolean success, Object response) {
            JSONObject object = (JSONObject) response;
            JSONArray array = null;
            try {
                ArrayList<Result> results = new ArrayList<>();
                array = object.getJSONArray("results");

                for (int i = 0; i < array.length(); i++) {
                    Object apiResponse = array.get(i);
                    Result result = new Gson().fromJson(String.valueOf(apiResponse), Result.class);
                    results.add(result);
                }

                suggestedLocationModels.clear();
                suggestedLocationModels.addAll(results);
                if (searchLocationAdapter != null)
                    searchLocationAdapter.notifyDataSetChanged();
                Log.d(TAG, "onBindParams: " + results);

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        @Override
        public boolean isDestroyed() {
            return false;
        }
    };


}
