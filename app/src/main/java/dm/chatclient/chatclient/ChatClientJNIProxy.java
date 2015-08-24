package dm.chatclient.chatclient;

import dm.chatclient.chatclient.notifier.IChatClientNotifier;
import dm.chatclient.chatclient.notifier.JNIChatClientNotifier;
import dm.chatclient.model.BaseUser;
import dm.chatclient.model.Message;
import dm.chatclient.model.User;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by Ice on 8/9/2015.
 */
public class ChatClientJNIProxy implements IChatClient, Closeable
{
    private long nativeChatClientPtr;

    static
    {
        System.loadLibrary("jsoncpp");
        System.loadLibrary("websockets");
        System.loadLibrary("chatClientAPI");
        System.loadLibrary("chatClientJNI");
    }

    public ChatClientJNIProxy()
    {
        nativeChatClientPtr = createClientNative();
    }


    public void setServer(String address, int port)
    {
        setServerNative(nativeChatClientPtr, address, port);
    }

    public void login(String username, String password, BaseUser.USER_STATE state)
    {
        loginNative(nativeChatClientPtr, username, password, state.ordinal());
    }

    public void disconnect()
    {
        disconnectNative(nativeChatClientPtr);
    }

    public void sendMessage(Message message)
    {
        sendMessageNative(nativeChatClientPtr, message.getReceiver().getId(), message.getMessageText());
    }

    @Override
    public void addListener(IChatClientNotifier notifier)
    {
        addListenerNative(nativeChatClientPtr, ((JNIChatClientNotifier) notifier).getListener());
    }


    @Override
    public void removeListener(IChatClientNotifier notifier)
    {
        removeListenerNative(nativeChatClientPtr, ((JNIChatClientNotifier) notifier).getListener());

    }

    @Override
    public void addContact(String userName)
    {
        addContactNative(nativeChatClientPtr, userName);
    }

    @Override
    public void removeContact(int contactId)
    {
        removeContactNative(nativeChatClientPtr, contactId);
    }

    @Override
    public void changeState(BaseUser.USER_STATE state)
    {
        changeStateNative(nativeChatClientPtr, state.ordinal());
    }

    @Override
    public void registerUser(User user)
    {
        registerUserNative(nativeChatClientPtr, user.getUserName(), user.getPassword(), user.getFirstName(), user.getLastName());
    }

    @Override
    public void updateUser(User user)
    {
        updateUserNative(nativeChatClientPtr, user.getUserName(), user.getPassword(), user.getFirstName(), user.getLastName());

    }


    public void requestContacts()
    {
        requestContactsNative(nativeChatClientPtr);
    }

    public void close() throws IOException
    {
        if (nativeChatClientPtr != 0)
        {
            destroyClientNative(nativeChatClientPtr);
            nativeChatClientPtr = 0;
        }
    }

    @Override
    protected void finalize() throws Throwable
    {
        close();
    }

    private native long createClientNative();

    private native void setServerNative(long nativeChatClient, String address, int port);

    private native void loginNative(long nativeChatClient, String username, String password, int state);

    private native void disconnectNative(long nativeChatClient);

    private native void sendMessageNative(long nativeChatClient, int receiverId, String message);

    private native void destroyClientNative(long nativeChatClient);

    private native void requestContactsNative(long nativeChatClient);

    private native void addListenerNative(long nativeChatClient, long listener);

    private native void removeListenerNative(long nativeChatClient, long listener);

    private native void addContactNative(long nativeChatClient, String userName);
    private native void removeContactNative(long nativeChatClient, int contactId);

    private native void changeStateNative(long nativeChatClient,int state);

    private native void registerUserNative(long nativeChatClient, String userName, String password,
                                           String firstName, String lastName);

    private native void updateUserNative(long nativeChatClient, String userName, String password,
                                         String firstName, String lastName);

}
