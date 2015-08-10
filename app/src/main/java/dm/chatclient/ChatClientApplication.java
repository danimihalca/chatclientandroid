package dm.chatclient;

import android.app.Application;
import dm.chatclient.controller.ChatClientController;
import dm.chatclient.controller.IChatClientController;

public class ChatClientApplication extends Application
{
    IChatClientController m_chatClientController;

    @Override
    public void onCreate()
    {
        m_chatClientController = new ChatClientController();
    }

    public IChatClientController getController()
    {
        return m_chatClientController;
    }
}
