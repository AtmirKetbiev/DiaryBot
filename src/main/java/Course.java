import org.telegram.abilitybots.api.db.DBContext;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

class Course implements Serializable {

    private String name;
    private String description;
    private Long idTeacher;
    private ArrayList<Integer> tasks;

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

    public void addTask(Integer id) {
        if (tasks == null) {
            ArrayList<Integer> coue = new ArrayList<Integer>();
            coue.add(id);
            this.tasks = coue;
        } else {
            tasks.add(id);
        }
    }

    public ArrayList<Integer> getIdTasks() {
        return tasks;
    }

    public void setIdTasks(ArrayList<Integer> idTasks) {
        this.tasks = idTasks;
    }

    private static final long serialVersionUID = 1L;

    String[] get(Map<Integer, String> courseMap) {
        String[] myArray = new String[courseMap.size()];
        for (int i : courseMap.keySet()) {
            myArray[i] = (courseMap.get(i));
        }
        return myArray;
    }

    boolean check() {
        if (name == null)  return false;
        if (description == null)  return false;
        //if (idTasks == null)  return false;
        return true;
    }

}
