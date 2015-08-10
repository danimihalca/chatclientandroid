package dm.chatclient.chatclient.listener;

import dm.chatclient.model.Contact;
import dm.chatclient.model.Message;

/**
 * Created by Ice on 8/9/2015.
 */
public interface IRuntimeListener extends IBaseListener
{
    void onContactsReceived();
    void onContactStatusChanged(Contact contact);
    void onMessageReceived(Message message);
}
