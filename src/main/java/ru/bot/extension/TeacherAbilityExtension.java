package ru.bot.extension;

import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.abilitybots.api.objects.Reply;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.abilitybots.api.util.AbilityExtension;
import ru.bot.extension.DBManager;
import ru.bot.extension.Keyboard;
import ru.bot.objects.Course;
import ru.bot.objects.Status;
import ru.bot.objects.Task;
import ru.bot.objects.Teacher;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class TeacherAbilityExtension implements AbilityExtension {

    private String answer = "";
    private SilentSender silent;
    private DBManager db;

    public TeacherAbilityExtension(SilentSender silent, DBContext db) {

        this.silent = silent;
        DBManager ddb = new DBManager(db);
        this.db = ddb;
    }

    /*...............................................................................................................*/
    /*����� �������������*/
    /*public Reply start() {
        return Reply.of(update -> {
            Long id = update.getMessage().getChatId();
            Teacher teacher = new Teacher();
            teacher.setName("������");
            db.addTeacher(id, teacher);
            answer = "��������: add course";
            silent.execute(Keyboard.addKeyboard(new String[]{"�������� ����", "���������� �����", "�������� �������", "������"}, update, answer));
        }, update -> update.getMessage().getText().equals("/start"));
    }

    public Reply back() {
        return Reply.of(update -> {
            Long id = update.getMessage().getChatId();
            Status status = db.getUserStatus(id);
            if (status.getIdTask() == -1 && status.getIdCourse() == -1) {
                status.setIdCourse(-1);
                silent.execute(Keyboard.addKeyboard(new String[]{"�������� ����", "���������� �����", "�������� �������", "������"}, update, answer));
            }
            if (status.getIdTask() == -1 && status.getIdCourse() != -1) {
                status.setIdCourse(-1);
                db.addUserStatus(id, status);
                String[] myArray = getCourse(id);
                answer = "���� �����:";
                silent.execute(Keyboard.addKeyboard(myArray, update, answer));
            }
            if (status.getIdTask() != -1) {
                status.setIdTask(-1);
                db.addUserStatus(id, status);
                String[] myArray = getTask(id);
                answer = "���� �������:";
                silent.execute(Keyboard.addKeyboard(myArray, update, answer));
            }
        }, update -> update.getMessage().getText().equals("�����"));
    }
*/
    /*...����........................................................................................................*/
   /* public Reply course() {
        return Reply.of(update -> {
            Long id = update.getMessage().getChatId();
            String text = update.getMessage().getText();
            Status status = new Status();
            status.setIdCourse(db.getIdByNameCourse(text, id));
            db.addUserStatus(id, status);
            answer = db.getCourse(status.getIdCourse()).getName() + "\n\n" + db.getCourse(status.getIdCourse()).getDescription();
            silent.execute(Keyboard.addKeyboard(new String[]{"�������� �������", "���������� �������", "������", "������", "�������� ����", "������� ����"}, update, answer));
        }, update -> Arrays.stream(getCourse(update.getMessage().getChatId())).anyMatch(update.getMessage().getText()::equals));
    }

    public Reply viewCourse() {
        return Reply.of(update -> {
            Long id = update.getMessage().getChatId();
            String[] myArray = getCourse(id);
            answer = "���� �����:";
            silent.execute(Keyboard.addKeyboard(myArray, update, answer));
        }, update -> update.getMessage().getText().equals("���������� �����"));
    }
*/
    /*public String[] getCourse(Long id) {
        Teacher teacher = db.getTeacher(id);
        Map<Integer, Course>  course = db.getCourseMap();
        int q = 0;
        String[] myArray = new String[teacher.getCourses().size()];
        for (int i : teacher.getCourses()) {
            myArray[q] = course.get(i).getName();
            q++;
        }
        return myArray;
    }

    public Reply addCourse() {
        return Reply.of(update -> {
            Long id = update.getMessage().getChatId();
            Course course = new Course();
            course.setIdTeacher(id);
            db.setCreateCourse(course);
            silent.send("������� ��������", update.getMessage().getChatId());
        }, update -> (update.getMessage().getText().equals("�������� ����")
                || update.getMessage().getText().equals("�������� ����")));
    }

    public Reply addNextCourse() {
        return Reply.of(update -> {
            Long id = update.getMessage().getChatId();
            Course course = db.getCreateCourse().get(id);
            if(update.getMessage().getText().equals("�����")) {
                silent.send("�������������� ����� ��������", id);
                db.removeCreateCourse(id);
            } else {
                if (course.getName() == null) {
                    silent.send("��������", id);
                    course.setName(update.getMessage().getText());
                } else if (course.getDescription() == null) {
                    course.setDescription(update.getMessage().getText());
                }
                if (course.check()) {
                    db.addCourseMap(course);
                    db.removeCreateCourse(id);
                    silent.send("�������", id);
                } else {
                    db.setCreateCourse(course);
                }
            }
        }, update -> db.getCreateCourse().get(update.getMessage().getChatId()) != null
                && !update.getMessage().getText().equals("�������� ����")
                && !update.getMessage().getText().equals("�������� ����"));
    }

    public Reply delCourse() {
        return Reply.of(update -> {
            Long id = update.getMessage().getChatId();
            int idCourse = db.getUserStatus().get(id).getIdCourse();
            db.removeCourse(idCourse, id);
            silent.send("���� ������!", id);

            Status status = db.getUserStatus(id);
            status.setIdCourse(-1);
            db.addUserStatus(id, status);
            String[] myArray = getCourse(id);
            answer = "���� �����:";
            silent.execute(Keyboard.addKeyboard(myArray, update, answer));
        }, update -> update.getMessage().getText().equals("������� ����"));
    }

    *//*...�������.....................................................................................................*//*
    public Reply task() {
        return Reply.of(update -> {
            Long id = update.getMessage().getChatId();
            String text = update.getMessage().getText();
            Status status = db.getUserStatus(id);
            status.setIdTask(db.getIdByNameTask(text, status.getIdCourse()));
            db.addUserStatus(id, status);
            //answer = "�� ������� �������" + text;
            if (status.getIdStudent() != -1) {
                silent.execute(Keyboard.addKeyboard(new String[]{"����� �������", "�������� ������", "�������� �����������"}, update, answer));
            } else {
                answer = db.getTask(status.getIdTask()).getName() + "\n" +
                        db.getTask(status.getIdTask()).getDescription() + "\n\nSoft dedline\n" +
                        db.getTask(status.getIdTask()).getSoft() + "\n\nHard dedline\n" +
                        db.getTask(status.getIdTask()).getHard() + "\n\n";
                silent.execute(Keyboard.addKeyboard(new String[]{"�������� �������", "������� �������"}, update, answer));
            }
        }, update -> Arrays.stream(getTask(update.getMessage().getChatId())).anyMatch(update.getMessage().getText()::equals));
    }

    public Reply viewTask() {
        return Reply.of(update -> {
            Long id = update.getMessage().getChatId();
            String[] myArray = getTask(id);
            answer = "���� �������:";
            silent.execute(Keyboard.addKeyboard(myArray, update, answer));
        }, update -> update.getMessage().getText().equals("���������� �������"));
    }

    public String[] getTask(Long id) {
        if (db.getUserStatus().get(id).getIdCourse() == -1) {
            return new String[0];
        }
        Map<Integer, Task> task = db.getTaskMap();
        Map<Integer, Course> course = db.getCourseMap();
        Course idCourse = course.get(db.getUserStatus().get(id).getIdCourse());
        List<Integer> idTask = idCourse.getIdTasks();
        int q = 0;
        String[] myArray;
        if (idTask != null) {
            myArray = new String[idTask.size()];

        } else {
            myArray = new String[0];
            return myArray;
        }
        for (int i : idTask) {
            myArray[q] = task.get(i).getName();
            q++;
        }
        return myArray;
    }

    public Reply addTask() {
        return Reply.of(update -> {
            Long id = update.getMessage().getChatId();
            Task task = new Task();
            task.setIdCourse(db.getUserStatus(id).getIdCourse());
            db.setCreateTask(id, task);
            silent.send("������� �������� �������", update.getMessage().getChatId());
        }, update -> (update.getMessage().getText().equals("�������� �������")
                || update.getMessage().getText().equals("�������� �������")));
    }

    public Reply addNextTask() {
        return Reply.of(update -> {
            Long id = update.getMessage().getChatId();
            Task task = db.getCreateTask().get(id);
            if(update.getMessage().getText().equals("�����")) {
                silent.send("�������������� ������� ��������", id);
                db.removeCreateTask(id);
            } else {
                if (task.getName() == null) {
                    silent.send("��������", id);
                    task.setName(update.getMessage().getText());
                } else if (task.getDescription() == null) {
                    task.setDescription(update.getMessage().getText());
                    silent.send("������� soft � ������� dd/mm/yyyy", id);
                } else if (task.getSoft() == null) {
                    try {
                        task.setSoft(update.getMessage().getText());
                        silent.send("������� hard � ������� dd/mm/yyyy", id);
                    } catch (ParseException e) {
                        silent.send("�� ���������� ����, ������� � ������� dd/mm/yyyy", id);
                        e.printStackTrace();
                    }
                } else if (task.getHard() == null) {
                    try {
                        task.setHard(update.getMessage().getText());
                    } catch (ParseException e) {
                        silent.send("�� ���������� ����, ������� � ������� dd/mm/yyyy", id);
                        e.printStackTrace();
                    }
                }
                if (task.check()) {
                    db.addTaskMap(task);
                    db.removeCreateTask(id);
                    silent.send("�������", id);
                } else {
                    db.setCreateTask(id, task);
                }
            }
        }, update ->
                db.getCreateTask().get(update.getMessage().getChatId()) != null
                        && !update.getMessage().getText().equals("�������� �������")
                        && !update.getMessage().getText().equals("�������� �������"));
    }

    public Reply delTask() {
        return Reply.of(update -> {
            Long id = update.getMessage().getChatId();
            Course course = db.getCourse(db.getUserStatus().get(id).getIdCourse());
            course.removeTask(db.getUserStatus().get(id).getIdTask());
            db.addCourseMap(course);
            silent.send("������� �������!", id);

            Status status = db.getUserStatus(id);
            status.setIdTask(-1);
            db.addUserStatus(id, status);
            String[] myArray = getTask(id);
            answer = "���� �������:";
            silent.execute(Keyboard.addKeyboard(myArray, update, answer));
        }, update -> update.getMessage().getText().equals("������� �������"));
    }
*/

    /*...������......................................................................................................*/
/*    public Reply viewGroup() {
        return Reply.of(update -> {
            Long id = update.getMessage().getChatId();
            String[] myArray = getGroup(id);
            answer = "���� �������:";
            silent.execute(ru.bot.extension.Keyboard.addKeyboard(myArray, update, answer));
        }, update -> update.getMessage().getText().equals("������"));
    }

    public String[] getGroup(Long id) {
        ru.bot.objects.Teacher teacher = db.getTeacher(id);
        int idCourse = db.getUserStatus().get(id).getIdCourse();
        ru.bot.objects.Course course = db.getCourse(idCourse);
        int q = 0;
        String[] myArray = course.getGroup().toArray(new String[0]);
        return myArray;
    }


    public Reply addGroup() {
        return Reply.of(update -> {
            Long id = update.getMessage().getChatId();
            int idCourse = db.getUserStatus().get(id).getIdCourse();
            db.putCreateAddGroupInCourse(id, idCourse);
            silent.send("������� ������", update.getMessage().getChatId());
        }, update -> update.getMessage().getText().startsWith("�������� ������"));
    }

    public Reply addGroupNext() {
        return Reply.of(update -> {
            if (db.getIdByNameGroup(update.getMessage().getText()) != -1) {
                Long id = update.getMessage().getChatId();
                int idCourse = db.getUserStatus().get(id).getIdCourse();
                ru.bot.objects.Course course = db.getCourse(idCourse);
                course.addGroup(update.getMessage().getText());
                db.addCourseMap(course);
                db.removeCreateAddGroupInCourse(id);
                silent.send(("������ ���������"), update.getMessage().getChatId());
            } else {
                silent.send(("����� ������ �� ������� " +
                        "������� �������� ������ ��� ��� ��� ������� " +
                        "�� ����������� ����� ����"), update.getMessage().getChatId());
            }
        }, update -> db.getCreateAddGroupInCourse().get(update.getMessage().getChatId()) != null);
    }

    public Reply groups() {
        return Reply.of(update -> {
            Long id = update.getMessage().getChatId();
            ru.bot.objects.Status status = db.getUserStatus(id);
            status.setIdGroup(db.getIdByNameGroup(update.getMessage().getText()));
            db.addUserStatus(id, status);
            answer = "������ " + update.getMessage().getText();
            silent.execute(ru.bot.extension.Keyboard.addKeyboard(new String[]{"����������", "���������"}, update, answer));
        }, update -> Arrays.stream(getGroup(update.getMessage().getChatId())).anyMatch(update.getMessage().getText()::equals));
    }*/


    /*...��������.....................................................................................................*/
//    public Reply student() {
//        return Reply.of(update -> {
//            Long id = update.getMessage().getChatId();
//            String text = update.getMessage().getText();
//            ru.bot.objects.Status status = db.getUserStatus(id);
//            status.setIdTask(db.getIdByNameTask(text, status.getIdCourse()));
//            db.addUserStatus(id, status);
//            //answer = "�� ������� �������" + text;
//            answer = db.getTask(status.getIdTask()).getName() + "\n" +
//                    db.getTask(status.getIdTask()).getDescription() + "\n\nSoft dedline\n" +
//                    db.getTask(status.getIdTask()).getSoft() + "\n\nHard dedline\n" +
//                    db.getTask(status.getIdTask()).getHard() + "\n\n";
//            silent.execute(ru.bot.extension.Keyboard.addKeyboard(new String[]{"�������� �������", "������� �������"}, update, answer));
//        }, update -> Arrays.stream(getStudent(update.getMessage().getChatId())).anyMatch(update.getMessage().getText()::equals));
//    }

    /*public Reply viewStudent() {
        return Reply.of(update -> {
            Long id = update.getMessage().getChatId();
            String[] myArray = getStudent(id);
            answer = "��������:";
            silent.execute(ru.bot.extension.Keyboard.addKeyboard(myArray, update, answer));
        }, update -> update.getMessage().getText().equals("���������"));
    }

    public String[] getStudent(Long id) {
        ru.bot.objects.Group group = db.getGroup(db.getUserStatus().get(id).getIdGroup());
        ru.bot.objects.Student student = new ru.bot.objects.Student();
        Map<Long, ru.bot.objects.Student> studentMap = db.getStudentMap();
        String array = null;
        if (db.getUserStatus().get(id).getIdCourse() != -1 && db.getUserStatus().get(id).getIdGroup() != -1) {

            for (Long idStudent : studentMap.keySet()) {
                if (studentMap.get(idStudent).getGroup().equals(group.getName())) {
                    array =+ idStudent + " ";
                }
            }
        }
        String[] myArray = new String[array.split(" ").length];
        int q = 0;
        for (String str : array.split(" ")) {
            myArray[q] = studentMap.get(Long.valueOf(str)).getName();
            q++;
        }
        return myArray;
    }*/


    /*...������......................................................................................................*/

/*    public Reply allDB() {
        return Reply.of(update -> {
            Map<Long, ru.bot.diary.objects.ru.bot.objects.Teacher> teacherMap = db.getTeacherMap();
            Map<Long, ru.bot.diary.objects.ru.bot.objects.Student> studentMap = db.getStudentMap();
            Map<Integer, ru.bot.diary.objects.ru.bot.objects.Course> courseMap = db.getCourseMap();
            Map<Integer, ru.bot.diary.objects.ru.bot.objects.Task> taskMap = db.getTaskMap();

            Map<Long, ru.bot.diary.objects.ru.bot.objects.Student> createStudent = db.getCreateStudent();
            Map<Long, ru.bot.diary.objects.ru.bot.objects.Course> createCourse = db.getCreateCourse();
            Map<Long, ru.bot.diary.objects.ru.bot.objects.Task> createTask = db.getCreateTask();

            silent.send("����", update.getMessage().getChatId());
        }, update -> update.getMessage().getText().equals("q"));
    }*/
/*

    public Reply del() {
        return Reply.of(update -> {
            db.deleteCourses();
            db.deleteTasks();
            db.deleteTeacher();
            db.deleteStudent();
            db.deleteCreate();
            db.deleteStatus();
            silent.send("��� �������", update.getMessage().getChatId());
            start();
        }, update -> update.getMessage().getText().equals("del"));
    }

    public Reply ssss() {
        return Reply.of(update -> {
            String str = "1-���-4";
            Long id = update.getMessage().getChatId();
            */
/*try {
                String[] group = new String[db.group().size()];
                int q = 0;
                for (int i : db.group().keySet()) {
                    group[q] = db.group().get(i).getName();
                }
                if (group.equals(str)) {
*//*

                    int idCourse = db.getUserStatus().get(id).getIdCourse();
                    Course course = db.getCourse(idCourse);
                    course.addGroup(str);
                    db.addCourseMap(course);
                    silent.send(("������ ���������"), id);
                */
/*} else {
                    silent.send(("����� ������ �� ������� \n" +
                            "����������, ������� �������� ������ � ��� ���� " +
                            "� ������� ��� �������� �� ����������� ����� ����"), id);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }*//*

        }, update -> update.getMessage().getText().equals("s"));
    }*/
}

