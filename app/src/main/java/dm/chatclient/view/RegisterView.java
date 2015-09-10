package dm.chatclient.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import dm.chatclient.ChatClientApplication;
import dm.chatclient.R;
import dm.chatclient.chatclient.listener.IRegisterListener;
import dm.chatclient.chatclient.notifier.IChatClientNotifier;
import dm.chatclient.controller.IChatClientController;
import dm.chatclient.model.User;
import dm.chatclient.utils.ToastDisplayer;

public class RegisterView extends AppCompatActivity implements IRegisterListener
{
    private IChatClientController controller;
    private User user;

    private Button updateRegisterButton;

    private EditText username;
    private EditText password;
    private EditText firstname;
    private EditText lastname;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_update);

        controller = ((ChatClientApplication) getApplication()).getController();
        updateRegisterButton = (Button) findViewById(R.id.registerUpdateButton);
        user = new User();

        username=(EditText) findViewById(R.id.userNameInput);
        password=(EditText) findViewById(R.id.passwordInput);
        firstname=(EditText) findViewById(R.id.firstNameInput);
        lastname=(EditText) findViewById(R.id.lastNameInput);

        controller.addRegisterListener(this);


        setTitle("Register");
        updateRegisterButton.setText("Register");
        updateRegisterButton.setOnClickListener(new RegisterButtonListener());
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
                    controller.removeRegisterListener(RegisterView.this);
                    finish();
                }
            }
        });
    }

    @Override
    public void onDisconnected()
    {
        this.runOnUiThread(new Runnable()
        {
            public void run()
            {
                ToastDisplayer.displayToast(getApplicationContext(),"Disconnected!");
            }
        });
    }


    @Override
    public void onConnectionError()
    {
        this.runOnUiThread(new Runnable()
        {
            public void run()
            {
                ToastDisplayer.displayToast(getApplicationContext(),"Connection error!");
            }
        });
    }

    class RegisterButtonListener implements View.OnClickListener
    {
        @Override
        public void onClick(View view)
        {
            user.setUserName(username.getText().toString());
            user.setPassword(password.getText().toString());
            user.setFirstName(firstname.getText().toString());
            user.setLastName(lastname.getText().toString());

            controller.registerUser(user);
        }
    }

}
