package dm.chatclient;

import android.util.Log;

import java.io.Closeable;
import java.io.IOException;

public class ChatClient implements Closeable
{
    private long m_pClient;
    private IChatMessageListener m_listener;
    static
    {
        System.loadLibrary("libwebsockets");
        System.loadLibrary("chatClientAPI");
        System.loadLibrary("chatClientJNI");
    }

    public ChatClient()
    {
        m_pClient = createClientNative();
    }
    public void addListener(IChatMessageListener listener)
    {
        m_listener = listener;
    }

    public void connect(String address, int port)
    {
        connectNative(m_pClient, address, port);
    }

    public void disconnect()
    {
        disconnectNative(m_pClient);
    }

    public void sendMessage(String message)
    {
        sendMessageNative(m_pClient, message);
    }

    public void notifyOnMessage(String message)
    {
        m_listener.onNewMessage(message);
    }

    public void notifyOnConnected()
    {
        m_listener.onConnected();
    }

    public void notifyOnDisconnected()
    {
        m_listener.onDisconnected();
    }
    @Override
    public void close() throws IOException
    {
        Log.i("ChatClient", "called close()");
        destroyClientNative(m_pClient);
        m_pClient = 0;
    }

    private native long createClientNative();

    private native void connectNative(long client, String address, int port);

    private native void disconnectNative(long client);

    private native void sendMessageNative(long client, String message);

    private native void destroyClientNative(long client);
}
