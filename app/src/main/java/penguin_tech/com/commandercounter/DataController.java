package penguin_tech.com.commandercounter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;

public class DataController extends BaseAdapter {

    public static final char NULL = (char)0;
    public static final char ETX = (char)3;

    private static DataController instance = null;

    public static DataController loadInstance(File f) throws IOException {
        if(instance == null) {
            instance = new DataController(f);
        }
        return instance;
    }

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

    private DataController(File file) throws IOException {
        commanders = new ArrayList<>();
        byte[] bytes = new byte[(int)file.length()];
        FileInputStream fin = new FileInputStream(file);
        fin.read(bytes);
        fin.close();
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        while(buffer.hasRemaining()) {
            commanders.add(new Commander(buffer));
        }
    }

    public void save(File file) throws IOException {
        int saveSize = 0;
        for(Commander c : commanders) {
            saveSize += c.saveSize();
        }
        ByteBuffer buffer = ByteBuffer.allocate(saveSize);
        for(Commander c : commanders) {
            c.save(buffer);
        }
        FileOutputStream fOut = new FileOutputStream(file);
        fOut.write(buffer.array());
        fOut.close();
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

    public void update(int index, String name, String[] mana, int headerText, int counterText, int buttons, int background, boolean buttonImageBlack) {
        Commander c = getItem(index);
        c.name = name;
        c.manaCost = mana;
        c.headerText = headerText;
        c.counterText = counterText;
        c.buttons = buttons;
        c.background = background;
        c.buttonImageBlack = buttonImageBlack;
        Collections.sort(commanders);
        notifyDataSetChanged();
    }

    public void create(String name, String[] mana, int headerText, int counterText, int buttons, int background, boolean buttonImageBlack) {
        Commander c = new Commander(name, mana, headerText, counterText, buttons, background, buttonImageBlack);
        commanders.add(c);
        Collections.sort(commanders);
        notifyDataSetChanged();
    }

    public void delete(int index) {
        commanders.remove(index);
        notifyDataSetChanged();
    }
}
