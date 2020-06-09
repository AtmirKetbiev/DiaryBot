package ru.bot.db;

import org.telegram.abilitybots.api.db.DBContext;
import ru.bot.objects.Course;

import java.util.Map;

public class StorageCourses implements Storage <Course, String, Long> {

    private Map<String, Course> courseMap;

    public StorageCourses(DBContext db){
        this.courseMap = db.getMap("Courses");
    }

    @Override
    public Course get(String id) {
        return courseMap.get(id);
    }

    @Override
    public void set(Course course) {
            this.courseMap.put(course.getCode(), course);
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