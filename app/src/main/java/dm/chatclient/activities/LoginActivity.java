package dm.chatclient.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import dm.chatclient.R;

import dm.chatclient.controller.IChatClientController;
import dm.chatclient.controller.IChatClientListener;

import dm.chatclient.ChatClientApplication;
import dm.chatclient.model.Contact;
import dm.chatclient.model.Message;
import dm.chatclient.utils.ToastDisplayer;

import java.util.List;

public class LoginActivity extends AppCompatActivity implements IChatClientListener
{
    private IChatClientController m_controller;

    private EditText m_usernameInput;
    private EditText m_passwordInput;

    Button m_loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        m_controller = ((ChatClientApplication) getApplication()).getController();

        m_usernameInput = (EditText) findViewById(R.id.usernameInput);
        m_passwordInput = (EditText) findViewById(R.id.passwordInput);

        m_loginButton =(Button) findViewById(R.id.loginButton);
        m_loginButton.setOnClickListener(new LoginButtonListener());

        Button advancedSettingsButton =(Button) findViewById(R.id.advancedSettingsButton);
        advancedSettingsButton.setOnClickListener(new AdvancedSettingsButtonListener());
    }

    @Override
    protected void onDestroy()
    {
        m_controller.removeListener(this);
        super.onDestroy();
    }


    @Override
    public boolean onNewMessage(Message message)
    {
        return false;
    }

    @Override
    public void onConnected()
    {
    }

    @Override
    public void onDisconnected()
    {
        this.runOnUiThread(new Runnable()
        {
            public void run()
            {
                m_loginButton.setEnabled(true);
                ToastDisplayer.displayToast(getApplicationContext(), "Disconnected!");
            }
        });
    }

    @Override
    public void onLoginSuccessful()
    {
        this.runOnUiThread(new Runnable()
        {
            public void run()
            {
                ToastDisplayer.displayToast(getApplicationContext(), "Login successful!");
            }
        });
        Intent intent = new Intent(LoginActivity.this, ContactListActivity.class);
        startActivity(intent);
    }

    @Override
    public void onLoginFailed(final String message)
    {
        this.runOnUiThread(new Runnable()
        {
            public void run()
            {
                m_loginButton.setEnabled(true);
                ToastDisplayer.displayToast(getApplicationContext(), "Login failed: " + message);
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
                 m_loginButton.setEnabled(true);
                 ToastDisplayer.displayToast(getApplicationContext(),"Connection error!");
             }
         });
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
    public void onContactOnlineStatusChanged(Contact contact)
    {

    }

     class LoginButtonListener implements View.OnClickListener
     {
         @Override
         public void onClick(View view)
         {
             String username = m_usernameInput.getText().toString();
             String password = m_passwordInput.getText().toString();

             m_loginButton.setEnabled(false);

             m_controller.setServerProperties("192.168.0.3", 9003);
             m_controller.addListener(LoginActivity.this);

             m_controller.login(username, password);

         }
     }

     class AdvancedSettingsButtonListener implements View.OnClickListener
     {
         @Override
         public void onClick(View view)
         {
             Intent intent = new Intent(LoginActivity.this, AdvancedSettingsActivity.class);
             startActivity(intent);
         }
     }
 }