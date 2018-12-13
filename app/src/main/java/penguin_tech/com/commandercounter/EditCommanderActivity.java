package penguin_tech.com.commandercounter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.LevelListDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class EditCommanderActivity extends AppCompatActivity implements DialogClickListener {

    public static final String INDEX = "EditCommanderActivity.index";

    private static final String COLOUR_DIALOG = "EditCommanderActivity.colourDialog";

    private static final int[][] NO_STATE = new int[1][0];

    private int index;
    private int[] colours;
    private HashMap<Integer, View> manaDots;
    private int nextHash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_commander);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        index = getIntent().getIntExtra(INDEX, -1);
        colours = new int[5];
        manaDots = new HashMap<>();
        nextHash = 0;
        LevelListDrawable d = (LevelListDrawable)((ImageView)findViewById(R.id.mana_1)).getDrawable();
        d.setLevel(1);
        d = (LevelListDrawable)((ImageView)findViewById(R.id.mana_2)).getDrawable();
        d.setLevel(2);
        d = (LevelListDrawable)((ImageView)findViewById(R.id.mana_3)).getDrawable();
        d.setLevel(3);
        d = (LevelListDrawable)((ImageView)findViewById(R.id.mana_4)).getDrawable();
        d.setLevel(4);
        d = (LevelListDrawable)((ImageView)findViewById(R.id.mana_5)).getDrawable();
        d.setLevel(5);
        if(index >= 0) {
            Commander c = DataController.getInstance().getItem(index);
            EditText et = findViewById(R.id.edt_name);
            et.setText(c.name);
            TextView tv = findViewById(R.id.edt_cost);
            LinearLayout ll = findViewById(R.id.lo_mana);
            LayoutInflater li = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View manaDot;
            for(String mana : c.manaCost) {
                manaDot = li.inflate(R.layout.mana_dot, null);
                d = (LevelListDrawable)((ImageView)manaDot.findViewById(R.id.img_background)).getDrawable();
                if(mana.matches("[URGBWP]+")) {
                    if(mana.indexOf("U") >= 0) {
                        d.setLevel(1);
                    } else if (mana.indexOf("R") >= 0) {
                        d.setLevel(2);
                    } else if (mana.indexOf("G") >= 0) {
                        d.setLevel(3);
                    } else if (mana.indexOf("B") >= 0) {
                        d.setLevel(4);
                    } else if (mana.indexOf("W") >= 0) {
                        d.setLevel(5);
                    }
                    if(mana.indexOf("P") >= 0) {
                        manaDot.findViewById(R.id.img_phyrexian).setVisibility(View.VISIBLE);
                    }
                    manaDot.setTag(nextHash++);
                    manaDot.setClickable(true);
                    manaDot.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            btnClick(view);
                        }
                    });
                    ll.addView(manaDot);
                    manaDots.put((Integer) manaDot.getTag(), manaDot);
                } else {
                    tv.setText(mana);
                }
            }
            ImageView iv = findViewById(R.id.iv_header_text);
            colours[0] = c.headerText;
            iv.setBackgroundColor(colours[0]);
            tv = findViewById(R.id.lbl_example);
            tv.setTextColor(colours[0]);
            iv = findViewById(R.id.iv_counter_text);
            colours[1] = c.counterText;
            iv.setBackgroundColor(colours[1]);
            iv = findViewById(R.id.iv_buttons);
            tv = findViewById(R.id.txt_example_counter);
            tv.setTextColor(colours[1]);
            colours[2] = c.buttons;
            iv.setBackgroundColor(colours[2]);
            ImageButton btn = findViewById(R.id.btn_example);
            ColorStateList csl = new ColorStateList(NO_STATE, new int[]{colours[2]});
            btn.setBackgroundTintList(csl);
            iv = findViewById(R.id.iv_background);
            colours[3] = c.background;
            iv.setBackgroundColor(colours[3]);
            ll = findViewById(R.id.ll_example);
            ll.setBackgroundColor(colours[3]);
            iv = findViewById(R.id.iv_commander_text);
            colours[4] = c.commanderText;
            iv.setBackgroundColor(colours[4]);
            tv = findViewById(R.id.txt_example_header);
            tv.setTextColor(colours[4]);

            Switch sw = findViewById(R.id.sw_button_image_colour);
            sw.setChecked(c.buttonImageBlack);
            if(c.buttonImageBlack) {
                btn.setImageDrawable(getDrawable(R.drawable.add_black));
            }
        } else {
            ImageView iv = findViewById(R.id.iv_header_text);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                colours[0] = getColor(R.color.headerTextDefault);
            } else {
                colours[0] = getResources().getColor(R.color.headerTextDefault);
            }
            iv.setBackgroundColor(colours[0]);
            iv = findViewById(R.id.iv_counter_text);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                colours[1] = getColor(R.color.counterTextDefault);
            } else {
                colours[1] = getResources().getColor(R.color.counterTextDefault);
            }
            iv.setBackgroundColor(colours[1]);
            iv = findViewById(R.id.iv_buttons);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                colours[2] = getColor(R.color.colorAccent);
            } else {
                colours[2] = getResources().getColor(R.color.colorAccent);
            }
            iv.setBackgroundColor(colours[2]);
            iv = findViewById(R.id.iv_background);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                colours[3] = getColor(R.color.backgroundDefault);
            } else {
                colours[3] = getResources().getColor(R.color.backgroundDefault);
            }
            iv.setBackgroundColor(colours[3]);
            iv = findViewById(R.id.iv_commander_text);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                colours[4] = getColor(R.color.commanderTextDefault);
            } else {
                colours[4] = getResources().getColor(R.color.commanderTextDefault);
            }
            iv.setBackgroundColor(colours[4]);
        }

    }

    public void btnClick(View view) {
        DialogFragment frag;
        Bundle args;
        LevelListDrawable d;
        if(view.getTag() == null) {
            switch (view.getId()) {
                case R.id.btn_add_mana:
                    LinearLayout ll = (LinearLayout) findViewById(R.id.lo_mana);
                    LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View manaDot = li.inflate(R.layout.mana_dot, null);
                    d = (LevelListDrawable) ((ImageView) manaDot.findViewById(R.id.img_background)).getDrawable();
                    d.setLevel(1);
                    manaDot.setTag(nextHash++);
                    manaDot.setClickable(true);
                    manaDot.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            btnClick(view);
                        }
                    });
                    ll.addView(manaDot);
                    manaDots.put((Integer) manaDot.getTag(), manaDot);
                    break;
                case R.id.iv_commander_text:
                    frag = new ColourDialogFragment();
                    args = new Bundle();
                    args.putInt(ColourDialogFragment.COLOUR, colours[4]);
                    args.putString(ColourDialogFragment.TITLE, "Commander Text Colour");
                    frag.setArguments(args);
                    frag.show(getSupportFragmentManager(), COLOUR_DIALOG + "4");
                    break;
                case R.id.iv_header_text:
                    frag = new ColourDialogFragment();
                    args = new Bundle();
                    args.putInt(ColourDialogFragment.COLOUR, colours[0]);
                    args.putString(ColourDialogFragment.TITLE, "Header Text Colour");
                    frag.setArguments(args);
                    frag.show(getSupportFragmentManager(), COLOUR_DIALOG + "0");
                    break;
                case R.id.iv_counter_text:
                    frag = new ColourDialogFragment();
                    args = new Bundle();
                    args.putInt(ColourDialogFragment.COLOUR, colours[1]);
                    args.putString(ColourDialogFragment.TITLE, "Counter Text Colour");
                    frag.setArguments(args);
                    frag.show(getSupportFragmentManager(), COLOUR_DIALOG + "1");
                    break;
                case R.id.iv_buttons:
                    frag = new ColourDialogFragment();
                    args = new Bundle();
                    args.putInt(ColourDialogFragment.COLOUR, colours[2]);
                    args.putString(ColourDialogFragment.TITLE, "Button Colour");
                    frag.setArguments(args);
                    frag.show(getSupportFragmentManager(), COLOUR_DIALOG + "2");
                    break;
                case R.id.iv_background:
                    frag = new ColourDialogFragment();
                    args = new Bundle();
                    args.putInt(ColourDialogFragment.COLOUR, colours[3]);
                    args.putString(ColourDialogFragment.TITLE, "Background Colour");
                    frag.setArguments(args);
                    frag.show(getSupportFragmentManager(), COLOUR_DIALOG + "3");
                    break;
                case R.id.sw_button_image_colour:
                    Switch s = (Switch) view;
                    ImageButton ib = findViewById(R.id.btn_example);
                    if (s.isChecked()) {
                        ib.setImageDrawable(getDrawable(R.drawable.add_black));
                    } else {
                        ib.setImageDrawable(getDrawable(R.drawable.add_white));
                    }
                    break;
            }
        } else {
            frag = new EditManaDialogFragment();
            args = new Bundle();
            int index = (Integer)view.getTag();
            args.putInt(EditManaDialogFragment.INDEX, index);
            View dot = manaDots.get(index);
            d = (LevelListDrawable)((ImageView)dot.findViewById(R.id.img_background)).getDrawable();
            switch(d.getLevel()) {
                case 1:
                    args.putString(EditManaDialogFragment.CODE, "U");
                    break;
                case 2:
                    args.putString(EditManaDialogFragment.CODE, "R");
                    break;
                case 3:
                    args.putString(EditManaDialogFragment.CODE, "G");
                    break;
                case 4:
                    args.putString(EditManaDialogFragment.CODE, "B");
                    break;
                case 5:
                    args.putString(EditManaDialogFragment.CODE, "W");
                    break;
            }
            args.putBoolean(EditManaDialogFragment.IS_PHYREXIAN, dot.findViewById(R.id.img_phyrexian).getVisibility() == View.VISIBLE);
            frag.setArguments(args);
            frag.show(getSupportFragmentManager(), "edit_mana");
        }
    }

    public void saveClick(View v) {
        switch(v.getId()) {
            case R.id.btn_cancel:
                setResult(RESULT_CANCELED);
                break;
            case R.id.btn_save:
                String errs = "";
                EditText et = findViewById(R.id.edt_name);
                String name = et.getText().toString().trim();
                if(name.isEmpty()) {
                    errs = "\nName cannot be blank.";
                }
                et = findViewById(R.id.edt_cost);
                String untyped = et.getText().toString().trim();
                int manaCount = manaDots.size();
                if(untyped.isEmpty() && manaCount == 0) {
                    errs += "\nMana cannot be nothing, add some dots or enter a number.";
                }
                if(!errs.isEmpty()) {
                    DialogFragment dFrag = new MessageDialogFragment();
                    Bundle data = new Bundle();
                    data.putString(MessageDialogFragment.MESSAGE, "Cannot save because of the following errors:"+errs);
                    dFrag.setArguments(data);
                    dFrag.show(getSupportFragmentManager(), "edit_error");
                    return;
                }
                if(untyped.compareTo("0") != 0 && !untyped.isEmpty()) {
                    manaCount++;
                }
                String[] manaCost;
                if(manaCount > 0) {
                    manaCost = new String[manaCount];
                    int i = 0;
                    if(untyped.compareTo("0") != 0 && !untyped.isEmpty()) {
                        manaCost[i++] = untyped;
                    }
                    LevelListDrawable d;
                    for(Object key : manaDots.keySet()) {
                        d = (LevelListDrawable)((ImageView)manaDots.get(key).findViewById(R.id.img_background)).getDrawable();
                        switch(d.getLevel()) {
                            case 1:
                                manaCost[i] = "U";
                                break;
                            case 2:
                                manaCost[i] = "R";
                                break;
                            case 3:
                                manaCost[i] = "G";
                                break;
                            case 4:
                                manaCost[i] = "B";
                                break;
                            case 5:
                                manaCost[i] = "W";
                                break;
                        }
                        if(manaDots.get(key).findViewById(R.id.img_phyrexian).getVisibility() == View.VISIBLE) {
                            manaCost[i] += "P";
                        }
                        i++;
                    }
                } else {
                    manaCost = new String[]{untyped};
                }
                Switch sw = findViewById(R.id.sw_button_image_colour);
                if(index >= 0) {
                    DataController.getInstance().update(index, name, manaCost, colours[4], colours[0], colours[1], colours[2], colours[3], sw.isChecked());
                } else {
                    DataController.getInstance().create(name, manaCost, colours[4], colours[0], colours[1], colours[2], colours[3], sw.isChecked());
                }
                try {
                    DataController instance = DataController.getInstance();
                    instance.save(new File(getFilesDir(), "data.bin"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                setResult(RESULT_OK);
                break;
        }
        finish();
    }

    @Override
    public void onDialogClick(String tag, Bundle data) {
        if(tag.startsWith(COLOUR_DIALOG)) {
            ImageView iv;
            TextView tv;
            switch (tag.charAt(COLOUR_DIALOG.length())) {
                case '0':
                    colours[0] = data.getInt(ColourDialogFragment.COLOUR);
                    iv = findViewById(R.id.iv_header_text);
                    iv.setBackgroundColor(colours[0]);
                    tv = findViewById(R.id.lbl_example);
                    tv.setTextColor(colours[0]);
                    break;
                case '1':
                    colours[1] = data.getInt(ColourDialogFragment.COLOUR);
                    iv = findViewById(R.id.iv_counter_text);
                    iv.setBackgroundColor(colours[1]);
                    tv = findViewById(R.id.txt_example_counter);
                    tv.setTextColor(colours[1]);
                    break;
                case '2':
                    colours[2] = data.getInt(ColourDialogFragment.COLOUR);
                    iv = findViewById(R.id.iv_buttons);
                    iv.setBackgroundColor(colours[2]);
                    ImageButton btn = findViewById(R.id.btn_example);
                    ColorStateList csl = new ColorStateList(NO_STATE, new int[]{colours[2]});
                    btn.setBackgroundTintList(csl);
                    break;
                case '3':
                    colours[3] = data.getInt(ColourDialogFragment.COLOUR);
                    iv = findViewById(R.id.iv_background);
                    iv.setBackgroundColor(colours[3]);
                    LinearLayout ll = findViewById(R.id.ll_example);
                    ll.setBackgroundColor(colours[3]);
                    break;
                case '4':
                    colours[4] = data.getInt(ColourDialogFragment.COLOUR);
                    iv = findViewById(R.id.iv_commander_text);
                    iv.setBackgroundColor(colours[4]);
                    tv = findViewById(R.id.txt_example_header);
                    tv.setTextColor(colours[4]);
                    break;
            }
        } else if(tag.compareTo("edit_mana") == 0) {
            int index = data.getInt(EditManaDialogFragment.INDEX);
            if(data.containsKey(EditManaDialogFragment.CODE)) {
                View dot = manaDots.get(index);
                LevelListDrawable d = (LevelListDrawable)((ImageView)dot.findViewById(R.id.img_background)).getDrawable();
                switch(data.getString(EditManaDialogFragment.CODE)) {
                    case "U":
                        d.setLevel(1);
                        break;
                    case "R":
                        d.setLevel(2);
                        break;
                    case "G":
                        d.setLevel(3);
                        break;
                    case "B":
                        d.setLevel(4);
                        break;
                    case "W":
                        d.setLevel(5);
                        break;
                }
                if(data.getBoolean(EditManaDialogFragment.IS_PHYREXIAN)) {
                    dot.findViewById(R.id.img_phyrexian).setVisibility(View.VISIBLE);
                } else {
                    dot.findViewById(R.id.img_phyrexian).setVisibility(View.GONE);
                }
            } else {
                View dot = manaDots.remove(index);
                LinearLayout ll = findViewById(R.id.lo_mana);
                ll.removeView(dot);
            }
        }
    }
}
