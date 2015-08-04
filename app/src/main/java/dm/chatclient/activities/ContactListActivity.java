package dm.chatclient.activities;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;
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
    private boolean m_close;

    private ListView m_contactListView;
    ContactListAdapter contactListAdapter;

    @Override
    public void onBackPressed()
    {
        if (m_close)
        {
            m_controller.disconnect();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("EXIT", true);
            startActivity(intent);
        }
        else
        {
            displayToast("Press again to exit");
            m_close = true;
        }
    }

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

        m_close = false;
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
    public void onContactsReceived(final List<Contact> contactList)
    {
        Log.d("OnContactsReceived", contactList.toString());
            this.runOnUiThread(new Runnable()
            {
                public void run()
                {
                    contactListAdapter.setContactList(contactList);
                    contactListAdapter.notifyDataSetChanged();
                }
            });
    }

    private void displayToast(String message)
    {
        Context context = getApplicationContext();

        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.show();
    }
  }
