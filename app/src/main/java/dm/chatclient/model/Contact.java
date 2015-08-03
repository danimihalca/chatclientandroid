package dm.chatclient.model;

public class Contact
{
    private int m_id;
    private String m_userName;
    private String m_fullName;
    private boolean m_isOnline;

    public Contact(int id, String userName, String fullName, boolean isOnline)
    {
        this.m_id = id;
        this.m_userName = userName;
        this.m_fullName = fullName;
        this.m_isOnline = isOnline;
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
}
