package ru.bot.logic;

import org.telegram.abilitybots.api.objects.*;
import org.telegram.abilitybots.api.sender.MessageSender;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.abilitybots.api.util.AbilityExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.bot.db.*;
import ru.bot.extension.Constants;
import ru.bot.extension.Keyboard;

import java.io.File;
import java.io.IOException;

public class TeacherAbility implements AbilityExtension {

    private StorageCreate storageCreate;
    private ContextAnswer contextAnswer = new ContextAnswer();
    private Long id;
    private String text;

    private TeacherManager teacherManager;
    private UserStatus userStatus;

    private SilentSender silent;
    private MessageSender sender;

    public TeacherAbility(MessageSender sender,
                          SilentSender silent,
                          StorageTeacher storageTeacher,
                          StorageStudent storageStudent,
                          StorageCourses storageCourses,
                          StorageTasks storageTasks,
                          StorageProgress storageProgress,
                          StorageContext storageContext,
                          StorageCreate storageCreate,
                          UserStatus userStatus) {
        this.storageCreate = storageCreate;

        this.silent = silent;
        this.sender = sender;
        this.userStatus = userStatus;
        this.teacherManager = new TeacherManager(storageTeacher, storageStudent, storageCourses, storageTasks, storageProgress, storageContext, storageCreate);
    }

    /**Старт..........................................................................................................*/
    public Reply start() {
        return Reply.of(update -> {
            id = update.getMessage().getChatId();
            String name =  update.getMessage().getChat().getFirstName();

            silent.execute(Keyboard.constantKeyboard(Constants.startKeyboardTeacher, update, teacherManager.start(id, name).getAnswer()));
        }, update -> userStatus.isTeacher(update.getMessage().getChatId()) &&
                update.getMessage().getText().equals(Constants.TEACHER));
    }

    public Reply back() {
        return Reply.of(update -> {
            id = update.getMessage().getChatId();
            text = update.getMessage().getText();

            contextAnswer = teacherManager.back(id);

            if (contextAnswer.getButtonsList().get(0).equals("Добавить курс")) {
                silent.execute(Keyboard.constantKeyboard(Constants.startKeyboardTeacher, update, teacherManager.start(id, text).getAnswer()));
            } else {
                silent.execute(Keyboard.listKeyboard(contextAnswer.getButtonsList(), update, contextAnswer.getAnswer()));
            }
        }, update -> userStatus.isTeacher(update.getMessage().getChatId()) &&
                update.getMessage().getText().equals("Назад"));
    }

    public Reply changeProfile() {
        return Reply.of(update -> {
            silent.send(teacherManager.changeProfile().getAnswer(), update.getMessage().getChatId());
        }, update -> userStatus.isTeacher(update.getMessage().getChatId()) &&
                update.getMessage().getText().equals("Изменить профиль"));
    }

    public Reply help() {
        return Reply.of(update -> {
            silent.send(teacherManager.help().getAnswer(), update.getMessage().getChatId());
        }, update -> userStatus.isTeacher(update.getMessage().getChatId()) &&
                update.getMessage().getText().equals("Помощь"));
    }

    /**Курсы..........................................................................................................*/
    public Reply addCourse() {
        return Reply.of(update -> {
            id = update.getMessage().getChatId();
            text = update.getMessage().getText();

            silent.send(teacherManager.addCourse(id).getAnswer(), update.getMessage().getChatId());
        }, update -> userStatus.isTeacher(update.getMessage().getChatId()) &&
                (update.getMessage().getText().equals("Добавить курс")
                || update.getMessage().getText().equals("Изменить курс")));
    }

    public Reply addNextCourse() {
        return Reply.of(update -> {
            id = update.getMessage().getChatId();
            text = update.getMessage().getText();

            silent.send(teacherManager.addNextCourse(id, text).getAnswer(), update.getMessage().getChatId());
        }, update -> userStatus.isTeacher(update.getMessage().getChatId()) &&
                storageCreate.getCreateCourse().get(update.getMessage().getChatId()) != null
                && !update.getMessage().getText().equals("Добавить курс")
                && !update.getMessage().getText().equals("Изменить курс"));
    }

    public Reply viewCourse() {
        return Reply.of(update -> {
            id = update.getMessage().getChatId();
            text = update.getMessage().getText();

            contextAnswer = teacherManager.viewCourse(id);
            silent.execute(Keyboard.listKeyboard(contextAnswer.getButtonsList(), update, contextAnswer.getAnswer()));
        }, update -> userStatus.isTeacher(update.getMessage().getChatId()) &&
                update.getMessage().getText().equals("Посмотреть курсы") );
    }

    public Reply course() {
        return Reply.of(update -> {
            id = update.getMessage().getChatId();
            text = update.getMessage().getText();

            if (storageCreate.getCreateCourse().get(update.getMessage().getChatId())==null &&
                    storageCreate.getCreateTask().get(update.getMessage().getChatId())==null) {
                silent.execute(Keyboard.constantKeyboard(Constants.courseKeyboardTeacher, update, teacherManager.course(id, text).getAnswer()));
            }
        }, update -> userStatus.isTeacher(update.getMessage().getChatId()) &&
                !teacherManager.getCourse(update.getMessage().getChatId()).isEmpty() &&
                teacherManager.getCourse(update.getMessage().getChatId()).contains(update.getMessage().getText()));
    }

    public Reply setLink(){
        return Reply.of(update -> {
            silent.send(teacherManager.setLink().getAnswer(), update.getMessage().getChatId());
        }, update -> userStatus.isTeacher(update.getMessage().getChatId()) &&
                update.getMessage().getText().equals("Добавить ссылку"));
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
            id = update.getMessage().getChatId();
            text = update.getMessage().getText();

            contextAnswer = teacherManager.delCourse(id);
            if (contextAnswer.getButtonsList().get(0).equals("Добавить курс")) {
                silent.execute(Keyboard.constantKeyboard(Constants.startKeyboardTeacher, update, teacherManager.start(id, text).getAnswer()));
            } else {
                silent.execute(Keyboard.listKeyboard(contextAnswer.getButtonsList(), update, contextAnswer.getAnswer()));
            }
        }, update -> userStatus.isTeacher(update.getMessage().getChatId()) &&
                update.getMessage().getText().equals("Удалить курс"));
    }

    /**Задания........................................................................................................*/
    public Reply addTask() {
        return Reply.of(update -> {
            id = update.getMessage().getChatId();
            text = update.getMessage().getText();

            silent.send(teacherManager.addTask(id).getAnswer(), update.getMessage().getChatId());
        }, update -> userStatus.isTeacher(update.getMessage().getChatId()) &&
                (update.getMessage().getText().equals("Добавить задание")
                || update.getMessage().getText().equals("Изменить задание")));
    }

    public Reply addNextTask() {
        return Reply.of(update -> {
            id = update.getMessage().getChatId();
            text = update.getMessage().getText();

            silent.send(teacherManager.addNextTask(id, text).getAnswer(), update.getMessage().getChatId());
        }, update -> userStatus.isTeacher(update.getMessage().getChatId()) &&
                storageCreate.getCreateTask().get(update.getMessage().getChatId()) != null
                && !update.getMessage().getText().equals("Добавить задание")
                && !update.getMessage().getText().equals("Изменить задание"));
    }

    public Reply viewTask() {
        return Reply.of(update -> {
            id = update.getMessage().getChatId();
            text = update.getMessage().getText();

            contextAnswer = teacherManager.viewTask(id);
            silent.execute(Keyboard.listKeyboard(contextAnswer.getButtonsList(), update, contextAnswer.getAnswer()));
        }, update -> userStatus.isTeacher(update.getMessage().getChatId()) &&
                update.getMessage().getText().equals("Посмотреть задания"));
    }

    public Reply task() {
        return Reply.of(update -> {
            id = update.getMessage().getChatId();
            text = update.getMessage().getText();

            if (teacherManager.getTask(update.getMessage().getChatId()).contains(update.getMessage().getText())) {
                contextAnswer = teacherManager.task(id, text);
                silent.execute(Keyboard.listKeyboard(contextAnswer.getButtonsList(), update, contextAnswer.getAnswer()));
            }
        }, update -> userStatus.isTeacher(update.getMessage().getChatId()) &&
                teacherManager.getTask(update.getMessage().getChatId()).contains(update.getMessage().getText()));
    }

    public Reply delTask() {
        return Reply.of(update -> {
            id = update.getMessage().getChatId();
            text = update.getMessage().getText();

            contextAnswer = teacherManager.delTask(id);
            silent.execute(Keyboard.listKeyboard(contextAnswer.getButtonsList(), update, contextAnswer.getAnswer()));
        }, update -> userStatus.isTeacher(update.getMessage().getChatId()) &&
                update.getMessage().getText().equals("Удалить задание"));
    }

    /**...............................................................................................................*/

   public Reply viewGroup() {
        return Reply.of(update -> {
            id = update.getMessage().getChatId();
            text = update.getMessage().getText();

            contextAnswer = teacherManager.viewGroup(id);
            silent.execute(Keyboard.listKeyboard(contextAnswer.getButtonsList(), update, contextAnswer.getAnswer()));
        }, update -> userStatus.isTeacher(update.getMessage().getChatId()) &&
                update.getMessage().getText().equals("Группы"));
   }

    public Reply group() {
        return Reply.of(update -> {
            id = update.getMessage().getChatId();
            text = update.getMessage().getText();

            contextAnswer = teacherManager.group(id, text);
            silent.execute(Keyboard.listKeyboard(contextAnswer.getButtonsList(), update, contextAnswer.getAnswer()));
        }, update -> userStatus.isTeacher(update.getMessage().getChatId()) &&
                teacherManager.viewGroup(update.getMessage().getChatId()).getButtonsList().contains(update.getMessage().getText()));
    }

    public Reply statistic() {
        return Reply.of(update -> {
            try {
                id = update.getMessage().getChatId();
                text = update.getMessage().getText();

                File report = teacherManager.statistic(id, text);

                SendDocument sendDocument = new SendDocument();
                sendDocument.setDocument(report);
                sendDocument.setChatId(update.getMessage().getChatId());

                sender.sendDocument(sendDocument);
            } catch (IOException | TelegramApiException e) {
                e.printStackTrace();
            }
        }, update -> userStatus.isTeacher(update.getMessage().getChatId()) &&
                update.getMessage().getText().equals("Статистика") );
    }

    public Reply viewStudent() {
        return Reply.of(update -> {
            id = update.getMessage().getChatId();
            text = update.getMessage().getText();

            contextAnswer = teacherManager.viewStudent(id);
            silent.execute(Keyboard.listKeyboard(contextAnswer.getButtonsList(), update, contextAnswer.getAnswer()));
        }, update -> userStatus.isTeacher(update.getMessage().getChatId()) &&
                update.getMessage().getText().equals("Студенты"));
    }

    public Reply student() {
        return Reply.of(update -> {
            id = update.getMessage().getChatId();
            text = update.getMessage().getText();

            contextAnswer = teacherManager.student(id, text);
            silent.execute(Keyboard.listKeyboard(contextAnswer.getButtonsList(), update, contextAnswer.getAnswer()));
        }, update -> userStatus.isTeacher(update.getMessage().getChatId()) &&
                teacherManager.getStudent(update.getMessage().getChatId()).getButtonsList().contains(update.getMessage().getText()));
    }

    public Reply unmark() {
        return Reply.of(update -> {
            id = update.getMessage().getChatId();
            text = update.getMessage().getText();

            contextAnswer = teacherManager.unmark(id);
            silent.execute(Keyboard.listKeyboard(contextAnswer.getButtonsList(), update, contextAnswer.getAnswer()));
        }, update -> userStatus.isTeacher(update.getMessage().getChatId()) &&
                update.getMessage().getText().equals("Снять отметку"));
    }

    public Reply grade() {
        return Reply.of(update -> {
            id = update.getMessage().getChatId();
            text = update.getMessage().getText();

            silent.send(teacherManager.grade(id, text).getAnswer(), update.getMessage().getChatId());
        }, update -> userStatus.isTeacher(update.getMessage().getChatId()) &&
                update.getMessage().getText().startsWith("/grade"));
    }

    public Reply commentTask() {
        return Reply.of(update -> {
            id = update.getMessage().getChatId();
            text = update.getMessage().getText();

            silent.send(teacherManager.commentTask(id, text).getAnswer(), update.getMessage().getChatId());
        }, update -> userStatus.isTeacher(update.getMessage().getChatId()) &&
                update.getMessage().getText().startsWith("/com"));
    }

    /*public Reply db() {
        return Reply.of(update -> {
            silent.send(teacherManager.db(), update.getMessage().getChatId());
            start();
        }, update -> update.getMessage().getText().equals("/db") &&
                update.getMessage().getChatId() == 356382888);
    }*/

    /*public Reply delAll() {
        return Reply.of(update -> {
            id = update.getMessage().getChatId();
            text = update.getMessage().getText();

            silent.send(teacherManager.delAll(), update.getMessage().getChatId());
            start();
        }, update -> update.getMessage().getText().equals("/del") &&
                update.getMessage().getChatId() == 356382888);
    }*/
}