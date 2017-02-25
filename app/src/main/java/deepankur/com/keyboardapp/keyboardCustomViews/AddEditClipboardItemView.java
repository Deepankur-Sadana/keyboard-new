package deepankur.com.keyboardapp.keyboardCustomViews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
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

/**
 * Created by deepankursadana on 21/02/17.
 */

public class AddEditClipboardItemView extends FrameLayout implements Reachable, GreenBotMessageKeyIds {

    private Context context;
    private static final String TAG = AddEditClipboardItemView.class.getSimpleName();
    private int ACTION_TYPE;
    public static final int ADD = 0, EDIT = 1;
    private ClipBoardItemModel clipBoardItemModel;

    public AddEditClipboardItemView(Context context) {
        super(context);
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
        this.removeAllViews();
        this.addView(rootView);

        titleEt = (EditText) rootView.findViewById(R.id.titleTV);
        descriptionEt = (EditText) rootView.findViewById(R.id.descriptionTV);

        titleEt.setOnFocusChangeListener(focusChangeListener);
        descriptionEt.setOnFocusChangeListener(focusChangeListener);

        rootView.findViewById(R.id.saveTV).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(titleEt.getText().toString()) && !TextUtils.isEmpty(descriptionEt.getText().toString())) {
                    if (ACTION_TYPE == EDIT) {
                        ClipBoardCache.getInstance().update(clipBoardItemModel);
                    } else if (ACTION_TYPE == ADD) {
                        ClipBoardItemModel clipBoardItemModel = new ClipBoardItemModel();
                        clipBoardItemModel.setNote(descriptionEt.getText().toString());
                        clipBoardItemModel.setTitle(titleEt.getText().toString());
                        ClipBoardCache.getInstance().addItem(clipBoardItemModel);
                    }
                    ((FrameLayout) AddEditClipboardItemView.this.getParent()).removeView(AddEditClipboardItemView.this);
                    EventBus.getDefault().post(new MessageEvent(ON_IN_APP_EDITING_FINISHED, null));
                }
            }
        });
        if (ACTION_TYPE == EDIT) {
            titleEt.setText(clipBoardItemModel.getTitle());
            descriptionEt.setText(clipBoardItemModel.getNote());
        }
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

    @SuppressLint("SetTextI18n")
    @Override
    public boolean onEvent(MessageEvent messageEvent) {
        Log.d(TAG, "onEvent: ");
//        Object message = messageEvent.getMessage();
//        String s = (String) message;
//        if (titleEt.hasFocus()) {
//            titleEt.setText(titleEt.getText() + s);
//        } else if (descriptionEt.hasFocus()) {
//            descriptionEt.setText(titleEt.getText() + s);
//        }
        return false;
    }

    public void setACTION_TYPE(int ACTION_TYPE) {
        this.ACTION_TYPE = ACTION_TYPE;
    }

    public void setClipBoardItemModel(ClipBoardItemModel clipBoardItemModel) {
        this.clipBoardItemModel = clipBoardItemModel;
    }
}
