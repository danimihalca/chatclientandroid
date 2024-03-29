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
    private Drawable onlineDrawable;
    private Drawable busyDrawable;
    private Drawable idleDrawable;
    public ContactListAdapter(Context context)
    {
        super(context, 0, new ArrayList<Contact>());

        onlineDrawable = getContext().getResources().getDrawable(R.drawable.online);
        busyDrawable = getContext().getResources().getDrawable(R.drawable.busy);
        idleDrawable = getContext().getResources().getDrawable(R.drawable.idle2);
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
                onlineImageView.setImageDrawable(onlineDrawable);
                break;
            }
            case BUSY:
            {
                onlineImageView.setImageDrawable(busyDrawable);
                break;
            }
            case IDLE:
            {
                onlineImageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.idle2));
            }
            case OFFLINE:
            {
                onlineImageView.setImageDrawable(null);
            }
        }

        return view;
    }

}
