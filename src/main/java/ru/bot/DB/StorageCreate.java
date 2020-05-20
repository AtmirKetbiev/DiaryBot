package ru.bot.DB;

import org.telegram.abilitybots.api.db.DBContext;
import ru.bot.objects.Course;
import ru.bot.objects.Student;
import ru.bot.objects.Task;
import ru.bot.objects.Teacher;

import java.util.Map;

public class StorageCreate {

    private Map<Long, Teacher> createTeacher;
    private Map<Long, Student> createStudent;
    private Map<Long, Course> createCourse;
    private Map<Long, Task> createTask;

    public StorageCreate(DBContext db) {
        this.createTeacher = db.getMap("createTeacher");
        this.createStudent = db.getMap("createStudent");
        this.createCourse = db.getMap("createCourse");
        this.createTask = db.getMap("createTask");
    }

    /** Создание курса................................................................................................*/
    public Map<Long, Course> getCreateCourse() {
        return createCourse;
    }

    public void setCreateCourse(Course course) {
        this.createCourse.put(course.getIdTeacher(), course);
    }

    public void removeCreateCourse(Long id) {
        createCourse.remove(id);
    }

    /** Создание задания..............................................................................................*/
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
