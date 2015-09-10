package dm.chatclient;

import android.app.Application;
import dm.chatclient.controller.ChatClientController;
import dm.chatclient.controller.IChatClientController;

public class ChatClientApplication extends Application
{
    IChatClientController chatClientController;
    public String serverAddress;
    public int serverPort;
    @Override
    public void onCreate()
    {
        chatClientController = new ChatClientController();
        serverAddress = "Howldron";
        serverPort = 9003;
//        chatClientController.setServer(serverAddress, serverPort);
    }

    public IChatClientController getController()
    {
        return chatClientController;
    }
}
