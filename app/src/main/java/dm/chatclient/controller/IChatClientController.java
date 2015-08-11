package dm.chatclient.controller;

import dm.chatclient.chatclient.listener.ILoginListener;
import dm.chatclient.chatclient.listener.IRuntimeListener;
import dm.chatclient.model.Contact;
import dm.chatclient.model.Message;
import dm.chatclient.model.User;

import java.util.List;

public interface IChatClientController
{
    void addRuntimeListener(IRuntimeListener listener);
    void removeRuntimeListener(IRuntimeListener listener);
    void setLoginListener(ILoginListener listener);

    void requestContacts();

    void connect(String address, int port);
    void login(String userName, String password);

    void disconnect();

    void sendMessage(Message message);

    void addReceivedMessage(Message message);

    List<Message> getMessages(Contact contact);

    List<Contact> getContacts();

    void setContacts(List<Contact> contacts);
    Contact getContact(int contactId);

    void clearMessages();
    void clearContacts();

    User getUser();
    void setUser(User user);
}
