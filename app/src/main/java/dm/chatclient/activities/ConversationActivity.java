package dm.chatclient.activities;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import dm.chatclient.ChatClientApplication;
import dm.chatclient.R;
import dm.chatclient.controller.IChatClientController;
import dm.chatclient.controller.IChatClientListener;
import dm.chatclient.model.Contact;
import dm.chatclient.model.Message;
import dm.chatclient.utils.MessageListAdapter;
import dm.chatclient.utils.ToastDisplayer;

import java.util.List;


public class ConversationActivity extends AppCompatActivity implements IChatClientListener
{
    private IChatClientController m_controller;

    private Contact m_contact;
    private ListView m_messageListView;
    private Button m_sendButton;
    private EditText m_messageInput;

    private MessageListAdapter m_messageListAdapter;

    @Override
    public void onBackPressed()
    {
        m_controller.removeListener(this);
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        m_controller = ((ChatClientApplication) getApplication()).getController();
        m_controller.addListener(this);

        m_messageInput = (EditText) findViewById(R.id.messageInput);

        Intent intent = getIntent();
        int id = intent.getIntExtra("ContactId", -1);

        m_contact = m_controller.getContact(id);
        if (m_contact.getUnreadMessagesCount() != 0)
        {
            //TODO: verify if last view from listview is visibile
            // or how many and decrement from unread messages count
            m_contact.setUnreadMessagesCount(0);
            m_controller.notifyOnContactUpdated(m_contact);
        }
        setTitle(m_contact.getFullName());

        m_sendButton = (Button) findViewById(R.id.sendButton);
        m_sendButton.setOnClickListener(new SendButtonListener());

        List<Message> receivedMessages = m_controller.getMessages(m_contact);
        m_messageListView = (ListView) findViewById(R.id.conversationListView);
        m_messageListAdapter = new MessageListAdapter(getApplicationContext(),receivedMessages);
        m_messageListView.setAdapter(m_messageListAdapter);

    }

    class SendButtonListener implements View.OnClickListener
    {

        @Override
        public void onClick(View view)
        {
            String message = m_messageInput.getText().toString();
            m_controller.sendMessage(m_contact.getId(), message);
            m_messageListAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public boolean onNewMessage(final Message message)
    {
        if (m_contact.equals(message.getSender()))
        {
            this.runOnUiThread(new Runnable()
            {
                public void run()
                {
                    m_messageListAdapter.notifyDataSetChanged();
                }
            });
            return true;
        }
        return false;
    }

    @Override
    public void onConnected()
    {
    }

    @Override
    public void onDisconnected()
    {
        finish();
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
    public void onContactUpdated(Contact contact)
    {

    }

    @Override
    public void onContactsReceived(List<Contact> contactList)
    {
    }

    @Override
    public void onContactOnlineStatusChanged(final Contact contact)
    {
        this.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                ToastDisplayer.displayToast(getApplicationContext(), contact.getFullName() + " is now " + (contact.isOnline() ? "online" : "offline"));
            }
        });
    }
}
