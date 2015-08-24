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
    private RadioGroup stateGroup;
    private BaseUser.USER_STATE state;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.change_state_layout, container);
        view.setMinimumWidth(300);
        getDialog().setTitle("State");
        state =BaseUser.USER_STATE.convert(getArguments().getInt("state"));

        stateGroup = (RadioGroup) view.findViewById(R.id.stateGroup);

        switch (state)
        {
            case ONLINE:
                stateGroup.check(R.id.onlineButton);
                break;
            case IDLE:
                stateGroup.check(R.id.idleButton);
                break;
            case BUSY:
                stateGroup.check(R.id.busyButton);
                break;
            case INVISIBLE:
                stateGroup.check(R.id.invisibleButton);
                break;
        }

        stateGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i)
            {
                switch (i)
                {
                    case R.id.onlineButton:
                        state = BaseUser.USER_STATE.ONLINE;
                        break;
                    case R.id.idleButton:
                        state = BaseUser.USER_STATE.IDLE;
                        break;
                    case R.id.busyButton:
                        state = BaseUser.USER_STATE.BUSY;
                        break;
                    case R.id.invisibleButton:
                        state = BaseUser.USER_STATE.INVISIBLE;
                        break;
                }
                ((ChatClientApplication) (getActivity()).getApplication()).getController().changeState(state);
                dismiss();
            }
        });
        return view;
    }
}
