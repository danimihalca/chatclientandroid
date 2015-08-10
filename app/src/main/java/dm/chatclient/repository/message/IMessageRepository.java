package dm.chatclient.repository.message;

import dm.chatclient.model.Contact;
import dm.chatclient.model.Message;

import java.util.List;

/**
 * Created by Ice on 8/9/2015.
 */
public interface IMessageRepository
{
    void addMessage(Message message);
    List<Message> getMessagesWithContact(Contact c);

    void clearMessages();
}
