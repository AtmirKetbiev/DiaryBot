package ru.bot.DB;

import org.telegram.abilitybots.api.db.DBContext;
import ru.bot.objects.Course;
import ru.bot.objects.Teacher;

import java.util.Map;

public class StorageCourses implements Storage <Course, String, Long> {

    private Map<String, Course> courseMap;
    private DBContext db;

    public StorageCourses(DBContext db){
        this.courseMap = db.getMap("Courses");
        this.db = db;
    }

    @Override
    public Map<String, Course> getMap() {
        return courseMap;
    }

    @Override
    public Course get(String id) {
        return courseMap.get(id);
    }

    @Override
    public void set(Course course) {
        StorageContext storageContext = new StorageContext(db);

        String idCourse = storageContext.get(course.getIdTeacher()).getIdCourse();
        if (idCourse != null) {
            if (storageContext.get(course.getIdTeacher()).getIdTask() == null) {
                course.setIdTasks(this.courseMap.get(idCourse).getIdTasks());
            }
            this.courseMap.put(idCourse, course);
        } else {
            this.courseMap.put(course.getCode(), course);
            //teacher.addCourses(course.getCode());
            //storageTeacher.getMap().put(course.getIdTeacher(), teacher);
        }
    }

    @Override
    public void remove(String idCourse, Long idTeacher) {
        StorageTeacher storageTeacher = new StorageTeacher(db);
        Teacher teacher = storageTeacher.getMap().get(idTeacher);
        teacher.removeCourse(idCourse);
        storageTeacher.getMap().put(idTeacher, teacher);
    }

    @Override
    public String getIdByName(String name, Long idTeacher) {
        for (String i : courseMap.keySet()) {
            if (courseMap.get(i).getName().equals(name) && courseMap.get(i).getIdTeacher().equals(idTeacher)) {
                return i;
            }
        }
        return "";
    }
}