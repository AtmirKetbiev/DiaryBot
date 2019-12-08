import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;

public class Keyboard extends AbilityBot {
    private ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
    private static final String BOT_USERNAME = "YourDiaryBot";
    private static String TOKEN = System.getenv("BOT_TOKEN");

    protected Keyboard (DefaultBotOptions botOptions) {
        super(BOT_USERNAME, TOKEN, botOptions);
    }

    @Override
    public int creatorId() {
        return 0;
    }

    private SendMessage addKeyboard (String[] text, Update update) {
        SendMessage sendMessage = new SendMessage().setChatId(update.getMessage().getChatId());
        ArrayList<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();

        keyboard.clear();
        keyboardRow.clear();

        for (String s : text) {
            keyboardRow.add(s);
            keyboard.add(keyboardRow);
            keyboardRow = new KeyboardRow();
        }
        replyKeyboardMarkup.setKeyboard(keyboard);
        sendMessage.setText("select");
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        return  sendMessage;
    }
}
