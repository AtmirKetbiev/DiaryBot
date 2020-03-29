import java.io.Serializable;

public class Status implements Serializable {
    private static final long serialVersionUID = 5L;

    private int idCourse;
    private int idTask;
    private int idGroup;
    private int idStudent;

    public Status() {
        this.idCourse = -1;
        this.idTask = -1;
        this.idGroup = -1;
        this.idStudent = -1;
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
