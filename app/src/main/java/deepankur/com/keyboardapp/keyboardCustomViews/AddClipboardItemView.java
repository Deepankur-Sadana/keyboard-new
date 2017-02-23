package deepankur.com.keyboardapp.keyboardCustomViews;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import deepankur.com.keyboardapp.R;

/**
 * Created by deepankursadana on 21/02/17.
 */

public class AddClipboardItemView extends FrameLayout {

    private Context context;
    static final String TAG = AddClipboardItemView.class.getSimpleName();

    public AddClipboardItemView(Context context) {
        super(context);
        init(context);
    }

    public AddClipboardItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AddClipboardItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    TextView titleTv, descriptionTv;

    private void init(Context context) {
        View rootView = inflate(context, R.layout.clipboard_add_item, null);
        this.removeAllViews();
        this.addView(rootView);
        titleTv = (TextView) rootView.findViewById(R.id.titleTV);
        descriptionTv = (TextView) rootView.findViewById(R.id.descriptionTV);
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

    public interface AddClipboardItemListener {
        void onItemAdded(String title, String description);
    }
}
