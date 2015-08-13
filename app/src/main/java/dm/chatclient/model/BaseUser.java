package dm.chatclient.model;

/**
 * Created by Ice on 8/9/2015.
 */
public abstract class BaseUser
{
    public enum USER_STATE
    {
        OFFLINE,
        ONLINE,
        IDLE,
        BUSY,
        INVISIBLE;

        public static USER_STATE convert(int ordinal)
        {
            switch (ordinal)
            {
                case 0:
                    return OFFLINE;
                case 1:
                    return ONLINE;
                case 2:
                    return IDLE;
                case 3:
                    return BUSY;
                case 4:
                    return INVISIBLE;
            }
            return  OFFLINE;
        }
    }

    private int m_id;
    private String m_userName;
    private String m_firstName;
    private String m_lastName;

    public BaseUser()
    {
    }

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
