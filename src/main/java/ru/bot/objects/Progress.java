package ru.bot.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Progress implements Serializable {
    private String idCourse;
    private String idTask;
    private boolean mark;
    private String grade;
    private ArrayList<String> comment = new ArrayList<>();

    public boolean isMark() {
        return mark;
    }

    public void setMark(boolean mark) {
        this.mark = mark;
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

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public List<String> getComment() {
        return comment;
    }

    public void addComment(String comment) {
            this.comment.add(comment);
    }
}
