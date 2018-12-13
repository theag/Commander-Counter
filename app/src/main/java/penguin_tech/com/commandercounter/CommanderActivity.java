package penguin_tech.com.commandercounter;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.LevelListDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CommanderActivity extends AppCompatActivity implements EditValueDialogFragment.EditValueListener, DialogClickListener {

    public static final String INDEX = "CommanderActivity.index";
    public static final String PLAYER_COUNT = "CommanderActivity.player.count";
    public static final String EDIT_LIFE_DIALOG = "CommanderActivity.EditLifeDialog";
    public static final String RESET_DIALOG = "CommanderActivity.ResetDialog";

    private static final int PLAYER_NAME_REQUEST = 1;
    private static final int[][] NO_STATE = new int[1][0];

    private int index;
    private int initialUntyped;
    private int castCount;
    private int life;
    private CommanderDamageAdapter adapter;
    private View manaDot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commander);
        index = getIntent().getIntExtra(INDEX, -1);
        castCount = 0;
        life = 40;
        Commander c = DataController.getInstance().getItem(index);
        String[] names = new String[getIntent().getIntExtra(PLAYER_COUNT, 4)];
        for(int i = 0; i < names.length; i++) {
            names[i] = "Player " +(i+1);
        }
        adapter = new CommanderDamageAdapter(this, names.length, names, c.headerText, c.counterText, c.buttons, c.buttonImageBlack);
        TextView tv = findViewById(R.id.txt_commander_name);
        tv.setText(c.name);
        tv.setTextColor(c.commanderText);
        tv = findViewById(R.id.lbl_1);
        tv.setTextColor(c.headerText);
        tv = findViewById(R.id.lbl_2);
        tv.setTextColor(c.headerText);
        tv = findViewById(R.id.lbl_3);
        tv.setTextColor(c.headerText);
        LinearLayout ll = (LinearLayout) tv.getParent();
        ll.setBackgroundColor(c.background);
        tv = findViewById(R.id.txt_cast);
        tv.setText(""+castCount);
        tv.setTextColor(c.counterText);
        tv = findViewById(R.id.txt_life);
        tv.setText(""+life);
        tv.setTextColor(c.counterText);
        GridView gv = findViewById(R.id.gv_commander_damage);
        gv.setAdapter(adapter);
        setupMana(c.manaCost, true);
        ColorStateList csl = new ColorStateList(NO_STATE, new int[]{c.buttons});
        findViewById(R.id.btn_cast_down).setBackgroundTintList(csl);
        findViewById(R.id.btn_cast_up).setBackgroundTintList(csl);
        findViewById(R.id.btn_life_down).setBackgroundTintList(csl);
        findViewById(R.id.btn_life_up).setBackgroundTintList(csl);
        if(c.buttonImageBlack) {
            ImageButton btn = findViewById(R.id.btn_cast_down);
            btn.setImageDrawable(getDrawable(R.drawable.remove_black));
            btn = findViewById(R.id.btn_cast_up);
            btn.setImageDrawable(getDrawable(R.drawable.add_black));
            btn = findViewById(R.id.btn_life_down);
            btn.setImageDrawable(getDrawable(R.drawable.remove_black_big));
            btn = findViewById(R.id.btn_life_up);
            btn.setImageDrawable(getDrawable(R.drawable.add_black_big));
        }
    }

    private void setupMana(String[] manaCost, boolean setInitial) {
        LayoutInflater li = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
        LinearLayout ll = findViewById(R.id.lo_mana);
        View manaDot;
        LevelListDrawable d;
        TextView tv;
        if(setInitial) {
            initialUntyped = 0;
        }
        for(String mana : manaCost) {
            manaDot = li.inflate(R.layout.mana_dot, null);
            d = (LevelListDrawable)((ImageView)manaDot.findViewById(R.id.img_background)).getDrawable();
            if(mana.indexOf("U") >= 0) {
                d.setLevel(1);
            } else if(mana.indexOf("R") >= 0) {
                d.setLevel(2);
            } else if(mana.indexOf("G") >= 0) {
                d.setLevel(3);
            } else if(mana.indexOf("B") >= 0) {
                d.setLevel(4);
            } else if(mana.indexOf("W") >= 0) {
                d.setLevel(5);
            } else {
                d.setLevel(0);
                tv = manaDot.findViewById(R.id.txt_amount);
                tv.setText(mana);
                this.manaDot = manaDot;
                if(setInitial) {
                    initialUntyped = Integer.parseInt(mana);
                }
            }
            if(mana.indexOf("P") >= 0) {
                manaDot.findViewById(R.id.img_phyrexian).setVisibility(View.VISIBLE);
            }
            ll.addView(manaDot);
        }
    }

    private void updateMana() {
        if(manaDot == null) {
            LinearLayout ll = findViewById(R.id.lo_mana);
            ll.removeAllViewsInLayout();
            Commander c = DataController.getInstance().getItem(index);
            String[] newMana = new String[c.manaCost.length + 1];
            newMana[0] = "" + (2 * castCount);
            System.arraycopy(c.manaCost, 0, newMana, 1, c.manaCost.length);
            setupMana(newMana, false);
        } else if(castCount == 0 && initialUntyped == 0) {
            LinearLayout ll = findViewById(R.id.lo_mana);
            ll.removeView(manaDot);
            manaDot = null;
        } else {
            TextView tv = manaDot.findViewById(R.id.txt_amount);
            tv.setText("" + (initialUntyped + 2 * castCount));
        }
    }

    private void reset() {
        castCount = 0;
        life = 40;
        TextView tv = findViewById(R.id.txt_cast);
        tv.setText(""+castCount);
        tv = findViewById(R.id.txt_life);
        tv.setText(""+life);
        adapter.reset();
        updateMana();
    }

    public void btnClick(View view) {
        TextView tv;
        DialogFragment frag;
        Bundle args;
        switch(view.getId()) {
            case R.id.btn_cast_down:
                if(castCount > 0) {
                    castCount--;
                    tv = findViewById(R.id.txt_cast);
                    tv.setText(""+castCount);
                    updateMana();
                }
                break;
            case R.id.btn_cast_up:
                castCount++;
                tv = findViewById(R.id.txt_cast);
                tv.setText(""+castCount);
                updateMana();
                break;
            case R.id.btn_life_down:
                if(life > 0) {
                    life--;
                    tv = findViewById(R.id.txt_life);
                    tv.setText(""+life);
                    if(life == 0) {
                        frag = new MessageDialogFragment();
                        args = new Bundle();
                        args.putString(MessageDialogFragment.MESSAGE, "You are dead.");
                        frag.setArguments(args);
                        frag.show(getSupportFragmentManager(),"");
                    } else if(life < 0) {
                        frag = new MessageDialogFragment();
                        args = new Bundle();
                        args.putString(MessageDialogFragment.MESSAGE, "You are even more dead.");
                        frag.setArguments(args);
                        frag.show(getSupportFragmentManager(),"");
                    }
                }
                break;
            case R.id.btn_life_up:
                life++;
                tv = findViewById(R.id.txt_life);
                tv.setText(""+life);
                break;
            case R.id.btn_commander_down:
                if(adapter.getItem((int)view.getTag()) > 0) {
                    adapter.down((int) view.getTag());
                }
                break;
            case R.id.btn_commander_up:
                adapter.up((int)view.getTag());
                if(adapter.getItem((int)view.getTag()) == 20) {
                    frag = new MessageDialogFragment();
                    args = new Bundle();
                    args.putString(MessageDialogFragment.MESSAGE, "You are dead.");
                    frag.setArguments(args);
                    frag.show(getSupportFragmentManager(),"");
                } else if(adapter.getItem((int)view.getTag()) > 20) {
                    frag = new MessageDialogFragment();
                    args = new Bundle();
                    args.putString(MessageDialogFragment.MESSAGE, "You are very dead.");
                    frag.setArguments(args);
                    frag.show(getSupportFragmentManager(),"");
                }
                break;
            case R.id.txt_life:
                frag = new EditValueDialogFragment();
                args = new Bundle();
                args.putInt(EditValueDialogFragment.INITAL_VALUE, life);
                frag.setArguments(args);
                frag.show(getSupportFragmentManager(), EDIT_LIFE_DIALOG);
                break;
            case R.id.lbl_3:
                Intent intent = new Intent(this, EditPlayersActivity.class);
                intent.putExtra(EditPlayersActivity.COUNT, adapter.getCount());
                intent.putExtra(EditPlayersActivity.NAMES, adapter.getNames());
                startActivityForResult(intent, PLAYER_NAME_REQUEST);
                break;
            case R.id.txt_commander_name:
                frag = new ConfirmDialogFragment();
                args = new Bundle();
                args.putString(ConfirmDialogFragment.MESSAGE, "Are you sure you wish to reset this game?");
                args.putString(ConfirmDialogFragment.POS, "Yes");
                args.putString(ConfirmDialogFragment.NEG, "No");
                frag.setArguments(args);
                frag.show(getSupportFragmentManager(), RESET_DIALOG);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PLAYER_NAME_REQUEST && resultCode == RESULT_OK) {
            adapter.setCount(data.getIntExtra(EditPlayersActivity.COUNT, 4), data.getStringArrayExtra(EditPlayersActivity.NAMES));
        }
    }

    @Override
    public void valueChanged(String tag, int value) {
        life = value;
        TextView tv = findViewById(R.id.txt_life);
        tv.setText(""+life);
        if(life <= 0) {
            DialogFragment frag = new MessageDialogFragment();
            Bundle args = new Bundle();
            args.putString(MessageDialogFragment.MESSAGE, "You are dead.");
            frag.setArguments(args);
            frag.show(getSupportFragmentManager(),"");
        }
    }

    @Override
    public void onDialogClick(String tag, Bundle data) {
        if(tag.compareTo(RESET_DIALOG) == 0 && data.getInt(ConfirmDialogFragment.RESULT) == ConfirmDialogFragment.YES) {
            reset();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        } else {
            showSystemUI();
        }
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    // Shows the system bars by removing all the flags
// except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

}
