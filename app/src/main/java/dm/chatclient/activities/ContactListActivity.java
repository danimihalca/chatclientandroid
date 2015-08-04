package dm.chatclient.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.widget.ListView;
import dm.chatclient.ChatClientApplication;
import dm.chatclient.R;
import dm.chatclient.controller.IChatClientController;
import dm.chatclient.controller.IChatClientListener;
import dm.chatclient.model.Contact;
import dm.chatclient.utils.ContactListAdapter;

import java.util.List;

public class ContactListActivity extends AppCompatActivity implements IChatClientListener
{
    private IChatClientController m_controller;

    private ListView m_contactListView;
    ContactListAdapter contactListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        m_contactListView = (ListView) findViewById(R.id.contactListView);
        contactListAdapter = new ContactListAdapter(getApplicationContext());
        m_contactListView.setAdapter(contactListAdapter);

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

    @Override
    public void onContactsReceived(List<Contact> contactList)
    {
        Log.d("OnContactsReceived", contactList.toString());

        contactListAdapter.setContactList(contactList);
        contactListAdapter.notifyDataSetChanged();
    }
}
