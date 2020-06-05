package ru.bot.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Teacher implements Serializable {

    private String name;
    private List<String> courses = new ArrayList<>();

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public List<String> getCourses() {
        return courses;
    }
    public void setCourses(List<String> courses) {
        this.courses = courses;
    }
    public void addCourses(String id) {
            courses.add(id);
    }
    public void removeCourse(String id) {
        courses.remove(courses.indexOf(id));
    }

}