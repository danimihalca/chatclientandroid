package dm.chatclient.chatclient.listener;

import dm.chatclient.chatclient.notifier.IChatClientNotifier;
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

    void onRemovedByContact(Contact contact);
    void onAddContactResponse(String userName,  IChatClientNotifier.ADD_REQUEST_STATUS status);
    boolean onAddingByContact(String requester);


}
