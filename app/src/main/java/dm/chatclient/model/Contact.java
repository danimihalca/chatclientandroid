package dm.chatclient.model;

public class Contact extends BaseUser
{
    private boolean m_isOnline;
    private int m_unreadMessagesCount;

    public Contact(int id, String userName, String fullName, boolean isOnline)
    {
        super(id, userName, fullName);
        this.m_isOnline = isOnline;
        this.m_unreadMessagesCount = 0;
    }

    public boolean isOnline()
    {
        return m_isOnline;
    }

    public void setOnline(boolean m_isOnline)
    {
        this.m_isOnline = m_isOnline;
    }

    public int getUnreadMessagesCount()
    {
        return m_unreadMessagesCount;
    }

    public void setUnreadMessagesCount(int m_unreadMessagesCount)
    {
        this.m_unreadMessagesCount = m_unreadMessagesCount;
    }

}
