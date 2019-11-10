import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;

public class Bot extends TelegramLongPollingBot {

    private static final String BOT_USERNAME = "YourDiaryBot";
    private String TOKEN = System.getenv("BOT_TOKEN");
    private ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
    private String user;

    Bot(DefaultBotOptions botOptions) {
        super(botOptions);
    }

    @Override
    public void onUpdateReceived(Update update) {
        long chat_id = update.getMessage().getChatId();
        SendMessage sendMessage = new SendMessage().setChatId(update.getMessage().getChatId());
        String text = update.getMessage().getText();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        try {
            sendMessage.setText(getMessage(text));
            execute(sendMessage);
        } catch (TelegramApiException | IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private String getMessage(String msg) throws TelegramApiException, IOException, URISyntaxException {
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_WEEK);
        ArrayList<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();

        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        keyboard.clear();
        keyboardRow.clear();

        switch (msg) {
            case ("/start"):
                addKeyboard(new String[]{"Teacher  \uD83D\uDC68\u200D\uD83C\uDFEB ", "Student \uD83D\uDC68\u200D\uD83C\uDF93"});
                return "Choose who you are";

            case ("Teacher  \uD83D\uDC68\u200D\uD83C\uDFEB"):
                user = "Teacher";
                addKeyboard(new String[]{"Course  \uD83D\uDCDA", "Groups  \uD83D\uDC65"});
                return "Select..";

            case ("Course  \uD83D\uDCDA"):
                addKeyboard(new String[]{ "Add task", "List of course", "Add course", "back"});
                return "Select..";
            case ("Groups  \uD83D\uDC65"):
                addKeyboard(new String[]{"Choose group", "Edit group", "back"});
                return "Select..";

            case ("Student \uD83D\uDC68\u200D\uD83C\uDF93"):
                user = "Student";
                addKeyboard(new String[]{"Timetable", "My course \uD83D\uDCDA", "My tasks", "Go to Moodle", "Repositories"});
                return "Select..";

            case ("Timetable"):
                addKeyboard(new String[]{"Today", "Tomorrow", "For a week", "back"});
                return "Select..";
            case ("Today"):
                return Timetable.timetable(day);
            case ("Tomorrow"):
                return Timetable.timetable(day+1);
            case ("For a week"):
                return Timetable.timetable(0);
            case ("My tasks"):
                addKeyboard(new String[]{"Course Information  \uD83D\uDCDA", "View performance", "My tasks", "back"});
                return "Select..";
            case ("My course  \uD83D\uDCDA"):
                addKeyboard(new String[]{"Mark done", "Upload file"});
                return "Select..";
            case ("Go to Moodle"):
                String url = "https://moodle.spbgasu.ru/";
                java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
                return "under developing";
            case ("Repositories"):
                return "under developing";
            case ("back"):
                if (user.equals("Teacher")) {
                    addKeyboard(new String[]{"Course  \uD83D\uDCDA", "Groups  \uD83D\uDC65"});
                } else if (user.equals("Student")) {
                    addKeyboard(new String[]{"Timetable", "My course \uD83D\uDCDA", "My tasks", "Go to Moodle", "Repositories"});
                } else {
                    addKeyboard(new String[]{"Teacher  \uD83D\uDC68\u200D\uD83C\uDFEB ", "Student \uD83D\uDC68\u200D\uD83C\uDF93"});
                }
                return "Select..";
            default:
                return "under development..";
        }
    }

    private void addKeyboard (String[] text ) {
        ArrayList<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();
        for (String s : text) {
            keyboardRow.add(s);
            keyboard.add(keyboardRow);
            keyboardRow = new KeyboardRow();
        }
        replyKeyboardMarkup.setKeyboard(keyboard);
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
