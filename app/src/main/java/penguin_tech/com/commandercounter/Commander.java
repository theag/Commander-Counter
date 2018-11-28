package penguin_tech.com.commandercounter;

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
}
