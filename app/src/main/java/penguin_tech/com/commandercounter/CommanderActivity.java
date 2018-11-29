package penguin_tech.com.commandercounter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.LevelListDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CommanderActivity extends AppCompatActivity implements EditValueDialogFragment.EditValueListener {

    public static final String INDEX = "CommanderActivity.index";
    public static final String EDIT_LIFE_DIALOG = "CommanderActivity.EditLifeDialog";

    private static final int SETTINGS_REQUEST = 1;

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
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        adapter = new CommanderDamageAdapter(this, Integer.parseInt(sharedPreferences.getString(SettingsActivity.KEY_PREF_PLAYER_COUNT, "3")));
        Commander c = DataController.getInstance().getItem(index);
        TextView tv = findViewById(R.id.txt_commander_name);
        tv.setText(c.name +" Casting");
        tv = findViewById(R.id.txt_cast);
        tv.setText(""+castCount);
        tv = findViewById(R.id.txt_life);
        tv.setText(""+life);
        GridView gv = findViewById(R.id.gv_commander_damage);
        gv.setAdapter(adapter);
        setupMana(c.manaCost, true);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_commander, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.mi_reset:
                castCount = 0;
                life = 40;
                TextView tv = findViewById(R.id.txt_cast);
                tv.setText(""+castCount);
                tv = findViewById(R.id.txt_life);
                tv.setText(""+life);
                adapter.reset();
                updateMana();
                return true;
            case R.id.mi_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivityForResult(intent, SETTINGS_REQUEST);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
                }
                break;
            case R.id.txt_life:
                frag = new EditValueDialogFragment();
                args = new Bundle();
                args.putInt(EditValueDialogFragment.INITAL_VALUE, life);
                frag.setArguments(args);
                frag.show(getSupportFragmentManager(), EDIT_LIFE_DIALOG);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case SETTINGS_REQUEST:
                if (resultCode == RESULT_OK) {
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                    int count = Integer.parseInt(sharedPreferences.getString(SettingsActivity.KEY_PREF_PLAYER_COUNT, "3"));
                    if(count != adapter.getCount()) {
                        adapter.setCount(count);
                    }
                }
                break;
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
}
