import java.util.LinkedList;

public class Student {
    private int idStudent;
    private LinkedList course;
    private LinkedList task;
    private LinkedList repositories;

    public Student() {
    }

    String getCourse() {
        StringBuilder allCourse = new StringBuilder();
        for(Object o : course){
            allCourse.append(o).append("\n");
        }
        return allCourse.toString();
    }

    void addCourse (String name) {
        Course course = new Course();
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

}
