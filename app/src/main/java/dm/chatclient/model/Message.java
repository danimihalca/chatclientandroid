package dm.chatclient.model;

import java.util.Date;

public class Message
{
//    public enum Message_Type
//    {
//        SENT,
//        RECEIVED
//    }

//    private int m_id;
    private Contact m_sender;
    private String m_messageText;


    public Message(Contact senderId, String message)
    {
        m_sender = senderId;
        m_messageText = message;
    }

    public String getMessageText()
    {
        return m_messageText;
    }

    public void setMessageText(String m_messageText)
    {
        this.m_messageText = m_messageText;
    }

    public Contact getSender()
    {
        return m_sender;
    }

    public void setSender(Contact sender)
    {
        this.m_sender = sender;
    }
}
