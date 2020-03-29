import java.util.Calendar;
import java.util.Map;

public interface Constants {

    enum answerKeyboard {
        SELECT("Select");

        String select;

        answerKeyboard(String select) {
            this.select = select;
        }

        @Override
        public String toString() {
            return select;
        }
    }

    //initialization
    String BOT_USERNAME = "YourDiaryBot";
    String TOKEN = System.getenv("BOT_TOKEN");

    //reserved buttons
    String[] keyboardWords = new String[]{
            "/start",
            "ru.bot.diary.objects.Teacher",
            "add course",
            "view course",
            "edit course",
            "delete course",
            "add task",
            "view tasks",
            "edit task",
            "delete task",
            "view group",
            "ru.bot.diary.objects.Student",
            "repositories",
            "timetable",
            "today",
            "Tomorrow",
            "week",
            "add repositories",
            "course",
            "sign up",
            "my course",
            "mark",
            "unmark",
            "Today",
            "For a week",
            "Today",
            "back"
    };

    //calendar
    Calendar c = Calendar.getInstance();
    int day = c.get(Calendar.DAY_OF_WEEK);
}
