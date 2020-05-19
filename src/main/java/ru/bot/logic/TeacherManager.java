package ru.bot.logic;

import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bot.DB.*;
import ru.bot.extension.Keyboard;
import ru.bot.objects.Course;
import ru.bot.objects.Status;
import ru.bot.objects.Task;
import ru.bot.objects.Teacher;
import ru.bot.view.ViewCourse;

import java.util.List;
import java.util.Map;

public class TeacherManager {

    private StorageTeacher storageTeacher;
    private StorageStudent storageStudent;
    private StorageCourses storageCourses;
    private StorageTasks storageTasks;
    private StorageContext storageContext;
    private DBContext db;

    public TeacherManager(DBContext db) {
        this.storageTeacher = new StorageTeacher(db);
        this.storageStudent = new StorageStudent(db);
        this.storageCourses = new StorageCourses(db);
        this.storageTasks = new StorageTasks(db);
        this.storageContext = new StorageContext(db);
        this.db = db;
    }

    public String start(Update update) {
        Long id = update.getMessage().getChatId();
        Teacher teacher = new Teacher();
        teacher.setName("препод");
        storageTeacher.set(id, teacher);
        return "Добро пожаловать!";
    }

    public void back(Update update) {
        String answer;
        Long id = update.getMessage().getChatId();

        Status status = storageContext.get(id);
        if (status.getIdTask() == null && status.getIdCourse() == null) {
            status.setIdCourse(null);
            answer = "";
            //silent.execute(Keyboard.addKeyboard(new String[]{"Добавить курс", "Посмотреть курсы", "Изменить профиль", "Помощь"}, update, answer));
        }
        if (status.getIdTask() == null && status.getIdCourse() != null) {
            status.setIdCourse(null);
            storageContext.set(id, status);
            String[] myArray = getCourse(id);
            answer = "Ваши курсы:";
            //silent.execute(Keyboard.addKeyboard(myArray, update, answer));
        }
        if (status.getIdTask() != null) {
            status.setIdTask(null);
            storageContext.set(id, status);
            String[] myArray = getTask(id);
            answer = "Ваши задания:";
            //silent.execute(Keyboard.addKeyboard(myArray, update, answer));
        }
    }

    public String course(Update update) {
        String answer;
        Long id = update.getMessage().getChatId();
        String text = update.getMessage().getText();
        Status status = new Status();
        status.setIdCourse(storageCourses.getIdByName(text, id));
        storageContext.set(id, status);
        ViewCourse view = new ViewCourse();
        answer = view.make(storageCourses.get(status.getIdCourse()), db);
        return answer;
    }

    public String[] getCourse(Long id) {
        Teacher teacher = storageTeacher.get(id);
        Map<String, Course> course = storageCourses.getMap();
        int q = 0;
        String[] myArray = new String[teacher.getCourses().size()];
        for (String i : teacher.getCourses()) {
            myArray[q] = course.get(i).getName();
            q++;
        }
        return myArray;
    }

    public String[] getTask(Long id) {
        if (storageContext.get(id).getIdCourse() == null) {
            return new String[0];
        }
        Map<String, Task> task = storageTasks.getMap();
        Map<String, Course> course = storageCourses.getMap();
        Course idCourse = course.get(storageContext.get(id).getIdCourse());
        List<String> idTask = idCourse.getIdTasks();
        int q = 0;
        String[] myArray;
        if (idTask != null) {
            myArray = new String[idTask.size()];

        } else {
            myArray = new String[0];
            return myArray;
        }
        for (String i : idTask) {
            myArray[q] = task.get(i).getName();
            q++;
        }
        return myArray;
    }
}
