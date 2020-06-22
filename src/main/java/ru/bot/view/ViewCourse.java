package ru.bot.view;

import ru.bot.objects.Course;

public class ViewCourse implements StringMaker<Course> {

    @Override
    public String make(Course course) {
        return "Название курса:\n" + course.getName() +
                "\n\nОписание курса:\n" + course.getDescription() +
                "\n\nУникальный идентификатор:\n" + course.getCode();
    }


    /*@Override
    public String make(Course course, DBContext db) {
        StorageTeacher storageTeacher = new StorageTeacher(db);
        StorageCourses storageCourses = new StorageCourses(db);
        Long idTeacher = course.getIdTeacher();
        String nameTeacher = storageTeacher.get(idTeacher).getName();

        return "Название курса:\n" + course.getName() +
                "\n\nОписание курса:\n" + course.getDescription() +
                "\n\nПреподаватель:\n" + nameTeacher +
                "\n\nУникальный идентификатор:" + storageCourses.getIdByName(course.getName() , idTeacher);
    }*/

}