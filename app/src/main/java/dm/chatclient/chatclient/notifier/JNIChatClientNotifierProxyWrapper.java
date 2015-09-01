package dm.chatclient.chatclient.notifier;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by Ice on 8/9/2015.
 */
public class JNIChatClientNotifierProxyWrapper implements Closeable
{
    public enum CALLBACK_METHOD
    {
//        ON_CONNECTED,
        ON_DISCONNECTED,
        ON_CONNECTION_ERROR,
        ON_LOGIN_SUCCESSFUL,
        ON_LOGIN_FAILED,
        ON_CONTACT_STATE_CHANGED,
        ON_MESSAGE_RECEIVED,
        ON_CONTACTS_RECEIVED,

        ON_REMOVED_BY_CONTACT,
        ON_ADD_CONTACT_RESPONSE,
        ON_ADD_REQUEST,

        ON_REGISTER_UPDATE_RESPONSE


    }

    private long nativeNotifierProxyPtr;

    static
    {
        System.loadLibrary("chatClientJNI");
    }

    public JNIChatClientNotifierProxyWrapper(JNIChatClientNotifier actualNotifier)
    {
        nativeNotifierProxyPtr = createNativeNotifierProxy(actualNotifier);
    }

//    public void setOnConnectedCallback(String methodName)
//    {
//        setCallbackMethod(CALLBACK_METHOD.ON_CONNECTED, methodName);
//    }

//    public void setOnDisconnectedCallback(String methodName)
//    {
//        setCallbackMethod(CALLBACK_METHOD.ON_DISCONNECTED, methodName);
//    }
//
//
//    public void setOnConnectionErrorCallback(String methodName)
//    {
//        setCallbackMethod(CALLBACK_METHOD.ON_CONNECTION_ERROR, methodName);
//    }
//
//    public void setOnLoginSuccessfulCallback(String methodName)
//    {
//        setCallbackMethod(CALLBACK_METHOD.ON_LOGIN_SUCCESSFUL, methodName);
//    }
//
//    public void setOnLoginFailedCallback(String methodName)
//    {
//        setCallbackMethod(CALLBACK_METHOD.ON_LOGIN_FAILED, methodName);
//    }
//
//    public void setOnContactStatusChangedCallback(String methodName)
//    {
//    }
//
//    public void setOnMessageReceivedCallback(String methodName)
//    {
//        setCallbackMethod(CALLBACK_METHOD.ON_MESSAGE_RECEIVED, methodName);
//    }
//    public void setOnContactsReceivedCallback(String methodName)
//    {
//        setCallbackMethod(CALLBACK_METHOD.ON_CONTACTS_RECEIVED, methodName);
//    }
//
//    public void setOnRemovedByContactCallback(String methodName)
//    {
//        setCallbackMethod(CALLBACK_METHOD.ON_REMOVED_BY_CONTACT, methodName);
//    }
//
//    public void setOnAddContactResponseCallback(String methodName)
//    {
//        setCallbackMethod(CALLBACK_METHOD.ON_ADD_CONTACT_RESPONSE, methodName);
//    }
//    public void setOnAddingByContactCallback(String methodName)
//    {
//        setCallbackMethod(CALLBACK_METHOD.ON_ADD_REQUEST, methodName);
//    }
//
//    public void setOnRegisterUpdateResponse(String methodName)
//    {
//        setCallbackMethod(CALLBACK_METHOD.ON_REGISTER_UPDATE_RESPONSE, methodName);
//    }

    @Override
    public void close() throws IOException
    {
        destroyNativeNotifierProxy(nativeNotifierProxyPtr);
        nativeNotifierProxyPtr = 0;
    }


    public long getNativeNotifierProxyAddress()
    {
        return nativeNotifierProxyPtr;
    }

    public void setCallbackMethod(CALLBACK_METHOD callbackMethod, String methodName)
    {
        setCallbackMethodNative(nativeNotifierProxyPtr,callbackMethod.ordinal(), methodName);
    }

    @Override
    protected void finalize() throws Throwable
    {
        if (nativeNotifierProxyPtr != 0)
        {
            close();
        }
    }



    private native long createNativeNotifierProxy(JNIChatClientNotifier actualNotifier);

    private native void setCallbackMethodNative(long nativeNotifier, int callbackMethod, String methodName);

    private native void destroyNativeNotifierProxy(long nativeNotifier);

}
