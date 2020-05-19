package ru.bot.view;

import org.telegram.abilitybots.api.db.DBContext;
import ru.bot.DB.StorageContext;
import ru.bot.DB.StorageTeacher;
import ru.bot.extension.Keyboard;
import ru.bot.objects.Course;

public class ViewCourse implements ViewMaker<Course> {

    @Override
    public String make(Course course, DBContext db) {
        StorageTeacher storageTeacher = new StorageTeacher(db);
        Long idTeacher = course.getIdTeacher();
        String nameTeacher = storageTeacher.get(idTeacher).getName();

        return "�������� �����:\n" + course.getName() +
                "�������� �����:\n" + course.getDescription() +
                "�������������:\n" + nameTeacher;
    }
}
