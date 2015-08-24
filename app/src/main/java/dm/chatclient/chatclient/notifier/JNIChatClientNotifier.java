package dm.chatclient.chatclient.notifier;

import android.util.Log;
import dm.chatclient.controller.IChatClientController;
import dm.chatclient.model.*;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ice on 8/9/2015.
 */
public class JNIChatClientNotifier extends ChatClientNotifier implements Closeable
{
    private long nativeListenerPtr;
    private JNIChatClientNotifierProxy notifierProxy;

    static
    {
        System.loadLibrary("chatClientJNI");
    }

    public JNIChatClientNotifier(IChatClientController controller)
    {
        super(controller);

        notifierProxy= new JNIChatClientNotifierProxy(this);
        initializeNotifierProxy();

        nativeListenerPtr = createNativeJNIListener(notifierProxy.getNativeNotifierProxyAddress());
    }

    public long getListener()
    {
        return nativeListenerPtr;
    }

    @Override
    public void close() throws IOException
    {
        if (nativeListenerPtr != 0)
        {
            destroyNativeJNIListener(nativeListenerPtr);
            nativeListenerPtr = 0;
        }
    }

    private void initializeNotifierProxy()
    {

//        notifierProxy.setOnConnectedCallback("notifyOnConnected");
        notifierProxy.setCallbackMethod(JNIChatClientNotifierProxy.CALLBACK_METHOD.ON_DISCONNECTED,
                "notifyOnDisconnected");
        notifierProxy.setCallbackMethod(JNIChatClientNotifierProxy.CALLBACK_METHOD.ON_CONNECTION_ERROR,
                "notifyOnConnectionError");

        notifierProxy.setCallbackMethod(JNIChatClientNotifierProxy.CALLBACK_METHOD.ON_LOGIN_FAILED,
                "notifyOnLoginFailedFromJNI");
        notifierProxy.setCallbackMethod(JNIChatClientNotifierProxy.CALLBACK_METHOD.ON_LOGIN_SUCCESSFUL,
                "notifyOnLoginSuccessfulFromJNI");
        notifierProxy.setCallbackMethod(JNIChatClientNotifierProxy.CALLBACK_METHOD.ON_MESSAGE_RECEIVED,
                "notifyOnMessageReceivedFromJNI");
        notifierProxy.setCallbackMethod(JNIChatClientNotifierProxy.CALLBACK_METHOD.ON_CONTACTS_RECEIVED,
                "notifyOnContactsReceivedFromJNI");
        notifierProxy.setCallbackMethod(JNIChatClientNotifierProxy.CALLBACK_METHOD.ON_CONTACT_STATE_CHANGED
                ,"notifyOnContactOnlineStatusChangedFromJNI");

        notifierProxy.setCallbackMethod(JNIChatClientNotifierProxy.CALLBACK_METHOD.ON_REMOVED_BY_CONTACT,
                "notifyOnRemovedByContact");
        notifierProxy.setCallbackMethod(JNIChatClientNotifierProxy.CALLBACK_METHOD.ON_ADD_CONTACT_RESPONSE,
                "notifyOnAddContactResponseFromJNI");
        notifierProxy.setCallbackMethod(JNIChatClientNotifierProxy.CALLBACK_METHOD.ON_ADD_REQUEST,
                "notifyOnAddRequest");

        notifierProxy.setCallbackMethod(JNIChatClientNotifierProxy.CALLBACK_METHOD.ON_REGISTER_UPDATE_RESPONSE,
                "notifyOnRegisterUpdateResponseFromJNI");
    }


    @Override
    protected void finalize() throws Throwable
    {
        close();
    }
    public void notifyOnLoginFailedFromJNI(byte reason)
    {
        notifyOnLoginFailed(AUTHENTICATION_STATUS.convert(reason));
    }
    public void notifyOnMessageReceivedFromJNI(int senderId, String messageText)
    {
        Contact sender = controller.getContact(senderId);
        User receiver = controller.getUser();

        Message message = new Message(sender,receiver, messageText);
        notifyOnMessageReceived(message);
    }

    public void notifyOnContactsReceivedFromJNI(byte[] contactsBuffer, int size)
    {
        List<Contact> contactList = new ArrayList<Contact>();

        ByteBuffer bb = ByteBuffer.wrap(contactsBuffer);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        for (int i = 0; i<size; ++i)
        {
            Log.d("Controller", "C:" + Byte.toString(bb.get(i)));
        }
        int count = 0;
        while (count < size)
        {
            int id = bb.getInt(count);
            Log.d("Controller","ID:"+Integer.toString(id));

            count +=4;

            byte c;

            String username="";
            do
            {
                c=bb.get(count++);
                if (c==0)
                {
                    break;
                }
                username += (char)c;
            }
            while (c != 0);
            Log.d("Controller", "U:" + username);
            String firstname="";
            do
            {
                c=bb.get(count++);
                if (c==0)
                {
                    break;
                }
                firstname += (char)c;
            }
            while (c != 0);
            Log.d("Controller","F:"+firstname);
            String lastname="";
            do
            {
                c=bb.get(count++);
                if (c==0)
                {
                    break;
                }
                lastname += (char)c;
            }
            while (c != 0);
            Log.d("Controller","L:"+lastname);

            byte state = bb.get(count++);
            Contact contact  = new Contact(id,username,firstname,lastname, BaseUser.USER_STATE.convert(state));
            contactList.add(contact);

            notifyOnContactsReceived(contactList);
        }
    }

    public void notifyOnLoginSuccessfulFromJNI(byte[] userDetailsBuffer, int size)
    {
        ByteBuffer bb = ByteBuffer.wrap(userDetailsBuffer);
        bb.order(ByteOrder.LITTLE_ENDIAN);


        int count = 0;
            int id = bb.getInt(count);
            Log.d("Controller","ID:"+Integer.toString(id));

            count +=4;

            byte c;


            String firstname="";
            do
            {
                c=bb.get(count++);
                if (c==0)
                {
                    break;
                }
                firstname += (char)c;
            }
            while (c != 0);
            Log.d("Controller","F:"+firstname);

            String lastname="";
            do
            {
                c=bb.get(count++);
                if (c==0)
                {
                    break;
                }
                lastname += (char)c;
            }
            while (c != 0);
            Log.d("Controller", "L:" + lastname);

            UserDetails userDetails = new UserDetails(id,firstname,lastname);

            notifyOnLoginSuccessful(userDetails);
    }


    public void notifyOnContactOnlineStatusChangedFromJNI(int contactId, byte state)
    {
        Contact contact = controller.getContact(contactId);
        contact.setState(BaseUser.USER_STATE.convert(state));

        notifyOnContactStatusChanged(contact);
    }

    public void notifyOnAddContactResponseFromJNI(String userName, byte status)
    {
        notifyOnAddContactResponse(userName, ADD_REQUEST_STATUS.convert(status));
    }

    public void notifyOnRegisterUpdateResponseFromJNI(byte status)
    {
        notifyOnRegisterUpdateResponse(REGISTER_UPDATE_STATUS.convert(status));
    }

    private native long createNativeJNIListener(long notifierProxyPointer);

    private native void destroyNativeJNIListener(long nativeListener);
}
