package ru.bot.logic;

import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.abilitybots.api.objects.Flag;
import org.telegram.abilitybots.api.objects.Reply;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.abilitybots.api.util.AbilityExtension;
import ru.bot.DB.UserStatus;
import ru.bot.extension.Constants;
import ru.bot.extension.Keyboard;

import java.util.Arrays;

public class StartAbility implements AbilityExtension {

    private SilentSender silent;
    private UserStatus userStatus;
    private ContextAnswer contextAnswer = new ContextAnswer();

    public StartAbility(SilentSender silent, DBContext db) {
        this.silent = silent;
        this.userStatus = new UserStatus(db);
    }

    public Reply start() {
        return Reply.of(update -> {
            Long id = update.getMessage().getChatId();

            userStatus.remove(id);

            contextAnswer.setAnswer("��� ��?!");
            contextAnswer.setButtonsList(Arrays.asList("�������������", "�������"));

            silent.execute(Keyboard.listKeyboard(contextAnswer.getButtonsList(), update, contextAnswer.getAnswer()));
        }, update -> update.getMessage().getText().equals("/start"));
    }

    public Reply status() {
        return Reply.of(update -> {
            Long id = update.getMessage().getChatId();
            String text = update.getMessage().getText();

            if (text.equals("�������������")) {
                userStatus.setUserStatus(id, Constants.TEACHER);
            } else if (text.equals("�������")) {
                userStatus.setUserStatus(id, Constants.STUDENT);
            }
        }, Flag.TEXT);
    }
}