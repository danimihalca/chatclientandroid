package dm.chatclient.activities;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import dm.chatclient.ChatClientApplication;
import dm.chatclient.R;
import dm.chatclient.controller.IChatClientController;
import dm.chatclient.controller.IChatClientListener;
import dm.chatclient.model.Contact;
import dm.chatclient.model.Message;
import dm.chatclient.utils.MessageListAdapter;

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
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        m_controller = ((ChatClientApplication) getApplication()).getController();
        m_controller.addListener(this);

        m_messageInput = (EditText) findViewById(R.id.messageInput);

        Intent intent = getIntent();
        int id = intent.getIntExtra("ContactId", -1);
        String userName = intent.getStringExtra("ContactUserName");
        String fullName = intent.getStringExtra("ContactFullName");
        Boolean isOnline = intent.getBooleanExtra("ContactOnline", false);
        m_contact = new Contact(id,userName,fullName,isOnline);
        setTitle(fullName);

        m_sendButton = (Button) findViewById(R.id.sendButton);
        m_sendButton.setOnClickListener(new SendButtonListener());

        m_messageListView = (ListView) findViewById(R.id.conversationListView);
        m_messageListAdapter = new MessageListAdapter(getApplicationContext());
        m_messageListView.setAdapter(m_messageListAdapter);

    }

    class SendButtonListener implements View.OnClickListener
    {

        @Override
        public void onClick(View view)
        {
            String message = m_messageInput.getText().toString();
            m_controller.sendMessage(m_contact.getId(), message);
        }
    }


    @Override
    public void onNewMessage(final String message)
    {
        this.runOnUiThread(new Runnable()
        {
            public void run()
            {
                m_messageListAdapter.add(new Message("SENDER",message, Message.Message_Type.RECEIVED, null));
                m_messageListAdapter.notifyDataSetChanged();
            }
        });
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
    }
}
