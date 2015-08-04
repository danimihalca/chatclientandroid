package dm.chatclient.controller;

import dm.chatclient.model.Contact;
import dm.chatclient.model.Message;

import java.util.List;

public interface IChatClientListener
{
    boolean onNewMessage(Message message);
    void onConnected();
    void onDisconnected();
    void onLoginSuccessful();
    void onLoginFailed(String message);
    void onConnectionError();
    void onContactUpdated(Contact contact);
    void onContactsReceived(List<Contact> contactList);
}