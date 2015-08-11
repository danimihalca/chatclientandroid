package dm.chatclient.chatclient.notifier;

import dm.chatclient.chatclient.listener.ILoginListener;
import dm.chatclient.chatclient.listener.IRuntimeListener;
import dm.chatclient.model.Contact;
import dm.chatclient.model.Message;
import dm.chatclient.model.UserDetails;

import java.util.List;

/**
 * Created by Ice on 8/9/2015.
 */
public interface IChatClientNotifier
{
    void addRuntimeListener(IRuntimeListener listener);
    void removeRuntimeListener(IRuntimeListener listener);
    void setLoginListener(ILoginListener listener);

    void notifyOnConnected();
    void notifyOnDisconnected();
    void notifyOnConnectionError();
    void notifyOnLoginSuccessful(UserDetails userDetails);
    void notifyOnLoginFailed(String reason);
    void notifyOnContactsReceived(List<Contact> contacts);
    void notifyOnContactStatusChanged(Contact contact);
    void notifyOnMessageReceived(Message message);
}
