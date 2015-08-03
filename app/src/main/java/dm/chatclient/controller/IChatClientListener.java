package dm.chatclient.controller;

public interface IChatClientListener
{
    void onNewMessage(String message);
    void onConnected();
    void onDisconnected();
    void onLoginSuccessful();
    void onLoginFailed(String message);
    void onConnectionError();
}