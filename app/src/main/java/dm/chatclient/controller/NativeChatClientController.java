package dm.chatclient.controller;

import android.util.Log;
import dm.chatclient.model.Contact;
import dm.chatclient.model.Message;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class NativeChatClientController implements Closeable, IChatClientController
{
    private long m_pClient;
    private List<IChatClientListener> m_listeners;
    private List<Message> m_messages;
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
        m_messages = new ArrayList<Message>();
    }

//    public List<Message> getMesssages(int senderId)
//    {
//        List<Message> senderMessages = new ArrayList<Message>();
//
//        Iterator<Message> iterator = m_messages.iterator();
//        while (iterator.hasNext())
//        {
//            Message m = iterator.next();
//            if (m.()  == senderId)
//            {
//
//            }
//        }
//    }

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

    public void sendMessage(int receiverId, String message)
    {
        sendMessageNative(m_pClient, receiverId, message);
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
        List<Contact> contacts = new ArrayList<Contact>();
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
            Log.d("Controller", "U:" + username);
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

            boolean isOnline = bb.get(count++) != 0;

            contacts.add(new Contact(id,username,fullname,isOnline));
        }

        for(IChatClientListener listener: m_listeners)
        {
            listener.onContactsReceived(contacts);
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

    private native void sendMessageNative(long client, int receiverId, String message);

    private native void destroyClientNative(long client);

    private native void requestContactsNative(long client);
}
