package dm.chatclient.controller;

public interface IChatClientController
{
    void addListener(IChatClientListener listener);

    void setServerProperties(String address, int port);
    void login(String username, String password);
    void disconnect();
    void sendMessage(String message);

    void notifyOnMessage(String message);
    void notifyOnConnected();
    void notifyOnDisconnected();
    void notifyOnLoginSuccessful();
    void notifyOnLoginFailed(String message);
    void notifyOnConnectionError();
}
