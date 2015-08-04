package dm.chatclient.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import dm.chatclient.R;
import dm.chatclient.model.Contact;
import dm.chatclient.model.Message;

import java.util.ArrayList;
import java.util.List;

public class MessageListAdapter extends ArrayAdapter<Message>
{
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

        TextView senderView = (TextView) view.findViewById(R.id.senderFullNameView);
        TextView messageView = (TextView) view.findViewById(R.id.messageView);
//        TextView dateView = (TextView) view.findViewById(R.id.dateView);

        Message message = getItem(i);

        senderView.setText(message.getSender().getFullName());
        messageView.setText(message.getMessageText());

        return view;
    }
}
