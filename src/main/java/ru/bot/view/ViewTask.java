package ru.bot.view;

import org.telegram.abilitybots.api.db.DBContext;
import ru.bot.DB.StorageCourses;
import ru.bot.DB.StorageTeacher;
import ru.bot.objects.Task;

public class ViewTask implements ViewMaker<Task> {
    @Override
    public String make(Task task, DBContext db) {
        StorageCourses storageCourses = new StorageCourses(db);
        String idCourse = task.getIdCourse();
        String nameCourse = storageCourses.get(idCourse).getName();

        return "Название задания:\n" + task.getName() +
                "Описание задания:\n" + task.getDescription() +
                "Soft deadline:\n" + task.getSoft() +
                "Hard deadline:\n" + task.getHard() +
                "Курс:\n" + nameCourse;
    }
}
