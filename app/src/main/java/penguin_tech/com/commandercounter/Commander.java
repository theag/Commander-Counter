package penguin_tech.com.commandercounter;

import java.nio.ByteBuffer;

public class Commander implements Comparable<Commander> {

    public static boolean isValidCost(String cost) {
        String wubrg = "WUBRG";
        String[] split = cost.split(",");
        boolean isNumber;
        int c;
        for(int i = 0; i < split.length; i++) {
            isNumber = false;
            if(i == 0) {
               try {
                   c = Integer.parseInt(split[i]);
                   if(c == 0 && split.length > 1) {
                       return false;
                   } else if(c < 0) {
                       return false;
                   } else {
                       isNumber = true;
                   }
               } catch (NumberFormatException ex) {
               }
            }
            if(!isNumber) {
                if(split[i].length() > 2 || split[i].length() < 1) {
                    return false;
                } else if(split[i].length() == 2) {
                    if(!wubrg.contains(split[i].substring(0, 1))) {
                        return false;
                    }
                    if(split[i].charAt(1) != 'P') {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public String name;
    public String[] manaCost;

    public Commander(String name, String[] manaCost) {
        this.name = name;
        this.manaCost = new String[manaCost.length];
        System.arraycopy(manaCost, 0, this.manaCost, 0, manaCost.length);
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
    }

    public String getManaCostString() {
        String rv = "";
        for(String mc : manaCost) {
            if(!rv.isEmpty()) {
                rv += ",";
            }
            rv += mc;
        }
        return rv;
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
    }
}
