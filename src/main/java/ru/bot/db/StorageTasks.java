package ru.bot.db;

import org.telegram.abilitybots.api.db.DBContext;
import ru.bot.objects.Task;

import java.util.Map;

public class StorageTasks implements Storage<Task, String, String> {

    private Map<String, Task> taskMap;

    public StorageTasks(DBContext db) {
        this.taskMap = db.getMap("Tasks");
    }

    @Override
    public Task get(String id) {
        return taskMap.get(id);
    }

    @Override
    public void set(Task task) {
            this.taskMap.put(task.getCode(), task);
    }

    @Override
    public void remove(Task task) {
        if (!taskMap.isEmpty()) {
            this.taskMap.remove(task.getCode());
        }
    }

    @Override
    public String getIdByName(String name, String idCourse) {
        for (String i : taskMap.keySet()) {
            if (taskMap.get(i).getName().equals(name) && taskMap.get(i).getIdCourse().equals(idCourse)) {
                return i;
            }
        }
        return "";
    }
}
