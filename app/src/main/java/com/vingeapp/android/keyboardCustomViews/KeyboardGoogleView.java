package com.vingeapp.android.keyboardCustomViews;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.google.gson.Gson;
import com.vingeapp.android.MessageEvent;
import com.vingeapp.android.R;
import com.vingeapp.android.adapters.SearchMapsAdapter;
import com.vingeapp.android.adapters.SearchResultPreviewAdapter;
import com.vingeapp.android.apiHandling.RequestManager;
import com.vingeapp.android.apiHandling.ServerRequestType;
import com.vingeapp.android.interfaces.GreenBotMessageKeyIds;
import com.vingeapp.android.interfaces.Refreshable;
import com.vingeapp.android.interfaces.View_State;
import com.vingeapp.android.serverGoogleSearchResponse.Link;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by deepankursadana on 02/04/17.
 */

public class KeyboardGoogleView extends FrameLayout implements GreenBotMessageKeyIds, Refreshable {
    private View rootView;
    private Context context;
    private RecyclerView mRecycler;
    private EditText mEditText;
    private SearchResultPreviewAdapter searchLocationAdapter;
    ArrayList<Link> recommendedLinks;

    String TAG = getClass().getSimpleName();

    public KeyboardGoogleView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public KeyboardGoogleView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public KeyboardGoogleView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
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


    GoogleVerticalListSearchView searchView;
    View horizontalResultListView;

    private void init(final Context context) {
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        this.setBackgroundColor(Color.WHITE);
        searchView = getSearchView(context);

        horizontalResultListView = inflate(context, R.layout.keyboard_view_google_search, null);
        mRecycler = (RecyclerView) horizontalResultListView.findViewById(R.id.recyclerView);
        this.addView(horizontalResultListView);
        this.addView(searchView);

        horizontalResultListView.findViewById(R.id.backIV).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleViews(false);
            }
        });

    }


    private OnFocusChangeListener focusChangeListener = new OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            Log.d(TAG, "onFocusChange: ");
        }
    };


    private GoogleVerticalListSearchView getSearchView(Context context) {
        GoogleVerticalListSearchView searchGoogleView = new GoogleVerticalListSearchView(context);

        searchGoogleView.setFinalQueryListener(new GoogleVerticalListSearchView.FinalQueryListener() {
            @Override
            public void onFinalQuerySubmitted(String query) {
                toggleViews(true);
                makeWebSearchRequest(query);
            }
        });
        return searchGoogleView;
    }

    private void toggleViews(boolean hideSearchView) {
        if (hideSearchView) {
            horizontalResultListView.setVisibility(VISIBLE);
            searchView.setVisibility(GONE);
            EventBus.getDefault().post(new MessageEvent(ON_IN_APP_EDITING_FINISHED, null));

        } else {
            horizontalResultListView.setVisibility(GONE);
            searchView.setVisibility(VISIBLE);
            searchView.doRefresh();
            EventBus.getDefault().post(new MessageEvent(POPUP_KEYBOARD_FOR_IN_APP_EDITING, null));

        }
        mCurrentViewState = hideSearchView ? View_State.NORMAL : View_State.SEARCH;
    }

    private View_State mCurrentViewState = View_State.SEARCH;

    public View_State getCurrentViewState() {
        return mCurrentViewState;
    }


    @SuppressWarnings("deprecation")
    private void makeWebSearchRequest(String query) {
        List<NameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("query", query));
        RequestManager.makeGetRequest(getContext(), ServerRequestType.WEB_SEARCH, pairs, onRequestFinishCallback);
    }

    RequestManager.OnRequestFinishCallback onRequestFinishCallback = new RequestManager.OnRequestFinishCallback() {
        @Override
        public void onBindParams(boolean success, Object response) {
            Log.d(TAG, "onBindParams: " + response);
            JSONObject object = (JSONObject) response;
            JSONArray array;
            try {
                ArrayList<Link> results = new ArrayList<>();
                array = object.getJSONObject("result").getJSONArray("links");

                for (int i = 0; i < array.length(); i++) {
                    Object apiResponse = array.get(i);
                    Link link = new Gson().fromJson(String.valueOf(apiResponse), Link.class);
                    results.add(link);
                }

                if (recommendedLinks == null)
                    recommendedLinks = new ArrayList<>();
                else
                    recommendedLinks.clear();
                recommendedLinks.addAll(results);
                registerResponseToMap(response);
                Log.d(TAG, "onBindParams: " + results);
                initRecycler(recommendedLinks);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        @Override
        public boolean isDestroyed() {
            return false;
        }
    };

    private static final HashMap<String, org.json.JSONObject> cachedResponses = new HashMap<>();

    private void registerResponseToMap(Object response) {
        JSONObject object = (JSONObject) response;
        try {
            String query = object.getJSONObject("result").getString("query");
            cachedResponses.put(query, object);
        } catch (JSONException e) {


        }

    }

    private void initRecycler(ArrayList<Link> linkArrayList) {
        mRecycler.setAdapter(new SearchResultPreviewAdapter(getContext(),linkArrayList));
        mRecycler.setLayoutManager(new GridLayoutManager(getContext(),3));
//        if (searchLocationAdapter != null)
//            searchLocationAdapter.notifyDataSetChanged();
    }
}
