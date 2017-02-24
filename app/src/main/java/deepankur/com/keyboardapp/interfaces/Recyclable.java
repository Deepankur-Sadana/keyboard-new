package deepankur.com.keyboardapp.interfaces;

/**
 * Created by deepankursadana on 25/02/17.
 */

/**
 * A view is Recyclable if it can pause and resume multiple times instead of
 * being just created and destroyed
 */
public interface Recyclable {
    void onRestInBackground();
}
