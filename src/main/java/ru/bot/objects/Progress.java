package ru.bot.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Progress implements Serializable {
    private String idProgress;
    private Long idStudent;
    private String idCourse;
    private String idTask;
    private boolean mark;
    private String grade;
    private List<String> comments = new ArrayList<>();

    public String getIdProgress() {
        return idProgress;
    }

    public void setIdProgress(String idProgress) {
        this.idProgress = idProgress;
    }

    public Long getIdStudent() {
        return idStudent;
    }

    public void setIdStudent(Long idStudent) {
        this.idStudent = idStudent;
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

    public boolean isMark() {
        return mark;
    }

    public void setMark(boolean mark) {
        this.mark = mark;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public List<String> getComment() {
        return comments;
    }

    public void setComment(String comment) {
            this.comments.add(comment);
    }
}
