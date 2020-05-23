package ru.bot.view;

import org.telegram.abilitybots.api.db.DBContext;
import ru.bot.DB.StorageTeacher;
import ru.bot.objects.Course;

public class ViewCourse implements ViewMaker<Course> {

    @Override
    public String make(Course course, DBContext db) {
        StorageTeacher storageTeacher = new StorageTeacher(db);
        Long idTeacher = course.getIdTeacher();
        String nameTeacher = storageTeacher.get(idTeacher).getName();

        return "�������� �����:\n" + course.getName() +
                "\n\n�������� �����:\n" + course.getDescription() +
                "\n\n�������������:\n" + nameTeacher +
                "\n\n���������� �������������:" + course.getIdTasks();
    }
}