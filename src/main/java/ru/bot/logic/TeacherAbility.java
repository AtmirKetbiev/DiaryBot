package ru.bot.logic;

import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.abilitybots.api.objects.Reply;
import org.telegram.abilitybots.api.sender.SilentSender;
import ru.bot.DB.StorageCreate;
import ru.bot.extension.Keyboard;

import java.util.Arrays;

public class TeacherAbility implements org.telegram.abilitybots.api.util.AbilityExtension {
    private SilentSender silent;
    private DBContext db;

    public TeacherAbility(SilentSender silent, DBContext db) {
        this.silent = silent;
        this.db = db;
    }

    public Reply start() {
        return Reply.of(update -> {
            TeacherManager teacherManager = new TeacherManager(db);
            String answer = teacherManager.start(update);
            silent.execute(Keyboard.addKeyboard(new String[]{"Добавить курс", "Посмотреть курсы", "Изменить профиль", "Помощь"}, update, answer));
        }, update -> update.getMessage().getText().equals("/start"));
    }

    public Reply back() {
        return Reply.of(update -> {
            TeacherManager teacherManager = new TeacherManager(db);
            silent.execute(Keyboard.addKeyboard(teacherManager.back(update), update, "Назад"));
        }, update -> update.getMessage().getText().equals("Назад"));
    }

    public Reply course() {
        TeacherManager teacherManager = new TeacherManager(db);
        return Reply.of(update -> {
            String answer = teacherManager.course(update);
            silent.execute(Keyboard.addKeyboard(new String[]{"Добавить задание", "Посмотреть задания", "Группы", "Ссылки", "Изменить курс", "Удалить курс"}, update, answer));
        }, update -> Arrays.stream(teacherManager.getCourse(update.getMessage().getChatId())).anyMatch(update.getMessage().getText()::equals));
    }

    public Reply addCourse() {
        return Reply.of(update -> {
            TeacherManager teacherManager = new TeacherManager(db);
            String answer = teacherManager.addCourse(update);
            silent.send("Введите название", update.getMessage().getChatId());
        }, update -> (update.getMessage().getText().equals("Добавить курс")
                || update.getMessage().getText().equals("Изменить курс")));
    }

    public Reply addNextCourse() {
        StorageCreate storageCreate = new StorageCreate(db);
        return Reply.of(update -> {
            TeacherManager teacherManager = new TeacherManager(db);
            silent.send(teacherManager.addNextCourse(update), update.getMessage().getChatId());
        }, update -> storageCreate.getCreateCourse().get(update.getMessage().getChatId()) != null
                && !update.getMessage().getText().equals("Добавить курс")
                && !update.getMessage().getText().equals("Изменить курс"));
    }

    public Reply viewCourse() {
        return Reply.of(update -> {
            TeacherManager teacherManager = new TeacherManager(db);
            String[] myArray = teacherManager.viewCourse(update);
            silent.execute(Keyboard.addKeyboard(myArray, update, "Ваши курсы:"));
        }, update -> update.getMessage().getText().equals("Посмотреть курсы"));
    }

    public Reply delCourse() {
        return Reply.of(update -> {
            TeacherManager teacherManager = new TeacherManager(db);
            String[] myArray = teacherManager.delCourse(update);
            silent.execute(Keyboard.addKeyboard(myArray, update, "Курс удален\nВаши курсы:"));
        }, update -> update.getMessage().getText().equals("Удалить курс"));
    }

    /**.......................................................................*/
    public Reply task() {
        TeacherManager teacherManager = new TeacherManager(db);
        return Reply.of(update -> {
            String answer = teacherManager.task(update);
            silent.execute(Keyboard.addKeyboard(new String[]{"Изменить задание", "Удалить задание"}, update, answer));
        }, update -> Arrays.asList(teacherManager.getTask(update.getMessage().getChatId())).contains(update.getMessage().getText()));
    }

    public Reply addTask() {
        return Reply.of(update -> {
            TeacherManager teacherManager = new TeacherManager(db);
            String answer = teacherManager.addTask(update);
            silent.send(answer, update.getMessage().getChatId());
        }, update -> (update.getMessage().getText().equals("Добавить задание")
                || update.getMessage().getText().equals("Изменить задание")));
    }

    public Reply addNextTask() {
        StorageCreate storageCreate = new StorageCreate(db);
        return Reply.of(update -> {
            TeacherManager teacherManager = new TeacherManager(db);
            silent.send(teacherManager.addNextTask(update), update.getMessage().getChatId());
        }, update ->
                storageCreate.getCreateTask().get(update.getMessage().getChatId()) != null
                        && !update.getMessage().getText().equals("Добавить задание")
                        && !update.getMessage().getText().equals("Изменить задание"));
    }

    public Reply viewTask() {
        return Reply.of(update -> {
            TeacherManager teacherManager = new TeacherManager(db);
            String[] myArray = teacherManager.viewTask(update);
            silent.execute(Keyboard.addKeyboard(myArray, update, "Ваши курсы:"));
        }, update -> update.getMessage().getText().equals("Посмотреть задания"));
    }

    public Reply delTask() {
        return Reply.of(update -> {
            TeacherManager teacherManager = new TeacherManager(db);
            String[] myArray = teacherManager.delTask(update);
            silent.execute(Keyboard.addKeyboard(myArray, update, "Курс удален\nВаши курсы:"));
        }, update -> update.getMessage().getText().equals("Удалить задание"));
    }
}
