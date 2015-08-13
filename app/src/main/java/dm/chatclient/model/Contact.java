package dm.chatclient.model;

public class Contact extends BaseUser
{



    private USER_STATE m_state;
    private int m_unreadMessagesCount;

    public Contact(int id, String userName, String firstName,String lastName, USER_STATE state)
    {
        super(id, userName, firstName,lastName);
        this.m_state = state;
        this.m_unreadMessagesCount = 0;
    }

    public USER_STATE getState()
    {
        return m_state;
    }

    public void setState(USER_STATE state)
    {
        this.m_state = state;
    }

    public int getUnreadMessagesCount()
    {
        return m_unreadMessagesCount;
    }

    public void setUnreadMessagesCount(int m_unreadMessagesCount)
    {
        this.m_unreadMessagesCount = m_unreadMessagesCount;
    }

}
