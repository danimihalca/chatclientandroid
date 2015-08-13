package dm.chatclient.chatclient.notifier;

import dm.chatclient.chatclient.listener.*;
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

    private List<IConnectListener> m_connectListeners;
    private List<ILoginListener> m_loginListeners;
    private List<IRegisterUpdateListener> m_registerUpdateListeners;
    private List<IRuntimeListener> m_runtimeListeners;


    public ChatClientNotifier(IChatClientController controller)
    {
        m_controller = controller;
        m_runtimeListeners = new ArrayList<IRuntimeListener>();
        m_connectListeners =new ArrayList<IConnectListener>();
        m_loginListeners = new ArrayList<ILoginListener>();
        m_registerUpdateListeners = new ArrayList<IRegisterUpdateListener>();
    }

    @Override
    public void addRegisterListener(IRegisterListener listener)
    {
        m_registerUpdateListeners.add(listener);
        m_connectListeners.add(listener);
    }
    @Override
    public void removeRegisterListener(IRegisterListener listener)
    {
        m_registerUpdateListeners.remove(listener);
        m_connectListeners.remove(listener);
    }

    @Override
    public void addUpdateListener(IUpdateListener listener)
    {
        m_registerUpdateListeners.add(listener);
        m_runtimeListeners.add(listener);
    }
    @Override
    public void removeUpdateListener(IUpdateListener listener)
    {
        m_registerUpdateListeners.remove(listener);
        m_runtimeListeners.remove(listener);
    }

    @Override
    public void addRuntimeListener(IRuntimeListener listener)
    {
        m_runtimeListeners.add(listener);
    }

    @Override
    public void removeRuntimeListener(IRuntimeListener listener)
    {
        m_runtimeListeners.remove(listener);
    }

    @Override
    public void addLoginListener(ILoginListener listener)
    {
        m_loginListeners.add(listener);
        m_connectListeners.add(listener);
    }

    @Override
    public void removeLoginListener(ILoginListener listener)
    {
        m_loginListeners.remove(listener);
        m_connectListeners.remove(listener);
    }

    public void notifyOnConnected()
    {

        List<IConnectListener> reverseList =(List<IConnectListener>) ((ArrayList<IConnectListener>)m_connectListeners).clone();
        Collections.reverse(reverseList);
        for(IConnectListener listener : reverseList)
        {
            listener.onConnected();
        }
    }

    public void notifyOnDisconnected()
    {
        m_controller.setConnected(false);
        m_controller.clearContacts();
        m_controller.clearMessages();

        List<IRuntimeListener> reverseRuntimeListeners =
                (List<IRuntimeListener>) ((ArrayList<IRuntimeListener>)m_runtimeListeners).clone();
        Collections.reverse(reverseRuntimeListeners);
        for(IRuntimeListener listener : reverseRuntimeListeners)
        {
            listener.onDisconnected();
        }

        List<IConnectListener> reverseConnectListeners =
                (List<IConnectListener>) ((ArrayList<IConnectListener>)m_connectListeners).clone();
        Collections.reverse(reverseRuntimeListeners);
        for(IConnectListener listener : reverseConnectListeners)
        {
            listener.onDisconnected();
        }

//        List<IRegisterUpdateListener> reverseRegisterUpdateListeners =
//                (List<IRegisterUpdateListener>) ((ArrayList<IRegisterUpdateListener>)m_registerUpdateListeners).clone();
//        Collections.reverse(reverseRuntimeListeners);
//        for(IRegisterUpdateListener listener : reverseRegisterUpdateListeners)
//        {
//            listener.onDisconnected();
//        }
//
//        if (m_registerUpdateListener != null)
//        {
//            m_registerUpdateListener.onDisconnected();
//        }
    }

    public void notifyOnConnectionError()
    {
        List<IConnectListener> reverseList =(List<IConnectListener>) ((ArrayList<IConnectListener>)m_connectListeners).clone();
        Collections.reverse(reverseList);
        for(IConnectListener listener : reverseList)
        {
            listener.onConnectionError();
        }
    }

    public void notifyOnLoginSuccessful(UserDetails userDetails)
    {
        List<ILoginListener> reverseList =(List<ILoginListener>) ((ArrayList<ILoginListener>)m_loginListeners).clone();
        Collections.reverse(reverseList);
        for(ILoginListener listener : reverseList)
        {
            listener.onLoginSuccessful(userDetails);
        }
    }

    public void notifyOnLoginFailed(AUTHENTICATION_STATUS reason)
    {
        List<ILoginListener> reverseList =(List<ILoginListener>) ((ArrayList<ILoginListener>)m_loginListeners).clone();
        Collections.reverse(reverseList);
        for(ILoginListener listener : reverseList)
        {
            listener.onLoginFailed(reason);
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

    @Override
    public void notifyOnRemovedByContact(int contactId)
    {
        Contact contact = m_controller.getContact(contactId);
        m_controller.removeContact(contact, true);

        List<IRuntimeListener> reverseList =(List<IRuntimeListener>) ((ArrayList<IRuntimeListener>)m_runtimeListeners).clone();
        Collections.reverse(reverseList);
        for(IRuntimeListener listener : reverseList)
        {
            listener.onRemovedByContact(contact);
        }
    }

    @Override
    public void notifyOnAddContactResponse(String userName, ADD_REQUEST_STATUS status)
    {
        List<IRuntimeListener> reverseList =(List<IRuntimeListener>) ((ArrayList<IRuntimeListener>)m_runtimeListeners).clone();
        Collections.reverse(reverseList);
        for(IRuntimeListener listener : reverseList)
        {
            listener.onAddContactResponse(userName, status);
        }
    }

    @Override
    public boolean notifyOnAddingByContact(String requester)
    {
        List<IRuntimeListener> reverseList =(List<IRuntimeListener>) ((ArrayList<IRuntimeListener>)m_runtimeListeners).clone();
        Collections.reverse(reverseList);
        for(IRuntimeListener listener : reverseList)
        {
            boolean accepted = listener.onAddingByContact(requester);
            if (accepted)
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public void notifyOnRegisterUpdateResponse(REGISTER_UPDATE_USER_STATUS status)
    {
        List<IRegisterUpdateListener> reverseList =(List<IRegisterUpdateListener>)
                ((ArrayList<IRegisterUpdateListener>)m_registerUpdateListeners).clone();
        Collections.reverse(reverseList);
        for(IRegisterUpdateListener listener : reverseList)
        {
            listener.onRegisterUpdateResponse(status);
        }
    }
}
