import java.util.LinkedList;

public class Teacher {
    private int idTeacher;
    private LinkedList<Course> course;
    private LinkedList<String> repositories;

    Teacher (int idTeacher) {
        this.idTeacher = idTeacher;
    }

    String[] getCourse () {
        String[] courses = new String[course.size()];
        int i = -1;
        for (Course s : course) {
            courses[i++] += s.getName();
        }
        return (courses);
    }

    void addCourse (String name) {
        Course course = new Course();
        this.course.addLast(course);
    }

    void deleteCourse(String name) {

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
