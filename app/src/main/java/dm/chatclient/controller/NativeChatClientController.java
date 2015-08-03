package dm.chatclient.controller;

import android.util.Log;
import dm.chatclient.model.Contact;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.LinkedList;
import java.util.List;

public class NativeChatClientController implements Closeable, IChatClientController
{
    private long m_pClient;
    private List<IChatClientListener> m_listeners;
    static
    {
        System.loadLibrary("jsoncpp");
        System.loadLibrary("websockets");
        System.loadLibrary("chatClientAPI");
        System.loadLibrary("chatClientJNI");
    }

    public NativeChatClientController()
    {
        m_pClient = createClientNative();
        m_listeners = new LinkedList<IChatClientListener>();
    }

    public void addListener(IChatClientListener listener)
    {
        m_listeners.add(listener);
    }

    public void setServerProperties(String address, int port)
    {
        setServerPropertiesNative(m_pClient, address, port);
    }

    public void login(String username, String password)
    {
        loginNative(m_pClient, username, password);
    }

    public void disconnect()
    {
        disconnectNative(m_pClient);
    }

    public void sendMessage(String message)
    {
        sendMessageNative(m_pClient, message);
    }

    @Override
    public void requestContacts()
    {
        requestContactsNative(m_pClient);
    }

    public void notifyOnMessage(String message)
    {
        for(IChatClientListener listener: m_listeners)
        {
            listener.onNewMessage(message);
        }
    }

    public void notifyOnConnected()
    {
        for(IChatClientListener listener: m_listeners)
        {
            listener.onConnected();
        }
    }

    public void notifyOnDisconnected()
    {
        for(IChatClientListener listener: m_listeners)
        {
            listener.onDisconnected();
        }
    }

    public void notifyOnLoginSuccessful()
    {
        for(IChatClientListener listener: m_listeners)
        {
            listener.onLoginSuccessful();
        }
    }

    public void notifyOnLoginFailed(String message)
    {
        for(IChatClientListener listener: m_listeners)
        {
            listener.onLoginFailed(message);
        } }

    public void notifyOnConnectionError()
    {
        for(IChatClientListener listener: m_listeners)
        {
            listener.onConnectionError();
        }}

    public void notifyOnContactsReceived(byte[] contactsBuffer, int size)
    {
        List<Contact> contacts = new LinkedList<Contact>();
        ByteBuffer bb = ByteBuffer.wrap(contactsBuffer);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        for (int i = 0; i<size; ++i)
        {
            Log.d("Controller","C:"+Byte.toString(bb.get(i)));
        }
        int count = 0;
        while (count < size)
        {
            int id = bb.getInt(count);
            Log.d("Controller","ID:"+Integer.toString(id));

            count +=4;

            byte c;

            String username="";
            do
            {
                c=bb.get(count++);
                if (c==0)
                {
                    break;
                }
                username += (char)c;
            }
            while (c != 0);
            Log.d("Controller","U:"+username);
            String fullname="";
            do
            {
                c=bb.get(count++);
                if (c==0)
                {
                    break;
                }
                fullname += (char)c;
            }
            while (c != 0);
            Log.d("Controller","F:"+fullname);
        }
    }

    @Override
    public void close() throws IOException
    {
        if (m_pClient != 0)
        {
            destroyClientNative(m_pClient);
            m_pClient = 0;
        }
    }

    @Override
    protected void finalize() throws Throwable
    {
        close();
    }

    private native long createClientNative();

    private native void setServerPropertiesNative(long client, String address, int port);

    private native void loginNative(long client, String username, String password);

    private native void disconnectNative(long client);

    private native void sendMessageNative(long client, String message);

    private native void destroyClientNative(long client);

    private native void requestContactsNative(long client);
}
