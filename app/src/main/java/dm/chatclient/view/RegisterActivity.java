package dm.chatclient.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
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

public class RegisterActivity extends AppCompatActivity implements IRegisterListener
{
    private IChatClientController m_controller;
    private User m_user;

    private Button m_updateRegisterButton;

    private EditText m_username;
    private EditText m_password;
    private EditText m_firstname;
    private EditText m_lastname;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_update);

        m_controller = ((ChatClientApplication) getApplication()).getController();
        m_updateRegisterButton = (Button) findViewById(R.id.registerUpdateButton);
        m_user = new User();

        m_username=(EditText) findViewById(R.id.userNameInput);
        m_password=(EditText) findViewById(R.id.passwordInput);
        m_firstname=(EditText) findViewById(R.id.firstNameInput);
        m_lastname=(EditText) findViewById(R.id.lastNameInput);

        m_controller.addRegisterListener(this);


        setTitle("Register");
        m_updateRegisterButton.setText("Register");
        m_updateRegisterButton.setOnClickListener(new RegisterButtonListener());
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
                    m_controller.removeRegisterListener(RegisterActivity.this);
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
    public void onConnected()
    {
        m_controller.setConnected(true);
        m_controller.registerUser(m_user);
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
            m_user.setUserName(m_username.getText().toString());
            m_user.setPassword(m_password.getText().toString());
            m_user.setFirstName(m_firstname.getText().toString());
            m_user.setLastName(m_lastname.getText().toString());
            if (m_controller.isConnected())
            {
                m_controller.registerUser(m_user);
            }
            else
            {
                m_controller.connect("192.168.0.3",9003);
            }
        }
    }

}
