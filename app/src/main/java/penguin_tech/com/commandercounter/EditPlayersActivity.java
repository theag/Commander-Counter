package penguin_tech.com.commandercounter;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

public class EditPlayersActivity extends AppCompatActivity {

    public static final String COUNT = "EditPlayersActivity.count";
    public static final String NAMES = "EditPlayersActivity.names";

    private int count;
    private PlayerNameAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_players);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        count = getIntent().getIntExtra(COUNT, 4);
        EditText et = findViewById(R.id.edt_player_count);
        et.setText(""+count);
        adapter = new PlayerNameAdapter(this, getIntent().getStringArrayExtra(NAMES));
        ListView lv = findViewById(R.id.lv_names);
        lv.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void btnClick(View v) {
        switch (v.getId()) {
            case R.id.btn_update_count:
                EditText et = findViewById(R.id.edt_player_count);
                if(et.getText().length() == 0) {
                    DialogFragment frag = new MessageDialogFragment();
                    Bundle args = new Bundle();
                    args.putString(MessageDialogFragment.MESSAGE, "You must enter a number.");
                    frag.setArguments(args);
                    frag.show(getSupportFragmentManager(), "");
                } else {
                    count = Integer.parseInt(et.getText().toString());
                    adapter.setCount(count);
                }
                break;
            case R.id.btn_cancel:
                setResult(RESULT_CANCELED);
                finish();
                break;
            case R.id.btn_save:
                Intent result = new Intent("com.example.RESULT_ACTION", Uri.parse("content://result_uri"));
                result.putExtra(COUNT, count);
                String[] names = new String[count];
                for(int i = 0; i < count; i++) {
                    names[i] = adapter.getItem(i);
                }
                result.putExtra(NAMES, names);
                setResult(RESULT_OK, result);
                finish();
                break;
        }
    }
}
