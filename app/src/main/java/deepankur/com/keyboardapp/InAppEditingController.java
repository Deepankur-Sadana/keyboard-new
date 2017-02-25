package deepankur.com.keyboardapp;

import android.util.Log;
import android.widget.EditText;

/**
 * Created by deepankursadana on 25/02/17.
 */
public class InAppEditingController {
    private static InAppEditingController ourInstance;
    private final static String TAG = InAppEditingController.class.getSimpleName();

    private InAppEditingController() {
    }

    public static InAppEditingController getInstance() {
        if (ourInstance == null)
            ourInstance = new InAppEditingController();
        return ourInstance;
    }

    private EditText editText;

    public void setEditText(EditText editText) {
        this.editText = editText;
    }

    public void onKey(int primaryCode, int[] keyCodes) {
        Log.d(TAG, "onKey: feeding " + primaryCode + " to: " + editText);
    }

    private void feedKey(int primaryCode,EditText editText) {
        String previousText = editText.getText().toString();
    }
}
