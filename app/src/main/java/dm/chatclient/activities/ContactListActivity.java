package dm.chatclient.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import dm.chatclient.ChatClientApplication;
import dm.chatclient.R;
import dm.chatclient.controller.IChatClientController;
import dm.chatclient.controller.IChatClientListener;

public class ContactListActivity extends AppCompatActivity implements IChatClientListener
{
    private IChatClientController m_controller;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        m_controller = ((ChatClientApplication) getApplication()).getController();
        m_controller.addListener(this);
        m_controller.requestContacts();
    }

    @Override
    public void onNewMessage(String message)
    {
    }

    @Override
    public void onConnected()
    {
    }

    @Override
    public void onDisconnected()
    {
    }

    @Override
    public void onLoginSuccessful()
    {
    }

    @Override
    public void onLoginFailed(String message)
    {
    }

    @Override
    public void onConnectionError()
    {
    }
}
