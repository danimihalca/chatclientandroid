package dm.chatclient.chatclient.notifier;

import android.util.Log;
import dm.chatclient.controller.IChatClientController;
import dm.chatclient.model.Contact;
import dm.chatclient.model.Message;
import dm.chatclient.model.User;
import dm.chatclient.model.UserDetails;

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
    private long m_nativeListener;
    JNIChatClientNotifierProxy notifierProxy;

    static
    {
        System.loadLibrary("chatClientJNI");
    }

    public JNIChatClientNotifier(IChatClientController controller)
    {
        super(controller);

        notifierProxy= new JNIChatClientNotifierProxy(this);
        initializeNotifierProxy();

        m_nativeListener = createNativeJNIListener(notifierProxy.getNativeNotifierProxy());
    }

    public long getListener()
    {
        return m_nativeListener;
    }

    @Override
    public void close() throws IOException
    {
        if (m_nativeListener != 0)
        {
            destroyNativeJNIListener(m_nativeListener);
            m_nativeListener = 0;
        }
    }

    private void initializeNotifierProxy()
    {

        notifierProxy.setOnConnectedCallback("notifyOnConnected");
        notifierProxy.setOnDisconnectedCallback("notifyOnDisconnected");
        notifierProxy.setOnConnectionErrorCallback("notifyOnConnectionError");
        notifierProxy.setOnLoginFailedCallback("notifyOnLoginFailed");

        notifierProxy.setOnLoginSuccessfulCallback("notifyOnLoginSuccessfulFromJNI");
        notifierProxy.setOnMessageReceivedCallback("notifyOnMessageReceivedFromJNI");
        notifierProxy.setOnContactsReceivedCallback("notifyOnContactsReceivedFromJNI");
        notifierProxy.setOnContactStatusChangedCallback("notifyOnContactOnlineStatusChangedFromJNI");
    }


    @Override
    protected void finalize() throws Throwable
    {
        close();
    }

    public void notifyOnMessageReceivedFromJNI(int senderId, String messageText)
    {
        Contact sender = m_controller.getContact(senderId);
        User receiver = m_controller.getUser();

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
            Contact contact  = new Contact(id,username,firstname,lastname,Contact.CONTACT_STATE.convert(state));
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
            Log.d("Controller","L:"+lastname);

            UserDetails userDetails = new UserDetails(id,firstname,lastname);

            notifyOnLoginSuccessful(userDetails);
    }


    void notifyOnContactOnlineStatusChangedFromJNI(int contactId, byte state)
    {
        Contact contact = m_controller.getContact(contactId);
        contact.setState(Contact.CONTACT_STATE.convert(state));

        notifyOnContactStatusChanged(contact);
    }

    private native long createNativeJNIListener(long notifierProxyPointer);

    private native void destroyNativeJNIListener(long nativeListener);
}
