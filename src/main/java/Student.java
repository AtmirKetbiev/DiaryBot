import org.telegram.abilitybots.api.db.DBContext;

import java.util.Map;

// TODO: Реализовать список студентов

public class Student {

    private Map<Integer, String> studentMap;

    public Student(DBContext db) {
        studentMap = db.getMap("Task");
    }

    public void add(String s) {
        studentMap.put(studentMap.size(), s);
    }

    public String get() {
        return studentMap.values().toString();
    }

    public void remove() {
        for (int i : studentMap.keySet()) {
            studentMap.remove(i);
        }
    }

    /*private int idStudent;
    private LinkedList course;
    private LinkedList task;
    private LinkedList repositories;

    String getCourse() {
        StringBuilder allCourse = new StringBuilder();
        for(Object o : course){
            allCourse.append(o).append("\n");
        }
        return allCourse.toString();
    }

    void addCourse (String name) {
        //Course course = new Course();
    }

    String getRepositories() {
        StringBuilder allRepositories = new StringBuilder();
        for(Object o : repositories){
            allRepositories.append(o).append("\n");
        }
        return allRepositories.toString();
    }

    void addRepositories(String name) {
        repositories.addLast(name);
    }
*/
}
