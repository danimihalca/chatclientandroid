package dm.chatclient.chatclient.notifier;

import android.util.Log;
import dm.chatclient.controller.IChatClientController;
import dm.chatclient.model.Contact;
import dm.chatclient.model.Message;
import dm.chatclient.model.User;

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

        JNIChatClientNotifierProxy notif2ierProxy = createNotifierProxy();
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

    private JNIChatClientNotifierProxy createNotifierProxy()
    {
        notifierProxy= new JNIChatClientNotifierProxy(this);

        notifierProxy.setOnConnectedCallback("notifyOnConnected");
        notifierProxy.setOnDisonnectedCallback("notifyOnDisconnected");
        notifierProxy.setOnConnectionErrorCallback("notifyOnConnectionError");
        notifierProxy.setOnLoginSuccessfulCallback("notifyOnLoginSuccessful");
        notifierProxy.setOnLoginFailedCallback("notifyOnLoginFailed");

        notifierProxy.setOnMessageReceivedCallback("notifyOnMessageReceivedFromJNI");
        notifierProxy.setOnContactsReceivedCallback("notifyOnContactsReceivedFromJNI");
        notifierProxy.setOnContactStatusChangedCallback("notifyOnContactOnlineStatusChangedFromJNI");

        return notifierProxy;
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
            String fullname="";
            do
            {
                c=bb.get(count++);
                if (c==0)
                {
                    break;
                }
                fullname += (char)c;
            }
            while (c != 0);
            Log.d("Controller","F:"+fullname);

            boolean isOnline = bb.get(count++) != 0;
            Contact contact  = new Contact(id,username,fullname,isOnline);
            contactList.add(contact);

            notifyOnContactsReceived(contactList);
        }
    }

    void notifyOnContactOnlineStatusChangedFromJNI(int contactId, boolean isOnline)
    {
        Contact contact = m_controller.getContact(contactId);
        contact.setOnline(isOnline);

        notifyOnContactStatusChanged(contact);
    }

    private native long createNativeJNIListener(long notifierProxyPointer);

    private native void destroyNativeJNIListener(long nativeListener);
}