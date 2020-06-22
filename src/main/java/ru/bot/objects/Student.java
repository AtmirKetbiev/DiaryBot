package ru.bot.objects;

import java.io.Serializable;
import java.util.*;

public class Student implements Serializable {

    private String name;
    private String group;
    private List<String> courses  = new ArrayList<>();
    private List<String> progresses  = new ArrayList<>();

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }
    public void setGroup(String group) {
        this.group = group;
    }

    public List<String> getCourses() {
        return courses;
    }
    public void setCourses(String course) {
        this.courses.add(course);
    }
    public void removeCourse(String course) {
        courses.remove(course);
    }

    public List<String> getProgresses() {
        return progresses;
    }
    public void setProgresses(String progress) {
        this.progresses.add(progress);
    }
    public void removeProgresses(String progress) {
        progresses.remove(progress);
    }

    public List<String> getProgresses(String idCourse) {
        return progresses;
    }

}
