import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.*;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import java.util.ArrayList;
import java.util.Arrays;

public class Bot extends AbilityBot implements Constants {

    private ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
    private String user = "";

    Bot(DefaultBotOptions botOptions) {
        super(BOT_USERNAME, TOKEN, botOptions);
    }

    public Reply keyboard() {
        return Reply.of(update -> {
            String answer = "select";
            switch (update.getMessage().getText()) {
                case ("/start"):
                    silent.execute(addKeyboard(new String[]{"Teacher", "Student"}, update, answerKeyboard.SELECT.toString()));
                    break;
                case ("Teacher"):
                    user = "Teacher";
                    answer = "Просит ФИО, кафедру и основную информацию";
                    silent.execute(addKeyboard(new String[]{"add course", "view course"}, update, answer));
                    break;
                case ("view course"):
                    answer = "Ваши курсы:";
                    silent.execute(addKeyboard(courses, update, answer));
                    break;
                case ("add course"):
                case ("edit course"):
                    silent.send("Просит ввести основную информацию о курсе", update.getMessage().getChatId());
                    break;
                case ("delete course"):
                    silent.send("Курс удален", update.getMessage().getChatId());
                    break;
                case ("add task"):
                case ("edit task"):
                    answer = "Просит ввести основную информацию о задании: Название, описание, deadline";
                    silent.send(answer, update.getMessage().getChatId());
                    break;
                case ("view tasks"):
                    answer = "Список заданий:";
                    silent.execute(addKeyboard(tasks, update, answer));
                    break;
                case ("view group"):
                    answer = "Список групп подписанных на ваш курс:";
                    silent.execute(addKeyboard(group, update, answer));
                    break;
                case ("delete task"):
                    silent.send("Задание удалено", update.getMessage().getChatId());
                    break;

                case ("Student"):
                    user = "Student";
                    answer = "Выберете вашу группу:";
                    silent.execute(addKeyboard(group, update, answer));
                    break;
                case ("course"):
                    silent.execute(addKeyboard(new String[]{"sign up ", "my course"}, update, answer));
                    break;
                case ("sign up"):
                    answer = "Выберете курс на который вы хотите подписаться:";
                    silent.execute(addKeyboard(courses, update, answer));
                    break;
                case ("my course"):
                    silent.execute(addKeyboard(courses, update, answer));
                    break;
                case ("timetable"):
                    silent.execute(addKeyboard(new String[]{"Today ", "Tomorrow", "For a week", "back"}, update, answer));
                    break;
                case ("Today"):
                    silent.send(Timetable.timetable(day), update.getMessage().getChatId());
                    break;
                case ("Tomorrow"):
                    silent.send(Timetable.timetable(day + 1), update.getMessage().getChatId());
                    break;
                case ("For a week"):
                    silent.send(Timetable.timetable(0), update.getMessage().getChatId());
                    break;
                case ("repositories"):
                    silent.execute(addKeyboard(repositories, update, answer));
                    break;
                case ("mark"):
                    silent.send("Задание отмечено как сделанное:", update.getMessage().getChatId());
                    break;
                case ("unmark"):
                    silent.send("Задание отмечено как не сделанное:", update.getMessage().getChatId());
                    break;
                case ("back"):
                    if (user.equals("Teacher")) {
                        silent.execute(addKeyboard(new String[]{"add course", "view course"}, update, "select"));
                    } else if (user.equals("Student")) {
                        silent.execute(addKeyboard(new String[]{"timetable ", "repositories", "course"}, update, "select"));
                    } else {
                        silent.execute(addKeyboard(new String[]{"Teacher ", "Student"}, update, answer));
                    }
                    break;
                default:
                    silent.execute(addKeyboard(new String[]{"Teacher ", "Student"}, update, answer));
            }
        }, update -> Arrays.asList(keyboardWords).contains(update.getMessage().getText()));
    }

    public Reply courseList() {
        return Reply.of(update -> {
            String answer = "Основная информация о курсе";
            if (user.equals("Teacher")) {
                silent.execute(addKeyboard(new String[]{"edit course ", "delete course", "add task", "view tasks", "view group", "back"}, update, answer));
            } else {
                silent.execute(addKeyboard(tasks, update, answer));
            }
        }, update -> Arrays.asList(courses).contains(update.getMessage().getText()));
    }

    public Reply taskList() {
        return Reply.of(update -> {
            if (user.equals("Teacher")) {
                silent.execute(addKeyboard(new String[]{"edit task ", "delete task", "back"}, update, "select"));
            } else {
                silent.execute(addKeyboard(new String[]{"mark ", "unmark", "back"}, update, "select"));
            }
        }, update -> Arrays.asList(tasks).contains(update.getMessage().getText()));
    }

    public Reply groupList() {
        return Reply.of(update -> {
            if (user.equals("Teacher")) {
                silent.execute(addKeyboard(student, update, "select"));
            } else {
                silent.execute(addKeyboard(new String[]{"timetable ", "repositories", "course", "back"}, update, "select"));
            }
        }, update -> Arrays.asList(group).contains(update.getMessage().getText()));
    }

    public Reply studentList() {
        return Reply.of(update -> {
            silent.execute(addKeyboard(tasks, update, "select"));
        }, update -> Arrays.asList(student).contains(update.getMessage().getText()));
    }

    private SendMessage addKeyboard (String[] text, Update update, String answer) {
        SendMessage sendMessage = new SendMessage().setChatId(update.getMessage().getChatId());
        ArrayList<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();

        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        keyboard.clear();
        keyboardRow.clear();

        for (String s : text) {
            keyboardRow.add(s);
            keyboard.add(keyboardRow);
            keyboardRow = new KeyboardRow();
        }
        replyKeyboardMarkup.setKeyboard(keyboard);
        sendMessage.setText(answer);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        return  sendMessage;
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
