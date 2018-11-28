package penguin_tech.com.commandercounter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class CommanderDialog extends DialogFragment {

    public static final String INDEX = "CommanderDialog.index";
    public static final String ERROR = "CommanderDialog.error";
    public static final String NAME = "CommanderDialog.name";
    public static final String MANA = "CommanderDialog.mana";

    private DialogClickListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (DialogClickListener)context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater li = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.dialog_commander, null);
        if(getArguments() != null) {
            if (getArguments().containsKey(NAME)) {
                EditText et = v.findViewById(R.id.edt_name);
                et.setText(getArguments().getString(NAME));
                et = v.findViewById(R.id.edt_cost);
                et.setText(getArguments().getString(MANA));
            } else if (getArguments().containsKey(INDEX)) {
                Commander c = DataController.getInstance().getItem(getArguments().getInt(INDEX));
                EditText et = v.findViewById(R.id.edt_name);
                et.setText(c.name);
                et = v.findViewById(R.id.edt_cost);
                et.setText(c.getManaCostString());
            }
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(v)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        saveClick();
                    }
                })
                .setNegativeButton("Cancel", null);
        return builder.create();
    }

    private void saveClick() {
        Bundle args = new Bundle();
        if(getArguments() != null && getArguments().containsKey(INDEX)) {
            args.putInt(INDEX, getArguments().getInt(INDEX));
        }
        EditText et = getDialog().findViewById(R.id.edt_name);
        String name = et.getText().toString().trim();
        args.putString(NAME, name);
        String err = "";
        if(name.isEmpty()) {
            err += "Name cannot be blank.";
        }
        et = getDialog().findViewById(R.id.edt_cost);
        String cost = et.getText().toString().trim().toUpperCase();
        args.putString(MANA, cost);
        if(cost.isEmpty()) {
            if(!err.isEmpty()) {
                err += "\n";
            }
            err += "Mana cost cannot be blank";
        } else if(!Commander.isValidCost(cost)) {
            if(!err.isEmpty()) {
                err += "\n";
            }
            err += "Mana cost is not valid";
        }
        if(!err.isEmpty()) {
            args.putString(ERROR, err);
        }
        listener.onDialogClick(getTag(), args);
    }

}
