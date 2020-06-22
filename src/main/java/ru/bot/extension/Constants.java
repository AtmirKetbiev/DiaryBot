package ru.bot.extension;

public class Constants {

    //keyboard
    public final static String[][] startKeyboardTeacher = {{"�������� ����", "���������� �����"},
            {"�������� �������", "������"}};
    public final static String[][] startKeyboardStudent = {{"����������", "�����"},
            {"����������� �� ����", "������"}};

    public final static String[][] courseKeyboardTeacher = {{"�������� �������", "���������� �������"},
            {"������", "������"},
            {"�������� ����", "������� ����"}};
    public final static String[][] taskKeyboardTeacher = {{"�������� �������", "������� �������"}};
    public final static String[][] studentTaskKeyboardTeacher = {{"����� �������", "�������� ������"},
            {"�������� �����������", "�������"}};

    //initialization
    public final static String BOT_USERNAME = "YourDiaryBot";
    public final static String TOKEN = System.getenv("BOT_TOKEN");
    public final static String TEACHER = "�������������";
    public final static String STUDENT = "�������";

}
