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

        return "Название задания:\n" + task.getName() +
                "\n\nОписание задания:\n" + task.getDescription() +
                "\n\nSoft deadline:\n" + task.getSoft() +
                "\n\nHard deadline:\n" + task.getHard() +
                "\n\nКурс:\n" + nameCourse;
    }*/

    @Override
    public String make(Task task) {
        return "Название задания:\n" + task.getName() +
                "\n\nОписание задания:\n" + task.getDescription() +
                "\n\nSoft deadline:\n" + task.getSoft() +
                "\n\nHard deadline:\n" + task.getHard();
    }
}