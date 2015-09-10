package dm.chatclient.controller;

import android.util.Log;
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
    private IContactRepository contactRepository;
    private IMessageRepository messageRepository;
    private IChatClient chatClient;
    private IChatClientNotifier notifier;
    private User user;

    public BaseUser.USER_STATE getState()
    {
        return state;
    }

    public void changeState(BaseUser.USER_STATE state)
    {
        this.state = state;
        chatClient.changeState(state);
    }

    @Override
    public void registerUser(User user)
    {
        chatClient.registerUser(user);
    }

    @Override
    public void updateUser(User user)
    {
        chatClient.updateUser(user);
    }


    private BaseUser.USER_STATE state;

    public ChatClientController()
    {
        contactRepository = new InMemoryContactRepository();
        messageRepository = new InMemoryMessageRepository();
        chatClient = new ChatClientJNIProxy();
        notifier = new JNIChatClientNotifier(this);
        chatClient.addListener(notifier);
        user = new User();

    }

    public User getUser()
    {
        return user;
    }

    @Override
    public void setUser(User user)
    {
        Log.d("ctr","setuser"+ user);
        this.user.setUserName(user.getUserName());
        this.user.setFirstName(user.getFirstName());
        this.user.setLastName(user.getLastName());
        this.user.setPassword(user.getPassword());
        Log.d("ctr", "setuser" + user);
    }

    @Override
    public void addContact(String userName)
    {
        chatClient.addContact(userName);
    }

    @Override
    public void removeContact(Contact contact, boolean notifyServer)
    {
        if (notifyServer)
        {
            chatClient.removeContact(contact.getId());
        }
        contactRepository.deleteContact(contact);
        messageRepository.removeMessages(contact);
    }

    public void setServer(String address, int port)
    {
        chatClient.setServer(address, port);
    }

    public void login(String username, String password, BaseUser.USER_STATE state)
    {
        chatClient.login(username, password, state);
        this.state = state;
    }

    public void disconnect()
    {
        chatClient.disconnect();
    }

    public void sendMessage(Message message)
    {
        messageRepository.addMessage(message);

        chatClient.sendMessage(message);
    }


    public List<Contact> getContacts()
    {
        return contactRepository.getContacts();
    }

    public void requestContacts()
    {
        chatClient.requestContacts();
    }

    public List<Message> getMessages(Contact contact)
    {
        return messageRepository.getMessagesWithContact(contact);
    }


    public void setContacts(List<Contact> contacts)
    {
        contactRepository.setContacts(contacts);
    }

    public Contact getContact(int contactId)
    {
        return contactRepository.getContact(contactId);
    }

    public void clearMessages()
    {
        messageRepository.clearMessages();
    }

    public void clearContacts()
    {
        contactRepository.clearContacts();
    }


    public void addReceivedMessage(Message message)
    {
        messageRepository.addMessage(message);
    }

    @Override
    public void addRegisterListener(IRegisterListener listener)
    {
        notifier.addRegisterListener(listener);
    }

    @Override
    public void removeRegisterListener(IRegisterListener listener)
    {
        notifier.removeRegisterListener(listener);
    }

    @Override
    public void addUpdateListener(IUpdateListener listener)
    {
        notifier.addUpdateListener(listener);
    }

    @Override
    public void removeUpdateListener(IUpdateListener listener)
    {
        notifier.removeUpdateListener(listener);
    }

    @Override
    public void addRuntimeListener(IRuntimeListener listener)
    {
        notifier.addRuntimeListener(listener);
    }

    @Override
    public void removeRuntimeListener(IRuntimeListener listener)
    {
        notifier.removeRuntimeListener(listener);
    }

    @Override
    public void addLoginListener(ILoginListener listener)
    {
        notifier.addLoginListener(listener);
    }

    @Override
    public void removeLoginListener(ILoginListener listener)
    {
        notifier.removeLoginListener(listener);
    }

}
