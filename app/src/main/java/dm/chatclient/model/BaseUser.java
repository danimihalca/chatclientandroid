package dm.chatclient.model;

/**
 * Created by Ice on 8/9/2015.
 */
public abstract class BaseUser
{
    @Override
    public String toString()
    {
        return "BaseUser{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }

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

    private int id;
    private String userName;
    private String firstName;
    private String lastName;

    public BaseUser()
    {
    }

    public BaseUser(int id, String userName, String firstName, String lastName)
    {
        this.id = id;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String fullName)
    {
        firstName = fullName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseUser baseUser = (BaseUser) o;

        return id == baseUser.id;

    }

    @Override
    public int hashCode()
    {
        return id;
    }
}
