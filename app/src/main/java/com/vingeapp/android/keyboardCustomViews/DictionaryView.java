package com.vingeapp.android.keyboardCustomViews;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.vingeapp.android.R;
import com.vingeapp.android.interfaces.GreenBotMessageKeyIds;
import com.vingeapp.android.interfaces.Refreshable;

/**
 * Created by divisha on 5/15/17.
 */


public class DictionaryView extends FrameLayout implements GreenBotMessageKeyIds,Refreshable {
    private EditText mEditText;
    private View rootView;
    private Context context;
    private RecyclerView mRecycler;
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


    private View.OnFocusChangeListener focusChangeListener = new OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {

        }
    };


    public DictionaryView(@NonNull Context context) {
        super(context);
        init(context);
    }

    private void init(final Context context) {
        rootView = inflate(context, R.layout.keyboard_view_search_list1, null);
        this.context = context;
        this.mRecycler = (RecyclerView) rootView.findViewById(R.id.dictionaryRecycler);
        this.mRecycler.setLayoutManager(new LinearLayoutManager(context));
        this.mEditText = (EditText) rootView.findViewById(R.id.searchET);
        this.mEditText.addTextChangedListener(textWatcher);
        mEditText.setOnFocusChangeListener(focusChangeListener);
    }

    public DictionaryView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DictionaryView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
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
}
