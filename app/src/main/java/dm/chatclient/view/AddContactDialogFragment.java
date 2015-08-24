package dm.chatclient.view;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import dm.chatclient.R;

/**
 * Created by Ice on 8/12/2015.
 */
public class AddContactDialogFragment extends DialogFragment
{
    private Button addButton;
    Button cancelButton;
    private EditText userNameInput;
    public AddContactDialogFragment()
    {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_contact_layout, container);
        userNameInput = (EditText) view.findViewById(R.id.userName);
        getDialog().setTitle("Add contact");

        addButton = (Button) view.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ((ContactListView) getActivity()).addContact(userNameInput.getText().toString());
                dismiss();
            }
        });

        cancelButton = (Button) view.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener()
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
