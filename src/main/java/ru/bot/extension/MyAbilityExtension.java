package ru.bot.extension;

import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.abilitybots.api.objects.*;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.abilitybots.api.util.AbilityExtension;

public class MyAbilityExtension implements AbilityExtension {
    private SilentSender silent;
    private DBContext db;
    private String answer = "";
    private String flagCourse = "";

    MyAbilityExtension(SilentSender silent, DBContext db) {
        this.silent = silent;
        this.db = db;
    }

    /*public Reply start() {
        return Reply.of(update -> {
            answer = "Выберете:";
            silent.execute(Keyboard.addKeyboard(new String[]{"add course", "view course", "delete course"}, update, answer));
        }, update -> update.getMessage().getText().equals("/start"));
    }*/



//    public Reply keyboard() {
//        ru.bot.diary.objects.ru.bot.objects.Course course = new ru.bot.diary.objects.ru.bot.objects.Course(db);
//        ru.bot.diary.objects.ru.bot.objects.Task task = new ru.bot.diary.objects.ru.bot.objects.Task(db);
//        return Reply.of(update -> {
//            Bot.flagT = "";
//            switch (update.getMessage().getText()) {
//                case ("/start"):
//                    silent.execute(ru.bot.extension.Keyboard.addKeyboard(new String[]{"ru.bot.diary.objects.ru.bot.objects.Teacher", "ru.bot.diary.objects.ru.bot.objects.Student"}, update, Constants.answerKeyboard.SELECT.toString()));
//                    break;
//
//                //**Выбор пользователя*//*
//                case ("ru.bot.diary.objects.ru.bot.objects.Teacher"):
//                    Bot.user = "ru.bot.diary.objects.ru.bot.objects.Teacher";
//                    //silent.send("ru.bot.diary.objects.ru.bot.objects.Teacher:", update.getMessage().getChatId());
//                    silent.execute(ru.bot.extension.Keyboard.addKeyboard(new String[]{"add course", "view course", "delete course"}, update, answer));
//                    break;
//                case ("ru.bot.diary.objects.ru.bot.objects.Student"):
//                    Bot.user = "ru.bot.diary.objects.ru.bot.objects.Student";
//                    answer = "Выберете:";
//                    silent.execute(ru.bot.extension.Keyboard.addKeyboard(new String[]{"view course", "timetable"}, update, answer));
//                    break;
//
//                //**Редактирование курса преподователем*//*
//                case ("add course"):
//                    silent.send("Напишите название:", update.getMessage().getChatId());
//                    Bot.flagT = "addCourse";
//                    break;
//                case ("view course"):
//                    answer = "Ваши курсы:";
//                    silent.execute(ru.bot.extension.Keyboard.addKeyboard(course.get(), update, answer));
//                    break;
//                case ("delete course"):
//                    course.remove();
//                    silent.send("Курсы удалены!", update.getMessage().getChatId());
//                    break;
//
//                *//**Редактирование преподователем заданий*//*
//                case ("add task"):
//                    silent.send("Напишите название:", update.getMessage().getChatId());
//                    Bot.flagT = "addTask";
//                    break;
//                case ("view tasks"):
//                    answer = "Список заданий:";
//
//                    silent.execute(ru.bot.extension.Keyboard.addKeyboard(task.get(flagCourse), update, answer));
//                    silent.send(flagCourse, update.getMessage().getChatId());
//                    break;
//
//                *//**Просмотр расписания студентом*//*
//                case ("timetable"):
//                    silent.execute(ru.bot.extension.Keyboard.addKeyboard(new String[]{"Today ", "Tomorrow", "For a week", "back"}, update, answer));
//                    break;
//                case ("Today"):
//                    silent.send(ru.bot.extension.Timetable.timetable(Constants.day), update.getMessage().getChatId());
//                    break;
//                case ("Tomorrow"):
//                    silent.send(ru.bot.extension.Timetable.timetable(Constants.day + 1), update.getMessage().getChatId());
//                    break;
//                case ("For a week"):
//                    silent.send(ru.bot.extension.Timetable.timetable(0), update.getMessage().getChatId());
//                    break;
//
//                //**Возврат*//*
//                case ("back"):
//                    if (Bot.user.equals("ru.bot.diary.objects.ru.bot.objects.Teacher")) {
//                        silent.execute(ru.bot.extension.Keyboard.addKeyboard(new String[]{"add course", "view course", "delete course"}, update, "select"));
//                    } else if (Bot.user.equals("ru.bot.diary.objects.ru.bot.objects.Student")) {
//                        silent.execute(ru.bot.extension.Keyboard.addKeyboard(new String[]{"view course", "view tasks", "timetable"}, update, "select"));
//                    } else {
//                        silent.execute(ru.bot.extension.Keyboard.addKeyboard(new String[]{"ru.bot.diary.objects.ru.bot.objects.Teacher ", "ru.bot.diary.objects.ru.bot.objects.Student"}, update, answer));
//                    }
//                    break;
//                default:
//                    silent.execute(ru.bot.extension.Keyboard.addKeyboard(new String[]{"ru.bot.diary.objects.ru.bot.objects.Teacher ", "ru.bot.diary.objects.ru.bot.objects.Student"}, update, answer));
//            }
//        }, update -> Arrays.asList(Constants.keyboardWords).contains(update.getMessage().getText()));
//    }

    /*public Reply viewCourse() {
        Bot.flagT = "";
        ru.bot.diary.objects.ru.bot.objects.Course course = new ru.bot.diary.objects.ru.bot.objects.Course(db);
        return Reply.of(update -> {
            Bot.flagT = "";
            flagCourse = update.getMessage().getText();
            if (Bot.user.equals("ru.bot.diary.objects.ru.bot.objects.Teacher")) {

                silent.execute(ru.bot.extension.Keyboard.addKeyboard(new String[]{"add task", "view tasks", "delete course"}, update, answer));
            } else if (Bot.user.equals("ru.bot.diary.objects.ru.bot.objects.Student")){
                ru.bot.diary.objects.ru.bot.objects.Task task = new ru.bot.diary.objects.ru.bot.objects.Task(db);
                answer = "Список заданий:";
                silent.execute(ru.bot.extension.Keyboard.addKeyboard(task.get(flagCourse), update, answer));
            }
        }, update -> Arrays.toString(course.get()).contains(update.getMessage().getText()));
    }*/

    /**
     Ожидается ввод значения
     */
//    public Reply addCourse() {
//        return Reply.of(update -> {
//            if (Bot.flagT.equals("addCourse")) {
//                String text = update.getMessage().getText();
//                ru.bot.diary.objects.ru.bot.objects.Course course = new ru.bot.diary.objects.ru.bot.objects.Course(db);
//                course.add(text);
//                silent.send("Добавлено", update.getMessage().getChatId());
//            }
//            Bot.flagT = "";
//        }, update -> Bot.flagT.equals("addCourse"));
//    }

//    public Reply addTask() {
//        return Reply.of(update -> {
//            if (Bot.flagT.equals("addTask")) {
//                String text = update.getMessage().getText();
//                ru.bot.diary.objects.ru.bot.objects.Task task = new ru.bot.diary.objects.ru.bot.objects.Task(db);
//                task.add(flagCourse, text);
//                silent.send("Добавлено", update.getMessage().getChatId());
//            }
//            Bot.flagT = "";
//        }, update -> Bot.flagT.equals("addTask"));
//    }

    /**
     * Удаление всех значений из всех бд
     */
    /*public Reply delete() {
        return Reply.of(update -> {
            ru.bot.diary.objects.ru.bot.objects.Task task = new ru.bot.diary.objects.ru.bot.objects.Task(db);
            task.remove();
            ru.bot.diary.objects.ru.bot.objects.Course course = new ru.bot.diary.objects.ru.bot.objects.Course(db);
            course.remove();
            silent.send("delete All", update.getMessage().getChatId());
        }, update -> update.getMessage().getText().equals("/del"));
    }*/

    /**
     * Получение всех значений бд с заданиями
     */
    /*public Reply allTasks() {
        return Reply.of(update -> {
            Map<Integer, String[]> taskMap;
            taskMap = db.getMap("Tasks");
            String[] myArrayTask1 = new String[taskMap.size()];
            String[] myArrayTask2 = new String[taskMap.size()];
            for (int i : taskMap.keySet()) {
                myArrayTask1[i] = taskMap.get(i)[0];
                myArrayTask2[i] = taskMap.get(i)[1];
            }
            silent.send(Arrays.toString(myArrayTask1), update.getMessage().getChatId());
            silent.send(Arrays.toString(myArrayTask2), update.getMessage().getChatId());
        }, update -> update.getMessage().getText().equals("/all"));
    }*/
}