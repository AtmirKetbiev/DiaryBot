import org.telegram.abilitybots.api.db.DBContext;

import java.util.Map;

class Course {
    private Map<Integer, String> courseMap;

    Course(DBContext db) {
        courseMap = db.getMap("Courses");
    }

    public void add(String s) {
        courseMap.put(courseMap.size(), s);
    }

    String[] get() {
        String[] myArray = new String[courseMap.size()];
        ;
        for (int i : courseMap.keySet()) {
            myArray[i] = (courseMap.get(i));
        }
        return myArray;
    }

    public void remove() {
        for (int i : courseMap.keySet()) {
            courseMap.remove(i);
        }
    }

}
