package deepankur.com.keyboardapp.keyboardCustomViews;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import deepankur.com.keyboardapp.MessageEvent;
import deepankur.com.keyboardapp.R;
import deepankur.com.keyboardapp.cache.ClipBoardCache;
import deepankur.com.keyboardapp.interfaces.Reachable;
import deepankur.com.keyboardapp.models.ClipBoardItemModel;

/**
 * Created by deepankursadana on 21/02/17.
 */

public class AddEditClipboardItemView extends FrameLayout implements Reachable {

    private Context context;
    static final String TAG = AddEditClipboardItemView.class.getSimpleName();
    int ACTION_TYPE;
    final int ADD = 0, EDIT = 1;
    ClipBoardItemModel clipBoardItemModel;

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
                        ClipBoardCache.getInstance().addItem(clipBoardItemModel);
                    }
                    ((FrameLayout) AddEditClipboardItemView.this.getParent()).removeView(AddEditClipboardItemView.this);
                }
            }
        });
        if (ACTION_TYPE == EDIT) {
            titleEt.setText(clipBoardItemModel.getTitle());
            descriptionEt.setText(clipBoardItemModel.getNote());
        }
    }

    private View.OnFocusChangeListener focusChangeListener = new OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
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

    @Override
    public boolean onEvent(MessageEvent messageEvent) {
        Log.d(TAG, "onEvent: ");
        Object message = messageEvent.getMessage();
        String s = (String) message;
        if (titleEt.hasFocus()) {
            titleEt.setText(titleEt.getText() + s);
        } else if (descriptionEt.hasFocus()) {
            descriptionEt.setText(titleEt.getText() + s);
        }
        return false;
    }
}
