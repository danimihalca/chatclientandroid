package dm.chatclient.model;

/**
 * Created by Ice on 8/11/2015.
 */
public class UserDetails
{
    private int m_id;
    private String m_firstName;
    private String m_lastName;

    public UserDetails(int id, String firstName, String lastName)
    {
        m_id = id;
        m_firstName = firstName;
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

}
