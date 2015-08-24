package dm.chatclient;

import android.app.Application;
import dm.chatclient.controller.ChatClientController;
import dm.chatclient.controller.IChatClientController;

public class ChatClientApplication extends Application
{
    IChatClientController chatClientController;

    @Override
    public void onCreate()
    {
        chatClientController = new ChatClientController();
    }

    public IChatClientController getController()
    {
        return chatClientController;
    }
}
