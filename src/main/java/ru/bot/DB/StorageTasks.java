package ru.bot.DB;

import org.telegram.abilitybots.api.db.DBContext;
import ru.bot.objects.Course;
import ru.bot.objects.Task;

import java.util.Map;
import java.util.UUID;

public class StorageTasks implements Storage<Task, String, String> {

    private Map<String, Task> taskMap;
    private DBContext db;

    public StorageTasks(DBContext db) {
        this.taskMap = db.getMap("Tasks");;
        this.db = db;
    }

    @Override
    public Map<String, Task> getMap() {
        return taskMap;
    }

    @Override
    public Task get(String id) {
        return taskMap.get(id);
    }

    @Override
    public void set(Task task) {
        StorageCourses storageCourses = new StorageCourses(db);
        Course course = storageCourses.get(task.getIdCourse());

        StorageContext storageContext = new StorageContext(db);
        String idTask = storageContext.get(course.getIdTeacher()).getIdTask();

        if (idTask != null) {
            this.taskMap.put(storageContext.get(course.getIdTeacher()).getIdTask(), task);
        } else {
            String id = UUID.randomUUID().toString().replace("-", "");
            this.taskMap.put(id, task);
            course.setIdTask(id);
            storageCourses.getMap().put(task.getIdCourse(), course);
        }
    }

    @Override
    public void remove(String s, String s2) {

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
