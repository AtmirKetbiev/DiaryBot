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
        }, update -> userStatus.isStudent(update.getMessage().getChatId()) &&
                userStatus.getUserStatus(update.getMessage().getChatId()).equals(Constants.STUDENT));
    }

    public Reply back() {
        return Reply.of(update -> {
            ContextAnswer contextAnswer = studentManager.back(update);
            silent.execute(Keyboard.listKeyboard(contextAnswer.getButtonsList(), update, contextAnswer.getAnswer()));
        }, update -> userStatus.isStudent(update.getMessage().getChatId()) &&
                update.getMessage().getText().equals("Назад"));
    }

    public Reply timetable() {
        return Reply.of(update -> {
            silent.send(studentManager.timetable(update).getAnswer(), update.getMessage().getChatId());
        }, update -> userStatus.isStudent(update.getMessage().getChatId()) &&
                update.getMessage().getText().equals("Расписание"));
    }

    /**...............................................................................................................*/

    public Reply course() {
        return Reply.of(update -> {
            ContextAnswer contextAnswer = studentManager.course(update);
            silent.execute(Keyboard.listKeyboard(contextAnswer.getButtonsList(), update, contextAnswer.getAnswer()));
        }, update -> userStatus.isStudent(update.getMessage().getChatId()) &&
                studentManager.getCourse(update.getMessage().getChatId()).contains(update.getMessage().getText()));
    }

    public Reply viewCourse() {
        return Reply.of(update -> {
            ContextAnswer contextAnswer = studentManager.viewCourse(update);
            silent.execute(Keyboard.listKeyboard(contextAnswer.getButtonsList(), update, contextAnswer.getAnswer()));
        }, update -> userStatus.isStudent(update.getMessage().getChatId()) &&
                update.getMessage().getText().equals("Курсы"));
    }

    public Reply addCourse() {
        return Reply.of(update -> {
            ContextAnswer contextAnswer = studentManager.addCourse(update);
            silent.send(contextAnswer.getAnswer(), update.getMessage().getChatId());
            }, update -> userStatus.isStudent(update.getMessage().getChatId()) &&
                update.getMessage().getText().startsWith("/add"));
    }

    public Reply delCourse() {
        return Reply.of(update -> {
            ContextAnswer contextAnswer = studentManager.delCourse(update);
            silent.execute(Keyboard.listKeyboard(contextAnswer.getButtonsList(), update, contextAnswer.getAnswer()));
        }, update -> userStatus.isStudent(update.getMessage().getChatId()) &&
                update.getMessage().getText().equals("Отписаться"));
    }

    /**...............................................................................................................*/

    public Reply task() {
        return Reply.of(update -> {
            if (studentManager.getTask(update.getMessage().getChatId()).contains(update.getMessage().getText())) {
                ContextAnswer contextAnswer = studentManager.task(update);
                silent.execute(Keyboard.listKeyboard(contextAnswer.getButtonsList(), update, contextAnswer.getAnswer()));
                silent.send("Чтобы написать комментарий к заданию напишите текст в форме:" +
                        "/com Ваш текст", update.getMessage().getChatId());
            }
        }, update -> userStatus.isStudent(update.getMessage().getChatId()) &&
                studentManager.getTask(update.getMessage().getChatId()).contains(update.getMessage().getText()));
    }

    public Reply viewTask() {
        return Reply.of(update -> {
            ContextAnswer contextAnswer = studentManager.viewTask(update);
            silent.execute(Keyboard.listKeyboard(contextAnswer.getButtonsList(), update, contextAnswer.getAnswer()));
        }, update -> userStatus.isStudent(update.getMessage().getChatId()) &&
                update.getMessage().getText().equals("Задания"));
    }

    public Reply markTask() {
        return Reply.of(update -> {
            silent.send(studentManager.markTask(update).getAnswer(), update.getMessage().getChatId());
        }, update -> userStatus.isStudent(update.getMessage().getChatId()) &&
                update.getMessage().getText().equals("Отметить как сделанное"));
    }

    public Reply commentTask() {
        return Reply.of(update -> {
            silent.send(studentManager.commentTask(update).getAnswer(), update.getMessage().getChatId());
        }, update -> userStatus.isStudent(update.getMessage().getChatId()) &&
                update.getMessage().getText().startsWith("/com"));
    }

    public Reply viewComment() {
        return Reply.of(update -> {
            silent.send(studentManager.viewComment(update).getAnswer(), update.getMessage().getChatId());
        }, update -> userStatus.isStudent(update.getMessage().getChatId()) &&
                update.getMessage().getText().startsWith("История комментариев"));
    }

}
