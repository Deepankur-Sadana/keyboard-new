package com.vingeapp.android.keyboardCustomViews;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.vingeapp.android.InAppEditingController;
import com.vingeapp.android.R;
import com.vingeapp.android.interfaces.GreenBotMessageKeyIds;
import com.vingeapp.android.interfaces.Refreshable;
import com.vingeapp.android.models.ContactsModel;

import java.util.ArrayList;

/**
 * Created by deepankursadana on 27/06/17.
 */

public class GoogleVerticalListSearchView extends FrameLayout implements GreenBotMessageKeyIds,Refreshable {


    private View rootView;
    private Context context;
    private RecyclerView mRecycler;
    private EditText mEditText;
    private final String TAG = getClass().getSimpleName();


    ArrayList<ContactsModel> allContacts;
    private FinalQueryListener finalQueryListener;

    public GoogleVerticalListSearchView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public GoogleVerticalListSearchView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GoogleVerticalListSearchView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
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
    private void init(final Context context) {
        rootView = inflate(context, R.layout.google_verticall_search_list, null);
//        mRecycler = (RecyclerView) rootView.findViewById(R.id.searchResultRecycler);
        this.context = context;


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

        mEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    InAppEditingController.getInstance().setEditText((EditText) v);
            }
        });
        mEditText.requestFocus();

        rootView.findViewById(R.id.queryDone).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalQueryListener !=null)
                    finalQueryListener.onFinalQuerySubmitted(mEditText.getText().toString());
            }
        });

        this.addView(rootView);
    }

    Handler queryHandler = new Handler();
    Runnable queryRunnable = new Runnable() {
        @Override
        public void run() {
//            if (mEditText != null)
//                makeLocationRequest(mEditText.getText().toString());
        }
    };

    //// TODO: 03/07/17 for auto complete
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



    interface FinalQueryListener {
        void onFinalQuerySubmitted(String query);
    }


    public void setFinalQueryListener(FinalQueryListener finalQueryListener) {
        this.finalQueryListener = finalQueryListener;
    }


}
