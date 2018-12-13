package penguin_tech.com.commandercounter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class ConfirmDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {

    public static final String MESSAGE = "ConfirmDialogFragment.message";
    public static final String POS = "ConfirmDialogFragment.pos";
    public static final String NEG = "ConfirmDialogFragment.neg";
    public static final String RESULT = "ConfirmDialogFragment.result";

    public static final int YES = 1;
    public static final int NO = -1;
    public static final int CANCEL = 0;

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
                .setPositiveButton(getArguments().getString(POS, "OK"), this)
                .setNegativeButton(getArguments().getString(NEG, "Cancel"), this);
        return builder.create();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        if(listener != null) {
            getArguments().putInt(RESULT, CANCEL);
            listener.onDialogClick(getTag(), getArguments());
        }
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int which) {
        if(which == DialogInterface.BUTTON_POSITIVE) {
            getArguments().putInt(RESULT, YES);
        } else if(which == DialogInterface.BUTTON_NEGATIVE) {
            getArguments().putInt(RESULT, NO);
        } else {
            getArguments().putInt(RESULT, CANCEL);
        }
        listener.onDialogClick(getTag(), getArguments());
    }
}
