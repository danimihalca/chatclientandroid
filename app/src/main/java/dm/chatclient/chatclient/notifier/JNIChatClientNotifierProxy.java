package dm.chatclient.chatclient.notifier;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by Ice on 8/9/2015.
 */
public class JNIChatClientNotifierProxy //implements Closeable
{
    private enum CALLBACK_METHOD
    {
        ON_CONNECTED,
        ON_DISCONNECTED,
        ON_CONNECTION_ERROR,
        ON_LOGIN_SUCCESSFUL,
        ON_LOGIN_FAILED,
        ON_CONTACT_STATUS_CHANGED,
        ON_MESSAGE_RECEIVED,
        ON_CONTACTS_RECEIVED
    }

    private long m_nativeNotifierProxy;

    static
    {
        System.loadLibrary("chatClientJNI");
    }

    public JNIChatClientNotifierProxy(JNIChatClientNotifier actualNotifier)
    {
        m_nativeNotifierProxy = createNativeNotifierProxy(actualNotifier);
    }

    public void setOnConnectedCallback(String methodName)
    {
        setCallbackMethod(CALLBACK_METHOD.ON_CONNECTED.ordinal(), methodName);
    }

    public void setOnDisonnectedCallback(String methodName)
    {
        setCallbackMethod(CALLBACK_METHOD.ON_DISCONNECTED.ordinal(), methodName);
    }


    public void setOnConnectionErrorCallback(String methodName)
    {
        setCallbackMethod(CALLBACK_METHOD.ON_CONNECTION_ERROR.ordinal(), methodName);
    }

    public void setOnLoginSuccessfulCallback(String methodName)
    {
        setCallbackMethod(CALLBACK_METHOD.ON_LOGIN_SUCCESSFUL.ordinal(), methodName);
    }

    public void setOnLoginFailedCallback(String methodName)
    {
        setCallbackMethod(CALLBACK_METHOD.ON_LOGIN_FAILED.ordinal(), methodName);
    }

    public void setOnContactStatusChangedCallback(String methodName)
    {
        setCallbackMethod(CALLBACK_METHOD.ON_CONTACT_STATUS_CHANGED.ordinal(), methodName);
    }

    public void setOnMessageReceivedCallback(String methodName)
    {
        setCallbackMethod(CALLBACK_METHOD.ON_MESSAGE_RECEIVED.ordinal(), methodName);
    }
    public void setOnContactsReceivedCallback(String methodName)
    {
        setCallbackMethod(CALLBACK_METHOD.ON_CONTACTS_RECEIVED.ordinal(), methodName);
    }

//    @Override
//    public void close() throws IOException
//    {
//        destroyNativeNotifierProxy(m_nativeNotifierProxy);
//        m_nativeNotifierProxy = 0;
//    }


    public long getNativeNotifierProxy()
    {
        return m_nativeNotifierProxy;
    }

//    @Override
//    protected void finalize() throws Throwable
//    {
//        if (m_nativeNotifierProxy != 0)
//        {
//            close();
//        }
//    }

    private void setCallbackMethod(int callbackMethod, String methodName)
    {
        setCallbackMethodNative(m_nativeNotifierProxy,callbackMethod, methodName);
    }

    private native long createNativeNotifierProxy(JNIChatClientNotifier actualNotifier);

    private native void setCallbackMethodNative(long nativeNotifier, int callbackMethod, String methodName);

//    private native void destroyNativeNotifierProxy(long nativeNotifier);

}
