package dm.chatclient.view;

import android.app.Application;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import dm.chatclient.ChatClientApplication;
import dm.chatclient.R;

public class SettingsActivity extends ActionBarActivity
{
    private EditText address;
    private EditText port;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        address = (EditText)findViewById(R.id.addressText);
        port = (EditText)findViewById(R.id.portText);
        address.setText(((ChatClientApplication) getApplication()).serverAddress);
        port.setText(Integer.toString(((ChatClientApplication) getApplication()).serverPort));
        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ChatClientApplication app = ((ChatClientApplication) getApplication());
                app.serverAddress = address.getText().toString();
                app.serverPort = Integer.parseInt(port.getText().toString());
                app.getController().setServer(app.serverAddress,app.serverPort);
                finish();
            }
        });
    }


}
