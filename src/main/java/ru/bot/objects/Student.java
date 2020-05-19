package ru.bot.objects;

//import com.sun.javaws.progress.Progress;
import org.telegram.abilitybots.api.db.DBContext;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;


public class Student implements Serializable {
    //private static final long serialVersionUID = 4L;

    private String stat;

    private String name;                    // Имя студента
    private String group;                // id группы
    private ArrayList<Integer> courses;    // Список с id курсов
    private Map<Integer, Progress> progresses;

    public String getStat() {
        return stat;
    }
    public void setStat(String stat) {
        this.stat = stat;
    }

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

    public ArrayList<Integer> getCourses() {
        return courses;
    }

    public void setCourses(ArrayList<Integer> courses) {
        this.courses = courses;
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

    public Progress getProgresses(int idCourse, int idTask) {
        for (int i : progresses.keySet()) {
            if (progresses.get(i).getIdCourse() == idCourse && progresses.get(i).getIdTask() == idTask) {
                return progresses.get(i);
            }
        }
        return null;
    }

    public void setProgresses(Map<Integer, Progress> progresses) {
        this.progresses = progresses;
    }
}
