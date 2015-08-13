package dm.chatclient.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import dm.chatclient.R;

import dm.chatclient.chatclient.listener.ILoginListener;
import dm.chatclient.chatclient.notifier.IChatClientNotifier;
import dm.chatclient.controller.IChatClientController;

import dm.chatclient.ChatClientApplication;
import dm.chatclient.model.User;
import dm.chatclient.model.UserDetails;
import dm.chatclient.utils.ToastDisplayer;


public class LoginActivity extends AppCompatActivity implements ILoginListener
{
    private IChatClientController m_controller;

    private String m_userName;
    private String m_password;

    Button m_loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        m_controller = ((ChatClientApplication) getApplication()).getController();
        m_controller.setLoginListener(LoginActivity.this);


        m_loginButton =(Button) findViewById(R.id.loginButton);
        m_loginButton.setOnClickListener(new LoginButtonListener());

        Button advancedSettingsButton =(Button) findViewById(R.id.advancedSettingsButton);
        advancedSettingsButton.setOnClickListener(new AdvancedSettingsButtonListener());
    }


    @Override
    public void onConnected()
    {

        m_userName = ((EditText) findViewById(R.id.usernameInput)).getText().toString();
        m_password = ((EditText) findViewById(R.id.passwordInput)).getText().toString();
        m_controller.login(m_userName, m_password);
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
    public void onLoginSuccessful(UserDetails userDetails)
    {
        User user = new User(userDetails.getId(),m_userName,m_password,userDetails.getFirstName(),userDetails.getLastName());
        m_controller.setUser(user);

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
    public void onLoginFailed(final IChatClientNotifier.AUTHENTICATION_STATUS reason)
    {
        this.runOnUiThread(new Runnable()
        {
            public void run()
            {
                m_loginButton.setEnabled(true);
                ToastDisplayer.displayToast(getApplicationContext(), "Login failed: " + reason.toString());
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
//                 Log.d("onLoginFailed")
                 ToastDisplayer.displayToast(getApplicationContext(),"Connection error!");
             }
         });
     }


     class LoginButtonListener implements View.OnClickListener
     {
         @Override
         public void onClick(View view)
         {

             m_loginButton.setEnabled(false);

             m_controller.connect("192.168.0.3", 9003);


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