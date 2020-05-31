package ru.bot.objects;

import java.io.Serializable;

public class Context implements Serializable {
    //private static final long serialVersionUID = 5L;

    private String idCourse;
    private String idTask;
    private String idGroup;
    private Long idStudent;

    public Context() {
        this.idCourse = null;
        this.idTask = null;
        this.idGroup = null;
        this.idStudent = -1L;
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

    public String getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(String idGroup) {
        this.idGroup = idGroup;
    }

    public Long getIdStudent() {
        return idStudent;
    }

    public void setIdStudent(Long idStudent) {
        this.idStudent = idStudent;
    }
}
