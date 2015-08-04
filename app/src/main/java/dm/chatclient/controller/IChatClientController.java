package dm.chatclient.controller;

import dm.chatclient.model.Contact;
import dm.chatclient.model.Message;

import java.util.List;

public interface IChatClientController
{
    void addListener(IChatClientListener listener);
    void removeListener(IChatClientListener listener);
    Contact getContact(int id);

    void setServerProperties(String address, int port);
    void login(String username, String password);
    void disconnect();
    void sendMessage(int receiverId, String message);
    void requestContacts();

    void notifyOnContactUpdated(Contact contact);

    List<Message> getMessages(Contact sender);
}
