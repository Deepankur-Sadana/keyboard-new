package com.vingeapp.android.keyboardCustomViews.maps;

import android.content.Context;
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

import com.vingeapp.android.InAppEditingController;
import com.vingeapp.android.R;
import com.vingeapp.android.adapters.SearchMapsAdapter;
import com.vingeapp.android.interfaces.GreenBotMessageKeyIds;
import com.vingeapp.android.interfaces.RecyclerViewClickInterface;
import com.vingeapp.android.interfaces.Refreshable;
import com.vingeapp.android.models.LocationModel;

import java.util.ArrayList;

/**
 * Created by deepankursadana on 02/04/17.
 */

public class SearchMapsView extends FrameLayout implements GreenBotMessageKeyIds, Refreshable {
    private View rootView;
    private Context context;
    private RecyclerView mRecycler;
    private EditText mEditText;
    private SearchMapsAdapter searchLocationAdapter;
    ArrayList<LocationModel> suggestedLocationModels;

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


    private void notifyEditTextContentChanged(String newText) {
        if (suggestedLocationModels != null && suggestedLocationModels.size() >= 1) {
            suggestedLocationModels.get(0).displayName = newText;
            if (searchLocationAdapter != null)
                searchLocationAdapter.notifyItemChanged(0);
        }
    }

    private void init(final Context context) {
        rootView = inflate(context, R.layout.keyboard_view_search_list, null);
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
                notifyEditTextContentChanged(s.toString());
            }
        });

        suggestedLocationModels = new ArrayList<>();
        suggestedLocationModels.add(new LocationModel());//addind zeroeth item ourselves
        searchLocationAdapter = new SearchMapsAdapter(suggestedLocationModels, context, new RecyclerViewClickInterface() {
            @Override
            public void onItemClick(int clickType, int extras, Object data) {
                LocationModel locationModel = (LocationModel) data;
//                EventBus.getDefault().post(new MessageEvent(SWITCH_TO_QWERTY, null));
//                EventBus.getDefault().post(new MessageEvent(ON_CLIPBOARD_ITEM_SELECTED, locationModel.displayName));
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
        void onItemClicked(LocationModel locationModel);
    }


}
