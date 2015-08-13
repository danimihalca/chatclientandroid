package dm.chatclient.view;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import dm.chatclient.ChatClientApplication;
import dm.chatclient.R;
import dm.chatclient.model.BaseUser;

/**
 * Created by Ice on 8/13/2015.
 */
public class ChangeStateDialogFragment extends DialogFragment
{
    private RadioGroup m_stateGroup;
    private BaseUser.USER_STATE m_state;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.change_state_layout, container);
        view.setMinimumWidth(300);
        getDialog().setTitle("State");
        m_state =BaseUser.USER_STATE.convert(getArguments().getInt("state"));

        m_stateGroup = (RadioGroup) view.findViewById(R.id.stateGroup);

        switch (m_state)
        {
            case ONLINE:
                m_stateGroup.check(R.id.onlineButton);
                break;
            case IDLE:
                m_stateGroup.check(R.id.idleButton);
                break;
            case BUSY:
                m_stateGroup.check(R.id.busyButton);
                break;
            case INVISIBLE:
                m_stateGroup.check(R.id.invisibleButton);
                break;
        }

        m_stateGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i)
            {
                switch (i)
                {
                    case R.id.onlineButton:
                        m_state = BaseUser.USER_STATE.ONLINE;
                        break;
                    case R.id.idleButton:
                        m_state = BaseUser.USER_STATE.IDLE;
                        break;
                    case R.id.busyButton:
                        m_state = BaseUser.USER_STATE.BUSY;
                        break;
                    case R.id.invisibleButton:
                        m_state = BaseUser.USER_STATE.INVISIBLE;
                        break;
                }
                ((ChatClientApplication) (getActivity()).getApplication()).getController().changeState(m_state);
                dismiss();
            }
        });
        return view;
    }
}
