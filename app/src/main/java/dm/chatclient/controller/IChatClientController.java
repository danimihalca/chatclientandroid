package dm.chatclient.controller;

public interface IChatClientController
{
    void addListener(IChatClientListener listener);

    void setServerProperties(String address, int port);
    void login(String username, String password);
    void disconnect();
    void sendMessage(int receiverId, String message);
    void requestContacts();
}
