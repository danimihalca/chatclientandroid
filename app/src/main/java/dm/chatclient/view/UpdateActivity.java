package dm.chatclient.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import dm.chatclient.ChatClientApplication;
import dm.chatclient.R;
import dm.chatclient.chatclient.listener.IRegisterUpdateListener;
import dm.chatclient.chatclient.listener.IRuntimeListener;
import dm.chatclient.chatclient.listener.IUpdateListener;
import dm.chatclient.chatclient.notifier.IChatClientNotifier;
import dm.chatclient.controller.IChatClientController;
import dm.chatclient.model.Contact;
import dm.chatclient.model.Message;
import dm.chatclient.model.User;
import dm.chatclient.utils.ToastDisplayer;

public class UpdateActivity extends AppCompatActivity implements IUpdateListener
{
    private IChatClientController m_controller;
    private User m_user;

    private Button m_updateRegisterButton;

    private EditText m_username;
    private EditText m_password;
    private EditText m_firstname;
    private EditText m_lastname;
    private boolean m_isVisible;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_update);

        m_controller = ((ChatClientApplication) getApplication()).getController();
        m_updateRegisterButton = (Button) findViewById(R.id.registerUpdateButton);
        m_user = new User();
        m_isVisible = true;

        m_username=(EditText) findViewById(R.id.userNameInput);
        m_password=(EditText) findViewById(R.id.passwordInput);
        m_firstname=(EditText) findViewById(R.id.firstNameInput);
        m_lastname=(EditText) findViewById(R.id.lastNameInput);

        m_controller.addUpdateListener(this);


        setTitle("Update");
        m_updateRegisterButton.setText("Update");
        m_updateRegisterButton.setOnClickListener(new UpdateButtonListener());

        User existingUser = m_controller.getUser();

        m_username.setText(existingUser.getUserName());
        m_password.setText(existingUser.getPassword());
        m_firstname.setText(existingUser.getFirstName());
        m_lastname.setText(existingUser.getLastName());

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
    }

    @Override
    public void onContactsReceived()
    {
    }

    @Override
    public void onContactStatusChanged(final Contact contact)
    {
        if (m_isVisible)
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

    @Override
    public void onMessageReceived(final Message message)
    {
        if (m_isVisible)
        {
            this.runOnUiThread(new Runnable()
            {
                public void run()
                {
                    {
                        ToastDisplayer.displayToast(getApplicationContext(), message.getSender().getFirstName() + " send you a message");
                    }
                }
            });
        }
    }

    @Override
    public void onRemovedByContact(final Contact contact)
    {
        m_controller.removeContact(contact, true);
        if (m_isVisible)
        {
            this.runOnUiThread(new Runnable()
            {
                public void run()
                {
                    ToastDisplayer.displayToast(getApplicationContext(), contact.getUserName() + " removed you from his contacts");
                }
            });
        }
    }

    @Override
    public void onAddContactResponse(String userName, IChatClientNotifier.ADD_REQUEST_STATUS status)
    {

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
            Log.d("onAddingByContact", requester);

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
    public void onRegisterUpdateResponse(final IChatClientNotifier.REGISTER_UPDATE_USER_STATUS status)
    {
        this.runOnUiThread(new Runnable()
        {
            public void run()
            {
                ToastDisplayer.displayToast(getApplicationContext(), "STATUS : " + status.toString());
                if (status == IChatClientNotifier.REGISTER_UPDATE_USER_STATUS.USER_OK)
                {
                    m_controller.setUser(m_user);
                    m_controller.removeUpdateListener(UpdateActivity.this);
                    finish();
                }
            }
        });
    }

    @Override
    public void onDisconnected()
    {

    }


    class UpdateButtonListener implements View.OnClickListener
    {
        @Override
        public void onClick(View view)
        {
            m_user.setUserName(m_username.getText().toString());
            m_user.setPassword(m_password.getText().toString());
            m_user.setFirstName(m_firstname.getText().toString());
            m_user.setLastName(m_lastname.getText().toString());

            m_controller.updateUser(m_user);
        }
    }

}
