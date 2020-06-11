package ru.bot.objects;

import java.io.Serializable;
import java.util.*;

public class Student implements Serializable {

    private String name;
    private String group;
    private List<String> courses  = new ArrayList<>();
    //private Map<Integer, Progress> progresses = new HashMap<>();
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

    /*public void addProgress(Progress progress) {
        for (int i : progresses.keySet()) {
            if (progresses.get(i).getIdCourse().equals(progress.getIdCourse()) && progresses.get(i).getIdTask().equals(progress.getIdTask())) {
                progresses.put(i, progress);
                return;
            }
        }
        progresses.put(progresses.size(), progress);
    }
   public Progress getProgresses(String idCourse, String idTask) {
        if (progresses.size()==0) {
            Progress progress = new Progress();
            progress.setIdCourse(idCourse);
            progress.setIdTask(idTask);
            return progress;
        } else {
            for (int i : progresses.keySet()) {
                if (progresses.get(i).getIdCourse().equals(idCourse) && progresses.get(i).getIdTask().equals(idTask)) {
                    return progresses.get(i);
                }
            }
        }
        Progress progress = new Progress();
        progress.setIdCourse(idCourse);
        progress.setIdTask(idTask);
        return progress;
    }*/
    public List<String> getProgresses(String idCourse) {
        return progresses;
    }
    /*public void setProgresses(Map<Integer, Progress> progresses) {
        this.progresses = progresses;
    }*/
}
