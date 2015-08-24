package dm.chatclient.repository.message;

import dm.chatclient.model.Contact;
import dm.chatclient.model.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ice on 8/9/2015.
 */
public class InMemoryMessageRepository implements IMessageRepository
{
    private Map<Contact, List<Message>> messages;

    public InMemoryMessageRepository()
    {
        messages = new HashMap<Contact, List<Message>>();
    }

    public void addMessage(Message message)
    {
        Contact relatedContact;
        if (message.getSender() instanceof Contact)
        {
            relatedContact = (Contact) message.getSender();
        }
        else
        {
            relatedContact = (Contact) message.getReceiver();
        }

        if (!messages.containsKey(relatedContact))
        {
            messages.put(relatedContact, new ArrayList<Message>());
        }
        messages.get(relatedContact).add(message);
    }

    public List<Message> getMessagesWithContact(Contact contact)
    {
        if (messages.containsKey(contact))
        {
            return  messages.get(contact);
        }
        else
        {
            List<Message> newMessageList = new ArrayList<Message>();
            messages.put(contact,newMessageList);
            return  newMessageList;
        }
    }

    @Override
    public void removeMessages(Contact contact)
    {
        messages.remove(contact);
    }


    public void clearMessages()
    {
        messages.clear();
    }
}
