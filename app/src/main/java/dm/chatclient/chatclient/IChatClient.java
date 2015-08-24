package dm.chatclient.chatclient;

import dm.chatclient.chatclient.notifier.IChatClientNotifier;
import dm.chatclient.model.BaseUser;
import dm.chatclient.model.Contact;
import dm.chatclient.model.Message;
import dm.chatclient.model.User;

/**
 * Created by Ice on 8/9/2015.
 */
public interface IChatClient
{
    void setServer(String address, int port);

    void login(String userName, String password, BaseUser.USER_STATE state);

    void disconnect();
    void requestContacts();

    void sendMessage(Message message);

    void addListener(IChatClientNotifier notifier);

    void removeListener(IChatClientNotifier notifier);
    void addContact(String userName);
    void removeContact(int contactId);

    void changeState(BaseUser.USER_STATE state);

    void registerUser(User user);
    void updateUser(User user);
}
