package ru.bot.objects;

import java.io.Serializable;

public class Status implements Serializable {
    //private static final long serialVersionUID = 5L;

    private String idCourse;
    private String idTask;
    private int idGroup;
    private int idStudent;

    public Status() {
        this.idCourse = null;
        this.idTask = null;
        this.idGroup = -1;
        this.idStudent = -1;
    }

    public String getIdCourse() {
        return idCourse;
    }

    public void setIdCourse(String idCourse) {
        this.idCourse = idCourse;
    }

    public String getIdTask() {
        return idTask;
    }

    public void setIdTask(String idTask) {
        this.idTask = idTask;
    }

    public int getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(int idGroup) {
        this.idGroup = idGroup;
    }

    public int getIdStudent() {
        return idStudent;
    }

    public void setIdStudent(int idStudent) {
        this.idStudent = idStudent;
    }
}
