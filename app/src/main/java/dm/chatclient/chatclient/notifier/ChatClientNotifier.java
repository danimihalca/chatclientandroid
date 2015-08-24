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
    protected IChatClientController controller;

    private List<IConnectListener> connectListeners;
    private List<ILoginListener> loginListeners;
    private List<IRegisterUpdateListener> registerUpdateListeners;
    private List<IRuntimeListener> runtimeListeners;


    public ChatClientNotifier(IChatClientController controller)
    {
        this.controller = controller;
        runtimeListeners = new ArrayList<IRuntimeListener>();
        connectListeners =new ArrayList<IConnectListener>();
        loginListeners = new ArrayList<ILoginListener>();
        registerUpdateListeners = new ArrayList<IRegisterUpdateListener>();
    }

    @Override
    public void addRegisterListener(IRegisterListener listener)
    {
        registerUpdateListeners.add(listener);
        connectListeners.add(listener);
    }
    @Override
    public void removeRegisterListener(IRegisterListener listener)
    {
        registerUpdateListeners.remove(listener);
        connectListeners.remove(listener);
    }

    @Override
    public void addUpdateListener(IUpdateListener listener)
    {
        registerUpdateListeners.add(listener);
        runtimeListeners.add(listener);
    }
    @Override
    public void removeUpdateListener(IUpdateListener listener)
    {
        registerUpdateListeners.remove(listener);
        runtimeListeners.remove(listener);
    }

    @Override
    public void addRuntimeListener(IRuntimeListener listener)
    {
        runtimeListeners.add(listener);
    }

    @Override
    public void removeRuntimeListener(IRuntimeListener listener)
    {
        runtimeListeners.remove(listener);
    }

    @Override
    public void addLoginListener(ILoginListener listener)
    {
        loginListeners.add(listener);
        connectListeners.add(listener);
    }

    @Override
    public void removeLoginListener(ILoginListener listener)
    {
        loginListeners.remove(listener);
        connectListeners.remove(listener);
    }

//    public void notifyOnConnected()
//    {
//
//        List<IConnectListener> reverseList =(List<IConnectListener>) ((ArrayList<IConnectListener>)connectListeners).clone();
//        Collections.reverse(reverseList);
//        for(IConnectListener listener : reverseList)
//        {
//            listener.onConnected();
//        }
//    }

    public void notifyOnDisconnected()
    {
        controller.clearContacts();
        controller.clearMessages();

        List<IRuntimeListener> reverseRuntimeListeners =
                (List<IRuntimeListener>) ((ArrayList<IRuntimeListener>)runtimeListeners).clone();
        Collections.reverse(reverseRuntimeListeners);
        for(IRuntimeListener listener : reverseRuntimeListeners)
        {
            listener.onDisconnected();
        }

        List<IConnectListener> reverseConnectListeners =
                (List<IConnectListener>) ((ArrayList<IConnectListener>)connectListeners).clone();
        Collections.reverse(reverseRuntimeListeners);
        for(IConnectListener listener : reverseConnectListeners)
        {
            listener.onDisconnected();
        }

//        List<IRegisterUpdateListener> reverseRegisterUpdateListeners =
//                (List<IRegisterUpdateListener>) ((ArrayList<IRegisterUpdateListener>)registerUpdateListeners).clone();
//        Collections.reverse(reverseRuntimeListeners);
//        for(IRegisterUpdateListener listener : reverseRegisterUpdateListeners)
//        {
//            listener.onDisconnected();
//        }
//
//        if (registerUpdateListener != null)
//        {
//            registerUpdateListener.onDisconnected();
//        }
    }

    public void notifyOnConnectionError()
    {
        List<IConnectListener> reverseList =(List<IConnectListener>) ((ArrayList<IConnectListener>)connectListeners).clone();
        Collections.reverse(reverseList);
        for(IConnectListener listener : reverseList)
        {
            listener.onConnectionError();
        }
    }

    public void notifyOnLoginSuccessful(UserDetails userDetails)
    {
        List<ILoginListener> reverseList =(List<ILoginListener>) ((ArrayList<ILoginListener>)loginListeners).clone();
        Collections.reverse(reverseList);
        for(ILoginListener listener : reverseList)
        {
            listener.onLoginSuccessful(userDetails);
        }
    }

    public void notifyOnLoginFailed(AUTHENTICATION_STATUS reason)
    {
        List<ILoginListener> reverseList =(List<ILoginListener>) ((ArrayList<ILoginListener>)loginListeners).clone();
        Collections.reverse(reverseList);
        for(ILoginListener listener : reverseList)
        {
            listener.onLoginFailed(reason);
        }
    }

    public void notifyOnContactsReceived(List<Contact> contacts)
    {
        controller.setContacts(contacts);

        List<IRuntimeListener> reverseList =(List<IRuntimeListener>) ((ArrayList<IRuntimeListener>)runtimeListeners).clone();
        Collections.reverse(reverseList);
        for(IRuntimeListener listener : reverseList)
        {
            listener.onContactsReceived();
        }
    }

    public void notifyOnContactStatusChanged(Contact contact)
    {
        List<IRuntimeListener> reverseList =(List<IRuntimeListener>) ((ArrayList<IRuntimeListener>)runtimeListeners).clone();
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
        controller.addReceivedMessage(message);

        List<IRuntimeListener> reverseList =(List<IRuntimeListener>) ((ArrayList<IRuntimeListener>)runtimeListeners).clone();
        Collections.reverse(reverseList);
        for(IRuntimeListener listener : reverseList)
        {
            listener.onMessageReceived(message);
        }
    }

    @Override
    public void notifyOnRemovedByContact(int contactId)
    {
        Contact contact = controller.getContact(contactId);
        controller.removeContact(contact, true);

        List<IRuntimeListener> reverseList =(List<IRuntimeListener>) ((ArrayList<IRuntimeListener>)runtimeListeners).clone();
        Collections.reverse(reverseList);
        for(IRuntimeListener listener : reverseList)
        {
            listener.onRemovedByContact(contact);
        }
    }

    @Override
    public void notifyOnAddContactResponse(String userName, ADD_REQUEST_STATUS status)
    {
        List<IRuntimeListener> reverseList =(List<IRuntimeListener>) ((ArrayList<IRuntimeListener>)runtimeListeners).clone();
        Collections.reverse(reverseList);
        for(IRuntimeListener listener : reverseList)
        {
            listener.onAddContactResponse(userName, status);
        }
    }

    @Override
    public boolean notifyOnAddRequest(String requester)
    {
        List<IRuntimeListener> reverseList =(List<IRuntimeListener>) ((ArrayList<IRuntimeListener>)runtimeListeners).clone();
        Collections.reverse(reverseList);
        for(IRuntimeListener listener : reverseList)
        {
            boolean accepted = listener.onAddRequest(requester);
            if (accepted)
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public void notifyOnRegisterUpdateResponse(REGISTER_UPDATE_STATUS status)
    {
        List<IRegisterUpdateListener> reverseList =(List<IRegisterUpdateListener>)
                ((ArrayList<IRegisterUpdateListener>)registerUpdateListeners).clone();
        Collections.reverse(reverseList);
        for(IRegisterUpdateListener listener : reverseList)
        {
            listener.onRegisterUpdateResponse(status);
        }
    }
}
