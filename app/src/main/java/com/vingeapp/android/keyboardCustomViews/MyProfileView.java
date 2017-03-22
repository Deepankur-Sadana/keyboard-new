package com.vingeapp.android.keyboardCustomViews;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.vingeapp.android.InAppEditingController;
import com.vingeapp.android.R;
import com.vingeapp.android.models.InformationViewModel;

/**
 * Created by divisha on 3/19/17.
 */

public class MyProfileView extends FrameLayout {
    private Context context;
    private static final String TAG = MyProfileView.class.getSimpleName();


    private InformationViewModel informationViewModel;


    public MyProfileView(Context context) {
        super(context);
        init(context);
    }

    public MyProfileView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyProfileView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MyProfileView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private EditText nameEt, phonenumberEt, emailEt;

    private void init(final Context context) {

        View rootView = inflate(context, R.layout.information, null);
        rootView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        this.removeAllViews();
        this.addView(rootView);


        nameEt = (EditText) rootView.findViewById(R.id.nameTV);
        phonenumberEt = (EditText) rootView.findViewById(R.id.phonenumberTV);
        emailEt = (EditText) rootView.findViewById(R.id.emailTV);

        nameEt.setOnFocusChangeListener(focusChangeListener);
        phonenumberEt.setOnFocusChangeListener(focusChangeListener);
        emailEt.setOnFocusChangeListener(focusChangeListener);


        nameEt.requestFocus();
        nameEt.setOnEditorActionListener(editorActionListener);
        phonenumberEt.setOnEditorActionListener(editorActionListener);
        emailEt.setOnEditorActionListener(editorActionListener);


    }

    EditText.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            Log.d(TAG, "onEditorAction: " + actionId);

            if (actionId == EditorInfo.IME_ACTION_DONE) {
                //do here your stuff f
                return true;
            }
            return false;
        }
    };

    private View.OnFocusChangeListener focusChangeListener = new OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            InAppEditingController.getInstance().setEditText((EditText) v);
            switch (v.getId()) {
                case R.id.nameTV:
                    Log.d(TAG, "onFocusChange: name " + hasFocus);
                    break;
                case R.id.phonenumberTV:
                    Log.d(TAG, "onFocusChange: " + "phonenumber " + hasFocus);
                    break;
                case R.id.emailTV:
                    break;

            }
        }
    };
    private View.OnKeyListener onKeyListener = new OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            Log.d(TAG, "onKey: keyCode " + keyCode + " event " + event + " view " + v);
            return false;
        }
    };


}



