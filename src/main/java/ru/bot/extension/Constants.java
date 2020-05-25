package ru.bot.extension;

import java.util.Calendar;
import java.util.Map;

public class Constants {

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

    //keyboard
    public static String[][] startKeyboardTeacher = {{"�������� ����", "���������� �����"},
            {"�������� �������", "������"}};

    public static String[][] courseKeyboardTeacher = {{"�������� �������", "���������� �������"},
            {"������", "������"},
            {"�������� ����", "������� ����"}};
    public static String[][] taskKeyboardTeacher = {{"�������� �������", "������� �������"}};
    public static String[][] studentTaskKeyboardTeacher = {{"����� �������", "�������� ������"},
            {"�������� �����������", "�������"}};

    //initialization
    public static String BOT_USERNAME = "YourDiaryBot";
    public static String TOKEN = System.getenv("BOT_TOKEN");

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
