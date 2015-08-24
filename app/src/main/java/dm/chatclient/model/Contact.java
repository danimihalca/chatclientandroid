package dm.chatclient.model;

public class Contact extends BaseUser
{



    private USER_STATE state;
    private int unreadMessagesCount;

    public Contact(int id, String userName, String firstName,String lastName, USER_STATE state)
    {
        super(id, userName, firstName,lastName);
        this.state = state;
        this.unreadMessagesCount = 0;
    }

    public USER_STATE getState()
    {
        return state;
    }

    public void setState(USER_STATE state)
    {
        this.state = state;
    }

    public int getUnreadMessagesCount()
    {
        return unreadMessagesCount;
    }

    public void setUnreadMessagesCount(int unreadMessagesCount)
    {
        this.unreadMessagesCount = unreadMessagesCount;
    }

}
