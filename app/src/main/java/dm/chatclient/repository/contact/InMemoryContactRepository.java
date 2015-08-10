package dm.chatclient.repository.contact;

import dm.chatclient.model.Contact;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ice on 8/9/2015.
 */
public class InMemoryContactRepository implements IContactRepository
{
    Map<Integer, Contact> m_contacts;

    public InMemoryContactRepository()
    {
        m_contacts = new HashMap<Integer, Contact>();
    }

    public void addContact(Contact c)
    {
        m_contacts.put(c.getId(), c);
    }

    public void addContacts(List<Contact> contacts)
    {
        for(Contact c : contacts)
        {
            addContact(c);
        }
    }

    public List<Contact> getContacts()
    {
        return new ArrayList<Contact>(m_contacts.values());
    }

    public Contact getContact(int contactId)
    {
        return m_contacts.get(contactId);
    }


    public void clearContacts()
    {
        m_contacts.clear();
    }

    public void setContacts(List<Contact> contacts)
    {
        clearContacts();
        addContacts(contacts);
    }
}
