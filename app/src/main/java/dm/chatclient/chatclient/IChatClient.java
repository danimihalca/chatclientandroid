package dm.chatclient.chatclient;

import dm.chatclient.chatclient.notifier.IChatClientNotifier;
import dm.chatclient.model.Message;

/**
 * Created by Ice on 8/9/2015.
 */
public interface IChatClient
{
    void connect(String address, int port);

    void login(String userName, String password);

    void disconnect();
    void requestContacts();

    void sendMessage(Message message);

    void addListener(IChatClientNotifier m_notifier);

    void removeListener(IChatClientNotifier m_notifier);

}
