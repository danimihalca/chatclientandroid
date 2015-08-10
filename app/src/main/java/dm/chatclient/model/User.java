package dm.chatclient.model;

/**
 * Created by Ice on 8/9/2015.
 */
public class User extends BaseUser
{
    private String m_password;

    public User(int id, String userName, String password, String fullName)
    {
        super(id, userName, fullName);
        m_password = password;
    }

    public String getPassword()
    {
        return m_password;
    }

    public void setPassword(String password)
    {
        m_password = password;
    }
}
