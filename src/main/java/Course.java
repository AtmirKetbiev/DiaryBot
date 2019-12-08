import java.util.LinkedList;

public class Course {
    private String name;
    private LinkedList<Task> task;

    void addTask () {
        Task task = new Task("","",null, null,null);
        this.task.addLast(task);
    }

    String[] getTasks () {
        String[] tasks = new String[task.size()];
        int i = -1;
        for (Task s : task) {
            tasks[i++] += s.getName();
        }
        return (tasks);
    }

    String getName() {
        return name;
    }
}
