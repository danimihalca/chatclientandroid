package dm.chatclient;

public interface IChatMessageListener
{
    void onNewMessage(String message);
    void onConnected();
    void onDisconnected();
}
