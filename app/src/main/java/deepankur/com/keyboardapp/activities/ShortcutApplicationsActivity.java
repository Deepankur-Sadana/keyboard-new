package deepankur.com.keyboardapp.activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import deepankur.com.keyboardapp.R;
import deepankur.com.keyboardapp.adapters.AllApplicationsListAdapter;
import deepankur.com.keyboardapp.interfaces.RecyclerViewClickInterface;


/**
 * Created by deepankur on 2/6/17.
 */

public class ShortcutApplicationsActivity extends BaseActivity {
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        super.setContentView(R.layout.activity_select_applications);

    }

    @Override
    protected void onResume() {
        super.onResume();
        AllApplicationsListAdapter adapter = new AllApplicationsListAdapter(this, new RecyclerViewClickInterface() {
            @Override
            public void onItemClick(int extras, Object data) {


            }
        });
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.selectApplicationRecycler);
        recyclerView.setAdapter(adapter);

    }
}
