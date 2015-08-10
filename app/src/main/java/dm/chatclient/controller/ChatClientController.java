package dm.chatclient.controller;

import android.util.Log;
import dm.chatclient.chatclient.ChatClientJNIProxy;
import dm.chatclient.chatclient.IChatClient;
import dm.chatclient.chatclient.listener.ILoginListener;
import dm.chatclient.chatclient.listener.IRuntimeListener;
import dm.chatclient.chatclient.notifier.IChatClientNotifier;
import dm.chatclient.chatclient.notifier.JNIChatClientNotifier;
import dm.chatclient.model.Contact;
import dm.chatclient.model.Message;
import dm.chatclient.model.User;
import dm.chatclient.repository.contact.IContactRepository;
import dm.chatclient.repository.contact.InMemoryContactRepository;
import dm.chatclient.repository.message.IMessageRepository;
import dm.chatclient.repository.message.InMemoryMessageRepository;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;

public class ChatClientController implements IChatClientController
{
    private IContactRepository m_contactRepository;
    private IMessageRepository m_messageRepository;
    private IChatClient m_chatClient;
    private IChatClientNotifier m_notifier;
    private User m_user;

    public ChatClientController()
    {
        m_contactRepository = new InMemoryContactRepository();
        m_messageRepository = new InMemoryMessageRepository();
        m_chatClient = new ChatClientJNIProxy();
        m_notifier = new JNIChatClientNotifier(this);
        m_chatClient.addListener(m_notifier);

        m_user = new User(9999, "me", "pwd", "ME");

    }

    public User getUser()
    {
        return m_user;
    }

    public void setServerProperties(String address, int port)
    {
        m_chatClient.setServerProperties(address, port);
    }

    public void login(String username, String password)
    {
        m_chatClient.login(username, password);
    }

    public void disconnect()
    {
        m_chatClient.disconnect();
    }

    public void sendMessage(Message message)
    {
        m_messageRepository.addMessage(message);

        m_chatClient.sendMessage(message);
    }


    public List<Contact> getContacts()
    {
        return m_contactRepository.getContacts();
    }

    public void requestContacts()
    {
        m_chatClient.requestContacts();
    }

    public List<Message> getMessages(Contact contact)
    {
        return m_messageRepository.getMessagesWithContact(contact);
    }


    public void setContacts(List<Contact> contacts)
    {
        m_contactRepository.setContacts(contacts);
    }

    public Contact getContact(int contactId)
    {
        return m_contactRepository.getContact(contactId);
    }

    public void clearMessages()
    {
        m_messageRepository.clearMessages();
    }

    public void clearContacts()
    {
        m_contactRepository.clearContacts();
    }

    public void addRuntimeListener(IRuntimeListener listener)
    {
        m_notifier.addRuntimeListener(listener);
    }

    public void removeRuntimeListener(IRuntimeListener listener)
    {
        m_notifier.removeRuntimeListener(listener);
    }

    public void setLoginListener(ILoginListener listener)
    {
        m_notifier.setLoginListener(listener);
    }


    public void addReceivedMessage(Message message)
    {
        m_messageRepository.addMessage(message);
    }

//    public void notifyOnMessage(int senderId,String message)
//    {
//        Contact sender = m_contacts.get(senderId);
//        int unreadedMessages = sender.getUnreadMessagesCount();
//        Message receivedMessage = new Message(sender,message);
//        m_messages.get(sender).add(receivedMessage);
//        boolean readingMessage = false;
//        for(IChatClientListener listener: m_listeners)
//        {
//            if(listener.onNewMessage(receivedMessage))
//            {
//                readingMessage = true;
//            }
//        }
//        if (readingMessage && unreadedMessages != 0)
//        {
//            Log.d("notifyOnMessage","reading");
//            sender.setUnreadMessagesCount(0);
//            notifyOnContactUpdated(sender);
//            m_contacts.put(senderId,sender);
//        }
//        else if (!readingMessage)
//        {
//            Log.d("notifyOnMessage",Integer.toString(unreadedMessages+1));
//            sender.setUnreadMessagesCount(++unreadedMessages);
//            notifyOnContactUpdated(sender);
//            m_contacts.put(senderId, sender);
//        }
//        Log.d("notifyOnMessage",m_contacts.toString());
//    }
//
//    public void notifyOnConnected()
//    {
//        Log.d("notifyOnConnected",Integer.toString(m_listeners.size()));
//        for(IChatClientListener listener: m_listeners)
//        {
//            listener.onConnected();
//        }
//    }
//
//    public void notifyOnDisconnected()
//    {
//        Log.d("notifyOnDisconnected",Integer.toString(m_listeners.size()));
//        for(IChatClientListener listener: m_listeners)
//        {
//            listener.onDisconnected();
//        }
//        m_listeners.clear();
//    }
//    public void notifyOnContactUpdated(Contact contact)
//    {
//        for(IChatClientListener listener: m_listeners)
//        {
//            listener.onContactUpdated(contact);
//        }
//    }
//    public void notifyOnLoginSuccessful()
//    {
//        Log.d("notifyOnLoginSuccessful",Integer.toString(m_listeners.size()));
//        for(IChatClientListener listener: m_listeners)
//        {
//            listener.onLoginSuccessful();
//        }
//    }
//
//    public void notifyOnLoginFailed(String message)
//    {
//        Log.d("notifyOnLoginFailed",Integer.toString(m_listeners.size()));
//        for(IChatClientListener listener: m_listeners)
//        {
//            listener.onLoginFailed(message);
//        }
//        m_listeners.clear();
//    }
//
//    public void notifyOnConnectionError()
//    {
//        Log.d("notifyOnConnectionError",Integer.toString(m_listeners.size()));
//        for(IChatClientListener listener: m_listeners)
//        {
//            listener.onConnectionError();
//        }
//        m_listeners.clear();
//    }
//
//    public void notifyOnContactsReceived(byte[] contactsBuffer, int size)
//    {
//        ByteBuffer bb = ByteBuffer.wrap(contactsBuffer);
//        bb.order(ByteOrder.LITTLE_ENDIAN);
//        for (int i = 0; i<size; ++i)
//        {
//            Log.d("Controller","C:"+Byte.toString(bb.get(i)));
//        }
//        int count = 0;
//        while (count < size)
//        {
//            int id = bb.getInt(count);
//            Log.d("Controller","ID:"+Integer.toString(id));
//
//            count +=4;
//
//            byte c;
//
//            String username="";
//            do
//            {
//                c=bb.get(count++);
//                if (c==0)
//                {
//                    break;
//                }
//                username += (char)c;
//            }
//            while (c != 0);
//            Log.d("Controller", "U:" + username);
//            String fullname="";
//            do
//            {
//                c=bb.get(count++);
//                if (c==0)
//                {
//                    break;
//                }
//                fullname += (char)c;
//            }
//            while (c != 0);
//            Log.d("Controller","F:"+fullname);
//
//            boolean isOnline = bb.get(count++) != 0;
//            Contact contact  = new Contact(id,username,fullname,isOnline);
//            m_contacts.put(id,contact);
//            m_messages.put(contact, new ArrayList<Message>());
//        }
//
//        for(IChatClientListener listener: m_listeners)
//        {
//            listener.onContactsReceived(new ArrayList<Contact>(m_contacts.values()));
//        }
//    }
//    void notifyOnContactOnlineStatusChanged(int contactId, boolean isOnline)
//    {
//        Log.d("online changed",Integer.toString(contactId) +" "+Boolean.toString(isOnline));
//        Contact c = m_contacts.get(contactId);
//        c.setOnline(isOnline);
//        for(IChatClientListener listener: m_listeners)
//        {
//            listener.onContactOnlineStatusChanged(c);
//        }
//    }

}
