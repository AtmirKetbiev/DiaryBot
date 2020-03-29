import java.io.Serializable;
import java.util.ArrayList;

// TODO: Реализовать список преподователей

public class Teacher implements Serializable {

    private String name;
    private ArrayList<Integer> courses = new ArrayList<>();

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
            ArrayList<Integer> coue = new ArrayList<>();
            coue.add(id);
            this.courses = coue;
        } else {
            courses.add(id);
        }
    }

    public void removeCourse(int id) {
        courses.remove(courses.indexOf(id));
    }

}