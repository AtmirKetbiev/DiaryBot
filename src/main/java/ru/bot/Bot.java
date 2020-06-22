package ru.bot;

import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.util.AbilityExtension;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import ru.bot.db.*;
import ru.bot.extension.Constants;
import ru.bot.logic.StartAbility;
import ru.bot.logic.StudentAbility;
import ru.bot.logic.TeacherAbility;

public class Bot extends AbilityBot {

    private StorageTeacher storageTeacher;
    private StorageStudent storageStudent;
    private StorageCourses storageCourses;
    private StorageTasks storageTasks;
    private StorageProgress storageProgress;
    private StorageContext storageContext;
    private StorageCreate storageCreate;
    private UserStatus userStatus;

    Bot(DefaultBotOptions botOptions) {
        super(Constants.TOKEN, Constants.BOT_USERNAME, botOptions);
    }

    public AbilityExtension ability() {
        dbInit();
        return new StartAbility(silent, userStatus);
    }

    public AbilityExtension abilityT() {
        dbInit();
        return new TeacherAbility(sender, silent, storageTeacher, storageStudent, storageCourses, storageTasks,
                storageProgress, storageContext, storageCreate, userStatus);
    }

    public AbilityExtension abilityS() {
        dbInit();
        return new StudentAbility(silent, storageStudent, storageCourses, storageTasks, storageProgress, storageContext,
                userStatus);
    }

    private void dbInit() {
        if (userStatus==null) {
            storageTeacher = new StorageTeacher(db);
            storageStudent = new StorageStudent(db);
            storageCourses = new StorageCourses(db);
            storageTasks = new StorageTasks(db);
            storageProgress = new StorageProgress(db);
            storageContext = new StorageContext(db);
            storageCreate = new StorageCreate(db);
            userStatus = new UserStatus(db);
        }
    }

    @Override
    public int creatorId() {
        return 0;
    }
    @Override
    public String getBotUsername() {
        return Constants.BOT_USERNAME;
    }
    @Override
    public String getBotToken() {
        return Constants.TOKEN;
    }
}
