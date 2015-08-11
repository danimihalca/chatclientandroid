package dm.chatclient.chatclient.notifier;

import dm.chatclient.chatclient.listener.ILoginListener;
import dm.chatclient.chatclient.listener.IRuntimeListener;
import dm.chatclient.controller.IChatClientController;
import dm.chatclient.model.Contact;
import dm.chatclient.model.Message;
import dm.chatclient.model.UserDetails;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Ice on 8/9/2015.
 */
public class ChatClientNotifier implements IChatClientNotifier
{
    protected IChatClientController m_controller;
    private ILoginListener m_loginListener;
    private List<IRuntimeListener> m_runtimeListeners;

    public ChatClientNotifier(IChatClientController controller)
    {
        m_controller = controller;
        m_runtimeListeners = new ArrayList<IRuntimeListener>();
    }

    public void addRuntimeListener(IRuntimeListener listener)
    {
        m_runtimeListeners.add(listener);
    }

    public void removeRuntimeListener(IRuntimeListener listener)
    {
        m_runtimeListeners.remove(listener);
    }

    public void setLoginListener(ILoginListener listener)
    {
        m_loginListener = listener;
    }

    public void notifyOnConnected()
    {
        if (m_loginListener != null)
        {
            m_loginListener.onConnected();
        }
    }

    public void notifyOnDisconnected()
    {
        m_controller.clearContacts();
        m_controller.clearMessages();

        List<IRuntimeListener> reverseList =(List<IRuntimeListener>) ((ArrayList<IRuntimeListener>)m_runtimeListeners).clone();
        Collections.reverse(reverseList);
        for(IRuntimeListener listener : reverseList)
        {
            listener.onDisconnected();
        }

        if (m_loginListener != null)
        {
            m_loginListener.onDisconnected();
        }
    }

    public void notifyOnConnectionError()
    {
        if (m_loginListener != null)
        {
            m_loginListener.onConnectionError();
        }
    }

    public void notifyOnLoginSuccessful(UserDetails userDetails)
    {
        if (m_loginListener != null)
        {
            m_loginListener.onLoginSuccessful(userDetails);
        }
    }

    public void notifyOnLoginFailed(String reason)
    {
        if (m_loginListener != null)
        {
            m_loginListener.onLoginFailed(reason);
        }
    }

    public void notifyOnContactsReceived(List<Contact> contacts)
    {
        m_controller.setContacts(contacts);

        List<IRuntimeListener> reverseList =(List<IRuntimeListener>) ((ArrayList<IRuntimeListener>)m_runtimeListeners).clone();
        Collections.reverse(reverseList);
        for(IRuntimeListener listener : reverseList)
        {
            listener.onContactsReceived();
        }
    }

    public void notifyOnContactStatusChanged(Contact contact)
    {
        List<IRuntimeListener> reverseList =(List<IRuntimeListener>) ((ArrayList<IRuntimeListener>)m_runtimeListeners).clone();
        Collections.reverse(reverseList);
        for(IRuntimeListener listener : reverseList)
        {
            listener.onContactStatusChanged(contact);
        }
    }

    public void notifyOnMessageReceived(Message message)
    {
        Contact contact = (Contact) message.getSender();
        int count = contact.getUnreadMessagesCount();
        contact.setUnreadMessagesCount(count + 1);
        m_controller.addReceivedMessage(message);

        List<IRuntimeListener> reverseList =(List<IRuntimeListener>) ((ArrayList<IRuntimeListener>)m_runtimeListeners).clone();
        Collections.reverse(reverseList);
        for(IRuntimeListener listener : reverseList)
        {
            listener.onMessageReceived(message);
        }
    }
}
