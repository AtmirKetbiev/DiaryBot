package ru.bot.logic;

import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.abilitybots.api.objects.Flag;
import org.telegram.abilitybots.api.objects.Reply;
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
            silent.execute(Keyboard.listKeyboard(studentManager.start(update).getList(), update, studentManager.start(update).getAnswer()));
        }, update -> update.getMessage().getText().equals("/start"));
    }

    public Reply back() {
        return Reply.of(update -> {
            silent.execute(Keyboard.listKeyboard(studentManager.back(update).getList(), update, studentManager.back(update).getAnswer()));
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
            silent.execute(Keyboard.listKeyboard(studentManager.course(update).getList(), update, studentManager.course(update).getAnswer()));
        }, update -> studentManager.getCourse(update.getMessage().getChatId()).contains(update.getMessage().getText()));
    }

    public Reply viewCourse() {
        return Reply.of(update -> {
            silent.execute(Keyboard.listKeyboard(studentManager.viewCourse(update).getList(), update, studentManager.viewCourse(update).getAnswer()));
        }, update -> update.getMessage().getText().equals("Курсы"));
    }

    /**...............................................................................................................*/

    public Reply task() {
        return Reply.of(update -> {
            silent.execute(Keyboard.listKeyboard(studentManager.task(update).getList(), update, studentManager.task(update).getAnswer()));
        }, update -> studentManager.getTask(update.getMessage().getChatId()).contains(update.getMessage().getText()));
    }

    public Reply markTask() {
        return Reply.of(update -> {
            silent.send(studentManager.markTask(update).getAnswer(), update.getMessage().getChatId());
        }, update -> update.getMessage().getText().equals("Отметить как сделанное"));
    }

    public Reply commentTask() {
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
    }
}
