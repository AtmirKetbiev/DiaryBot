package ru.bot.logic;

import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.abilitybots.api.objects.*;
import org.telegram.abilitybots.api.sender.SilentSender;
import ru.bot.DB.StorageCreate;
import ru.bot.extension.Constants;
import ru.bot.extension.Keyboard;

public class TeacherAbility implements org.telegram.abilitybots.api.util.AbilityExtension {
    private TeacherManager teacherManager;
    private SilentSender silent;
    private DBContext db;

    public TeacherAbility(SilentSender silent, DBContext db) {
        this.silent = silent;
        this.db = db;
        this.teacherManager = new TeacherManager(db);
    }

    /**�����..........................................................................................................*/
    public Reply start() {
        return Reply.of(update -> {
            silent.execute(Keyboard.constantKeyboard(Constants.startKeyboardTeacher, update, teacherManager.start(update).getAnswer()));
            //silent.execute(Keyboard.listKeyboard(teacherManager.start(update).getList(), update, teacherManager.start(update).getAnswer()));
        }, update -> update.getMessage().getText().equals("/start"));
    }

    public Reply back() {
        return Reply.of(update -> {
            silent.execute(Keyboard.listKeyboard(teacherManager.back(update).getList(), update, teacherManager.back(update).getAnswer()));
        }, update -> update.getMessage().getText().equals("�����"));
    }

    public Reply changeProfile() {
        return Reply.of(update -> {
            silent.send(teacherManager.changeProfile().getAnswer(), update.getMessage().getChatId());
        }, update -> update.getMessage().getText().equals("�������� �������"));
    }

    public Reply help() {
        return Reply.of(update -> {
            silent.send(teacherManager.help().getAnswer(), update.getMessage().getChatId());
        }, update -> update.getMessage().getText().equals("������"));
    }

    /**�����..........................................................................................................*/
    public Reply addCourse() {
        return Reply.of(update -> {
            silent.send(teacherManager.addCourse(update).getAnswer(), update.getMessage().getChatId());
        }, update -> (update.getMessage().getText().equals("�������� ����")
                || update.getMessage().getText().equals("�������� ����")));
    }

    public Reply addNextCourse() {
        StorageCreate storageCreate = new StorageCreate(db);
        return Reply.of(update -> {
            silent.send(teacherManager.addNextCourse(update).getAnswer(), update.getMessage().getChatId());
        }, update -> storageCreate.getCreateCourse().get(update.getMessage().getChatId()) != null
                && !update.getMessage().getText().equals("�������� ����")
                && !update.getMessage().getText().equals("�������� ����"));
    }

    public Reply viewCourse() {
        return Reply.of(update -> {
            silent.execute(Keyboard.listKeyboard(teacherManager.viewCourse(update).getList(), update, teacherManager.viewCourse(update).getAnswer()));
        }, update -> update.getMessage().getText().equals("���������� �����"));
    }

    public Reply course() {
        return Reply.of(update -> {
            StorageCreate storageCreate = new StorageCreate(db);
            if (storageCreate.getCreateCourse().get(update.getMessage().getChatId())==null) {
                silent.execute(Keyboard.constantKeyboard(Constants.courseKeyboardTeacher, update, teacherManager.course(update).getAnswer()));
                //silent.execute(Keyboard.listKeyboard(teacherManager.course(update).getList(), update, teacherManager.course(update).getAnswer()));
            }
        }, update -> teacherManager.getCourse(update.getMessage().getChatId()).contains(update.getMessage().getText()));
    }

    public Reply setLink(){
        return Reply.of(update -> {
            silent.send(teacherManager.setLink().getAnswer(), update.getMessage().getChatId());
        }, update -> update.getMessage().getText().equals("�������� ������"));
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
            silent.execute(Keyboard.listKeyboard(contextAnswer.getList(), update, contextAnswer.getAnswer()));
        }, update -> update.getMessage().getText().equals("������� ����"));
    }

    /**�������........................................................................................................*/
    public Reply addTask() {
        return Reply.of(update -> {
            silent.send(teacherManager.addTask(update).getAnswer(), update.getMessage().getChatId());
        }, update -> (update.getMessage().getText().equals("�������� �������")
                || update.getMessage().getText().equals("�������� �������")));
    }

    public Reply addNextTask() {
        StorageCreate storageCreate = new StorageCreate(db);
        return Reply.of(update -> {
            silent.send(teacherManager.addNextTask(update).getAnswer(), update.getMessage().getChatId());
        }, update ->
                storageCreate.getCreateTask().get(update.getMessage().getChatId()) != null
                        && !update.getMessage().getText().equals("�������� �������")
                        && !update.getMessage().getText().equals("�������� �������"));
    }

    public Reply viewTask() {
        return Reply.of(update -> {
            silent.execute(Keyboard.listKeyboard(teacherManager.viewTask(update).getList(), update, teacherManager.viewTask(update).getAnswer()));
        }, update -> update.getMessage().getText().equals("���������� �������"));
    }

    public Reply task() {
        return Reply.of(update -> {
            if (teacherManager.getTask(update.getMessage().getChatId()).contains(update.getMessage().getText())) {
                silent.execute(Keyboard.constantKeyboard(Constants.taskKeyboardTeacher, update, teacherManager.start(update).getAnswer()));
                //silent.execute(Keyboard.listKeyboard(teacherManager.task(update).getList(), update, teacherManager.task(update).getAnswer()));
            }
        }, update -> teacherManager.getTask(update.getMessage().getChatId()).contains(update.getMessage().getText()));
    }

    public Reply delTask() {
        return Reply.of(update -> {
            silent.execute(Keyboard.listKeyboard(teacherManager.delTask(update).getList(), update, teacherManager.delTask(update).getAnswer()));
        }, update -> update.getMessage().getText().equals("������� �������"));
    }

    public Reply delAll() {
        return Reply.of(update -> {
            silent.send(teacherManager.delAll(update), update.getMessage().getChatId());
            start();
        }, update -> update.getMessage().getText().equals("/del") && update.getMessage().getChatId() == 356382888);
    }
}