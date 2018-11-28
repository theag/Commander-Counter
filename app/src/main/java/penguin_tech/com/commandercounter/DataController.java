package penguin_tech.com.commandercounter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class DataController extends BaseAdapter {

    public static final char NULL = (char)0;
    public static final char ETX = (char)3;

    private static DataController instance = null;

    public static DataController getInstance() {
        if(instance == null) {
            instance = new DataController();
        }
        return instance;
    }

    public static boolean isNull() {
        return instance == null;
    }

    private ArrayList<Commander> commanders;
    public Context context;

    private DataController() {
        commanders = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return commanders.size();
    }

    @Override
    public Commander getItem(int i) {
        return commanders.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null) {
            LayoutInflater li = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = li.inflate(R.layout.item_commander, null);
        }
        view.setTag(i);
        Commander c = getItem(i);
        TextView tv = view.findViewById(R.id.txt_name);
        tv.setText(c.name);
        ImageButton ib = view.findViewById(R.id.btn_edit);
        ib.setTag(i);
        ib = view.findViewById(R.id.btn_delete);
        ib.setTag(i);
        return view;
    }

    public void update(int index, String name, String mana) {
        Commander c = getItem(index);
        c.name = name;
        c.manaCost = mana.split(",");
        Collections.sort(commanders);
        notifyDataSetChanged();
    }

    public void create(String name, String mana) {
        Commander c = new Commander(name, mana.split(","));
        commanders.add(c);
        Collections.sort(commanders);
        notifyDataSetChanged();
    }

    public void delete(int index) {
        commanders.remove(index);
        notifyDataSetChanged();
    }
}
