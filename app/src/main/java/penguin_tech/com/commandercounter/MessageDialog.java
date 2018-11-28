package penguin_tech.com.commandercounter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class MessageDialog extends DialogFragment {

    public static final String MESSAGE = "MessageDialog.message";

    private DialogClickListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof DialogClickListener) {
            listener = (DialogClickListener) context;
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getArguments().getString(MESSAGE))
                .setPositiveButton("OK", null);
        return builder.create();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        if(listener != null) {
            listener.onDialogClick(getTag(), getArguments());
        }
    }

}
