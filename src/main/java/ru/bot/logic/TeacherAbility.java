package ru.bot.logic;

import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.abilitybots.api.objects.Flag;
import org.telegram.abilitybots.api.objects.Reply;
import org.telegram.abilitybots.api.sender.SilentSender;
import ru.bot.DB.StorageCreate;
import ru.bot.extension.Keyboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TeacherAbility implements org.telegram.abilitybots.api.util.AbilityExtension {
    private TeacherManager teacherManager;
    private SilentSender silent;
    private DBContext db;

    public TeacherAbility(SilentSender silent, DBContext db) {
        this.silent = silent;
        this.db = db;
        this.teacherManager = new TeacherManager(db);
    }

    public Reply start() {
        return Reply.of(update -> {
            silent.execute(Keyboard.addKeyboard(teacherManager.start(update).getList(), update, teacherManager.start(update).getAnswer()));
        }, update -> update.getMessage().getText().equals("/start"));
    }

    public Reply back() {
        return Reply.of(update -> {
            silent.execute(Keyboard.addKeyboard(teacherManager.back(update).getList(), update, teacherManager.back(update).getAnswer()));
        }, update -> update.getMessage().getText().equals("Назад"));
    }

    public Reply course() {
        return Reply.of(update -> {
            StorageCreate storageCreate = new StorageCreate(db);
            if (!teacherManager.getCourse(update.getMessage().getChatId()).contains(update.getMessage().getText())) {
                silent.execute(Keyboard.addKeyboard(teacherManager.course(update).getList(), update, teacherManager.course(update).getAnswer()));
            }
        }, update -> teacherManager.getCourse(update.getMessage().getChatId()).contains(update.getMessage().getText()));
    }

    public Reply addCourse() {
        return Reply.of(update -> {
            silent.send(teacherManager.addCourse(update).getAnswer(), update.getMessage().getChatId());
        }, update -> (update.getMessage().getText().equals("Добавить курс")
                || update.getMessage().getText().equals("Изменить курс")));
    }

    public Reply addNextCourse() {
        StorageCreate storageCreate = new StorageCreate(db);
        return Reply.of(update -> {
            silent.send(teacherManager.addNextCourse(update).getAnswer(), update.getMessage().getChatId());
        }, update -> storageCreate.getCreateCourse().get(update.getMessage().getChatId()) != null
                && !update.getMessage().getText().equals("Добавить курс")
                && !update.getMessage().getText().equals("Изменить курс"));
    }

    public Reply viewCourse() {
        return Reply.of(update -> {
            silent.execute(Keyboard.addKeyboard(teacherManager.viewCourse(update).getList(), update, teacherManager.viewCourse(update).getAnswer()));
        }, update -> update.getMessage().getText().equals("Посмотреть курсы"));
    }

    public Reply delCourse() {
        return Reply.of(update -> {
            silent.execute(Keyboard.addKeyboard(teacherManager.delCourse(update).getList(), update, teacherManager.delCourse(update).getAnswer()));
        }, update -> update.getMessage().getText().equals("Удалить курс"));
    }

    /**.......................................................................*/
    public Reply task() {
        return Reply.of(update -> {
            silent.execute(Keyboard.addKeyboard(teacherManager.task(update).getList(), update, teacherManager.task(update).getAnswer()));
        }, update -> teacherManager.getTask(update.getMessage().getChatId()).contains(update.getMessage().getText()));
    }

    public Reply addTask() {
        return Reply.of(update -> {
            silent.send(teacherManager.addTask(update).getAnswer(), update.getMessage().getChatId());
        }, update -> (update.getMessage().getText().equals("Добавить задание")
                || update.getMessage().getText().equals("Изменить задание")));
    }

    public Reply addNextTask() {
        StorageCreate storageCreate = new StorageCreate(db);
        return Reply.of(update -> {
            silent.send(teacherManager.addNextTask(update).getAnswer(), update.getMessage().getChatId());
        }, update ->
                storageCreate.getCreateTask().get(update.getMessage().getChatId()) != null
                        && !update.getMessage().getText().equals("Добавить задание")
                        && !update.getMessage().getText().equals("Изменить задание"));
    }

    public Reply viewTask() {
        return Reply.of(update -> {
            silent.execute(Keyboard.addKeyboard(teacherManager.viewTask(update).getList(), update, teacherManager.viewTask(update).getAnswer()));
        }, update -> update.getMessage().getText().equals("Посмотреть задания"));
    }

    public Reply delTask() {
        return Reply.of(update -> {
            silent.execute(Keyboard.addKeyboard(teacherManager.delTask(update).getList(), update, teacherManager.delTask(update).getAnswer()));
        }, update -> update.getMessage().getText().equals("Удалить задание"));
    }

    public Reply del() {
        return Reply.of(update -> {
            silent.send(teacherManager.del(update), update.getMessage().getChatId());
            //start();
        }, update -> update.getMessage().getText().equals("/del"));
    }
}