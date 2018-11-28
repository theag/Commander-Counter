package penguin_tech.com.commandercounter;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class CommanderActivity extends AppCompatActivity {

    public static final String INDEX = "CommanderActivity.index";
//todo: grid view for commander damage?
    private int index;
    private int castCount;
    private int life;
    //todo:private int[] commanderDamage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commander);
        index = getIntent().getIntExtra(INDEX, -1);
        castCount = 0;
        life = 40;
        //todo:commanderDamage = new int[]{0};
        Commander c = DataController.getInstance().getItem(index);
        TextView tv = findViewById(R.id.txt_commander_name);
        tv.setText(c.name +" Casting");
        tv = findViewById(R.id.txt_cast);
        tv.setText(""+castCount);
        tv = findViewById(R.id.txt_life);
        tv.setText(""+life);
    }

    public void btnClick(View view) {

    }
}
