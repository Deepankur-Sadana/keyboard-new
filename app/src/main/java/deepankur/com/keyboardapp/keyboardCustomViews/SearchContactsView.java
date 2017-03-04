package deepankur.com.keyboardapp.keyboardCustomViews;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import deepankur.com.keyboardapp.R;
import deepankur.com.keyboardapp.adapters.SearchContactsAdapter;

/**
 * Created by deepankursadana on 05/03/17.
 */

public class SearchContactsView extends FrameLayout {
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
        this.addView(rootView);
        this.context = context;
        this.mRecycler = (RecyclerView) rootView.findViewById(R.id.contactsRecycler);
        this.mRecycler.setLayoutManager(new LinearLayoutManager(context));
        this.mEditText = (EditText) rootView.findViewById(R.id.searchET);
        this.mEditText.addTextChangedListener(textWatcher);
        SearchContactsAdapter searchContactsAdapter = new SearchContactsAdapter(null, context);
        mRecycler.setAdapter(searchContactsAdapter);
    }

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
}
