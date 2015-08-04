package dm.chatclient.utils;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
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
    private Drawable m_offlineDrawable;

    public ContactListAdapter(Context context)
    {
        super(context, 0, new ArrayList<Contact>());

        m_onlineDrawable = getContext().getResources().getDrawable(R.drawable.online);
        m_offlineDrawable = getContext().getResources().getDrawable(R.drawable.offline);
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
        TextView fullNameView = (TextView) view.findViewById(R.id.fullNameView);
        ImageView onlineImageView = (ImageView) view.findViewById(R.id.onlineImageView);
        Contact contact = getItem(i);

        userNameView.setText(contact.getUserName());
        fullNameView.setText(contact.getFullName());

        if (contact.isOnline())
        {
            onlineImageView.setImageDrawable(m_onlineDrawable);
        }
        else
        {
            onlineImageView.setImageDrawable(m_offlineDrawable);
        }

        return view;
    }

}
