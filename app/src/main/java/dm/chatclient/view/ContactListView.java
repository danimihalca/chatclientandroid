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
import dm.chatclient.model.Contact;
import dm.chatclient.model.Message;
import dm.chatclient.utils.ContactListAdapter;
import dm.chatclient.utils.ToastDisplayer;

public class ContactListView extends AppCompatActivity implements IRuntimeListener
{
    private IChatClientController controller;
    private boolean close;
    private boolean isVisible;
    private ListView contactListView;
    ContactListAdapter contactListAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        contactListView = (ListView) findViewById(R.id.contactListView);
        contactListAdapter = new ContactListAdapter(getApplicationContext());
        contactListView.setAdapter(contactListAdapter);
        contactListView.setOnItemClickListener(new ContactClickListener());
        registerForContextMenu(contactListView);

        controller = ((ChatClientApplication) getApplication()).getController();
        controller.addRuntimeListener(this);
        controller.requestContacts();

        isVisible =true;
        close = false;

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
                args.putInt("state", controller.getState().ordinal());
                fragment.setArguments(args);
                fragment.show(getSupportFragmentManager(),"Change state");
            }
        });

        Button updateButton = (Button) findViewById(R.id.updateButton);
        updateButton.setOnClickListener(new UpdateButtonListener());
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        isVisible = false;
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        isVisible = true;
        close = false;
        contactListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed()
    {
        if (close)
        {
            controller.removeRuntimeListener(this);
            controller.disconnect();
            super.onBackPressed();
        }
        else
        {
            ToastDisplayer.displayToast(getApplicationContext(), "Press again to exit");
            close = true;
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
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
            controller.removeContact(contact, true);
            ToastDisplayer.displayToast(getApplicationContext(), contact.getUserName() + " has been removed from contacts");
            contactListAdapter.remove(contact);
            contactListAdapter.notifyDataSetChanged();
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
        this.runOnUiThread(new Runnable()
        {
            public void run()
            {
                if (isVisible)
                {
                    ToastDisplayer.displayToast(getApplicationContext(), contact.getUserName() + " removed you from his contacts");
                }
                contactListAdapter.remove(contact);
                contactListAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onAddContactResponse(final String userName, final IChatClientNotifier.ADD_REQUEST_STATUS status)
    {
        if (isVisible)
        {
            this.runOnUiThread(new Runnable()
            {
                public void run()
                {
                    ToastDisplayer.displayToast(getApplicationContext(), userName + ":"+status.toString());
                }
            });
        }
    }

    @Override
    public boolean onAddRequest(String requester)
    {
        if (isVisible)
        {
            final AddContactRequestDialogFragment requestFragment = new AddContactRequestDialogFragment();
            Bundle args = new Bundle();
            args.putString("userName", requester);
            requestFragment.setArguments(args);
            Log.d("onAddRequest", requester);

            this.runOnUiThread(new Runnable()
            {
                public void run()
                {
                    requestFragment.show(getSupportFragmentManager(),"req");
                }
            });

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
       controller.removeRuntimeListener(this);
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
               contactListAdapter.setContactList(controller.getContacts());
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
        controller.addContact(userName);
    }


    class ContactClickListener implements ListView.OnItemClickListener
    {

       @Override
       public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
       {
           Intent intent = new Intent(ContactListView.this, ConversationView.class);

           Contact c = (Contact) adapterView.getItemAtPosition(i);

           intent.putExtra("ContactId", c.getId());
           startActivity(intent);
       }
    }

    class UpdateButtonListener implements View.OnClickListener
    {
        @Override
        public void onClick(View view)
        {
            Intent intent = new Intent(ContactListView.this, UpdateView.class);
            startActivity(intent);
        }
    }
}
