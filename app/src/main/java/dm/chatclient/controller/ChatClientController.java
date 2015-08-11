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

        m_user = new User(9999, "me", "pwd", "ME", "");

    }

    public User getUser()
    {
        return m_user;
    }

    public void connect(String address, int port)
    {
        m_chatClient.connect(address, port);
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

}
