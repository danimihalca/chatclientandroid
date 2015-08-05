package dm.chatclient.controller;

import android.util.Log;
import dm.chatclient.model.Contact;
import dm.chatclient.model.Message;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;

public class NativeChatClientController implements Closeable, IChatClientController
{
    private long m_pClient;
    private List<IChatClientListener> m_listeners;

    private Map<Contact,List<Message>> m_messages;
    private Map<Integer,Contact> m_contacts;
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

        m_contacts = new HashMap<Integer, Contact>();
        m_messages = new HashMap<Contact,List<Message>>();
    }


    public void addListener(IChatClientListener listener)
    {
        m_listeners.add(listener);
    }

    @Override
    public void removeListener(IChatClientListener listener)
    {
        m_listeners.remove(listener);
    }

    @Override
    public Contact getContact(int id)
    {
        return m_contacts.get(id);
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
        Contact c = m_contacts.get(receiverId);
        m_messages.get(c).add(new Message(new Contact(-1,"","ME",false),message));
    }

    @Override
    public void requestContacts()
    {
        requestContactsNative(m_pClient);
    }

    @Override
    public List<Message> getMessages(Contact sender)
    {
        List<Message> messages = new ArrayList<Message>();
        for (Message m: m_messages.get(sender))
        {
            if (sender.equals(m.getSender()))
            {
                messages.add(m);
            }
        }
        return m_messages.get(sender);
    }

    public void notifyOnMessage(int senderId,String message)
    {
        Contact sender = m_contacts.get(senderId);
        int unreadedMessages = sender.getUnreadMessagesCount();
        Message receivedMessage = new Message(sender,message);
        m_messages.get(sender).add(receivedMessage);
        boolean readingMessage = false;
        for(IChatClientListener listener: m_listeners)
        {
            if(listener.onNewMessage(receivedMessage))
            {
                readingMessage = true;
            }
        }
        if (readingMessage && unreadedMessages != 0)
        {
            Log.d("notifyOnMessage","reading");
            sender.setUnreadMessagesCount(0);
            notifyOnContactUpdated(sender);
            m_contacts.put(senderId,sender);
        }
        else if (!readingMessage)
        {
            Log.d("notifyOnMessage",Integer.toString(unreadedMessages+1));
            sender.setUnreadMessagesCount(++unreadedMessages);
            notifyOnContactUpdated(sender);
            m_contacts.put(senderId, sender);
        }
        Log.d("notifyOnMessage",m_contacts.toString());
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
    public void notifyOnContactUpdated(Contact contact)
    {
        for(IChatClientListener listener: m_listeners)
        {
            listener.onContactUpdated(contact);
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
            Contact contact  = new Contact(id,username,fullname,isOnline);
            m_contacts.put(id,contact);
            m_messages.put(contact, new ArrayList<Message>());
        }

        for(IChatClientListener listener: m_listeners)
        {
            listener.onContactsReceived(new ArrayList<Contact>(m_contacts.values()));
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
