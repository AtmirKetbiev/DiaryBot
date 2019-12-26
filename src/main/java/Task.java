import org.telegram.abilitybots.api.db.DBContext;

import java.util.Map;

class Task {

    private Map<Integer, String[]> taskMap;

    Task(DBContext db) {
        taskMap = db.getMap("Tasks");
    }

    public void add(String course, String s) {
        taskMap.put(taskMap.size(), new String[]{course, s});
    }

    String[] get(String course) {
        String[] myArrayTask = new String[taskMap.size()];
        for (int i : taskMap.keySet()) {
            if (taskMap.get(i)[0].equals(course)) {
                myArrayTask[i] = taskMap.get(i)[1];
            }
        }
        return myArrayTask;
    }

    public void remove() {
        for (int i : taskMap.keySet()) {
            taskMap.remove(i);
        }
    }
}
