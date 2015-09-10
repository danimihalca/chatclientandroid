package dm.chatclient.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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


public class LoginView extends AppCompatActivity implements ILoginListener
{
    private IChatClientController controller;

    private String userName;
    private String password;

    Button loginButton;

    @Override
    protected void onDestroy()
    {
        controller.removeLoginListener(this);
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        controller = ((ChatClientApplication) getApplication()).getController();
        controller.addLoginListener(LoginView.this);
        controller.setServer(((ChatClientApplication) getApplication()).serverAddress,
                ((ChatClientApplication) getApplication()).serverPort);

        loginButton =(Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new LoginButtonListener());

        Button advancedSettingsButton =(Button) findViewById(R.id.advancedSettingsButton);
        advancedSettingsButton.setOnClickListener(new AdvancedSettingsButtonListener());

        Button registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new RegisterButtonListener());
    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }


    @Override
    public void onDisconnected()
    {
        this.runOnUiThread(new Runnable()
        {
            public void run()
            {
                loginButton.setEnabled(true);
                ToastDisplayer.displayToast(getApplicationContext(), "Disconnected!");
            }
        });
    }

    @Override
    public void onLoginSuccessful(UserDetails userDetails)
    {
        User user = new User(userDetails.getId(),userName,password,userDetails.getFirstName(),userDetails.getLastName());
        Log.d("loginview", user.toString());
        controller.setUser(user);

        this.runOnUiThread(new Runnable()
        {
            public void run()
            {
                ToastDisplayer.displayToast(getApplicationContext(), "Login successful!");
            }
        });
        Intent intent = new Intent(LoginView.this, ContactListView.class);
        startActivity(intent);
    }

    @Override
    public void onLoginFailed(final IChatClientNotifier.AUTHENTICATION_STATUS reason)
    {
        this.runOnUiThread(new Runnable()
        {
            public void run()
            {
                loginButton.setEnabled(true);
                ToastDisplayer.displayToast(getApplicationContext(), reason.toString());
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
                 loginButton.setEnabled(true);
                 ToastDisplayer.displayToast(getApplicationContext(),"Connection error!");
             }
         });
     }


     class LoginButtonListener implements View.OnClickListener
     {
         @Override
         public void onClick(View view)
         {
             loginButton.setEnabled(false);
             doLogin();
         }
     }

    private void doLogin()
    {
        userName = ((EditText) findViewById(R.id.usernameInput)).getText().toString();
        password = ((EditText) findViewById(R.id.passwordInput)).getText().toString();

        BaseUser.USER_STATE state = ((CheckBox) findViewById(R.id.invisibleBox))
                .isChecked()? BaseUser.USER_STATE.INVISIBLE:
                BaseUser.USER_STATE.ONLINE;
        controller.login(userName, password, state);
    }

     class AdvancedSettingsButtonListener implements View.OnClickListener
     {
         @Override
         public void onClick(View view)
         {
             Intent intent = new Intent(LoginView.this, SettingsActivity.class);
             startActivity(intent);
         }
     }

    class RegisterButtonListener implements View.OnClickListener
    {
        @Override
        public void onClick(View view)
        {
            Intent intent = new Intent(LoginView.this, RegisterView.class);
            startActivity(intent);
        }
    }
 }