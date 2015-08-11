package dm.chatclient.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import dm.chatclient.R;
import dm.chatclient.model.Message;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MessageListAdapter extends ArrayAdapter<Message>
{
    public static SimpleDateFormat detailDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    public MessageListAdapter(Context context)
    {
        super(context, 0, new ArrayList<Message>());

    }

    public MessageListAdapter(Context context, List<Message> messageList)
    {
        super(context, 0, messageList);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        if (view == null)
        {
            view = LayoutInflater.from(getContext()).inflate(R.layout.message_layout,viewGroup,false);
        }

        TextView senderFirstNameView = (TextView) view.findViewById(R.id.senderFirstNameView);
        TextView senderLastNameView = (TextView) view.findViewById(R.id.senderLastNameView);
        TextView messageView = (TextView) view.findViewById(R.id.messageView);
        TextView dateView = (TextView) view.findViewById(R.id.dateView);

        Message message = getItem(i);

        senderFirstNameView.setText(message.getSender().getFirstName());
        senderLastNameView.setText(message.getSender().getLastName());
        messageView.setText(message.getMessageText());
        dateView.setText(detailDateFormat.format(message.getDate()));

        return view;
    }
}
