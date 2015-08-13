package dm.chatclient.chatclient.listener;

import dm.chatclient.chatclient.notifier.IChatClientNotifier;

/**
 * Created by Ice on 8/13/2015.
 */
public interface IRegisterUpdateListener
{
    void onRegisterUpdateResponse(IChatClientNotifier.REGISTER_UPDATE_USER_STATUS status);
}
