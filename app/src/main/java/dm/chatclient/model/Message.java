package dm.chatclient.model;

import java.util.Calendar;
import java.util.Date;

public class Message
{
    private BaseUser m_sender;
    private BaseUser m_receiver;
    private String m_messageText;
    private Date m_date;

    public Message(BaseUser sender, BaseUser receiver, String message)
    {
        m_sender = sender;
        m_receiver = receiver;
        m_messageText = message;
        m_date = Calendar.getInstance().getTime();
    }

    public String getMessageText()
    {
        return m_messageText;
    }

    public void setMessageText(String m_messageText)
    {
        this.m_messageText = m_messageText;
    }

    public BaseUser getSender()
    {
        return m_sender;
    }

    public void setSender(BaseUser sender)
    {
        this.m_sender = sender;
    }

    public BaseUser getReceiver()
    {
        return m_receiver;
    }

    public void setReceiver(BaseUser receiver)
    {
        m_receiver = receiver;
    }

    public Date getDate()
    {
        return  m_date;
    }
}
