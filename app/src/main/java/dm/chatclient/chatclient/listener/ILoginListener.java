package dm.chatclient.chatclient.listener;

import dm.chatclient.model.UserDetails;

/**
 * Created by Ice on 8/9/2015.
 */
public interface ILoginListener extends IBaseListener
{
    void onConnected();
    void onConnectionError();
    void onLoginSuccessful(UserDetails userDetails);
    void onLoginFailed(String reason);
}
