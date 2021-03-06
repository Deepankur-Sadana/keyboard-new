package com.vingeapp.android.keyboardCustomViews;

import android.content.Context;
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
import com.vingeapp.android.interfaces.Refreshable;
import com.vingeapp.android.models.ContactsModel;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * Created by deepankursadana on 05/03/17.
 */

public class SearchContactsView extends FrameLayout implements GreenBotMessageKeyIds, Refreshable {
    private View rootView;
    private Context context;
    private RecyclerView mRecycler;
    private EditText mEditText;
    private SearchContactsAdapter searchContactsAdapter;
    ArrayList<ContactsModel> allContacts;



    public SearchContactsView(Context context) {
        super(context);
        init(context);
    }

    public SearchContactsView(Context context, AttributeSet attrs) {




        super(context, attrs);
        init(context);
    }

    public SearchContactsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(final Context context) {
        rootView = inflate(context, R.layout.keyboard_view_search_list, null);
        this.context = context;
        this.mRecycler = (RecyclerView) rootView.findViewById(R.id.contactsRecycler);
        this.mRecycler.setLayoutManager(new LinearLayoutManager(context));
        this.mEditText = (EditText) rootView.findViewById(R.id.searchET);
        this.mEditText.addTextChangedListener(textWatcher);
        mEditText.setOnFocusChangeListener(focusChangeListener);

        long t1 = System.currentTimeMillis();
        if (allContacts == null || allContacts.size() == 0) {
            ContactFetcher fetcher = new ContactFetcher();
            fetcher.loadContactsInBackground(context, new ContactFetcher.ContactListener() {
                @Override
                public void onListLoaded(ArrayList<ContactsModel> contactsModels) {
                    allContacts = contactsModels;
                    searchContactsAdapter = new SearchContactsAdapter(allContacts, context, new RecyclerViewClickInterface() {
                        @Override
                        public void onItemClick(int clickType, int extras, Object data) {
                            ContactsModel contactsModel = (ContactsModel) data;
                            EventBus.getDefault().post(new MessageEvent(SWITCH_TO_QWERTY, null));
                            EventBus.getDefault().post(new MessageEvent(BROADCAST_STRING_TO_CONNECTED_APPLICATION, contactsModel.name + " " + contactsModel.number));
                        }
                    });
                    mRecycler.setAdapter(searchContactsAdapter);

                }
            });
        }

        mEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    InAppEditingController.getInstance().setEditText((EditText) v);
            }
        });

        Log.d(TAG, "init: fetching contacts took " + (System.currentTimeMillis() - t1));
        this.addView(rootView);
        mEditText.requestFocus();

    }


    private final String TAG = getClass().getSimpleName();
    private View.OnFocusChangeListener focusChangeListener = new OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            Log.d(TAG, "onFocusChange: ");
        }
    };
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (filteredList == null) filteredList = new ArrayList<>();
            else filteredList.clear();

            if (allContacts != null)
                for (ContactsModel model : allContacts) {

                    if (model.name != null && model.name.toLowerCase().contains(s.toString().trim().toLowerCase()))
                        filteredList.add(model);
                }

            if (searchContactsAdapter != null)
                searchContactsAdapter.setContactList(filteredList);

        }
    };

    ArrayList<ContactsModel> filteredList;

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
}
