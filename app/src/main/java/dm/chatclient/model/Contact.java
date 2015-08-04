package dm.chatclient.model;

public class Contact
{
    private int m_id;
    private String m_userName;
    private String m_fullName;
    private boolean m_isOnline;
    private int m_unreadMessagesCount;

    public Contact(int id, String userName, String fullName, boolean isOnline)
    {
        this.m_id = id;
        this.m_userName = userName;
        this.m_fullName = fullName;
        this.m_isOnline = isOnline;
        this.m_unreadMessagesCount = 0;
    }

    public int getId()
    {
        return m_id;
    }

    public void setId(int m_id)
    {
        this.m_id = m_id;
    }

    public String getUserName()
    {
        return m_userName;
    }

    public void setUserName(String m_userName)
    {
        this.m_userName = m_userName;
    }

    public String getFullName()
    {
        return m_fullName;
    }

    public void setFullName(String m_fullName)
    {
        this.m_fullName = m_fullName;
    }

    public boolean isOnline()
    {
        return m_isOnline;
    }

    public void setOnline(boolean m_isOnline)
    {
        this.m_isOnline = m_isOnline;
    }

    @Override
    public String toString()
    {
        return "Contact{" +
                "m_id=" + m_id +
                ", m_userName='" + m_userName + '\'' +
                ", m_fullName='" + m_fullName + '\'' +
                ", m_isOnline=" + m_isOnline +
                '}';
    }

    public int getUnreadMessagesCount()
    {
        return m_unreadMessagesCount;
    }

    public void setUnreadMessagesCount(int m_unreadMessagesCount)
    {
        this.m_unreadMessagesCount = m_unreadMessagesCount;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        Contact contact = (Contact) o;

        return m_id == contact.m_id;

    }

    @Override
    public int hashCode()
    {
        return m_id;
    }
}
