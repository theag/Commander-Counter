package penguin_tech.com.commandercounter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class CommanderDamageAdapter extends BaseAdapter {

    private Context context;
    private int[] commanderDamage;

    public CommanderDamageAdapter(Context context, int count) {
        this.context = context;
        commanderDamage = new int[count];
    }

    @Override
    public int getCount() {
        return commanderDamage.length;
    }

    @Override
    public Integer getItem(int position) {
        return commanderDamage[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if(view == null) {
            LayoutInflater li = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = li.inflate(R.layout.item_commander_damage, null);
        }
        TextView tv = view.findViewById(R.id.txt_player);
        tv.setText("Player " +(position+1));
        tv = view.findViewById(R.id.txt_commander);
        tv.setText(""+commanderDamage[position]);
        ImageButton ib = view.findViewById(R.id.btn_commander_down);
        ib.setTag(position);
        ib = view.findViewById(R.id.btn_commander_up);
        ib.setTag(position);
        return view;
    }

    public void setCount(int count) {
        int[] temp = commanderDamage;
        commanderDamage = new int[count];
        if(count > temp.length) {
            System.arraycopy(temp, 0, commanderDamage, 0, temp.length);
        } else {
            System.arraycopy(temp, 0, commanderDamage, 0, count);
        }
        notifyDataSetChanged();
    }

    public void down(int position) {
        commanderDamage[position]--;
        notifyDataSetChanged();
    }

    public void up(int position) {
        commanderDamage[position]++;
        notifyDataSetChanged();
    }

    public void reset() {
        for(int i = 0; i < commanderDamage.length; i++) {
            commanderDamage[i] = 0;
        }
        notifyDataSetChanged();
    }
}
