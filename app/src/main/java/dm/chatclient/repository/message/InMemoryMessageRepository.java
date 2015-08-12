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
    private Map<Contact, List<Message>> m_messages;

    public InMemoryMessageRepository()
    {
        m_messages = new HashMap<Contact, List<Message>>();
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

        if (!m_messages.containsKey(relatedContact))
        {
            m_messages.put(relatedContact, new ArrayList<Message>());
        }
        m_messages.get(relatedContact).add(message);
    }

    public List<Message> getMessagesWithContact(Contact contact)
    {
        if (m_messages.containsKey(contact))
        {
            return  m_messages.get(contact);
        }
        else
        {
            List<Message> newMessageList = new ArrayList<Message>();
            m_messages.put(contact,newMessageList);
            return  newMessageList;
        }
    }

    @Override
    public void removeMessages(Contact contact)
    {
        m_messages.remove(contact);
    }


    public void clearMessages()
    {
        m_messages.clear();
    }
}
