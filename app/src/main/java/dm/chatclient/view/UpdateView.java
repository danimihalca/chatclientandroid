package dm.chatclient.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import dm.chatclient.ChatClientApplication;
import dm.chatclient.R;
import dm.chatclient.chatclient.listener.IUpdateListener;
import dm.chatclient.chatclient.notifier.IChatClientNotifier;
import dm.chatclient.controller.IChatClientController;
import dm.chatclient.model.Contact;
import dm.chatclient.model.Message;
import dm.chatclient.model.User;
import dm.chatclient.utils.ToastDisplayer;

public class UpdateView extends AppCompatActivity implements IUpdateListener
{
    private IChatClientController controller;
    private User user;

    private Button updateRegisterButton;

    private EditText username;
    private EditText password;
    private EditText firstname;
    private EditText lastname;
    private boolean isVisible;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_update);

        controller = ((ChatClientApplication) getApplication()).getController();
        updateRegisterButton = (Button) findViewById(R.id.registerUpdateButton);
        user = new User();
        isVisible = true;

        username=(EditText) findViewById(R.id.userNameInput);
        password=(EditText) findViewById(R.id.passwordInput);
        firstname=(EditText) findViewById(R.id.firstNameInput);
        lastname=(EditText) findViewById(R.id.lastNameInput);

        controller.addUpdateListener(this);


        setTitle("Update");
        updateRegisterButton.setText("Update");
        updateRegisterButton.setOnClickListener(new UpdateButtonListener());

        User existingUser = controller.getUser();
        Log.d("update -existing",existingUser.toString());
        username.setText(existingUser.getUserName());
        password.setText(existingUser.getPassword());
        firstname.setText(existingUser.getFirstName());
        lastname.setText(existingUser.getLastName());
        Log.d("update -current", user.toString());

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
    public void onContactsReceived()
    {
    }

    @Override
    public void onContactStatusChanged(final Contact contact)
    {
        if (isVisible)
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
        if (isVisible)
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
        controller.removeContact(contact, true);
        if (isVisible)
        {
            this.runOnUiThread(new Runnable()
            {
                public void run()
                {
                    ToastDisplayer.displayToast(getApplicationContext(), contact.getUserName() + " removed you");
                }
            });
        }
    }

    @Override
    public void onAddContactResponse(String userName, IChatClientNotifier.ADD_REQUEST_STATUS status)
    {

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
    public void onRegisterUpdateResponse(final IChatClientNotifier.REGISTER_UPDATE_STATUS status)
    {
        this.runOnUiThread(new Runnable()
        {
            public void run()
            {
                ToastDisplayer.displayToast(getApplicationContext(), status.toString());
                if (status == IChatClientNotifier.REGISTER_UPDATE_STATUS.USER_OK)
                {
                    controller.setUser(user);
                    controller.removeUpdateListener(UpdateView.this);
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
            user.setUserName(username.getText().toString());
            user.setPassword(password.getText().toString());
            user.setFirstName(firstname.getText().toString());
            user.setLastName(lastname.getText().toString());

            controller.updateUser(user);
        }
    }

}
