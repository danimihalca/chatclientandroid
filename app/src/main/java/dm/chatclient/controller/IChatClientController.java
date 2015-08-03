package dm.chatclient.controller;

import java.nio.ByteBuffer;

public interface IChatClientController
{
    void addListener(IChatClientListener listener);

    void setServerProperties(String address, int port);
    void login(String username, String password);
    void disconnect();
    void sendMessage(String message);
    void requestContacts();
}
