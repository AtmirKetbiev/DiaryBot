package ru.bot.extension;

import java.util.Calendar;

public class Constants {

    //keyboard
    public static String[][] startKeyboardTeacher = {{"Добавить курс", "Посмотреть курсы"},
            {"Изменить профиль", "Помощь"}};

    public static String[][] courseKeyboardTeacher = {{"Добавить задание", "Посмотреть задания"},
            {"Группы", "Ссылки"},
            {"Изменить курс", "Удалить курс"}};
    public static String[][] taskKeyboardTeacher = {{"Изменить задание", "Удалить задание"}};
    public static String[][] studentTaskKeyboardTeacher = {{"Снять отметку", "Изменить оценку"},
            {"Добавить комментарий", "История"}};

    //initialization
    public static String BOT_USERNAME = "YourDiaryBot";
    public static String TOKEN = System.getenv("BOT_TOKEN");
    public static String TEACHER = "Преподаватель";
    public static String STUDENT = "Студент";

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
