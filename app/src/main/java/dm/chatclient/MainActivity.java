package dm.chatclient;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;


public class MainActivity extends ActionBarActivity implements IChatMessageListener
{
    private ChatClient client;
    Button connectButton;
    Button disconnectButton;
    EditText messageField;
    TextView messagesView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connectButton = (Button) findViewById(R.id.connectButton);
        disconnectButton = (Button) findViewById(R.id.disconnectButton);
        messageField = (EditText) findViewById(R.id.messageField);
        messagesView = (TextView) findViewById(R.id.messagesView);
        disconnectButton.setEnabled(false);
        connectButton.setOnClickListener(new View.OnClickListener()
        {
            EditText addressField = (EditText) findViewById(R.id.addressField);
            public void onClick(View v)
            {
                client.connect(addressField.getText().toString());
                connectButton.setEnabled(false);
                disconnectButton.setEnabled(true);
            }
        });

        disconnectButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                client.disconnect();
                connectButton.setEnabled(true);
                disconnectButton.setEnabled(false);
            }
        });


        Button sendButton = (Button) findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                String message = messageField.getText().toString();
                client.sendMessage(message);
                messageField.setText("");
            }
        });

        client = new ChatClient();
        client.addListener(this);
        client.initialize();
        client.startService();
    }

    @Override
    protected void onStop()
    {
        Log.i("MainActivity", "onStop");
        //        try
        //        {
        //            client.close();
        //        }
        //        catch (IOException e)
        //        {
        //            e.printStackTrace();
        //        }
        //        client = null;
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
        //        client = new ChatClient();
        //        client.initialize();
        //        client.startService();
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
                messagesView.append("\n" + message);
            }
        });
    }
}
