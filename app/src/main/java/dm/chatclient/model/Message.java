package dm.chatclient.model;

import java.util.Calendar;
import java.util.Date;

public class Message
{
    private BaseUser sender;
    private BaseUser receiver;
    private String messageText;
    private Date date;

    public Message(BaseUser sender, BaseUser receiver, String message)
    {
        this.sender = sender;
        this.receiver = receiver;
        messageText = message;
        date = Calendar.getInstance().getTime();
    }

    public String getMessageText()
    {
        return messageText;
    }

    public void setMessageText(String messageText)
    {
        this.messageText = messageText;
    }

    public BaseUser getSender()
    {
        return sender;
    }

    public void setSender(BaseUser sender)
    {
        this.sender = sender;
    }

    public BaseUser getReceiver()
    {
        return receiver;
    }

    public void setReceiver(BaseUser receiver)
    {
        this.receiver = receiver;
    }

    public Date getDate()
    {
        return  date;
    }
}
