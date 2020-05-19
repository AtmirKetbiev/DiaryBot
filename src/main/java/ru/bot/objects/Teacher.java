package ru.bot.objects;

import java.io.Serializable;
import java.util.ArrayList;


public class Teacher implements Serializable {

    private String name;
    private ArrayList<String> courses = new ArrayList<>();

    //private static final long serialVersionUID = 2L;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getCourses() {
        return courses;
    }

    public void setCourses(ArrayList<String> courses) {
        this.courses = courses;
    }

    public void addCourses(String id) {
        if (courses == null) {
            ArrayList<String> coue = new ArrayList<>();
            coue.add(id);
            this.courses = coue;
        } else {
            courses.add(id);
        }
    }

    public void removeCourse(String id) {
        courses.remove(courses.indexOf(id));
    }

}