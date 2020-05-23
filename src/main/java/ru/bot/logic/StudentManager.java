package ru.bot.logic;

import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bot.DB.*;
import ru.bot.extension.Timetable;
import ru.bot.objects.*;
import ru.bot.view.ViewCourse;
import ru.bot.view.ViewTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class StudentManager {

    private StorageTeacher storageTeacher;
    private StorageStudent storageStudent;
    private StorageCourses storageCourses;
    private StorageTasks storageTasks;
    private StorageContext storageContext;
    private StorageCreate storageCreate;
    private DBContext db;
    private ContextAnswer contextAnswer = new ContextAnswer();

    public StudentManager(DBContext db) {
        this.storageTeacher = new StorageTeacher(db);
        this.storageStudent = new StorageStudent(db);
        this.storageCourses = new StorageCourses(db);
        this.storageTasks = new StorageTasks(db);
        this.storageContext = new StorageContext(db);
        this.storageCreate = new StorageCreate(db);
        this.db = db;
    }

    public ContextAnswer start(Update update) {
        Long id = update.getMessage().getChatId();
        Student student = new Student();
        student.setName("студент");
        storageStudent.set(id, student);
        contextAnswer.setAnswer("Добро пожаловать!");
        contextAnswer.setList(Arrays.asList("Расписание", "Курсы", "Подписаться на курс", "Помощь"));
        return contextAnswer;
    }

    public ContextAnswer back(Update update) {
        Long id = update.getMessage().getChatId();

        Status status = storageContext.get(id);
        if (status.getIdTask() == null && status.getIdCourse() == null) {
            status.setIdCourse(null);
            contextAnswer.setAnswer("Добро пожаловать!");
            contextAnswer.setList(Arrays.asList("Расписание", "Курсы", "Подписаться на курс", "Помощь"));
        }
        if (status.getIdTask() == null && status.getIdCourse() != null) {
            status.setIdCourse(null);
            storageContext.set(id, status);
            contextAnswer.setAnswer("Ваши курсы:");
            contextAnswer.setList(getCourse(id));
        }
        if (status.getIdTask() != null) {
            status.setIdTask(null);
            storageContext.set(id, status);
            contextAnswer.setAnswer("Ваши задания:");
            contextAnswer.setList(getTask(id));
        }
        return contextAnswer;
    }

    public ContextAnswer timetable(Update update) {
        contextAnswer.setAnswer(Timetable.timetable(0));
        contextAnswer.setList(null);
        return contextAnswer;
    }



    public List<String> getCourse(Long id) {
        Student student = storageStudent.get(id);
        Map<String, Course> course = storageCourses.getMap();
        int q = 0;
        List<String> myArray = new ArrayList<String>(0);
        for (String i : student.getCourses()) {
            myArray.add(course.get(i).getName());
            q++;
        }
        return myArray;
    }

    public List<String> getTask(Long id) {
        List<String> myArray = new ArrayList<String>(0);
        if (storageContext.get(id).getIdCourse() == null) {
            return myArray;
        }
        Map<String, Task> task = storageTasks.getMap();
        Map<String, Course> course = storageCourses.getMap();
        Course idCourse = course.get(storageContext.get(id).getIdCourse());
        List<String> idTask = idCourse.getIdTasks();
        int q = 0;
        if (idTask != null) {
            for (String i : idTask) {
                myArray.add(task.get(i).getName());
                q++;
            }
        } else {
            return myArray;
        }
        return myArray;
    }

    public ContextAnswer course(Update update) {
        Long id = update.getMessage().getChatId();
        String text = update.getMessage().getText();
        Status status = new Status();
        status.setIdCourse(storageCourses.getIdByName(text, id));
        storageContext.set(id, status);
        ViewCourse view = new ViewCourse();
        contextAnswer.setAnswer(view.make(storageCourses.get(status.getIdCourse()), db));
        contextAnswer.setList(Arrays.asList("Задания", "Ссылки", "Отписаться"));
        return contextAnswer;
    }

    public ContextAnswer viewCourse(Update update) {
        Long id = update.getMessage().getChatId();
        contextAnswer.setAnswer("Ваши курсы");
        contextAnswer.setList(getCourse(id));
        return contextAnswer;
    }

    /**...............................................................................................................*/

    public ContextAnswer task(Update update) {
        Long id = update.getMessage().getChatId();
        String text = update.getMessage().getText();
        Status status = storageContext.get(id);
        status.setIdTask(storageTasks.getIdByName(text, status.getIdCourse()));
        storageContext.set(id, status);
        ViewTask view = new ViewTask();
        contextAnswer.setAnswer(view.make(storageTasks.get(status.getIdTask()), db));
        contextAnswer.setList(Arrays.asList("Отметить как сделанное", "Добавить комментарий", "История комментариев"));
        return contextAnswer;
    }

    public ContextAnswer markTask(Update update) {
        Long id = update.getMessage().getChatId();
        Student student = storageStudent.get(id);
        Progress progress = student.getProgresses(storageContext.get(id).getIdCourse(), storageContext.get(id).getIdTask());
        progress.setMark(true);
        student.addProgress(progress);
        storageStudent.set(id, student);
        contextAnswer.setAnswer("Отмеченно!");
        contextAnswer.setList(null);
        return contextAnswer;
    }

    public ContextAnswer commentTask(Update update) {
        Long id = update.getMessage().getChatId();
        Student student = storageStudent.get(id);
        student.setStat("КОММЕНТ");
        storageStudent.set(id, student);
        contextAnswer.setAnswer("Введите ваш комментарий");
        contextAnswer.setList(null);
        return contextAnswer;
    }

    public ContextAnswer addCommentTask(Update update) {
            Long id = update.getMessage().getChatId();
            Student student = storageStudent.get(id);
            if (student.getStat().equals("КОММЕНТ")) {
                Progress progress = student.getProgresses(storageContext.get(id).getIdCourse(), storageContext.get(id).getIdTask());
                progress.addComment(update.getMessage().getText());
                student.addProgress(progress);
                student.setStat("");
                storageStudent.set(id, student);
                contextAnswer.setAnswer("Комментарий сохранен!");
                contextAnswer.setList(null);
            } else {
                contextAnswer.setAnswer("");
                contextAnswer.setList(null);
            }
            return contextAnswer;
    }
}