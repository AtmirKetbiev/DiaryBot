package ru.bot.db;

import org.telegram.abilitybots.api.db.DBContext;
import ru.bot.objects.Course;
import ru.bot.objects.Task;

import java.util.Map;

public class StorageCreate {

    private Map<Long, Course> createCourse;
    private Map<Long, Task> createTask;

    public StorageCreate(DBContext db) {
        this.createCourse = db.getMap("createCourse");
        this.createTask = db.getMap("createTask");
    }

    /** �������� �����................................................................................................*/
    public Map<Long, Course> getCreateCourse() {
        return createCourse;
    }

    public void setCreateCourse(Course course) {
        this.createCourse.put(course.getIdTeacher(), course);
    }

    public void removeCreateCourse(Long id) {
        createCourse.remove(id);
    }

    /** �������� �������..............................................................................................*/
    public Map<Long, Task> getCreateTask() {
        return createTask;
    }

    public void setCreateTask(Long id, Task task) {
        this.createTask.put(id, task);
    }

    public void removeCreateTask(Long id) {
        createTask.remove(id);
    }

}
