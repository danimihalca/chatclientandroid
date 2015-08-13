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
    public enum AUTHENTICATION_STATUS
    {
        AUTH_SUCCESSFUL ,
        AUTH_ALREADY_LOGGED_IN,
        AUTH_INVALID_CREDENTIALS,
        AUTH_INVALID_STATE;
        public static AUTHENTICATION_STATUS convert(byte ordinal)
        {
            switch (ordinal)
            {
                case 0:
                    return AUTH_SUCCESSFUL;
                case 1:
                    return AUTH_ALREADY_LOGGED_IN;
                case 2:
                    return AUTH_INVALID_CREDENTIALS;
                case 3:
                    return AUTH_INVALID_STATE;
            }
            return  AUTH_INVALID_CREDENTIALS;
        }
    }
    enum ADD_REQUEST_STATUS
    {
        ADD_ACCEPTED,
        ADD_DECLINED,
        ADD_OFFLINE,
        ADD_INEXISTENT,
        ADD_YOURSELF;
        public static ADD_REQUEST_STATUS convert(byte ordinal)
        {
            switch (ordinal)
            {
                case 0:
                    return ADD_ACCEPTED;
                case 1:
                    return ADD_DECLINED;
                case 2:
                    return ADD_OFFLINE;
                case 3:
                    return ADD_INEXISTENT;
            }
            return  ADD_YOURSELF;
        }
    };
    void addRuntimeListener(IRuntimeListener listener);
    void removeRuntimeListener(IRuntimeListener listener);
    void setLoginListener(ILoginListener listener);

    void notifyOnConnected();
    void notifyOnDisconnected();
    void notifyOnConnectionError();
    void notifyOnLoginSuccessful(UserDetails userDetails);
    void notifyOnLoginFailed(AUTHENTICATION_STATUS reason);
    void notifyOnContactsReceived(List<Contact> contacts);
    void notifyOnContactStatusChanged(Contact contact);
    void notifyOnMessageReceived(Message message);


    void notifyOnRemovedByContact(int contactId);
    void notifyOnAddContactResponse(String userName, ADD_REQUEST_STATUS status);
    boolean notifyOnAddingByContact(String requester);
}
