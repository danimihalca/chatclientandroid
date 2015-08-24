package dm.chatclient.model;

/**
 * Created by Ice on 8/9/2015.
 */
public class User extends BaseUser
{
    private String password;

    public User()
    {

    }

    public User(int id, String userName, String password, String firstName, String lastName)
    {
        super(id, userName, firstName, lastName);
        this.password = password;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    @Override
    public String toString()
    {
        return super.toString()+" User{" +
                "password='" + password + '\'' +
                '}';
    }
}
