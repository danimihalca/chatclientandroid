package dm.chatclient.view;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import dm.chatclient.ChatClientApplication;
import dm.chatclient.R;
import dm.chatclient.chatclient.listener.IRuntimeListener;
import dm.chatclient.chatclient.notifier.IChatClientNotifier;
import dm.chatclient.controller.IChatClientController;
import dm.chatclient.model.Contact;
import dm.chatclient.model.Message;
import dm.chatclient.utils.MessageListAdapter;
import dm.chatclient.utils.ToastDisplayer;

import java.util.List;


public class ConversationView extends AppCompatActivity implements IRuntimeListener
{
    private IChatClientController controller;

    private Contact contact;
    private ListView messageListView;
    private Button sendButton;
    private EditText messageInput;

    private MessageListAdapter messageListAdapter;
    private boolean isVisible;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        controller = ((ChatClientApplication) getApplication()).getController();
        controller.addRuntimeListener(this);

        messageInput = (EditText) findViewById(R.id.messageInput);

        Intent intent = getIntent();
        int id = intent.getIntExtra("ContactId", -1);

        contact = controller.getContact(id);
        contact.setUnreadMessagesCount(0);

        setTitle(contact.getFirstName()+" "+contact.getLastName());

        sendButton = (Button) findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new SendButtonListener());

        List<Message> receivedMessages = controller.getMessages(contact);
        messageListView = (ListView) findViewById(R.id.conversationListView);
        messageListAdapter = new MessageListAdapter(getApplicationContext(),receivedMessages);
        messageListView.setAdapter(messageListAdapter);

        isVisible =true;
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
    }

    @Override
    public void onBackPressed()
    {
        controller.removeRuntimeListener(this);
        finish();
    }

    class SendButtonListener implements View.OnClickListener
    {

        @Override
        public void onClick(View view)
        {
            String messageText = messageInput.getText().toString();

            Message message = new Message(controller.getUser(),contact,messageText);
            controller.sendMessage(message);
            messageListAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onMessageReceived(final Message message)
    {
        this.runOnUiThread(new Runnable()
        {
            public void run()
            {
                if (contact.equals(message.getSender()))
                {
                    contact.setUnreadMessagesCount(0);
                    messageListAdapter.notifyDataSetChanged();
                }
                else
                {
                    ToastDisplayer.displayToast(getApplicationContext(), message.getSender().getFirstName() + " send you a message");
                }
            }
        });
    }

    @Override
    public void onRemovedByContact(final Contact contact)
    {
        if (contact.getId() == contact.getId())
        {
            this.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    finish();
                }
            });
        }
        else
        {
            if (isVisible)
            {
                controller.removeContact(contact, true);
                this.runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        ToastDisplayer.displayToast(getApplicationContext(), contact.getUserName() + " removed you from his contacts");
                    }
                });
            }
        }
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
                if (status == IChatClientNotifier.ADD_REQUEST_STATUS.ADD_YOURSELF)
                {
                    ToastDisplayer.displayToast(getApplicationContext(), status.toString());
                }
                else
                {
                    ToastDisplayer.displayToast(getApplicationContext(), userName + " "+status.toString());
                }
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
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                setTitle(contact.getFirstName()+" "+contact.getLastName());
                messageListAdapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public void onContactStatusChanged(final Contact contact)
    {
        this.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                ToastDisplayer.displayToast(getApplicationContext(), contact.getFirstName() + " is now " + contact.getState().toString());
            }
        });
    }
}
