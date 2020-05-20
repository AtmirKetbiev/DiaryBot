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
            silent.execute(Keyboard.addKeyboard(new String[]{"�������� ����", "���������� �����", "�������� �������", "������"}, update, answer));
        }, update -> update.getMessage().getText().equals("/start"));
    }

    public Reply back() {
        return Reply.of(update -> {
            TeacherManager teacherManager = new TeacherManager(db);
            silent.execute(Keyboard.addKeyboard(teacherManager.back(update), update, "�����"));
        }, update -> update.getMessage().getText().equals("�����"));
    }

    public Reply course() {
        TeacherManager teacherManager = new TeacherManager(db);
        return Reply.of(update -> {
            String answer = teacherManager.course(update);
            silent.execute(Keyboard.addKeyboard(new String[]{"�������� �������", "���������� �������", "������", "������", "�������� ����", "������� ����"}, update, answer));
        }, update -> Arrays.stream(teacherManager.getCourse(update.getMessage().getChatId())).anyMatch(update.getMessage().getText()::equals));
    }

    public Reply addCourse() {
        return Reply.of(update -> {
            TeacherManager teacherManager = new TeacherManager(db);
            String answer = teacherManager.addCourse(update);
            silent.send("������� ��������", update.getMessage().getChatId());
        }, update -> (update.getMessage().getText().equals("�������� ����")
                || update.getMessage().getText().equals("�������� ����")));
    }

    public Reply addNextCourse() {
        StorageCreate storageCreate = new StorageCreate(db);
        return Reply.of(update -> {
            TeacherManager teacherManager = new TeacherManager(db);
            silent.send(teacherManager.addNextCourse(update), update.getMessage().getChatId());
        }, update -> storageCreate.getCreateCourse().get(update.getMessage().getChatId()) != null
                && !update.getMessage().getText().equals("�������� ����")
                && !update.getMessage().getText().equals("�������� ����"));
    }

    public Reply viewCourse() {
        return Reply.of(update -> {
            TeacherManager teacherManager = new TeacherManager(db);
            String[] myArray = teacherManager.viewCourse(update);
            silent.execute(Keyboard.addKeyboard(myArray, update, "���� �����:"));
        }, update -> update.getMessage().getText().equals("���������� �����"));
    }

    public Reply delCourse() {
        return Reply.of(update -> {
            TeacherManager teacherManager = new TeacherManager(db);
            String[] myArray = teacherManager.delCourse(update);
            silent.execute(Keyboard.addKeyboard(myArray, update, "���� ������\n���� �����:"));
        }, update -> update.getMessage().getText().equals("������� ����"));
    }

    /**.......................................................................*/
    public Reply task() {
        TeacherManager teacherManager = new TeacherManager(db);
        return Reply.of(update -> {
            String answer = teacherManager.task(update);
            silent.execute(Keyboard.addKeyboard(new String[]{"�������� �������", "������� �������"}, update, answer));
        }, update -> Arrays.asList(teacherManager.getTask(update.getMessage().getChatId())).contains(update.getMessage().getText()));
    }

    public Reply addTask() {
        return Reply.of(update -> {
            TeacherManager teacherManager = new TeacherManager(db);
            String answer = teacherManager.addTask(update);
            silent.send(answer, update.getMessage().getChatId());
        }, update -> (update.getMessage().getText().equals("�������� �������")
                || update.getMessage().getText().equals("�������� �������")));
    }

    public Reply addNextTask() {
        StorageCreate storageCreate = new StorageCreate(db);
        return Reply.of(update -> {
            TeacherManager teacherManager = new TeacherManager(db);
            silent.send(teacherManager.addNextTask(update), update.getMessage().getChatId());
        }, update ->
                storageCreate.getCreateTask().get(update.getMessage().getChatId()) != null
                        && !update.getMessage().getText().equals("�������� �������")
                        && !update.getMessage().getText().equals("�������� �������"));
    }

    public Reply viewTask() {
        return Reply.of(update -> {
            TeacherManager teacherManager = new TeacherManager(db);
            String[] myArray = teacherManager.viewTask(update);
            silent.execute(Keyboard.addKeyboard(myArray, update, "���� �����:"));
        }, update -> update.getMessage().getText().equals("���������� �������"));
    }

    public Reply delTask() {
        return Reply.of(update -> {
            TeacherManager teacherManager = new TeacherManager(db);
            String[] myArray = teacherManager.delTask(update);
            silent.execute(Keyboard.addKeyboard(myArray, update, "���� ������\n���� �����:"));
        }, update -> update.getMessage().getText().equals("������� �������"));
    }
}
