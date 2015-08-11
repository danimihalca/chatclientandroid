package dm.chatclient.model;

/**
 * Created by Ice on 8/9/2015.
 */
public abstract class BaseUser
{
    private int m_id;
    private String m_userName;
    private String m_firstName;
    private String m_lastName;

    public BaseUser(int id, String userName, String fullName, String lastName)
    {
        m_id = id;
        m_userName = userName;
        m_firstName = fullName;
        m_lastName = lastName;
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

    public String getFirstName()
    {
        return m_firstName;
    }

    public void setFirstName(String fullName)
    {
        m_firstName = fullName;
    }

    public String getLastName()
    {
        return m_lastName;
    }

    public void setLastName(String lastName)
    {
        this.m_lastName = lastName;
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
