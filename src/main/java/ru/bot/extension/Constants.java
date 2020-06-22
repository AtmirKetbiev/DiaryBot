package ru.bot.extension;

public class Constants {

    //keyboard
    public final static String[][] startKeyboardTeacher = {{"Добавить курс", "Посмотреть курсы"},
            {"Изменить профиль", "Помощь"}};
    public final static String[][] startKeyboardStudent = {{"Расписание", "Курсы"},
            {"Подписаться на курс", "Помощь"}};

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

}
