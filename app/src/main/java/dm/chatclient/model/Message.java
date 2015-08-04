package dm.chatclient.model;

import java.util.Date;

public class Message
{
    public enum Message_Type
    {
        SENT,
        RECEIVED
    }

//    private int m_id;
    private String m_senderFullName;
    private String m_messageText;
    private Message_Type m_type;
    private Date m_date;

    public Message(String sender, String message, Message_Type type,Date date)
    {
        m_senderFullName = sender;
        m_messageText = message;
        m_type = type;
        m_date = date;
    }

    public String geSender()
    {
        return m_senderFullName;
    }

    public void setSender(String m_senderFullName)
    {
        this.m_senderFullName = m_senderFullName;
    }

    public String getMessageText()
    {
        return m_messageText;
    }

    public void setMessageText(String m_messageText)
    {
        this.m_messageText = m_messageText;
    }
}
