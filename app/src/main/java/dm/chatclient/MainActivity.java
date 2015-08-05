package dm.chatclient;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import dm.chatclient.controller.NativeChatClientController;
import dm.chatclient.controller.IChatClientListener;
import dm.chatclient.model.Contact;
import dm.chatclient.model.Message;

import java.io.IOException;
import java.util.List;


public class MainActivity extends AppCompatActivity implements IChatClientListener
{
    private NativeChatClientController client;
    Button connectButton;
    Button disconnectButton;
    EditText addressField;
    EditText portField;
    EditText usernameField;
    EditText passwordField;
    EditText messageField;
    TextView messagesView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connectButton = (Button) findViewById(R.id.connectButton);
        disconnectButton = (Button) findViewById(R.id.disconnectButton);
        addressField = (EditText) findViewById(R.id.addressField);
        portField = (EditText) findViewById(R.id.portField);
        usernameField = (EditText) findViewById(R.id.userNameField);
        passwordField = (EditText) findViewById(R.id.passwordField);

        messageField = (EditText) findViewById(R.id.messageField);
        messagesView = (TextView) findViewById(R.id.messagesView);
        disconnectButton.setEnabled(false);
        connectButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                connectButton.setEnabled(false);
                client.setServerProperties(addressField.getText().toString(), Integer.parseInt(portField.getText()
                                                                                                       .toString()));
                client.login(usernameField.getText().toString(),passwordField.getText().toString());
            }
        });

        disconnectButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                client.disconnect();
                disconnectButton.setEnabled(false);
            }
        });
        messageField.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE))
                {
                    performSend();
                }
                return false;
            }
        });

        Button sendButton = (Button) findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                performSend();
            }
        });

        client = new NativeChatClientController();
        client.addListener(this);
    }

    private void performSend()
    {
        String message = messageField.getText().toString();
        String username = usernameField.getText().toString();
//        client.sendMessage('<' + username + ">:" + message);
        messageField.setText("");
    }

    @Override
    protected void onStop()
    {
        Log.i("MainActivity", "onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy()
    {
        Log.i("MainActivity", "onDestroy");
        try
        {
            client.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        client = null;
        super.onDestroy();
    }

    @Override
    protected void onStart()
    {
        Log.i("MainActivity", "onStart");
        super.onStart();
    }


//    @Override
//    public void onNewMessage(final String message)
//    {
//        MainActivity.this.runOnUiThread(new Runnable()
//        {
//
//            public void run()
//            {
//
//                messagesView.append(message+"\n");
//                final ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
//                scrollView.post(new Runnable()
//                {
//                    @Override
//                    public void run()
//                    {
//                        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
//                    }
//                });
//            }
//        });
//    }

    @Override
    public boolean onNewMessage(Message message)
    {
        return false;
    }

    @Override
    public void onConnected()
    {
        MainActivity.this.runOnUiThread(new Runnable()
        {
            public void run()
            {
                messagesView.append("CONNECTED\n");
                Context context = getApplicationContext();
                CharSequence text = "Connected!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

                disconnectButton.setEnabled(true);
                addressField.setEnabled(false);
                usernameField.setEnabled(false);
            }
        });
    }

    @Override
    public void onDisconnected()
    {
        MainActivity.this.runOnUiThread(new Runnable()
        {

            public void run()
            {
                messagesView.append("DISCONNECTED\n");
                Context context = getApplicationContext();
                CharSequence text = "Disconnected!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

                connectButton.setEnabled(true);
                addressField.setEnabled(true);
                usernameField.setEnabled(true);
            }
        });
    }

    @Override
    public void onLoginSuccessful()
    {
        MainActivity.this.runOnUiThread(new Runnable()
        {

            public void run()
            {
                messagesView.append("Login successfull\n");
                Context context = getApplicationContext();
                CharSequence text = "Login successfull!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });
    }

    @Override
    public void onLoginFailed(final String message)
    {
        MainActivity.this.runOnUiThread(new Runnable()
        {

            public void run()
            {
                messagesView.append("Login failed:"+message+"\n");
                Context context = getApplicationContext();
                CharSequence text = "Login failed:" + message;
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });
    }

    @Override
    public void onConnectionError()
    {
            MainActivity.this.runOnUiThread(new Runnable()
            {

                public void run()
                {
                    connectButton.setEnabled(true);
                    addressField.setEnabled(true);
                    usernameField.setEnabled(true);
                    messagesView.append("Connection error\n");
                    Context context = getApplicationContext();
                    CharSequence text = "Connection error!";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

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
}

