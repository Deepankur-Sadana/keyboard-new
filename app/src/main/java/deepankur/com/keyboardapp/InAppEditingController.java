package deepankur.com.keyboardapp;

import android.widget.EditText;

/**
 * Created by deepankursadana on 25/02/17.
 */
public class InAppEditingController {
    private static InAppEditingController ourInstance;

    public static InAppEditingController getInstance() {
        if (ourInstance == null)
            ourInstance = new InAppEditingController();
        return ourInstance;
    }

    private static EditText editText;
    public static void setEditText(EditText editText){
        InAppEditingController.editText = editText;
    }
    private InAppEditingController() {
    }
}
