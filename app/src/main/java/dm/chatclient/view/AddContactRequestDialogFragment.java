package dm.chatclient.view;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import dm.chatclient.R;

/**
 * Created by Ice on 8/12/2015.
 */
public class AddContactRequestDialogFragment extends DialogFragment
{
    private Button acceptButton;
    private Button declineButton;
    private EditText userNameInput;
    private String userName;

    public boolean isDecided()
    {
        return decided;
    }


    public boolean isAccepted()
    {
        return accepted;
    }

    private boolean accepted;
    private boolean decided;
    public AddContactRequestDialogFragment()
    {
    }

    @Override
    public void onDismiss(DialogInterface dialog)
    {
        decided = true;
        super.onDismiss(dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        accepted =false;
        decided = false;
        userName = getArguments().getString("userName");
        View view = inflater.inflate(R.layout.add_contact_request_layout, container);
        TextView info = (TextView) view.findViewById(R.id.infoText);
        info.setText(userName + " would like to add you as a contact.");
        userNameInput = (EditText) view.findViewById(R.id.userName);
        getDialog().setTitle("Add contact");

        acceptButton = (Button) view.findViewById(R.id.acceptButton);
        acceptButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                accepted = true;
                dismiss();
            }
        });

        declineButton = (Button) view.findViewById(R.id.declineButton);
        declineButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dismiss();
            }
        });
        return view;
    }
}
