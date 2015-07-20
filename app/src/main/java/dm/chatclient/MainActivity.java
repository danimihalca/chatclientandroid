package dm.chatclient;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.*;

import java.io.IOException;


public class MainActivity extends ActionBarActivity implements IChatMessageListener
{
    private ChatClient client;
    Button connectButton;
    Button disconnectButton;
    EditText addressField;
    EditText usernameField;
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
        usernameField = (EditText) findViewById(R.id.userNameField);

        messageField = (EditText) findViewById(R.id.messageField);
        messagesView = (TextView) findViewById(R.id.messagesView);
        disconnectButton.setEnabled(false);
        connectButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                client.connect(addressField.getText().toString(), 9003);
                connectButton.setEnabled(false);
                disconnectButton.setEnabled(true);
                addressField.setEnabled(false);
                usernameField.setEnabled(false);
            }
        });

        disconnectButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                client.disconnect();
                connectButton.setEnabled(true);
                disconnectButton.setEnabled(false);
                addressField.setEnabled(true);
                usernameField.setEnabled(true);
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

        client = new ChatClient();
        client.addListener(this);
    }

    private void performSend()
    {
        String message = messageField.getText().toString();
        String username = usernameField.getText().toString();
        client.sendMessage('<'+username+">:"+message);
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNewMessage(final String message)
    {
        MainActivity.this.runOnUiThread(new Runnable()
        {

            public void run()
            {

                messagesView.append(message+"\n");
                final ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
                scrollView.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                });
            }
        });
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
            }
        });
    }
}
