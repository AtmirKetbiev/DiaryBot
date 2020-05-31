package ru.bot.logic;

import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.abilitybots.api.objects.*;
import org.telegram.abilitybots.api.sender.SilentSender;
import ru.bot.extension.Keyboard;

public class StudentAbility implements org.telegram.abilitybots.api.util.AbilityExtension {
    private StudentManager studentManager;
    private SilentSender silent;
    private DBContext db;

    public StudentAbility(SilentSender silent, DBContext db) {
        this.silent = silent;
        this.db = db;
        this.studentManager = new StudentManager(db);
    }

    public Reply start() {
        return Reply.of(update -> {
            silent.execute(Keyboard.listKeyboard(studentManager.start(update).getButtonsList(), update, studentManager.start(update).getAnswer()));
        }, update -> update.getMessage().getText().equals("/start"));
    }

    public Reply back() {
        return Reply.of(update -> {
            ContextAnswer contextAnswer = studentManager.back(update);
            silent.execute(Keyboard.listKeyboard(contextAnswer.getButtonsList(), update, contextAnswer.getAnswer()));
        }, update -> update.getMessage().getText().equals("Назад"));
    }

    public Reply timetable() {
        return Reply.of(update -> {
            silent.send(studentManager.timetable(update).getAnswer(), update.getMessage().getChatId());
        }, update -> update.getMessage().getText().equals("Расписание"));
    }

    /**...............................................................................................................*/

    public Reply course() {
        return Reply.of(update -> {
            silent.execute(Keyboard.listKeyboard(studentManager.course(update).getButtonsList(), update, studentManager.course(update).getAnswer()));
        }, update -> studentManager.getCourse(update.getMessage().getChatId()).contains(update.getMessage().getText()));
    }

    public Reply viewCourse() {
        return Reply.of(update -> {
            silent.execute(Keyboard.listKeyboard(studentManager.viewCourse(update).getButtonsList(), update, studentManager.viewCourse(update).getAnswer()));
        }, update -> update.getMessage().getText().equals("Курсы"));
    }

    public Reply addCourse() {
        return Reply.of(update -> {
            String massage = update.getMessage().getText().replaceAll("/add ", "");
            silent.send(studentManager.addCourse(update.getMessage().getChatId(), massage).getAnswer(), update.getMessage().getChatId());
            }, update -> update.getMessage().getText().startsWith("/add"));
    }

    public Reply delCourse() {
        return Reply.of(update -> {
            ContextAnswer contextAnswer = studentManager.delCourse(update);
            silent.execute(Keyboard.listKeyboard(contextAnswer.getButtonsList(), update, contextAnswer.getAnswer()));
        }, update -> update.getMessage().getText().equals("Отписаться"));
    }

    /**...............................................................................................................*/

    public Reply task() {
        return Reply.of(update -> {
            if (studentManager.getTask(update.getMessage().getChatId()).contains(update.getMessage().getText())) {
                silent.execute(Keyboard.listKeyboard(studentManager.task(update).getButtonsList(), update, studentManager.task(update).getAnswer()));
                silent.send("Чтобы написать комментарий к заданию напишите текст в форме:" +
                        "/com Ваш текст", update.getMessage().getChatId());
            }
        }, update -> studentManager.getTask(update.getMessage().getChatId()).contains(update.getMessage().getText()));
    }

    public Reply viewTask() {
        return Reply.of(update -> {
            silent.execute(Keyboard.listKeyboard(studentManager.viewTask(update).getButtonsList(), update, studentManager.viewTask(update).getAnswer()));
        }, update -> update.getMessage().getText().equals("Задания"));
    }

    public Reply markTask() {
        return Reply.of(update -> {
            silent.send(studentManager.markTask(update).getAnswer(), update.getMessage().getChatId());
        }, update -> update.getMessage().getText().equals("Отметить как сделанное"));
    }

    public Reply commentTask() {
        return Reply.of(update -> {
            String massage = update.getMessage().getText().replaceAll("/com ", "");
            silent.send(studentManager.commentTask(update).getAnswer(), update.getMessage().getChatId());
        }, update -> update.getMessage().getText().startsWith("/com"));
    }

    public Reply viewComment() {
        return Reply.of(update -> {
            silent.send(studentManager.viewComment(update).getAnswer(), update.getMessage().getChatId());
        }, update -> update.getMessage().getText().startsWith("История комментариев"));
    }

    /*public Reply commentTask2() {
        return Reply.of(update -> {
            silent.send(studentManager.commentTask(update).getAnswer(), update.getMessage().getChatId());
        }, update -> update.getMessage().getText().equals("Добавить комментарий"));
    }

    public Reply addCommentTask() {
        return Reply.of(update -> {
            if (!studentManager.addCommentTask(update).getAnswer().equals("")) {
                silent.send(studentManager.addCommentTask(update).getAnswer(), update.getMessage().getChatId());
            }
        }, Flag.TEXT);
    }*/
}
