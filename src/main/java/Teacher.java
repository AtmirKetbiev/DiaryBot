import java.util.LinkedList;

public class Teacher {
    private int idTeacher;
    private LinkedList course;
    private LinkedList task;
    private LinkedList repositories;

    String getCourse () {
        StringBuilder allCourse = new StringBuilder();
        for(Object o : course){
            allCourse.append(o).append("\n");
        }
        return allCourse.toString();
    }

    void addCourse (String name) {
        Course course = new Course();
        this.course.addLast(name);
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

    void getGroup () {
        Group group = new Group();
    }

    void editGroup () {
        Group group = new Group();
    }
}
