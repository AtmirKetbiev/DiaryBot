import org.telegram.abilitybots.api.db.DBContext;

import java.util.Map;

// TODO: Реализовать список групп

public class Group {
    private Map<Integer, String> groupMap;

    Group(DBContext db) {
        groupMap = db.getMap("Groups");
    }

    public void add(String s) {
        groupMap.put(groupMap.size(), s);
    }

    String[] get() {
        String[] myArray = new String[groupMap.size()];
        ;
        for (int i : groupMap.keySet()) {
            myArray[i] = (groupMap.get(i));
        }
        return myArray;
    }

    public void remove() {
        for (int i : groupMap.keySet()) {
            groupMap.remove(i);
        }
    }
}
