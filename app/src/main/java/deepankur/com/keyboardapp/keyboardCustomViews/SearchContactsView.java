package deepankur.com.keyboardapp.keyboardCustomViews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by deepankursadana on 05/03/17.
 */

public class SearchContactsView extends FrameLayout {
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

    }
}
