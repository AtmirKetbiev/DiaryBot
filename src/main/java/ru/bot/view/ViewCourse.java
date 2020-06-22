package ru.bot.view;

import ru.bot.objects.Course;

public class ViewCourse implements StringMaker<Course> {

    @Override
    public String make(Course course) {
        return "�������� �����:\n" + course.getName() +
                "\n\n�������� �����:\n" + course.getDescription() +
                "\n\n���������� �������������:\n" + course.getCode();
    }


    /*@Override
    public String make(Course course, DBContext db) {
        StorageTeacher storageTeacher = new StorageTeacher(db);
        StorageCourses storageCourses = new StorageCourses(db);
        Long idTeacher = course.getIdTeacher();
        String nameTeacher = storageTeacher.get(idTeacher).getName();

        return "�������� �����:\n" + course.getName() +
                "\n\n�������� �����:\n" + course.getDescription() +
                "\n\n�������������:\n" + nameTeacher +
                "\n\n���������� �������������:" + storageCourses.getIdByName(course.getName() , idTeacher);
    }*/

}