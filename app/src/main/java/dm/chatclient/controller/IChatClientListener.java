package dm.chatclient.controller;

import dm.chatclient.model.Contact;

import java.util.List;

public interface IChatClientListener
{
    void onNewMessage(String message);
    void onConnected();
    void onDisconnected();
    void onLoginSuccessful();
    void onLoginFailed(String message);
    void onConnectionError();
    void onContactsReceived(List<Contact> contactList);
}