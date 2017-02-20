package deepankur.com.keyboardapp.interfaces;

/**
 * Created by deepankur on 14/4/16.
 */
//this interface doesn't take just clicks but is used for general commmunication
public interface RecyclerViewClickInterface {
    int CLICK_TYPE_NORMAL = 0, CLICK_TYPE_LONG_PRESS = 1;

    //in most cases we will be only dealing with third parameter ie. data
    //first parameter is mostly ignored and assumed to be single clicks except in few cases
    // and implies either tap or long press

    //author must take the responsibility of documenting the  second parameter of the implementation
    void onItemClick(int clickType, int extras, Object data);

}
