import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.util.AbilityExtension;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import ru.bot.extension.TeacherAbilityExtension;
import ru.bot.logic.TeacherAbility;

public class Bot extends AbilityBot implements Constants {

    Bot(DefaultBotOptions botOptions) {

        super(TOKEN, BOT_USERNAME, botOptions);
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
        return BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return TOKEN;
    }
}
