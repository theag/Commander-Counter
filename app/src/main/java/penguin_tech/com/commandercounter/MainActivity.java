package penguin_tech.com.commandercounter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final int EDIT_COMMANDER_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        File f = new File(getFilesDir(), "data.bin");
        if(f.exists()) {
            try {
                DataController.loadInstance(f);
            } catch (IOException e) {
                e.printStackTrace(System.out);
            }
        }

        ListView lv = findViewById(R.id.lv_commanders);
        DataController.getInstance().context = this;
        lv.setAdapter(DataController.getInstance());

        SharedPreferences pref = getPreferences(MODE_PRIVATE);
        int count = pref.getInt(getString(R.string.pref_player_count),4);
        EditText et = findViewById(R.id.edt_player_count);
        et.setText(""+count);
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            DataController instance = DataController.getInstance();
            instance.save(new File(getFilesDir(), "data.bin"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        SharedPreferences pref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        EditText et = findViewById(R.id.edt_player_count);
        editor.putInt(getString(R.string.pref_player_count), Integer.parseInt(et.getText().toString()));
        editor.commit();
        System.out.println("pausing main");
    }

    public void btnClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btn_add_commander:
                intent = new Intent(this, EditCommanderActivity.class);
                startActivityForResult(intent, EDIT_COMMANDER_REQUEST);
                break;
            case R.id.btn_edit:
                intent = new Intent(this, EditCommanderActivity.class);
                intent.putExtra(EditCommanderActivity.INDEX, (int)v.getTag());
                startActivityForResult(intent, EDIT_COMMANDER_REQUEST);
                break;
            case R.id.btn_delete:
                DataController.getInstance().delete((int)v.getTag());
                break;
            case R.id.lo_commander:
                intent = new Intent(this, CommanderActivity.class);
                intent.putExtra(CommanderActivity.INDEX, (int)v.getTag());
                EditText et = findViewById(R.id.edt_player_count);
                intent.putExtra(CommanderActivity.PLAYER_COUNT, Integer.parseInt(et.getText().toString()));
                startActivity(intent);
                break;
        }
    }
}
