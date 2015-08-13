package dm.chatclient.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import dm.chatclient.model.Contact;

import java.util.ArrayList;
import java.util.List;

import dm.chatclient.R;

public class ContactListAdapter extends ArrayAdapter<Contact>
{
    private Drawable m_onlineDrawable;
    private Drawable m_busyDrawable;
    private Drawable m_idleDrawable;
    public ContactListAdapter(Context context)
    {
        super(context, 0, new ArrayList<Contact>());

        m_onlineDrawable = getContext().getResources().getDrawable(R.drawable.online);
        m_busyDrawable = getContext().getResources().getDrawable(R.drawable.offline);
// TODO:///sdagfagas
//     m_idleDrawable =
    }

    public ContactListAdapter(Context context, List<Contact> contacts)
    {
        super(context, 0, contacts);
    }

    public void setContactList(List<Contact> contacts)
    {
        super.clear();
        for (Contact c:contacts)
        {
            super.add(c);
        }
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        if (view == null)
        {
            view = LayoutInflater.from(getContext()).inflate(R.layout.contact_layout,viewGroup,false);
        }

        TextView userNameView = (TextView) view.findViewById(R.id.userNameView);
        TextView firstlNameView = (TextView) view.findViewById(R.id.firstNameView);
        TextView lastNameView = (TextView) view.findViewById(R.id.lastNameView);
        ImageView onlineImageView = (ImageView) view.findViewById(R.id.onlineImageView);
        TextView unreadMessagesCountView = (TextView) view.findViewById(R.id.unreadMessagesCountView);
        Contact contact = getItem(i);

        userNameView.setText(contact.getUserName());
        firstlNameView.setText(contact.getFirstName());
        lastNameView.setText(contact.getLastName());

        int unreadMessagesCount = contact.getUnreadMessagesCount();
        String unreadCountText;
        if (unreadMessagesCount >0)
        {
            unreadCountText = Integer.toString(unreadMessagesCount);
        }
        else
        {
            unreadCountText = "";
        }
        unreadMessagesCountView.setText(unreadCountText);


        switch (contact.getState())
        {
            case ONLINE:
            {
                onlineImageView.setImageDrawable(m_onlineDrawable);
                break;
            }
            case BUSY:
            {
                onlineImageView.setImageDrawable(m_busyDrawable);
                break;
            }
            case IDLE:
            {
                onlineImageView.setImageDrawable(m_idleDrawable);
            }
            case OFFLINE:
            {
                onlineImageView.setImageDrawable(null);
            }
        }

        return view;
    }

}
