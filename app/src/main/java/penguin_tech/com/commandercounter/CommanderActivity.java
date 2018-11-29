package penguin_tech.com.commandercounter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

public class CommanderActivity extends AppCompatActivity {

    public static final String INDEX = "CommanderActivity.index";

    private static final int SETTINGS_REQUEST = 1;

    private int castCount;
    private int life;
    private CommanderDamageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commander);
        int index = getIntent().getIntExtra(INDEX, -1);
        castCount = 0;
        life = 40;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        adapter = new CommanderDamageAdapter(this, sharedPreferences.getInt(SettingsActivity.KEY_PREF_PLAYER_COUNT, 3));
        Commander c = DataController.getInstance().getItem(index);
        TextView tv = findViewById(R.id.txt_commander_name);
        tv.setText(c.name +" Casting");
        tv = findViewById(R.id.txt_cast);
        tv.setText(""+castCount);
        tv = findViewById(R.id.txt_life);
        tv.setText(""+life);
        GridView gv = findViewById(R.id.gv_commander_damage);
        gv.setAdapter(adapter);
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
                //todo:reset
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
                }
                break;
            case R.id.btn_cast_up:
                castCount++;
                tv = findViewById(R.id.txt_cast);
                tv.setText(""+castCount);
                break;
            case R.id.btn_life_down:
                if(life > 0) {
                    life--;
                    tv = findViewById(R.id.txt_life);
                    tv.setText(""+life);
                    if(life == 0) {
                        frag = new MessageDialog();
                        args = new Bundle();
                        args.putString(MessageDialog.MESSAGE, "You are dead.");
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
                adapter.down((int)view.getTag());
                break;
            case R.id.btn_commander_up:
                adapter.up((int)view.getTag());
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case SETTINGS_REQUEST:
                if (resultCode == RESULT_OK) {
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                    int count = sharedPreferences.getInt(SettingsActivity.KEY_PREF_PLAYER_COUNT, 3);
                    if(count != adapter.getCount()) {
                        adapter.setCount(count);
                    }
                }
                break;
        }
    }
}
