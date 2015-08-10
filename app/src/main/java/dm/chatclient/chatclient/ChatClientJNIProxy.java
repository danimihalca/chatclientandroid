package dm.chatclient.chatclient;

import dm.chatclient.chatclient.notifier.IChatClientNotifier;
import dm.chatclient.chatclient.notifier.JNIChatClientNotifier;
import dm.chatclient.model.Message;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by Ice on 8/9/2015.
 */
public class ChatClientJNIProxy implements IChatClient, Closeable
{
    private long m_nativeChatClient;

    static
    {
        System.loadLibrary("jsoncpp");
        System.loadLibrary("websockets");
        System.loadLibrary("chatClientAPI");
        System.loadLibrary("chatClientJNI");
    }

    public ChatClientJNIProxy()
    {
        m_nativeChatClient = createClientNative();
    }


    public void setServerProperties(String address, int port)
    {
        setServerPropertiesNative(m_nativeChatClient, address, port);
    }

    public void login(String username, String password)
    {
        loginNative(m_nativeChatClient, username, password);
    }

    public void disconnect()
    {
        disconnectNative(m_nativeChatClient);
    }

    public void sendMessage(Message message)
    {
        sendMessageNative(m_nativeChatClient, message.getReceiver().getId(), message.getMessageText());
    }

    @Override
    public void addListener(IChatClientNotifier m_notifier)
    {
        addListenerNative(m_nativeChatClient, ((JNIChatClientNotifier) m_notifier).getListener());
    }


    @Override
    public void removeListener(IChatClientNotifier m_notifier)
    {
        removeListenerNative(m_nativeChatClient, ((JNIChatClientNotifier) m_notifier).getListener());

    }


    public void requestContacts()
    {
        requestContactsNative(m_nativeChatClient);
    }

    public void close() throws IOException
    {
        if (m_nativeChatClient != 0)
        {
            destroyClientNative(m_nativeChatClient);
            m_nativeChatClient = 0;
        }
    }

    @Override
    protected void finalize() throws Throwable
    {
        close();
    }

    private native long createClientNative();

    private native void setServerPropertiesNative(long clientAddress, String address, int port);

    private native void loginNative(long clientAddress, String username, String password);

    private native void disconnectNative(long clientAddress);

    private native void sendMessageNative(long clientAddress, int receiverId, String message);

    private native void destroyClientNative(long clientAddress);

    private native void requestContactsNative(long clientAddress);

    private native void addListenerNative(long clientAddress, long listener);

    private native void removeListenerNative(long clientAddress, long listener);

}
