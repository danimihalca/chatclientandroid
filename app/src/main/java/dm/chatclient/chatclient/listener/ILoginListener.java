package dm.chatclient.chatclient.listener;

/**
 * Created by Ice on 8/9/2015.
 */
public interface ILoginListener extends IBaseListener
{
    void onConnected();
    void onConnectionError();
    void onLoginSuccessful();
    void onLoginFailed(String reason);
}
