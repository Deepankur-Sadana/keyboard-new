package com.vingeapp.android;

import android.util.Log;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;

import de.greenrobot.event.EventBus;
import com.vingeapp.android.interfaces.GreenBotMessageKeyIds;

/**
 * Created by deepankursadana on 25/02/17.
 */
public class InAppEditingController implements GreenBotMessageKeyIds {
    private static InAppEditingController ourInstance;
    private final static String TAG = InAppEditingController.class.getSimpleName();
    private InputConnection editTextInputConnection;

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
        editTextInputConnection = new CustomInputConnection(editText);
        EventBus.getDefault().post(new MessageEvent(EDIT_TEXT_FOCUS_CHANGED, editText));

    }

    public void onKey(int primaryCode, int[] keyCodes) {
        Log.d(TAG, "onKey: feeding " + primaryCode + " to: " + editText);
    }

    private void feedKey(int primaryCode, EditText editText) {
        String previousText = editText.getText().toString();
    }

    public InputConnection getEditTextInputConnection() {
        return editTextInputConnection;
    }
}
