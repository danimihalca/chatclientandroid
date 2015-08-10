package dm.chatclient.model;

/**
 * Created by Ice on 8/9/2015.
 */
public abstract class BaseUser
{
    private int m_id;
    private String m_userName;
    private String m_fullName;

    public BaseUser(int id, String userName, String fullName)
    {
        m_id = id;
        m_userName = userName;
        m_fullName = fullName;
    }

    public int getId()
    {
        return m_id;
    }

    public void setId(int id)
    {
        m_id = id;
    }

    public String getUserName()
    {
        return m_userName;
    }

    public void setUserName(String userName)
    {
        m_userName = userName;
    }

    public String getFullName()
    {
        return m_fullName;
    }

    public void setMFullName(String fullName)
    {
        m_fullName = fullName;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseUser baseUser = (BaseUser) o;

        return m_id == baseUser.m_id;

    }

    @Override
    public int hashCode()
    {
        return m_id;
    }
}
