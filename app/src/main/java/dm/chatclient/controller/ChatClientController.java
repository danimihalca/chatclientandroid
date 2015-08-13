package dm.chatclient.controller;

import dm.chatclient.chatclient.ChatClientJNIProxy;
import dm.chatclient.chatclient.IChatClient;
import dm.chatclient.chatclient.listener.*;
import dm.chatclient.chatclient.notifier.IChatClientNotifier;
import dm.chatclient.chatclient.notifier.JNIChatClientNotifier;
import dm.chatclient.model.BaseUser;
import dm.chatclient.model.Contact;
import dm.chatclient.model.Message;
import dm.chatclient.model.User;
import dm.chatclient.repository.contact.IContactRepository;
import dm.chatclient.repository.contact.InMemoryContactRepository;
import dm.chatclient.repository.message.IMessageRepository;
import dm.chatclient.repository.message.InMemoryMessageRepository;

import java.util.*;

public class ChatClientController implements IChatClientController
{
    private IContactRepository m_contactRepository;
    private IMessageRepository m_messageRepository;
    private IChatClient m_chatClient;
    private IChatClientNotifier m_notifier;
    private User m_user;
    private boolean m_isconnected;

    public BaseUser.USER_STATE getState()
    {
        return m_state;
    }

    public void changeState(BaseUser.USER_STATE state)
    {
        m_state = state;
        m_chatClient.changeState(m_state);
    }

    @Override
    public void registerUser(User user)
    {
        m_chatClient.registerUser(user);
    }

    @Override
    public void updateUser(User user)
    {
        m_chatClient.updateUser(user);
    }

    @Override
    public boolean isConnected()
    {
        return m_isconnected;
    }


    @Override
    public void setConnected(boolean connected)
    {
        m_isconnected = connected;
    }

    private BaseUser.USER_STATE m_state;

    public ChatClientController()
    {
        m_contactRepository = new InMemoryContactRepository();
        m_messageRepository = new InMemoryMessageRepository();
        m_chatClient = new ChatClientJNIProxy();
        m_notifier = new JNIChatClientNotifier(this);
        m_chatClient.addListener(m_notifier);
        m_isconnected =false;
        m_user = new User();

    }

    public User getUser()
    {
        return m_user;
    }

    @Override
    public void setUser(User user)
    {
        m_user.setUserName(user.getUserName());
        m_user.setFirstName(user.getFirstName());
        m_user.setLastName(user.getLastName());
        m_user.setPassword(user.getPassword());
    }

    @Override
    public void addContact(String userName)
    {
        m_chatClient.addContact(userName);
    }

    @Override
    public void removeContact(Contact contact, boolean notifyServer)
    {
        if (notifyServer)
        {
            m_chatClient.removeContact(contact.getId());
        }
        m_contactRepository.deleteContact(contact);
        m_messageRepository.removeMessages(contact);
    }

    public void connect(String address, int port)
    {
        m_chatClient.connect(address, port);
    }

    public void login(String username, String password, BaseUser.USER_STATE state)
    {
        m_chatClient.login(username, password, state);
        m_state = state;
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

    public void setLoginListener(ILoginListener listener)
    {
        m_notifier.addLoginListener(listener);
    }


    public void addReceivedMessage(Message message)
    {
        m_messageRepository.addMessage(message);
    }

    @Override
    public void addRegisterListener(IRegisterListener listener)
    {
        m_notifier.addRegisterListener(listener);
    }

    @Override
    public void removeRegisterListener(IRegisterListener listener)
    {
        m_notifier.removeRegisterListener(listener);
    }

    @Override
    public void addUpdateListener(IUpdateListener listener)
    {
        m_notifier.addUpdateListener(listener);
    }

    @Override
    public void removeUpdateListener(IUpdateListener listener)
    {
        m_notifier.removeUpdateListener(listener);
    }

    @Override
    public void addRuntimeListener(IRuntimeListener listener)
    {
        m_notifier.addRuntimeListener(listener);
    }

    @Override
    public void removeRuntimeListener(IRuntimeListener listener)
    {
        m_notifier.removeRuntimeListener(listener);
    }

    @Override
    public void addLoginListener(ILoginListener listener)
    {
        m_notifier.addLoginListener(listener);
    }

    @Override
    public void removeLoginListener(ILoginListener listener)
    {
        m_notifier.removeLoginListener(listener);
    }

}
