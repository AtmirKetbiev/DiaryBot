package ru.bot.extension;

import java.util.Calendar;

public class Constants {

    //keyboard
    public final static String[][] startKeyboardTeacher = {{"Добавить курс", "Посмотреть курсы"},
            {"Изменить профиль", "Помощь"}};

    public final static String[][] courseKeyboardTeacher = {{"Добавить задание", "Посмотреть задания"},
            {"Группы", "Ссылки"},
            {"Изменить курс", "Удалить курс"}};
    public final static String[][] taskKeyboardTeacher = {{"Изменить задание", "Удалить задание"}};
    public final static String[][] studentTaskKeyboardTeacher = {{"Снять отметку", "Изменить оценку"},
            {"Добавить комментарий", "История"}};

    //initialization
    public final static String BOT_USERNAME = "YourDiaryBot";
    public final static String TOKEN = System.getenv("BOT_TOKEN");
    public final static String TEACHER = "Преподаватель";
    public final static String STUDENT = "Студент";

    //reserved buttons
    String[] keyboardWords = new String[]{
            "/start",
            "ru.bot.diary.objects.ru.bot.objects.Teacher",
            "add course",
            "view course",
            "edit course",
            "delete course",
            "add task",
            "view tasks",
            "edit task",
            "delete task",
            "view group",
            "ru.bot.diary.objects.ru.bot.objects.Student",
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
    private static Calendar c = Calendar.getInstance();
    public static int day = c.get(Calendar.DAY_OF_WEEK);
}
