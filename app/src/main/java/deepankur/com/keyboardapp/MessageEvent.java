package deepankur.com.keyboardapp;

/**
 * Created by deepankursadana on 23/02/17.
 */

public class MessageEvent {
    /* Additional fields if needed */
    private int messageType;
    private Object message;

    public int getMessageType() {
        return messageType;
    }

    public Object getMessage() {
        return message;
    }

    public MessageEvent(int messageType, Object message) {
        this.messageType = messageType;
        this.message = message;
    }
}
