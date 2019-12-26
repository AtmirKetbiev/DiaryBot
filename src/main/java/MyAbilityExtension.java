import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.abilitybots.api.objects.*;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.abilitybots.api.util.AbilityExtension;

import java.util.Arrays;
import java.util.Map;

public class MyAbilityExtension implements AbilityExtension {
    private SilentSender silent;
    private DBContext db;
    private String answer = "";
    private String flagCourse = "";

    MyAbilityExtension(SilentSender silent, DBContext db) {
        this.silent = silent;
        this.db = db;
    }


    public Reply keyboard() {
        Course course = new Course(db);
        Task task = new Task(db);
        //Bot.flagT = "";
        return Reply.of(update -> {
            Bot.flagT = "";
            switch (update.getMessage().getText()) {
                case ("/start"):
                    silent.execute(Keyboard.addKeyboard(new String[]{"Teacher", "Student"}, update, Constants.answerKeyboard.SELECT.toString()));
                    break;

                /**����� ������������*/
                case ("Teacher"):
                    Bot.user = "Teacher";
                    //silent.send("Teacher:", update.getMessage().getChatId());
                    silent.execute(Keyboard.addKeyboard(new String[]{"add course", "view course", "delete course"}, update, answer));
                    break;
                case ("Student"):
                    Bot.user = "Student";
                    answer = "��������:";
                    silent.execute(Keyboard.addKeyboard(new String[]{"view course", "timetable"}, update, answer));
                    break;

                /**�������������� ����� ��������������*/
                case ("add course"):
                    silent.send("�������� ��������:", update.getMessage().getChatId());
                    Bot.flagT = "addCourse";
                    break;
                case ("view course"):
                    answer = "���� �����:";
                    silent.execute(Keyboard.addKeyboard(course.get(), update, answer));
                    break;
                case ("delete course"):
                    course.remove();
                    silent.send("����� �������!", update.getMessage().getChatId());
                    break;

                /**�������������� �������������� �������*/
                case ("add task"):
                    silent.send("�������� ��������:", update.getMessage().getChatId());
                    Bot.flagT = "addTask";
                    break;
                case ("view tasks"):
                    answer = "������ �������:";

                    silent.execute(Keyboard.addKeyboard(task.get(flagCourse), update, answer));
                    silent.send(flagCourse, update.getMessage().getChatId());
                    break;

                /**�������� ���������� ���������*/
                case ("timetable"):
                    silent.execute(Keyboard.addKeyboard(new String[]{"Today ", "Tomorrow", "For a week", "back"}, update, answer));
                    break;
                case ("Today"):
                    silent.send(Timetable.timetable(Constants.day), update.getMessage().getChatId());
                    break;
                case ("Tomorrow"):
                    silent.send(Timetable.timetable(Constants.day + 1), update.getMessage().getChatId());
                    break;
                case ("For a week"):
                    silent.send(Timetable.timetable(0), update.getMessage().getChatId());
                    break;

                /**�������*/
                case ("back"):
                    if (Bot.user.equals("Teacher")) {
                        silent.execute(Keyboard.addKeyboard(new String[]{"add course", "view course", "delete course"}, update, "select"));
                    } else if (Bot.user.equals("Student")) {
                        silent.execute(Keyboard.addKeyboard(new String[]{"view course", "view tasks", "timetable"}, update, "select"));
                    } else {
                        silent.execute(Keyboard.addKeyboard(new String[]{"Teacher ", "Student"}, update, answer));
                    }
                    break;
                default:
                    silent.execute(Keyboard.addKeyboard(new String[]{"Teacher ", "Student"}, update, answer));
            }
        }, update -> Arrays.asList(Constants.keyboardWords).contains(update.getMessage().getText()));
    }

    public Reply viewCourse() {
        Bot.flagT = "";
        Course course = new Course(db);
        return Reply.of(update -> {
            Bot.flagT = "";
            flagCourse = update.getMessage().getText();
            if (Bot.user.equals("Teacher")) {

                silent.execute(Keyboard.addKeyboard(new String[]{"add task", "view tasks", "delete course"}, update, answer));
            } else if (Bot.user.equals("Student")){
                Task task = new Task(db);
                answer = "������ �������:";
                silent.execute(Keyboard.addKeyboard(task.get(flagCourse), update, answer));
            }
        }, update -> Arrays.toString(course.get()).contains(update.getMessage().getText()));
    }

    /**
     * ��������� ���� ��������
     * */
    public Reply addCourse() {
        return Reply.of(update -> {
            if (Bot.flagT.equals("addCourse")) {
                String text = update.getMessage().getText();
                Course course = new Course(db);
                course.add(text);
                silent.send("���������", update.getMessage().getChatId());
            }
            Bot.flagT = "";
        }, update -> Bot.flagT.equals("addCourse"));
    }

    public Reply addTask() {
        return Reply.of(update -> {
            if (Bot.flagT.equals("addTask")) {
                String text = update.getMessage().getText();
                Task task = new Task(db);
                task.add(flagCourse, text);
                silent.send("���������", update.getMessage().getChatId());
            }
            Bot.flagT = "";
        }, update -> Bot.flagT.equals("addTask"));
    }

    /**
     * �������� ���� �������� �� ���� ��
     */
    public Reply delete() {
        return Reply.of(update -> {
            Task task = new Task(db);
            task.remove();
            Course course = new Course(db);
            course.remove();
            silent.send("delete All", update.getMessage().getChatId());
        }, update -> update.getMessage().getText().equals("/del"));
    }

    /**
     * ��������� ���� �������� �� � ���������
     */
    public Reply allTasks() {
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
    }
}