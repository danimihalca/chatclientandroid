package dm.chatclient.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import dm.chatclient.R;

import dm.chatclient.chatclient.listener.ILoginListener;
import dm.chatclient.chatclient.notifier.IChatClientNotifier;
import dm.chatclient.controller.IChatClientController;

import dm.chatclient.ChatClientApplication;
import dm.chatclient.model.BaseUser;
import dm.chatclient.model.User;
import dm.chatclient.model.UserDetails;
import dm.chatclient.utils.ToastDisplayer;


public class LoginActivity extends AppCompatActivity implements ILoginListener
{
    private IChatClientController m_controller;

    private String m_userName;
    private String m_password;

    private boolean m_performLogin;
    private boolean m_isvisible;

    Button m_loginButton;

    @Override
    protected void onDestroy()
    {
        m_controller.removeLoginListener(this);
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        m_isvisible =true;

        m_controller = ((ChatClientApplication) getApplication()).getController();
        m_controller.addLoginListener(LoginActivity.this);

        m_loginButton =(Button) findViewById(R.id.loginButton);
        m_loginButton.setOnClickListener(new LoginButtonListener());

        Button advancedSettingsButton =(Button) findViewById(R.id.advancedSettingsButton);
        advancedSettingsButton.setOnClickListener(new AdvancedSettingsButtonListener());

        Button registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new RegisterButtonListener());
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        m_isvisible =false;
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        m_isvisible =true;
    }


    @Override
    public void onConnected()
    {
        if (m_performLogin)
        {
            m_controller.setConnected(true);
            doLogin();
            m_performLogin = false;
        }

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
             if (m_controller.isConnected())
             {
                 doLogin();
                 m_performLogin = false;
             }
             else
             {
                 m_performLogin = true;
                 m_controller.connect("192.168.0.3",9003);
             }

         }
     }

    private void doLogin()
    {
        m_userName = ((EditText) findViewById(R.id.usernameInput)).getText().toString();
        m_password = ((EditText) findViewById(R.id.passwordInput)).getText().toString();

        BaseUser.USER_STATE state = ((CheckBox) findViewById(R.id.invisibleBox))
                .isChecked()? BaseUser.USER_STATE.INVISIBLE:
                BaseUser.USER_STATE.ONLINE;
        m_controller.login(m_userName, m_password, state);
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

    class RegisterButtonListener implements View.OnClickListener
    {
        @Override
        public void onClick(View view)
        {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        }
    }
 }