import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.util.AbilityExtension;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import ru.bot.extension.Constants;
import ru.bot.logic.StudentAbility;
import ru.bot.logic.TeacherAbility;

public class Bot extends AbilityBot {

    Bot(DefaultBotOptions botOptions) {
        super(Constants.TOKEN, Constants.BOT_USERNAME, botOptions);
    }

    public AbilityExtension ability() {
                return new TeacherAbility(silent, db);
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
