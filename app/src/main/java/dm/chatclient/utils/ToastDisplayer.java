package dm.chatclient.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastDisplayer
{
    public static void displayToast(Context context,String message)
    {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();
    }
}
