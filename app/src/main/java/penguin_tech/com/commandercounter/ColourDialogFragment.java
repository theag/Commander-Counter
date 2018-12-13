package penguin_tech.com.commandercounter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

public class ColourDialogFragment extends DialogFragment implements SeekBar.OnSeekBarChangeListener {

    public static final String COLOUR = "ColourDialogFragment.colour";
    public static final String TITLE = "ColourDialogFragment.title";

    private static final int RED = 0;
    private static final int GREEN = 1;
    private static final int BLUE = 2;

    private DialogClickListener listener;
    private int r, g, b;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (DialogClickListener)context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.dialog_colour, null);
        int colour = getArguments().getInt(COLOUR);
        r = getPiece(colour, RED);
        g = getPiece(colour, GREEN);
        b = getPiece(colour, BLUE);
        SeekBar sb = v.findViewById(R.id.seek_red);
        sb.setOnSeekBarChangeListener(this);
        sb.setProgress(r);
        EditText tv = v.findViewById(R.id.text_red);
        tv.setText(""+sb.getProgress());
        tv.addTextChangedListener(new ChangeSeekBar(sb));
        sb = v.findViewById(R.id.seek_green);
        sb.setOnSeekBarChangeListener(this);
        sb.setProgress(g);
        tv = v.findViewById(R.id.text_green);
        tv.setText(""+sb.getProgress());
        tv.addTextChangedListener(new ChangeSeekBar(sb));
        sb = v.findViewById(R.id.seek_blue);
        sb.setOnSeekBarChangeListener(this);
        sb.setProgress(b);
        tv = v.findViewById(R.id.text_blue);
        tv.setText(""+sb.getProgress());
        tv.addTextChangedListener(new ChangeSeekBar(sb));
        v.findViewById(R.id.view_colour).setBackgroundColor(colour);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(v)
                .setTitle(getArguments().getString(TITLE))
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        saveClick();
                    }
                })
                .setNegativeButton("Cancel", null);
        return builder.create();
    }

    private int getPiece(int colour, int index) {
        int upper;
        switch(index) {
            case RED:
                upper = colour >> 24;
                return (colour - (upper << 24)) >> 16;
            case GREEN:
                upper = colour >> 16;
                return (colour - (upper << 16)) >> 8;
            case BLUE:
                upper = colour >> 8;
                return colour - (upper << 8);
            default:
                return -1;
        }
    }

    private int getColour() {
        return 0xFF000000 + (r << 16) + (g << 8) + b;
    }

    public void saveClick() {
        Bundle data = new Bundle();
        data.putInt(COLOUR, getColour());
        listener.onDialogClick(getTag(), data);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(fromUser) {
            TextView tv = null;
            switch (seekBar.getId()) {
                case R.id.seek_red:
                    r = progress;
                    tv = getDialog().findViewById(R.id.text_red);
                    break;
                case R.id.seek_green:
                    g = progress;
                    tv = getDialog().findViewById(R.id.text_green);
                    break;
                case R.id.seek_blue:
                    b = progress;
                    tv = getDialog().findViewById(R.id.text_blue);
                    break;
            }
            tv.setText(""+progress);
            getDialog().findViewById(R.id.view_colour).setBackgroundColor(getColour());
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    private class ChangeSeekBar implements TextWatcher {

        private SeekBar bar;
        private int start;

        ChangeSeekBar(SeekBar bar) {
            this.bar = bar;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (count == 0) {
                this.start = start;
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.length() == 0) {
                bar.setProgress(0);
            } else {
                int value = Integer.parseInt(editable.toString());
                if(value > 255) {
                    editable.delete(start, start+1);
                } else {
                    bar.setProgress(value);
                }
            }
        }
    }
}
