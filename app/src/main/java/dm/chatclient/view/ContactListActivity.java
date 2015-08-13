package dm.chatclient.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import dm.chatclient.ChatClientApplication;
import dm.chatclient.R;
import dm.chatclient.chatclient.listener.IRuntimeListener;
import dm.chatclient.chatclient.notifier.IChatClientNotifier;
import dm.chatclient.controller.IChatClientController;
import dm.chatclient.model.BaseUser;
import dm.chatclient.model.Contact;
import dm.chatclient.model.Message;
import dm.chatclient.utils.ContactListAdapter;
import dm.chatclient.utils.ToastDisplayer;

public class ContactListActivity extends AppCompatActivity implements IRuntimeListener
{
    private IChatClientController m_controller;
    private boolean m_close;
    private boolean m_isVisible;
//    private List<Contact> m_contacts;
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
        registerForContextMenu(m_contactListView);

        m_controller = ((ChatClientApplication) getApplication()).getController();
        m_controller.addRuntimeListener(this);
        m_controller.requestContacts();

        m_isVisible =true;
        m_close = false;

        Button addButton = (Button) findViewById(R.id.addContactButton);
        addButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                AddContactDialogFragment d = new AddContactDialogFragment();
                d.show(getSupportFragmentManager(),"Add");
            }
        });

        Button changeStateButton = (Button) findViewById(R.id.changeStateButton);
        changeStateButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ChangeStateDialogFragment fragment = new ChangeStateDialogFragment();
                Bundle args = new Bundle();
                args.putInt("state", m_controller.getState().ordinal());
                fragment.setArguments(args);
                fragment.show(getSupportFragmentManager(),"Change state");
            }
        });
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        m_isVisible = false;
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        m_isVisible = true;
        m_close = false;
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
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
//        menu.add(0, 3, 2, "Delete");
        menu.add("Delete");
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Contact contact = contactListAdapter.getItem(info.position);
        if (item.getTitle() == "Delete")
        {
            Log.d("Delete", contact.getUserName());
            m_controller.removeContact(contact, true);
            ToastDisplayer.displayToast(getApplicationContext(), contact.getUserName() + " has been removed from contacts");
            contactListAdapter.remove(contact);
            contactListAdapter.notifyDataSetChanged();
//            contactListAdapter.remove();
        }
        return true;
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
    public void onRemovedByContact(final Contact contact)
    {
        Log.d("onRemovedByContact", contact.getUserName());
        m_controller.removeContact(contact, true);
        this.runOnUiThread(new Runnable()
        {
            public void run()
            {
                ToastDisplayer.displayToast(getApplicationContext(), contact.getUserName() + " removed you from his contacts");
                contactListAdapter.remove(contact);
                contactListAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onAddContactResponse(final String userName, final IChatClientNotifier.ADD_REQUEST_STATUS status)
    {
        Log.d("onAddContactResponse",userName + " "+ status.toString());
        this.runOnUiThread(new Runnable()
        {
            public void run()
            {
                ToastDisplayer.displayToast(getApplicationContext(), userName + ":"+status.toString());
            }
        });
    }

    @Override
    public boolean onAddingByContact(String requester)
    {
        if (m_isVisible)
        {
            final AddContactRequestDialogFragment requestFragment = new AddContactRequestDialogFragment();
            Bundle args = new Bundle();
            args.putString("userName", requester);
            requestFragment.setArguments(args);
            Log.d("onAddingByContact",requester);

            if (m_isVisible)
            {
                this.runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        requestFragment.show(getSupportFragmentManager(),"req");
                    }
                });
            }
            while (!requestFragment.isDecided())
            {
                try
                {
                    Thread.sleep(100);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
            return requestFragment.isAccepted();
        }

        return false;
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
       Log.d("OnContactsReceived", "");
       this.runOnUiThread(new Runnable()
       {
           public void run()
           {
               contactListAdapter.setContactList(m_controller.getContacts());
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

    public void addContact(String userName)
    {
        Log.d("addContact",userName);
        m_controller.addContact(userName);
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
