package penguin_tech.com.commandercounter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final String DIALOG_COMMANDER = "MainActivity.Dialog.Commander";
    private static final String DIALOG_MESSAGE = "MainActivity.Dialog.Message";
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /*MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);*/
        return true;
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
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //todo: main menu stuff
        /*switch(item.getItemId()) {
            case R.id.mi_filter:
                return true;
            case R.id.mi_search:
                return true;
        }*/
        return super.onOptionsItemSelected(item);
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
                startActivity(intent);
                break;
        }
    }
}
