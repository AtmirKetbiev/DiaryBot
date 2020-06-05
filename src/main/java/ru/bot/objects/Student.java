package ru.bot.objects;

import java.io.Serializable;
import java.util.*;

public class Student implements Serializable {

    private String name;                    // ��� ��������
    private String group;                // id ������
    private List<String> courses  = new ArrayList<>();    // ������ � id ������
    private Map<Integer, Progress> progresses = new HashMap<>();

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
    public void setCourses(ArrayList<String> courses) {
        this.courses = courses;
    }
    public void setCourses(String courses) {
        this.courses.add(courses);
    }
    public void removeCourse(String course) {
        courses.remove(course);
    }

    public void addProgress(Progress progress) {
        for (int i : progresses.keySet()) {
            if (progresses.get(i).getIdCourse() == (progress.getIdCourse()) && progresses.get(i).getIdTask() == (progress.getIdTask())) {
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
    }
    public List<Progress> getProgresses(String idCourse) {
        if (progresses.size()!=0) {
            List<Progress> progressList = new ArrayList<>(0);
            for (int i : progresses.keySet()) {
                if (progresses.get(i).getIdCourse().equals(idCourse)) {
                    progressList.add(progresses.get(i));
                }
            }
            return progressList;
        }
        return null;
    }
    public void setProgresses(Map<Integer, Progress> progresses) {
        this.progresses = progresses;
    }
}
