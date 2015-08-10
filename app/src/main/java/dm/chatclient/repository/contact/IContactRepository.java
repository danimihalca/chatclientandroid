package dm.chatclient.repository.contact;

import dm.chatclient.model.Contact;

import java.util.List;

/**
 * Created by Ice on 8/9/2015.
 */
public interface IContactRepository
{
    void addContact(Contact c);

    void addContacts(List<Contact> contacts);
    List<Contact> getContacts();
    void setContacts(List<Contact> contacts);
    Contact getContact(int contactId);

    void clearContacts();
}
