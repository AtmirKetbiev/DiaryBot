package ru.bot.logic;

import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.abilitybots.api.objects.*;
import org.telegram.abilitybots.api.sender.SilentSender;
import ru.bot.DB.UserStatus;
import ru.bot.extension.Constants;
import ru.bot.extension.Keyboard;

public class StudentAbility implements org.telegram.abilitybots.api.util.AbilityExtension {
    private StudentManager studentManager;
    private UserStatus userStatus;
    private SilentSender silent;

    public StudentAbility(SilentSender silent, DBContext db) {
        this.silent = silent;
        this.studentManager = new StudentManager(db);
        this.userStatus = new UserStatus(db);
    }

    public Reply start() {
        return Reply.of(update -> {
            ContextAnswer contextAnswer = studentManager.start(update);
            silent.execute(Keyboard.listKeyboard(contextAnswer.getButtonsList(), update, contextAnswer.getAnswer()));
        }, update -> userStatus.getUserStatus(update.getMessage().getChatId())!=null &&
                update.getMessage().getText().equals("Студент") &&
                userStatus.getUserStatus(update.getMessage().getChatId()).equals(Constants.STUDENT));
    }

    public Reply back() {
        return Reply.of(update -> {
            ContextAnswer contextAnswer = studentManager.back(update);
            silent.execute(Keyboard.listKeyboard(contextAnswer.getButtonsList(), update, contextAnswer.getAnswer()));
        }, update -> update.getMessage().getText().equals("Назад") &&
                userStatus.getUserStatus(update.getMessage().getChatId()).equals(Constants.STUDENT));
    }

    public Reply timetable() {
        return Reply.of(update -> {
            silent.send(studentManager.timetable(update).getAnswer(), update.getMessage().getChatId());
        }, update -> update.getMessage().getText().equals("Расписание") &&
                userStatus.getUserStatus(update.getMessage().getChatId()).equals(Constants.STUDENT));
    }

    /**...............................................................................................................*/

    public Reply course() {
        return Reply.of(update -> {
            silent.execute(Keyboard.listKeyboard(studentManager.course(update).getButtonsList(), update, studentManager.course(update).getAnswer()));
        }, update -> studentManager.getCourse(update.getMessage().getChatId()).contains(update.getMessage().getText()) &&
                userStatus.getUserStatus(update.getMessage().getChatId()).equals(Constants.STUDENT));
    }

    public Reply viewCourse() {
        return Reply.of(update -> {
            silent.execute(Keyboard.listKeyboard(studentManager.viewCourse(update).getButtonsList(), update, studentManager.viewCourse(update).getAnswer()));
        }, update -> update.getMessage().getText().equals("Курсы") &&
                userStatus.getUserStatus(update.getMessage().getChatId()).equals(Constants.STUDENT));
    }

    public Reply addCourse() {
        return Reply.of(update -> {
            String massage = update.getMessage().getText().replaceAll("/add ", "");
            silent.send(studentManager.addCourse(update.getMessage().getChatId(), massage).getAnswer(), update.getMessage().getChatId());
            }, update -> update.getMessage().getText().startsWith("/add") &&
                userStatus.getUserStatus(update.getMessage().getChatId()).equals(Constants.STUDENT));
    }

    public Reply delCourse() {
        return Reply.of(update -> {
            ContextAnswer contextAnswer = studentManager.delCourse(update);
            silent.execute(Keyboard.listKeyboard(contextAnswer.getButtonsList(), update, contextAnswer.getAnswer()));
        }, update -> update.getMessage().getText().equals("Отписаться") &&
                userStatus.getUserStatus(update.getMessage().getChatId()).equals(Constants.STUDENT));
    }

    /**...............................................................................................................*/

    public Reply task() {
        return Reply.of(update -> {
            if (studentManager.getTask(update.getMessage().getChatId()).contains(update.getMessage().getText())) {
                silent.execute(Keyboard.listKeyboard(studentManager.task(update).getButtonsList(), update, studentManager.task(update).getAnswer()));
                silent.send("Чтобы написать комментарий к заданию напишите текст в форме:" +
                        "/com Ваш текст", update.getMessage().getChatId());
            }
        }, update -> userStatus.getUserStatus(update.getMessage().getChatId())!=null &&
                userStatus.getUserStatus(update.getMessage().getChatId()).equals(Constants.STUDENT) &&
                studentManager.getTask(update.getMessage().getChatId()).contains(update.getMessage().getText()));
    }

    public Reply viewTask() {
        return Reply.of(update -> {
            silent.execute(Keyboard.listKeyboard(studentManager.viewTask(update).getButtonsList(), update, studentManager.viewTask(update).getAnswer()));
        }, update -> update.getMessage().getText().equals("Задания") &&
                userStatus.getUserStatus(update.getMessage().getChatId()).equals(Constants.STUDENT));
    }

    public Reply markTask() {
        return Reply.of(update -> {
            silent.send(studentManager.markTask(update).getAnswer(), update.getMessage().getChatId());
        }, update -> update.getMessage().getText().equals("Отметить как сделанное") &&
                userStatus.getUserStatus(update.getMessage().getChatId()).equals(Constants.STUDENT));
    }

    public Reply commentTask() {
        return Reply.of(update -> {
            String massage = update.getMessage().getText().replaceAll("/com ", "");
            silent.send(studentManager.commentTask(update).getAnswer(), update.getMessage().getChatId());
        }, update -> update.getMessage().getText().startsWith("/com") &&
                userStatus.getUserStatus(update.getMessage().getChatId()).equals(Constants.STUDENT));
    }

    public Reply viewComment() {
        return Reply.of(update -> {
            silent.send(studentManager.viewComment(update).getAnswer(), update.getMessage().getChatId());
        }, update -> update.getMessage().getText().startsWith("История комментариев") &&
                userStatus.getUserStatus(update.getMessage().getChatId()).equals(Constants.STUDENT));
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
