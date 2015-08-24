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
    Map<Integer, Contact> contacts;

    public InMemoryContactRepository()
    {
        contacts = new HashMap<Integer, Contact>();
    }

    public void addContact(Contact c)
    {
        Contact existingContact = contacts.get(c.getId());
        if (existingContact == null)
        {
            contacts.put(c.getId(), c);
        }
        else
        {
            existingContact.setFirstName(c.getFirstName());
            existingContact.setLastName(c.getLastName());
            existingContact.setUserName(c.getUserName());
        }
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
        return new ArrayList<Contact>(contacts.values());
    }

    public Contact getContact(int contactId)
    {
        return contacts.get(contactId);
    }

    @Override
    public void deleteContact(Contact contact)
    {
        contacts.remove(contact.getId());
    }


    public void clearContacts()
    {
        contacts.clear();
    }

    public void setContacts(List<Contact> contacts)
    {
//        clearContacts();
        addContacts(contacts);
    }
}
