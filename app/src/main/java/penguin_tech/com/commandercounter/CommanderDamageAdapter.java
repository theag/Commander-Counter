package penguin_tech.com.commandercounter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class CommanderDamageAdapter extends BaseAdapter {

    private static final int[][] NO_STATE = new int[1][0];

    private Context context;
    private int[] commanderDamage;
    private int counterText;
    private int buttons;
    private boolean buttonImageBlack;

    public CommanderDamageAdapter(Context context, int count, int counterText, int buttons, boolean buttonImageBlack) {
        this.context = context;
        commanderDamage = new int[count];
        this.counterText = counterText;
        this.buttons = buttons;
        this.buttonImageBlack = buttonImageBlack;
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
            TextView tv = view.findViewById(R.id.txt_player);
            tv.setTextColor(counterText);
            ColorStateList csl = new ColorStateList(NO_STATE, new int[]{buttons});
            ImageButton ib = view.findViewById(R.id.btn_commander_down);
            ib.setBackgroundTintList(csl);
            if(buttonImageBlack) {
                ib.setImageDrawable(context.getDrawable(R.drawable.remove_black));
            }
            ib = view.findViewById(R.id.btn_commander_up);
            ib.setBackgroundTintList(csl);
            if(buttonImageBlack) {
                ib.setImageDrawable(context.getDrawable(R.drawable.add_black));
            }
        }
        TextView tv = view.findViewById(R.id.txt_player);
        tv.setText("Player " +(position+1));
        tv = view.findViewById(R.id.txt_commander);
        tv.setText(""+commanderDamage[position]);
        tv.setTextColor(counterText);
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
