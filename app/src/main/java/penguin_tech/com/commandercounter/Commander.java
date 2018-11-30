package penguin_tech.com.commandercounter;

import java.nio.ByteBuffer;

public class Commander implements Comparable<Commander> {

    public String name;
    public String[] manaCost;
    public int headerText;
    public int counterText;
    public int buttons;
    public int background;
    public boolean buttonImageBlack;

    public Commander(String name, String[] manaCost, int headerText, int counterText, int buttons, int background, boolean buttonImageBlack) {
        this.name = name;
        this.manaCost = new String[manaCost.length];
        System.arraycopy(manaCost, 0, this.manaCost, 0, manaCost.length);
        this.headerText = headerText;
        this.counterText = counterText;
        this.buttons = buttons;
        this.background = background;
        this.buttonImageBlack = buttonImageBlack;
    }

    public Commander(ByteBuffer buffer) {
        name = "";
        char c = buffer.getChar();
        while(c != DataController.ETX) {
            name += c;
            c = buffer.getChar();
        }
        int count = buffer.getInt();
        manaCost = new String[count];
        for(int i = 0; i < count; i++) {
            manaCost[i] = "";
            c = buffer.getChar();
            while(c != DataController.ETX) {
                manaCost[i] += c;
                c = buffer.getChar();
            }
        }
        headerText = buffer.getInt();
        counterText = buffer.getInt();
        buttons = buffer.getInt();
        background = buffer.getInt();
        buttonImageBlack = buffer.get() == 1;
    }

    @Override
    public int compareTo(Commander commander) {
        return name.compareTo(commander.name);
    }

    public int saveSize() {
        int rv = 2*(name.length() + 1) + 4;
        for(String mc : manaCost) {
            rv += 2*(mc.length() + 1);
        }
        rv += 4*4 + 1;
        return rv;
    }

    public void save(ByteBuffer buffer) {
        for(int i = 0; i < name.length(); i++) {
            buffer.putChar(name.charAt(i));
        }
        buffer.putChar(DataController.ETX);
        buffer.putInt(manaCost.length);
        for(String mc : manaCost) {
            for(int i = 0; i < mc.length(); i++) {
                buffer.putChar(mc.charAt(i));
            }
            buffer.putChar(DataController.ETX);
        }
        buffer.putInt(headerText);
        buffer.putInt(counterText);
        buffer.putInt(buttons);
        buffer.putInt(background);
        if(buttonImageBlack) {
            buffer.put((byte)1);
        } else {
            buffer.put((byte)0);
        }
    }
}
