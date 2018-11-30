package penguin_tech.com.commandercounter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;

public class EditManaDialogFragment extends DialogFragment {

    public static final String INDEX = "EditManaDialogFragment.index";
    public static final String CODE = "EditManaDialogFragment.code";
    public static final String IS_PHYREXIAN = "EditManaDialogFragment.isPhyrexian";

    private int index;
    private DialogClickListener listener;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (DialogClickListener)context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        index = getArguments().getInt(INDEX);
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.dialog_edit_mana, null);
        Spinner spn = v.findViewById(R.id.spn_mana);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getContext(), R.array.mana_color, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn.setAdapter(adapter);
        String[] manaCodes = getResources().getStringArray(R.array.mana_code);
        String code = getArguments().getString(CODE);
        for(int i = 0; i < manaCodes.length; i++) {
            if(code.compareTo(manaCodes[i]) == 0) {
                spn.setSelection(i);
                break;
            }
        }
        CheckBox chk = v.findViewById(R.id.chk_phyrexian);
        chk.setChecked(getArguments().getBoolean(IS_PHYREXIAN));

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(v)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        onPositiveClick();
                    }
                })
                .setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        onNeutralClick();
                    }
                })
                .setNegativeButton("Cancel", null);
        return builder.create();
    }

    private void onPositiveClick() {
        Bundle data = new Bundle();
        data.putInt(INDEX, index);
        String[] manaCodes = getResources().getStringArray(R.array.mana_code);
        Spinner spn = getDialog().findViewById(R.id.spn_mana);
        data.putString(CODE, manaCodes[spn.getSelectedItemPosition()]);
        CheckBox chk = getDialog().findViewById(R.id.chk_phyrexian);
        data.putBoolean(IS_PHYREXIAN, chk.isChecked());
        listener.onDialogClick(getTag(), data);
    }

    private void onNeutralClick() {
        Bundle data = new Bundle();
        data.putInt(INDEX, index);
        listener.onDialogClick(getTag(), data);
    }

}
