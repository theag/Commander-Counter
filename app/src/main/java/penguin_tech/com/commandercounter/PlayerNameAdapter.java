package penguin_tech.com.commandercounter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class PlayerNameAdapter extends BaseAdapter {

    private Context context;
    private String[] names;

    public PlayerNameAdapter(Context context, String[] names) {
        this.context = context;
        this.names = names;
    }

    @Override
    public int getCount() {
        return names.length;
    }

    @Override
    public String getItem(int i) {
        return names[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null) {
            LayoutInflater li = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = li.inflate(R.layout.item_edit_player_name, null);
        }
        EditText tv = view.findViewById(R.id.edt_player_name);
        tv.setText(names[i]);
        tv.setTag(i);
        tv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus) {
                    int pos = (Integer)view.getTag();
                    EditText et = (EditText) view;
                    names[pos] = et.getText().toString();
                }
            }
        });
        return view;
    }

    public void setCount(int count) {
        String[] temp = names;
        names = new String[count];
        System.arraycopy(temp, 0, names, 0, Math.min(count, temp.length));
        if(count > temp.length) {
            for(int i = temp.length; i < count; i++) {
                names[i] = "Player " +(i+1);
            }
        }
        notifyDataSetChanged();
    }
}
