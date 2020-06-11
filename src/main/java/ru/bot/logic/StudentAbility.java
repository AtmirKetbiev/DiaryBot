package ru.bot.logic;

import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.abilitybots.api.objects.*;
import org.telegram.abilitybots.api.sender.SilentSender;
import ru.bot.db.*;
import ru.bot.extension.Constants;
import ru.bot.extension.Keyboard;

public class StudentAbility implements org.telegram.abilitybots.api.util.AbilityExtension {

    private StudentManager studentManager;
    private UserStatus userStatus;
    private SilentSender silent;
    private Long id;
    private String text;

    private ContextAnswer contextAnswer = new ContextAnswer();

    public StudentAbility(SilentSender silent,
                          StorageStudent storageStudent,
                          StorageCourses storageCourses,
                          StorageTasks storageTasks,
                          StorageProgress storageProgress,
                          StorageContext storageContext,
                          UserStatus userStatus) {
        this.silent = silent;
        this.userStatus = userStatus;
        this.studentManager = new StudentManager(storageStudent, storageCourses, storageTasks, storageProgress, storageContext);
    }

    public Reply start() {
        return Reply.of(update -> {
            id = update.getMessage().getChatId();
            String name =  update.getMessage().getChat().getFirstName();

            contextAnswer = studentManager.start(id, name);
            silent.execute(Keyboard.listKeyboard(contextAnswer.getButtonsList(), update, contextAnswer.getAnswer()));
        }, update -> userStatus.isStudent(update.getMessage().getChatId()) &&
                update.getMessage().getText().equals(Constants.STUDENT));
    }

    public Reply back() {
        return Reply.of(update -> {
            id = update.getMessage().getChatId();
            text = update.getMessage().getText();

            contextAnswer = studentManager.back(id);
            silent.execute(Keyboard.listKeyboard(contextAnswer.getButtonsList(), update, contextAnswer.getAnswer()));
        }, update -> userStatus.isStudent(update.getMessage().getChatId()) &&
                update.getMessage().getText().equals("Назад"));
    }

    public Reply timetable() {
        return Reply.of(update -> {
            id = update.getMessage().getChatId();
            text = update.getMessage().getText();

            silent.send(studentManager.timetable().getAnswer(), update.getMessage().getChatId());
        }, update -> userStatus.isStudent(update.getMessage().getChatId()) &&
                update.getMessage().getText().equals("Расписание"));
    }

    /**...............................................................................................................*/

    public Reply course() {
        return Reply.of(update -> {
            id = update.getMessage().getChatId();
            text = update.getMessage().getText();

            contextAnswer = studentManager.course(id, text);
            silent.execute(Keyboard.listKeyboard(contextAnswer.getButtonsList(), update, contextAnswer.getAnswer()));
        }, update -> userStatus.isStudent(update.getMessage().getChatId()) &&
                studentManager.getCourse(update.getMessage().getChatId()).contains(update.getMessage().getText()));
    }

    public Reply viewCourse() {
        return Reply.of(update -> {
            id = update.getMessage().getChatId();
            text = update.getMessage().getText();

            contextAnswer = studentManager.viewCourse(id);
            silent.execute(Keyboard.listKeyboard(contextAnswer.getButtonsList(), update, contextAnswer.getAnswer()));
        }, update -> userStatus.isStudent(update.getMessage().getChatId()) &&
                update.getMessage().getText().equals("Курсы"));
    }

    public Reply addCourse() {
        return Reply.of(update -> {
            id = update.getMessage().getChatId();
            text = update.getMessage().getText();

            contextAnswer = studentManager.addCourse(id, text);
            silent.send(contextAnswer.getAnswer(), update.getMessage().getChatId());
            }, update -> userStatus.isStudent(update.getMessage().getChatId()) &&
                update.getMessage().getText().startsWith("/add"));
    }

    public Reply delCourse() {
        return Reply.of(update -> {
            id = update.getMessage().getChatId();
            text = update.getMessage().getText();

            contextAnswer = studentManager.delCourse(id);
            silent.execute(Keyboard.listKeyboard(contextAnswer.getButtonsList(), update, contextAnswer.getAnswer()));
        }, update -> userStatus.isStudent(update.getMessage().getChatId()) &&
                update.getMessage().getText().equals("Отписаться"));
    }

    /**...............................................................................................................*/

    public Reply task() {
        return Reply.of(update -> {
            id = update.getMessage().getChatId();
            text = update.getMessage().getText();

            if (studentManager.getTask(update.getMessage().getChatId()).contains(update.getMessage().getText())) {
                contextAnswer = studentManager.task(id, text);
                silent.execute(Keyboard.listKeyboard(contextAnswer.getButtonsList(), update, contextAnswer.getAnswer()));
                silent.send("Чтобы написать комментарий к заданию напишите текст в форме:" +
                        "/com Ваш текст", update.getMessage().getChatId());
            }
        }, update -> userStatus.isStudent(update.getMessage().getChatId()) &&
                studentManager.getTask(update.getMessage().getChatId()).contains(update.getMessage().getText()));
    }

    public Reply viewTask() {
        return Reply.of(update -> {
            id = update.getMessage().getChatId();
            text = update.getMessage().getText();

            contextAnswer = studentManager.viewTask(id);
            silent.execute(Keyboard.listKeyboard(contextAnswer.getButtonsList(), update, contextAnswer.getAnswer()));
        }, update -> userStatus.isStudent(update.getMessage().getChatId()) &&
                update.getMessage().getText().equals("Задания"));
    }

    public Reply markTask() {
        return Reply.of(update -> {
            id = update.getMessage().getChatId();
            text = update.getMessage().getText();

            silent.send(studentManager.markTask(id).getAnswer(), update.getMessage().getChatId());
        }, update -> userStatus.isStudent(update.getMessage().getChatId()) &&
                update.getMessage().getText().equals("Отметить как сделанное"));
    }

    public Reply commentTask() {
        return Reply.of(update -> {
            id = update.getMessage().getChatId();
            text = update.getMessage().getText();

            silent.send(studentManager.commentTask(id, text).getAnswer(), update.getMessage().getChatId());
        }, update -> userStatus.isStudent(update.getMessage().getChatId()) &&
                update.getMessage().getText().startsWith("/com"));
    }

    public Reply viewComment() {
        return Reply.of(update -> {
            id = update.getMessage().getChatId();
            text = update.getMessage().getText();

            silent.send(studentManager.viewComment(id).getAnswer(), update.getMessage().getChatId());
        }, update -> userStatus.isStudent(update.getMessage().getChatId()) &&
                update.getMessage().getText().startsWith("История комментариев"));
    }

}
