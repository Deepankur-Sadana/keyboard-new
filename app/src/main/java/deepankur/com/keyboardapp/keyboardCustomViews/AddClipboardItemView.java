package deepankur.com.keyboardapp.keyboardCustomViews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import deepankur.com.keyboardapp.R;

/**
 * Created by deepankursadana on 21/02/17.
 */

public class AddClipboardItemView extends FrameLayout {

    private Context context;
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

    private void init(Context context) {
        View view = inflate(context, R.layout.clipboard_add_item, null);
        this.removeAllViews();
        this.addView(view);
    }

    public interface AddClipboardItemListener{
        void onItemAdded(String title, String description);
    }
}
