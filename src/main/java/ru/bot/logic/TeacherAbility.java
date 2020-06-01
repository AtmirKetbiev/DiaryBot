package ru.bot.logic;

import org.apache.poi.ss.usermodel.Workbook;
import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.abilitybots.api.objects.*;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.bot.DB.StorageCreate;
import ru.bot.extension.Constants;
import ru.bot.extension.Keyboard;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class TeacherAbility implements org.telegram.abilitybots.api.util.AbilityExtension {
    private TeacherManager teacherManager;
    private SilentSender silent;
    private DBContext db;

    public TeacherAbility(SilentSender silent, DBContext db) {
        this.silent = silent;
        this.db = db;
        this.teacherManager = new TeacherManager(db);
    }

    /**Старт..........................................................................................................*/
    public Reply start() {
        return Reply.of(update -> {
            silent.execute(Keyboard.constantKeyboard(Constants.startKeyboardTeacher, update, teacherManager.start(update).getAnswer()));
        }, update -> update.getMessage().getText().equals("/start"));
    }

    public Reply back() {
        return Reply.of(update -> {
            ContextAnswer contextAnswer = teacherManager.back(update);
            silent.execute(Keyboard.listKeyboard(contextAnswer.getButtonsList(), update, contextAnswer.getAnswer()));
        }, update -> update.getMessage().getText().equals("Назад"));
    }

    public Reply changeProfile() {
        return Reply.of(update -> {
            silent.send(teacherManager.changeProfile().getAnswer(), update.getMessage().getChatId());
        }, update -> update.getMessage().getText().equals("Изменить профиль"));
    }

    public Reply help() {
        return Reply.of(update -> {
            silent.send(teacherManager.help().getAnswer(), update.getMessage().getChatId());
        }, update -> update.getMessage().getText().equals("Помощь"));
    }

    /**Курсы..........................................................................................................*/
    public Reply addCourse() {
        return Reply.of(update -> {
            silent.send(teacherManager.addCourse(update).getAnswer(), update.getMessage().getChatId());
        }, update -> (update.getMessage().getText().equals("Добавить курс")
                || update.getMessage().getText().equals("Изменить курс")));
    }

    public Reply addNextCourse() {
        StorageCreate storageCreate = new StorageCreate(db);
        return Reply.of(update -> {
            silent.send(teacherManager.addNextCourse(update).getAnswer(), update.getMessage().getChatId());
        }, update -> storageCreate.getCreateCourse().get(update.getMessage().getChatId()) != null
                && !update.getMessage().getText().equals("Добавить курс")
                && !update.getMessage().getText().equals("Изменить курс"));
    }

    public Reply viewCourse() {
        return Reply.of(update -> {
            ContextAnswer contextAnswer = teacherManager.viewCourse(update);
            silent.execute(Keyboard.listKeyboard(contextAnswer.getButtonsList(), update, contextAnswer.getAnswer()));
        }, update -> update.getMessage().getText().equals("Посмотреть курсы"));
    }

    public Reply course() {
        return Reply.of(update -> {
            StorageCreate storageCreate = new StorageCreate(db);
            if (storageCreate.getCreateCourse().get(update.getMessage().getChatId())==null) {
                silent.execute(Keyboard.constantKeyboard(Constants.courseKeyboardTeacher, update, teacherManager.course(update).getAnswer()));
            }
        }, update -> teacherManager.getCourse(update.getMessage().getChatId()).contains(update.getMessage().getText()));
    }

    public Reply setLink(){
        return Reply.of(update -> {
            silent.send(teacherManager.setLink().getAnswer(), update.getMessage().getChatId());
        }, update -> update.getMessage().getText().equals("Добавить ссылку"));
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
        }, update -> update.getMessage().getText().equals("Удалить курс"));
    }

    /**Задания........................................................................................................*/
    public Reply addTask() {
        return Reply.of(update -> {
            silent.send(teacherManager.addTask(update).getAnswer(), update.getMessage().getChatId());
        }, update -> (update.getMessage().getText().equals("Добавить задание")
                || update.getMessage().getText().equals("Изменить задание")));
    }

    public Reply addNextTask() {
        StorageCreate storageCreate = new StorageCreate(db);
        return Reply.of(update -> {
            silent.send(teacherManager.addNextTask(update).getAnswer(), update.getMessage().getChatId());
        }, update ->
                storageCreate.getCreateTask().get(update.getMessage().getChatId()) != null
                        && !update.getMessage().getText().equals("Добавить задание")
                        && !update.getMessage().getText().equals("Изменить задание"));
    }

    public Reply viewTask() {
        return Reply.of(update -> {
            ContextAnswer contextAnswer = teacherManager.viewTask(update);
            silent.execute(Keyboard.listKeyboard(contextAnswer.getButtonsList(), update, contextAnswer.getAnswer()));
        }, update -> update.getMessage().getText().equals("Посмотреть задания"));
    }

    public Reply task() {
        return Reply.of(update -> {
            if (teacherManager.getTask(update.getMessage().getChatId()).contains(update.getMessage().getText())) {
                silent.execute(Keyboard.constantKeyboard(Constants.taskKeyboardTeacher, update, teacherManager.task(update).getAnswer()));
            }
        }, update -> teacherManager.getTask(update.getMessage().getChatId()).contains(update.getMessage().getText()));
    }

    public Reply delTask() {
        return Reply.of(update -> {
            ContextAnswer contextAnswer = teacherManager.delTask(update);
            silent.execute(Keyboard.listKeyboard(contextAnswer.getButtonsList(), update, contextAnswer.getAnswer()));
        }, update -> update.getMessage().getText().equals("Удалить задание"));
    }

    /**...............................................................................................................*/

   public Reply group() {
        return Reply.of(update -> {
            ContextAnswer contextAnswer = teacherManager.group(update);
            silent.execute(Keyboard.listKeyboard(contextAnswer.getButtonsList(), update, contextAnswer.getAnswer()));
        }, update -> update.getMessage().getText().equals("Группы"));
   }

    public Reply statistic() {
        return Reply.of(update -> {
            try {
                File report = teacherManager.statistic(update);

                SendDocument sendDocument = new SendDocument();
                sendDocument.setDocument(report);
                sendDocument.setChatId(update.getMessage().getChatId());

                TelegramLongPollingBot telegramLongPollingBot = new TelegramLongPollingBot() {
                    @Override
                    public void onUpdateReceived(Update update) {
                    }

                    @Override
                    public String getBotUsername() {
                        return Constants.BOT_USERNAME;
                    }

                    @Override
                    public String getBotToken() {
                        return Constants.TOKEN;
                    }
                };
                telegramLongPollingBot.execute(sendDocument);
            } catch (IOException | TelegramApiException e) {
                e.printStackTrace();
            }
        }, update -> teacherManager.group(update).getButtonsList().contains(update.getMessage().getText()));
    }

    /*public Reply viewStudent() {
        return Reply.of(update -> {
            ContextAnswer contextAnswer = teacherManager.viewStudent(update);
            silent.execute(Keyboard.listKeyboard(contextAnswer.getButtonsList(), update, contextAnswer.getAnswer()));
        }, update -> teacherManager.group(update).getButtonsList().contains(update.getMessage().getText()));
    }*/

    /* public Reply student() {
        return Reply.of(update -> {
            ContextAnswer contextAnswer = teacherManager.student(update);
            silent.execute(Keyboard.listKeyboard(contextAnswer.getList(), update, contextAnswer.getAnswer()));
        }, update -> teacherManager.getStudent(update).getList().contains(update.getMessage().getText()));
    }*/

    public Reply delAll() {
        return Reply.of(update -> {
            silent.send(teacherManager.delAll(update), update.getMessage().getChatId());
            start();
        }, update -> update.getMessage().getText().equals("/del") && update.getMessage().getChatId() == 356382888);
    }
}