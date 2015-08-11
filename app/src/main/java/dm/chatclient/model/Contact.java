package dm.chatclient.model;

public class Contact extends BaseUser
{
    public enum CONTACT_STATE
    {
        OFFLINE,
        ONLINE;

        public static CONTACT_STATE convert(byte ordinal)
        {
            switch (ordinal)
            {
                case 0:
                    return OFFLINE;
                case 1:
                    return ONLINE;
            }
            return  OFFLINE;
        }
    }


    private CONTACT_STATE m_state;
    private int m_unreadMessagesCount;

    public Contact(int id, String userName, String firstName,String lastName, CONTACT_STATE state)
    {
        super(id, userName, firstName,lastName);
        this.m_state = state;
        this.m_unreadMessagesCount = 0;
    }

    public CONTACT_STATE getState()
    {
        return m_state;
    }

    public void setState(CONTACT_STATE state)
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
