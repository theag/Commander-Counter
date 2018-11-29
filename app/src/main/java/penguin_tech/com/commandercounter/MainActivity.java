package penguin_tech.com.commandercounter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements DialogClickListener {

    private static final String DIALOG_COMMANDER = "MainActivity.Dialog.Commander";
    private static final String DIALOG_MESSAGE = "MainActivity.Dialog.Message";

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
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
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
        DialogFragment frag;
        Bundle args;
        switch (v.getId()) {
            case R.id.btn_add_commander:
                frag = new CommanderDialog();
                frag.show(getSupportFragmentManager(), DIALOG_COMMANDER);
                break;
            case R.id.btn_edit:
                frag = new CommanderDialog();
                args = new Bundle();
                args.putInt(CommanderDialog.INDEX, (int)v.getTag());
                frag.setArguments(args);
                frag.show(getSupportFragmentManager(), DIALOG_COMMANDER);
                break;
            case R.id.btn_delete:
                DataController.getInstance().delete((int)v.getTag());
                break;
            case R.id.lo_commander:
                Intent intent = new Intent(this, CommanderActivity.class);
                intent.putExtra(CommanderActivity.INDEX, (int)v.getTag());
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onDialogClick(String tag, Bundle data) {
        DialogFragment frag;
        Bundle args;
        switch (tag) {
            case DIALOG_COMMANDER:
                if(data.containsKey(CommanderDialog.ERROR)) {
                    frag = new MessageDialogFragment();
                    args = new Bundle();
                    args.putAll(data);
                    args.putString(MessageDialogFragment.MESSAGE, data.getString(CommanderDialog.ERROR));
                    frag.setArguments(args);
                    frag.show(getSupportFragmentManager(), DIALOG_MESSAGE);
                } else if(data.containsKey(CommanderDialog.INDEX)) {
                    DataController.getInstance().update(data.getInt(CommanderDialog.INDEX),
                            data.getString(CommanderDialog.NAME),
                            data.getString(CommanderDialog.MANA));
                    try {
                        DataController instance = DataController.getInstance();
                        instance.save(new File(getFilesDir(), "data.bin"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    DataController.getInstance().create(data.getString(CommanderDialog.NAME),
                            data.getString(CommanderDialog.MANA));
                    try {
                        DataController instance = DataController.getInstance();
                        instance.save(new File(getFilesDir(), "data.bin"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case DIALOG_MESSAGE:
                frag = new CommanderDialog();
                frag.setArguments(data);
                frag.show(getSupportFragmentManager(), DIALOG_COMMANDER);
                break;
        }
    }
}
