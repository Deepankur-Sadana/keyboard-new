package deepankur.com.keyboardapp.keyboardCustomViews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import de.greenrobot.event.EventBus;
import deepankur.com.keyboardapp.InAppEditingController;
import deepankur.com.keyboardapp.MessageEvent;
import deepankur.com.keyboardapp.R;
import deepankur.com.keyboardapp.cache.ClipBoardCache;
import deepankur.com.keyboardapp.interfaces.GreenBotMessageKeyIds;
import deepankur.com.keyboardapp.interfaces.Reachable;
import deepankur.com.keyboardapp.models.ClipBoardItemModel;
import utils.AppLibrary;

/**
 * Created by deepankursadana on 21/02/17.
 */

public class AddEditClipboardItemView extends FrameLayout implements Reachable, GreenBotMessageKeyIds {

    private Context context;
    private static final String TAG = AddEditClipboardItemView.class.getSimpleName();

    public enum ActionType {
        ADD, EDIT
    }

    private ActionType actionType;
    private ClipBoardItemModel clipBoardItemModel;

    public AddEditClipboardItemView(Context context, ActionType actionType, @Nullable ClipBoardItemModel clipBoardItemModel) {
        super(context);
        this.actionType = actionType;
        this.clipBoardItemModel = clipBoardItemModel;
        init(context);
    }

    public AddEditClipboardItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AddEditClipboardItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private EditText titleEt, descriptionEt;

    private void init(Context context) {
        EventBus.getDefault().register(this);

        View rootView = inflate(context, R.layout.clipboard_add_item, null);
        rootView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {//intercepting touch on the background of the title and description views
                return true;
            }
        });
        this.removeAllViews();
        this.addView(rootView);

        titleEt = (EditText) rootView.findViewById(R.id.titleTV);
        descriptionEt = (EditText) rootView.findViewById(R.id.descriptionTV);

        titleEt.setOnFocusChangeListener(focusChangeListener);
        descriptionEt.setOnFocusChangeListener(focusChangeListener);
//        titleEt.setOnKeyListener(onKeyListener);
//        descriptionEt.setOnKeyListener(onKeyListener);

        rootView.findViewById(R.id.saveTV).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(titleEt.getText().toString()) && !TextUtils.isEmpty(descriptionEt.getText().toString())) {
                    if (actionType == ActionType.EDIT) {
                        clipBoardItemModel.setTitle(titleEt.getText().toString());
                        clipBoardItemModel.setDescription(descriptionEt.getText().toString());
                        ClipBoardCache.getInstance().update(clipBoardItemModel);
                    } else if (actionType == ActionType.ADD) {
                        ClipBoardItemModel clipBoardItemModel = new ClipBoardItemModel();
                        clipBoardItemModel.setDescription(descriptionEt.getText().toString());
                        clipBoardItemModel.setTitle(titleEt.getText().toString());
                        ClipBoardCache.getInstance().addItem(clipBoardItemModel);
                    }
                    ((FrameLayout) AddEditClipboardItemView.this.getParent()).removeView(AddEditClipboardItemView.this);
                    EventBus.getDefault().post(new MessageEvent(ON_IN_APP_EDITING_FINISHED, null));
                } else {
                    AppLibrary.showShortToast("PLease fill the details");
                }
            }
        });


        if (actionType == ActionType.EDIT) {
            titleEt.setText(clipBoardItemModel.getTitle());
            descriptionEt.setText(clipBoardItemModel.getDescription());

            rootView.findViewById(R.id.deleteTV).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        } else rootView.findViewById(R.id.deleteTV).setVisibility(GONE);


        titleEt.requestFocus();
        titleEt.setOnEditorActionListener(editorActionListener);
        descriptionEt.setOnEditorActionListener(editorActionListener);
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
            EventBus.getDefault().post(new MessageEvent(EDIT_TEXT_FOCUS_CHANGED, v));
            switch (v.getId()) {
                case R.id.titleTV:
                    Log.d(TAG, "onFocusChange: title " + hasFocus);
                    break;
                case R.id.descriptionTV:
                    Log.d(TAG, "onFocusChange: description " + hasFocus);
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

    @SuppressLint("SetTextI18n")
    @Override
    public boolean onEvent(MessageEvent messageEvent) {
        return false;
    }

    public void setACTION_TYPE(ActionType action_type) {
        Log.d(TAG, "setACTION_TYPE: " + action_type);
        this.actionType = action_type;
    }

}
