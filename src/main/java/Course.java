import java.io.Serializable;
import java.util.ArrayList;

public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private String description;
    private Long idTeacher;
    private ArrayList<Integer> tasks;
    private ArrayList<String> group;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getIdTeacher() {
        return idTeacher;
    }

    public void setIdTeacher(Long idTeacher) {
        this.idTeacher = idTeacher;
    }

    public ArrayList<Integer> getIdTasks() {
        return tasks;
    }

    public void setIdTasks(ArrayList<Integer> idTasks) {
        this.tasks = idTasks;
    }

    public void addTask(Integer id) {
        if (tasks == null) {
            ArrayList<Integer> coue = new ArrayList<>();
            coue.add(id);
            this.tasks = coue;
        } else {
            tasks.add(id);
        }
    }

    public void addTask(ArrayList<Integer> task) {
        this.tasks = task;
    }

    public void removeTask(Integer id) {
        tasks.remove(id);
    }

    public void addGroup(String str) {
        if (group == null) {
            ArrayList<String> coue = new ArrayList<>();
            coue.add(str);
            this.group = coue;
        } else {
            group.add(str);
        }
    }

    public ArrayList<String> getGroup() {
        return group;
    }

    public void removeGrop(Integer id) {
        group.remove(id);
    }

    public boolean check() {
        if (name == null)  return false;
        if (description == null)  return false;
        //if (idTasks == null)  return false;
        return true;
    }

}
