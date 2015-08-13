package dm.chatclient.chatclient.notifier;

import dm.chatclient.chatclient.listener.*;
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


    enum REGISTER_UPDATE_USER_STATUS
    {
        USER_OK,
        USER_EXISTING_USERNAME ,
        USER_INVALID_INPUT ;
        public static REGISTER_UPDATE_USER_STATUS convert(byte ordinal)
        {
            switch (ordinal)
            {
                case 0:
                    return USER_OK;
                case 1:
                    return USER_EXISTING_USERNAME;
                case 2:
                    return USER_INVALID_INPUT;
            }
            return  USER_INVALID_INPUT;
        }

    };

    void addRegisterListener(IRegisterListener listener);

    void removeRegisterListener(IRegisterListener listener);

    void addUpdateListener(IUpdateListener listener);

    void removeUpdateListener(IUpdateListener listener);

    void addRuntimeListener(IRuntimeListener listener);
    void removeRuntimeListener(IRuntimeListener listener);
    void addLoginListener(ILoginListener listener);

    void removeLoginListener(ILoginListener listener);

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

    void notifyOnRegisterUpdateResponse(REGISTER_UPDATE_USER_STATUS status);

}
