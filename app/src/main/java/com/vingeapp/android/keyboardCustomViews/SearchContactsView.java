package com.vingeapp.android.keyboardCustomViews;

import android.Manifest;
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
import android.widget.Toast;

import com.permissioneverywhere.PermissionEverywhere;
import com.permissioneverywhere.PermissionResponse;
import com.permissioneverywhere.PermissionResultCallback;

import com.vingeapp.android.R;
import com.vingeapp.android.adapters.SearchContactsAdapter;
import com.vingeapp.android.interfaces.GreenBotMessageKeyIds;
import com.vingeapp.android.interfaces.Refreshable;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by deepankursadana on 05/03/17.
 */

public class SearchContactsView extends FrameLayout implements GreenBotMessageKeyIds, Refreshable {
    private View rootView;
    private Context context;
    private RecyclerView mRecycler;
    private EditText mEditText;

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

    private void init(Context context) {
        rootView = inflate(context, R.layout.keyboard_view_search_contacts, null);
        this.context = context;
        this.mRecycler = (RecyclerView) rootView.findViewById(R.id.contactsRecycler);
        this.mRecycler.setLayoutManager(new LinearLayoutManager(context));
        this.mEditText = (EditText) rootView.findViewById(R.id.searchET);
        this.mEditText.addTextChangedListener(textWatcher);
        mEditText.setOnFocusChangeListener(focusChangeListener);
        SearchContactsAdapter searchContactsAdapter = new SearchContactsAdapter(null, context);
        mRecycler.setAdapter(searchContactsAdapter);
        this.addView(rootView);
//        mEditText.requestFocus();//todo

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

        }
    };

    @Override
    public boolean doRefresh() {
        if (mEditText != null && !mEditText.hasFocus()) {
            mEditText.requestFocus();
            return true;
        }
        return false;
    }


    final int REQ_CODE = 1;

    private void permissions(final Context context) {
        PermissionEverywhere.getPermission(getApplicationContext(),
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQ_CODE,
                "Notification title",
                "This app needs a write permission",
                R.mipmap.ic_launcher)
                .enqueue(new PermissionResultCallback() {
                    @Override
                    public void onComplete(PermissionResponse permissionResponse) {
                        Toast.makeText(context, "is Granted " + permissionResponse.isGranted(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
