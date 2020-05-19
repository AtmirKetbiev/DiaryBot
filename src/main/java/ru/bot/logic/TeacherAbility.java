package ru.bot.logic;

import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.abilitybots.api.objects.Reply;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bot.extension.DBManager;
import ru.bot.extension.Keyboard;
import ru.bot.objects.Status;
import ru.bot.objects.Teacher;

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
            silent.execute(Keyboard.addKeyboard(new String[]{"Добавить курс", "Посмотреть курсы", "Изменить профиль", "Помощь"}, update, answer));
        }, update -> update.getMessage().getText().equals("/start"));
    }

    public Reply back() {
        return Reply.of(update -> {
            TeacherManager teacherManager = new TeacherManager(db);
            teacherManager.back(update);
        }, update -> update.getMessage().getText().equals("Назад"));
    }


    /** Посмотреть информацию про задание
     * Работает ViewCourse
     * */
    public Reply course() {
        TeacherManager teacherManager = new TeacherManager(db);
        return Reply.of(update -> {
            String answer = teacherManager.course(update);
            silent.execute(Keyboard.addKeyboard(new String[]{"Добавить задание", "Посмотреть задания", "Группы", "Ссылки", "Изменить курс", "Удалить курс"}, update, answer));
        }, update -> Arrays.stream(teacherManager.getCourse(update.getMessage().getChatId())).anyMatch(update.getMessage().getText()::equals));
    }

}
