package dm.chatclient.view;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import dm.chatclient.ChatClientApplication;
import dm.chatclient.R;
import dm.chatclient.chatclient.listener.IRuntimeListener;
import dm.chatclient.controller.IChatClientController;
import dm.chatclient.model.Contact;
import dm.chatclient.model.Message;
import dm.chatclient.utils.MessageListAdapter;
import dm.chatclient.utils.ToastDisplayer;

import java.util.List;


public class ConversationActivity extends AppCompatActivity implements IRuntimeListener
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
        m_controller.addRuntimeListener(this);

        m_messageInput = (EditText) findViewById(R.id.messageInput);

        Intent intent = getIntent();
        int id = intent.getIntExtra("ContactId", -1);

        m_contact = m_controller.getContact(id);
        m_contact.setUnreadMessagesCount(0);

//        if (m_contact.getUnreadMessagesCount() != 0)
//        {
//            //TODO: verify if last view from listview is visibile
//            // or how many and decrement from unread messages count
//            m_contact.setUnreadMessagesCount(0);
//
//            //TODO :XXXXXXXXXXXXXXXXXXXX SEE IF IT IS NECESSARY XXXXXXXXXXXXXXXXx
////            m_controller.notifyOnContactUpdated(m_contact);
//        }
        setTitle(m_contact.getFullName());

        m_sendButton = (Button) findViewById(R.id.sendButton);
        m_sendButton.setOnClickListener(new SendButtonListener());

        List<Message> receivedMessages = m_controller.getMessages(m_contact);
        m_messageListView = (ListView) findViewById(R.id.conversationListView);
        m_messageListAdapter = new MessageListAdapter(getApplicationContext(),receivedMessages);
        m_messageListView.setAdapter(m_messageListAdapter);

    }

    @Override
    public void onBackPressed()
    {
        m_controller.removeRuntimeListener(this);
        finish();
    }

    class SendButtonListener implements View.OnClickListener
    {

        @Override
        public void onClick(View view)
        {
            String messageText = m_messageInput.getText().toString();

            Message message = new Message(m_controller.getUser(),m_contact,messageText);
            m_controller.sendMessage(message);
            m_messageListAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onMessageReceived(final Message message)
    {
        this.runOnUiThread(new Runnable()
        {
            public void run()
            {
                if (m_contact.equals(message.getSender()))
                {
                    m_messageListAdapter.notifyDataSetChanged();
                }
                else
                {
                    ToastDisplayer.displayToast(getApplicationContext(), message.getSender().getFullName() + " send you a message");
                }
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
    }

    @Override
    public void onContactStatusChanged(final Contact contact)
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
