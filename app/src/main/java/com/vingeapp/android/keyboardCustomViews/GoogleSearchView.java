package com.vingeapp.android.keyboardCustomViews;

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

import com.vingeapp.android.ContactFetcher;
import com.vingeapp.android.InAppEditingController;
import com.vingeapp.android.MessageEvent;
import com.vingeapp.android.R;
import com.vingeapp.android.adapters.SearchContactsAdapter;
import com.vingeapp.android.interfaces.GreenBotMessageKeyIds;
import com.vingeapp.android.interfaces.RecyclerViewClickInterface;
import com.vingeapp.android.interfaces.View_State;
import com.vingeapp.android.models.ContactsModel;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * Created by deepankursadana on 27/06/17.
 */

public class GoogleSearchView extends FrameLayout implements GreenBotMessageKeyIds {


    private View rootView;
    private Context context;
    private RecyclerView mRecycler;
    private EditText mEditText;
    private final String TAG = getClass().getSimpleName();


    ArrayList<ContactsModel> allContacts;
    private View_State currentViewState= View_State.SEARCH;;

    public GoogleSearchView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public GoogleSearchView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GoogleSearchView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(final Context context) {
        rootView = inflate(context, R.layout.keyboard_view_google_search, null);
        mRecycler = (RecyclerView) rootView.findViewById(R.id.searchResultRecycler);
        this.context = context;


    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {


        }
    };

    public View_State getCurrentViewState() {
        return currentViewState;
    }
}
