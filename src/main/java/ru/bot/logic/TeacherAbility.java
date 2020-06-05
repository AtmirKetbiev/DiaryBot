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

    /**�����..........................................................................................................*/
    public Reply start() {
        return Reply.of(update -> {
            silent.execute(Keyboard.constantKeyboard(Constants.startKeyboardTeacher, update, teacherManager.start(update).getAnswer()));
        }, update -> userStatus.isTeacher(update.getMessage().getChatId()) &&
                update.getMessage().getText().equals("�������������"));
    }

    public Reply back() {
        return Reply.of(update -> {
            ContextAnswer contextAnswer = teacherManager.back(update);
            silent.execute(Keyboard.listKeyboard(contextAnswer.getButtonsList(), update, contextAnswer.getAnswer()));
        }, update -> userStatus.isTeacher(update.getMessage().getChatId()) &&
                update.getMessage().getText().equals("�����"));
    }

    public Reply changeProfile() {
        return Reply.of(update -> {
            silent.send(teacherManager.changeProfile().getAnswer(), update.getMessage().getChatId());
        }, update -> userStatus.isTeacher(update.getMessage().getChatId()) &&
                update.getMessage().getText().equals("�������� �������"));
    }

    public Reply help() {
        return Reply.of(update -> {
            silent.send(teacherManager.help().getAnswer(), update.getMessage().getChatId());
        }, update -> userStatus.isTeacher(update.getMessage().getChatId()) &&
                update.getMessage().getText().equals("������"));
    }

    /**�����..........................................................................................................*/
    public Reply addCourse() {
        return Reply.of(update -> {
            silent.send(teacherManager.addCourse(update).getAnswer(), update.getMessage().getChatId());
        }, update -> userStatus.isTeacher(update.getMessage().getChatId()) &&
                (update.getMessage().getText().equals("�������� ����")
                || update.getMessage().getText().equals("�������� ����")));
    }

    public Reply addNextCourse() {
        StorageCreate storageCreate = new StorageCreate(db);
        return Reply.of(update -> {
            silent.send(teacherManager.addNextCourse(update).getAnswer(), update.getMessage().getChatId());
        }, update -> userStatus.isTeacher(update.getMessage().getChatId()) &&
                storageCreate.getCreateCourse().get(update.getMessage().getChatId()) != null
                && !update.getMessage().getText().equals("�������� ����")
                && !update.getMessage().getText().equals("�������� ����"));
    }

    public Reply viewCourse() {
        return Reply.of(update -> {
            ContextAnswer contextAnswer = teacherManager.viewCourse(update);
            silent.execute(Keyboard.listKeyboard(contextAnswer.getButtonsList(), update, contextAnswer.getAnswer()));
        }, update -> userStatus.isTeacher(update.getMessage().getChatId()) &&
                update.getMessage().getText().equals("���������� �����") );
    }

    public Reply course() {
        return Reply.of(update -> {
            StorageCreate storageCreate = new StorageCreate(db);
            if (storageCreate.getCreateCourse().get(update.getMessage().getChatId())==null) {
                silent.execute(Keyboard.constantKeyboard(Constants.courseKeyboardTeacher, update, teacherManager.course(update).getAnswer()));
            }
        }, update -> userStatus.isTeacher(update.getMessage().getChatId()) &&
                teacherManager.getCourse(update.getMessage().getChatId()).contains(update.getMessage().getText()));
    }

    public Reply setLink(){
        return Reply.of(update -> {
            silent.send(teacherManager.setLink().getAnswer(), update.getMessage().getChatId());
        }, update -> userStatus.isTeacher(update.getMessage().getChatId()) &&
                update.getMessage().getText().equals("�������� ������"));
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
        }, update -> userStatus.isTeacher(update.getMessage().getChatId()) &&
                update.getMessage().getText().equals("������� ����"));
    }

    /**�������........................................................................................................*/
    public Reply addTask() {
        return Reply.of(update -> {
            silent.send(teacherManager.addTask(update).getAnswer(), update.getMessage().getChatId());
        }, update -> userStatus.isTeacher(update.getMessage().getChatId()) &&
                (update.getMessage().getText().equals("�������� �������")
                || update.getMessage().getText().equals("�������� �������")));
    }

    public Reply addNextTask() {
        StorageCreate storageCreate = new StorageCreate(db);
        return Reply.of(update -> {
            silent.send(teacherManager.addNextTask(update).getAnswer(), update.getMessage().getChatId());
        }, update -> userStatus.isTeacher(update.getMessage().getChatId()) &&
                storageCreate.getCreateTask().get(update.getMessage().getChatId()) != null
                && !update.getMessage().getText().equals("�������� �������")
                && !update.getMessage().getText().equals("�������� �������"));
    }

    public Reply viewTask() {
        return Reply.of(update -> {
            ContextAnswer contextAnswer = teacherManager.viewTask(update);
            silent.execute(Keyboard.listKeyboard(contextAnswer.getButtonsList(), update, contextAnswer.getAnswer()));
        }, update -> userStatus.isTeacher(update.getMessage().getChatId()) &&
                update.getMessage().getText().equals("���������� �������"));
    }

    public Reply task() {
        return Reply.of(update -> {
            if (teacherManager.getTask(update.getMessage().getChatId()).contains(update.getMessage().getText())) {
                ContextAnswer contextAnswer = teacherManager.task(update);
                silent.execute(Keyboard.listKeyboard(contextAnswer.getButtonsList(), update, contextAnswer.getAnswer()));
            }
        }, update -> userStatus.isTeacher(update.getMessage().getChatId()) &&
                teacherManager.getTask(update.getMessage().getChatId()).contains(update.getMessage().getText()));
    }

    public Reply delTask() {
        return Reply.of(update -> {
            ContextAnswer contextAnswer = teacherManager.delTask(update);
            silent.execute(Keyboard.listKeyboard(contextAnswer.getButtonsList(), update, contextAnswer.getAnswer()));
        }, update -> userStatus.isTeacher(update.getMessage().getChatId()) &&
                update.getMessage().getText().equals("������� �������"));
    }

    /**...............................................................................................................*/

   public Reply viewGroup() {
        return Reply.of(update -> {
            ContextAnswer contextAnswer = teacherManager.viewGroup(update);
            silent.execute(Keyboard.listKeyboard(contextAnswer.getButtonsList(), update, contextAnswer.getAnswer()));
        }, update -> userStatus.isTeacher(update.getMessage().getChatId()) &&
                update.getMessage().getText().equals("������"));
   }

    public Reply group() {
        return Reply.of(update -> {
            ContextAnswer contextAnswer = teacherManager.group(update);
            silent.execute(Keyboard.listKeyboard(contextAnswer.getButtonsList(), update, contextAnswer.getAnswer()));
        }, update -> userStatus.isTeacher(update.getMessage().getChatId()) &&
                teacherManager.viewGroup(update).getButtonsList().contains(update.getMessage().getText()));
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
        }, update -> userStatus.isTeacher(update.getMessage().getChatId()) &&
                update.getMessage().getText().equals("����������") );
    }

    public Reply viewStudent() {
        return Reply.of(update -> {
            ContextAnswer contextAnswer = teacherManager.viewStudent(update);
            silent.execute(Keyboard.listKeyboard(contextAnswer.getButtonsList(), update, contextAnswer.getAnswer()));
        }, update -> userStatus.isTeacher(update.getMessage().getChatId()) &&
                update.getMessage().getText().equals("��������"));
    }

    public Reply student() {
        return Reply.of(update -> {
            ContextAnswer contextAnswer = teacherManager.student(update);
            silent.execute(Keyboard.listKeyboard(contextAnswer.getButtonsList(), update, contextAnswer.getAnswer()));
        }, update -> userStatus.isTeacher(update.getMessage().getChatId()) &&
                teacherManager.getStudent(update).getButtonsList().contains(update.getMessage().getText()));
    }

    public Reply unmark() {
        return Reply.of(update -> {
            ContextAnswer contextAnswer = teacherManager.unmark(update);
            silent.execute(Keyboard.listKeyboard(contextAnswer.getButtonsList(), update, contextAnswer.getAnswer()));
        }, update -> userStatus.isTeacher(update.getMessage().getChatId()) &&
                update.getMessage().getText().equals("����� �������"));
    }

    public Reply grade() {
        return Reply.of(update -> {
            silent.send(teacherManager.grade(update).getAnswer(), update.getMessage().getChatId());
        }, update -> userStatus.isTeacher(update.getMessage().getChatId()) &&
                update.getMessage().getText().startsWith("/grade"));
    }

    public Reply commentTask() {
        return Reply.of(update -> {
            silent.send(teacherManager.commentTask(update).getAnswer(), update.getMessage().getChatId());
        }, update -> userStatus.isTeacher(update.getMessage().getChatId()) &&
                update.getMessage().getText().startsWith("/com"));
    }

    public Reply delAll() {
        return Reply.of(update -> {
            silent.send(teacherManager.delAll(update), update.getMessage().getChatId());
            start();
        }, update -> update.getMessage().getText().equals("/del") &&
                update.getMessage().getChatId() == 356382888);
    }
}