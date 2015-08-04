package dm.chatclient.activities;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import dm.chatclient.R;
import dm.chatclient.model.Contact;

public class ConversationActivity extends AppCompatActivity
{
    private Contact m_contact;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        Intent intent = getIntent();

        int id = intent.getIntExtra("ContactId", -1);
        String userName = intent.getStringExtra("ContactUserName");
        String fullName = intent.getStringExtra("ContactFullName");
        Boolean isOnline = intent.getBooleanExtra("ContactOnline", false);
        m_contact = new Contact(id,userName,fullName,isOnline);

        setTitle(fullName);
    }


}
