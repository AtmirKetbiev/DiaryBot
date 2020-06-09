import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.util.AbilityExtension;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import ru.bot.db.*;
import ru.bot.extension.Constants;
import ru.bot.logic.StartAbility;
import ru.bot.logic.StudentAbility;
import ru.bot.logic.TeacherAbility;

public class Bot extends AbilityBot {

    private UserStatus userStatus = new UserStatus(db);

    Bot(DefaultBotOptions botOptions) {
        super(Constants.TOKEN, Constants.BOT_USERNAME, botOptions);
    }

    public AbilityExtension ability() {
        return new StartAbility(silent, db);
    }

    public AbilityExtension abilityT() {
        return new TeacherAbility(sender, silent, db);
    }

    public AbilityExtension abilityS() {
        return new StudentAbility(silent, db);
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