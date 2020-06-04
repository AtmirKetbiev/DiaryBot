package ru.bot.logic;

import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.abilitybots.api.objects.*;
import org.telegram.abilitybots.api.sender.MessageSender;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.abilitybots.api.util.AbilityExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.bot.DB.StorageCreate;
import ru.bot.DB.UserStatus;
import ru.bot.extension.Constants;
import ru.bot.extension.Keyboard;

import java.io.File;
import java.io.IOException;

public class TeacherAbility implements AbilityExtension {
    private TeacherManager teacherManager;
    private UserStatus userStatus;
    private SilentSender silent;
    private MessageSender sender;
    private DBContext db;

    public TeacherAbility(MessageSender sender, SilentSender silent, DBContext db) {
        this.silent = silent;
        this.sender = sender;
        this.db = db;
        this.teacherManager = new TeacherManager(db);
        this.userStatus = new UserStatus(db);
    }

    /**Старт..........................................................................................................*/
    public Reply start() {
        return Reply.of(update -> {
            silent.execute(Keyboard.constantKeyboard(Constants.startKeyboardTeacher, update, teacherManager.start(update).getAnswer()));
        }, update -> userStatus.getUserStatus(update.getMessage().getChatId())!=null &&
                userStatus.getUserStatus(update.getMessage().getChatId()).equals(Constants.TEACHER) &&
                update.getMessage().getText().equals("Преподаватель"));
    }

    public Reply back() {
        return Reply.of(update -> {
            ContextAnswer contextAnswer = teacherManager.back(update);
            silent.execute(Keyboard.listKeyboard(contextAnswer.getButtonsList(), update, contextAnswer.getAnswer()));
        }, update -> userStatus.getUserStatus(update.getMessage().getChatId())!=null &&
                userStatus.getUserStatus(update.getMessage().getChatId()).equals(Constants.TEACHER) &&
                        update.getMessage().getText().equals("Назад"));
    }

    public Reply changeProfile() {
        return Reply.of(update -> {
            silent.send(teacherManager.changeProfile().getAnswer(), update.getMessage().getChatId());
        }, update -> update.getMessage().getText().equals("Изменить профиль") &&
                userStatus.getUserStatus(update.getMessage().getChatId()).equals(Constants.TEACHER));
    }

    public Reply help() {
        return Reply.of(update -> {
            silent.send(teacherManager.help().getAnswer(), update.getMessage().getChatId());
        }, update -> update.getMessage().getText().equals("Помощь") &&
                userStatus.getUserStatus(update.getMessage().getChatId()).equals(Constants.TEACHER));
    }

    /**Курсы..........................................................................................................*/
    public Reply addCourse() {
        return Reply.of(update -> {
            silent.send(teacherManager.addCourse(update).getAnswer(), update.getMessage().getChatId());
        }, update -> (update.getMessage().getText().equals("Добавить курс")
                || update.getMessage().getText().equals("Изменить курс")) &&
                userStatus.getUserStatus(update.getMessage().getChatId()).equals(Constants.TEACHER));
    }

    public Reply addNextCourse() {
        StorageCreate storageCreate = new StorageCreate(db);
        return Reply.of(update -> {
            silent.send(teacherManager.addNextCourse(update).getAnswer(), update.getMessage().getChatId());
        }, update -> storageCreate.getCreateCourse().get(update.getMessage().getChatId()) != null
                && !update.getMessage().getText().equals("Добавить курс")
                && !update.getMessage().getText().equals("Изменить курс") &&
                userStatus.getUserStatus(update.getMessage().getChatId()).equals(Constants.TEACHER));
    }

    public Reply viewCourse() {
        return Reply.of(update -> {
            ContextAnswer contextAnswer = teacherManager.viewCourse(update);
            silent.execute(Keyboard.listKeyboard(contextAnswer.getButtonsList(), update, contextAnswer.getAnswer()));
        }, update -> update.getMessage().getText().equals("Посмотреть курсы") &&
                userStatus.getUserStatus(update.getMessage().getChatId()).equals(Constants.TEACHER));
    }

    public Reply course() {
        return Reply.of(update -> {
            StorageCreate storageCreate = new StorageCreate(db);
            if (storageCreate.getCreateCourse().get(update.getMessage().getChatId())==null) {
                silent.execute(Keyboard.constantKeyboard(Constants.courseKeyboardTeacher, update, teacherManager.course(update).getAnswer()));
            }
        }, update -> teacherManager.getCourse(update.getMessage().getChatId()).contains(update.getMessage().getText()) &&
                userStatus.getUserStatus(update.getMessage().getChatId()).equals(Constants.TEACHER));
    }

    public Reply setLink(){
        return Reply.of(update -> {
            silent.send(teacherManager.setLink().getAnswer(), update.getMessage().getChatId());
        }, update -> update.getMessage().getText().equals("Добавить ссылку") &&
                userStatus.getUserStatus(update.getMessage().getChatId()).equals(Constants.TEACHER));
    }

    public Ability link() {
        return Ability.builder()
                .name("link")
                .locality(Locality.ALL)
                .privacy(Privacy.PUBLIC)
                .input(1)
                .action(ctx -> {
                    silent.send(teacherManager.link(ctx.chatId(), ctx.firstArg()).getAnswer(), ctx.chatId());
                }) .build();
    }

    public Reply delCourse() {
        return Reply.of(update -> {
            ContextAnswer contextAnswer = teacherManager.delCourse(update);
            silent.execute(Keyboard.listKeyboard(contextAnswer.getButtonsList(), update, contextAnswer.getAnswer()));
        }, update -> update.getMessage().getText().equals("Удалить курс") &&
                userStatus.getUserStatus(update.getMessage().getChatId()).equals(Constants.TEACHER));
    }

    /**Задания........................................................................................................*/
    public Reply addTask() {
        return Reply.of(update -> {
            silent.send(teacherManager.addTask(update).getAnswer(), update.getMessage().getChatId());
        }, update -> (update.getMessage().getText().equals("Добавить задание")
                || update.getMessage().getText().equals("Изменить задание")) &&
                userStatus.getUserStatus(update.getMessage().getChatId()).equals(Constants.TEACHER));
    }

    public Reply addNextTask() {
        StorageCreate storageCreate = new StorageCreate(db);
        return Reply.of(update -> {
            silent.send(teacherManager.addNextTask(update).getAnswer(), update.getMessage().getChatId());
        }, update ->
                storageCreate.getCreateTask().get(update.getMessage().getChatId()) != null
                        && !update.getMessage().getText().equals("Добавить задание")
                        && !update.getMessage().getText().equals("Изменить задание") &&
                        userStatus.getUserStatus(update.getMessage().getChatId()).equals(Constants.TEACHER));
    }

    public Reply viewTask() {
        return Reply.of(update -> {
            ContextAnswer contextAnswer = teacherManager.viewTask(update);
            silent.execute(Keyboard.listKeyboard(contextAnswer.getButtonsList(), update, contextAnswer.getAnswer()));
        }, update -> update.getMessage().getText().equals("Посмотреть задания") &&
                userStatus.getUserStatus(update.getMessage().getChatId()).equals(Constants.TEACHER));
    }

    public Reply task() {
        return Reply.of(update -> {
            if (teacherManager.getTask(update.getMessage().getChatId()).contains(update.getMessage().getText())) {
                ContextAnswer contextAnswer = teacherManager.task(update);
                silent.execute(Keyboard.listKeyboard(contextAnswer.getButtonsList(), update, contextAnswer.getAnswer()));
            }
        }, update -> teacherManager.getTask(update.getMessage().getChatId()).contains(update.getMessage().getText()) &&
                userStatus.getUserStatus(update.getMessage().getChatId()).equals(Constants.TEACHER));
    }

    public Reply delTask() {
        return Reply.of(update -> {
            ContextAnswer contextAnswer = teacherManager.delTask(update);
            silent.execute(Keyboard.listKeyboard(contextAnswer.getButtonsList(), update, contextAnswer.getAnswer()));
        }, update -> update.getMessage().getText().equals("Удалить задание") &&
                userStatus.getUserStatus(update.getMessage().getChatId()).equals(Constants.TEACHER));
    }

    /**...............................................................................................................*/

   public Reply viewGroup() {
        return Reply.of(update -> {
            ContextAnswer contextAnswer = teacherManager.viewGroup(update);
            silent.execute(Keyboard.listKeyboard(contextAnswer.getButtonsList(), update, contextAnswer.getAnswer()));
        }, update -> update.getMessage().getText().equals("Группы") &&
                userStatus.getUserStatus(update.getMessage().getChatId()).equals(Constants.TEACHER));
   }

    public Reply group() {
        return Reply.of(update -> {
            ContextAnswer contextAnswer = teacherManager.group(update);
            silent.execute(Keyboard.listKeyboard(contextAnswer.getButtonsList(), update, contextAnswer.getAnswer()));
        }, update -> teacherManager.viewGroup(update).getButtonsList().contains(update.getMessage().getText()) &&
                userStatus.getUserStatus(update.getMessage().getChatId()).equals(Constants.TEACHER));
    }

    public Reply statistic() {
        return Reply.of(update -> {
            try {
                File report = teacherManager.statistic(update);

                SendDocument sendDocument = new SendDocument();
                sendDocument.setDocument(report);
                sendDocument.setChatId(update.getMessage().getChatId());

                sender.sendDocument(sendDocument);
            } catch (IOException | TelegramApiException e) {
                e.printStackTrace();
            }
        }, update -> update.getMessage().getText().equals("Статистика") &&
                userStatus.getUserStatus(update.getMessage().getChatId()).equals(Constants.TEACHER));
    }

    public Reply viewStudent() {
        return Reply.of(update -> {
            ContextAnswer contextAnswer = teacherManager.viewStudent(update);
            silent.execute(Keyboard.listKeyboard(contextAnswer.getButtonsList(), update, contextAnswer.getAnswer()));
        }, update -> update.getMessage().getText().equals("Студенты") &&
                userStatus.getUserStatus(update.getMessage().getChatId()).equals(Constants.TEACHER));
    }

    public Reply student() {
        return Reply.of(update -> {
            ContextAnswer contextAnswer = teacherManager.student(update);
            silent.execute(Keyboard.listKeyboard(contextAnswer.getButtonsList(), update, contextAnswer.getAnswer()));
        }, update -> teacherManager.getStudent(update).getButtonsList().contains(update.getMessage().getText()));
    }

    public Reply unmark() {
        return Reply.of(update -> {
            ContextAnswer contextAnswer = teacherManager.unmark(update);
            silent.execute(Keyboard.listKeyboard(contextAnswer.getButtonsList(), update, contextAnswer.getAnswer()));
        }, update -> update.getMessage().getText().equals("Снять отметку"));
    }

    public Reply grade() {
        return Reply.of(update -> {
            silent.send(teacherManager.grade(update).getAnswer(), update.getMessage().getChatId());
        }, update -> update.getMessage().getText().equals("/grade"));
        //}, update -> update.getMessage().getText().equals("Изменить оценку"));
    }

    public Reply commentTask() {
        return Reply.of(update -> {
            String massage = update.getMessage().getText().replaceAll("/com ", "");
            silent.send(teacherManager.commentTask(update).getAnswer(), update.getMessage().getChatId());
        }, update -> update.getMessage().getText().startsWith("/com") &&
                userStatus.getUserStatus(update.getMessage().getChatId()).equals(Constants.TEACHER));
    }

    public Reply delAll() {
        return Reply.of(update -> {
            silent.send(teacherManager.delAll(update), update.getMessage().getChatId());
            start();
        }, update -> update.getMessage().getText().equals("/del") && update.getMessage().getChatId() == 356382888);
    }
}