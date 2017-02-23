package deepankur.com.keyboardapp.interfaces;

import deepankur.com.keyboardapp.MessageEvent;

/**
 * Created by deepankursadana on 23/02/17.
 */

/**
 * A class that can listen to the messages by :--
 * {@link de.greenrobot.event.EventBus}
 * is declared as reachable
 */
public interface Reachable {
    /**
     * @param messageEvent the key-value message
     * @return true if it wants to consume the event false otherwise
     */
    boolean onEvent(MessageEvent messageEvent);
}
