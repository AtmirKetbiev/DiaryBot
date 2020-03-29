import java.util.ArrayList;

public class Progress {
    private int idCourse;
    private int idTask;
    private boolean mark;
    private String grade;
    private ArrayList<String> comment;

    public boolean isMark() {
        return mark;
    }

    public void setMark(boolean mark) {
        this.mark = mark;
    }

    public int getIdCourse() {
        return idCourse;
    }

    public void setIdCourse(int idCourse) {
        this.idCourse = idCourse;
    }

    public int getIdTask() {
        return idTask;
    }

    public void setIdTask(int idTask) {
        this.idTask = idTask;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public ArrayList<String> getComment() {
        return comment;
    }

    public void addComment(String comment) {
        if (comment == null) {
            ArrayList<String> coue = new ArrayList<>();
            coue.add(comment);
            this.comment = coue;
        } else {
            this.comment.add(comment);
        }
    }
}
