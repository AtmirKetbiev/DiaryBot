import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

// TODO: Реализовать список преподователей

public class Teacher implements Serializable {

    private String name;                // Имя преподавателя
    private ArrayList<Integer> courses = new ArrayList<Integer>();  // Список с id курсов

    private static final long serialVersionUID = 2L;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Integer> getCourses() {
        return courses;
    }

    public void setCourses(ArrayList<Integer> courses) {
        this.courses = courses;
    }

    public void addCourses(Integer id) {
        if (courses == null) {
            ArrayList<Integer> coue = new ArrayList<Integer>();
            coue.add(id);
            this.courses = coue;
        } else {
            courses.add(id);
        }
    }

    //private Map<Integer, String> course;

}
