package dm.chatclient.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import dm.chatclient.ChatClientApplication;
import dm.chatclient.R;
import dm.chatclient.chatclient.listener.IRuntimeListener;
import dm.chatclient.controller.IChatClientController;
import dm.chatclient.model.Contact;
import dm.chatclient.model.Message;
import dm.chatclient.utils.ContactListAdapter;
import dm.chatclient.utils.ToastDisplayer;

import java.util.List;

public class ContactListActivity extends AppCompatActivity implements IRuntimeListener
{
    private IChatClientController m_controller;
    private boolean m_close;

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
        m_contactListView.setOnItemClickListener(new ContactClickListener());

        m_controller = ((ChatClientApplication) getApplication()).getController();
        m_controller.addRuntimeListener(this);
        m_controller.requestContacts();

        m_close = false;
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        contactListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed()
    {
        if (m_close)
        {
            m_controller.removeRuntimeListener(this);
            m_controller.disconnect();
            super.onBackPressed();
        }
        else
        {
            ToastDisplayer.displayToast(getApplicationContext(), "Press again to exit");
            m_close = true;
        }
    }

    @Override
    public void onMessageReceived(Message message)
    {
        this.runOnUiThread(new Runnable()
        {
            public void run()
            {
                   contactListAdapter.notifyDataSetChanged();
            }
        });
    }


    @Override
    public void onDisconnected()
    {
       m_controller.removeRuntimeListener(this);
       finish();
    }


    @Override
    public void onContactsReceived()
    {
       final List<Contact> contactList = m_controller.getContacts();
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

    @Override
    public void onContactStatusChanged(Contact contact)
    {
       this.runOnUiThread(new Runnable()
       {
           public void run()
           {
               contactListAdapter.notifyDataSetChanged();
           }
       });
    }


    class ContactClickListener implements ListView.OnItemClickListener
    {

       @Override
       public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
       {
           Intent intent = new Intent(ContactListActivity.this, ConversationActivity.class);

           Contact c = (Contact) adapterView.getItemAtPosition(i);

           intent.putExtra("ContactId", c.getId());
           startActivity(intent);
       }
    }
}
