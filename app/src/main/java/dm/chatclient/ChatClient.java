package dm.chatclient;

import java.io.Closeable;
import java.io.IOException;

public class ChatClient implements Closeable
{
    private long m_pClient;

    static
    {
        System.loadLibrary("chatClientAPI");
        System.loadLibrary("chatClientJNI");
    }

    public ChatClient()
    {
        m_pClient = createClientNative();
    }

    public void initialize()
    {
        initializeNative(m_pClient);
    }

    public void connect()
    {
        connectNative(m_pClient);
    }

    public void startService()
    {
        startServiceNative(m_pClient);
    }
    public void sendMessage(String message)
    {
        sendMessageNative(m_pClient, message);
    }

    @Override
    public void close() throws IOException
    {
        destroyClientNative(m_pClient);
    }

    private native long createClientNative();

    private native void initializeNative(long client);

    private native void connectNative(long client);

    private native void startServiceNative(long client);

    private native void sendMessageNative(long client, String message);

    private native void destroyClientNative(long client);
}
