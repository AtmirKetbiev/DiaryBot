package ru.bot.view;

import org.telegram.abilitybots.api.db.DBContext;
import ru.bot.DB.StorageCourses;
import ru.bot.objects.Task;

public class ViewTask implements StringMaker<String, Task> {

    /*@Override
    public String make(Task task, DBContext db) {
        StorageCourses storageCourses = new StorageCourses(db);
        String idCourse = task.getIdCourse();
        String nameCourse = storageCourses.get(idCourse).getName();

        return "�������� �������:\n" + task.getName() +
                "\n\n�������� �������:\n" + task.getDescription() +
                "\n\nSoft deadline:\n" + task.getSoft() +
                "\n\nHard deadline:\n" + task.getHard() +
                "\n\n����:\n" + nameCourse;
    }*/

    @Override
    public String make(Task task) {
        return "�������� �������:\n" + task.getName() +
                "\n\n�������� �������:\n" + task.getDescription() +
                "\n\nSoft deadline:\n" + task.getSoft() +
                "\n\nHard deadline:\n" + task.getHard();
    }
}