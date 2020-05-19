package ru.bot.extension;

import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.abilitybots.api.objects.Flag;
import org.telegram.abilitybots.api.objects.Reply;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.abilitybots.api.util.AbilityExtension;
import ru.bot.objects.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class StudentAbilityExtension implements AbilityExtension {

    private String answer = "";
    private SilentSender silent;
    private DBManager db;

    public StudentAbilityExtension(SilentSender silent, DBContext db) {

        this.silent = silent;
        DBManager ddb = new DBManager(db);
        this.db = ddb;
    }

    /**Старт*//*
    public Reply start() {
        return Reply.of(update -> {
            Long id = update.getMessage().getChatId();
            Student student = new Student();
            student.setName("студент");
            db.statusNull(id);
            db.addStudent(id, student);
            answer = "Добро пожаловать!";
            silent.execute(Keyboard.addKeyboard(new String[]{"Расписание", "Курсы", "Подписаться на курс", "Помощь"}, update, answer));
        }, update -> update.getMessage().getText().equals("/start"));
    }

    public Reply back() {
        return Reply.of(update -> {
            Long id = update.getMessage().getChatId();
            Status status = db.getUserStatus(id);
            if (status.getIdTask() == -1 && status.getIdCourse() == -1) {
                status.setIdCourse(-1);
                silent.execute(Keyboard.addKeyboard(new String[]{"Расписание", "Курсы", "Подписаться на курс", "Помощь"}, update, answer));
            }
            if (status.getIdTask() == -1 && status.getIdCourse() != -1) {
                status.setIdCourse(-1);
                db.addUserStatus(id, status);
                String[] myArray = getCourse(id);
                answer = "Ваши курсы:";
                silent.execute(Keyboard.addKeyboard(myArray, update, answer));
            }
            if (status.getIdTask() != -1) {
                status.setIdTask(-1);
                db.addUserStatus(id, status);
                String[] myArray = getTask(id);

                Student student =  db.getStudent(id);
                if (student.getStat().equals("КОММЕНТ")) {
                    student.setStat("");
                    db.addStudent(id, student);
                }

                answer = "Ваши задания:";
                silent.execute(Keyboard.addKeyboard(myArray, update, answer));
            }
        }, update -> update.getMessage().getText().equals("Назад"));
    }

    public Reply timetable() {
        return Reply.of(update -> {
            Long id = update.getMessage().getChatId();
            silent.send(Timetable.timetable(0), id);
        }, update -> update.getMessage().getText().equals("Расписание"));
    }


    *//**...КУРС........................................................................................................*//*
    public Reply addCourse() {
        return Reply.of(update -> {
            Long id = update.getMessage().getChatId();
            silent.send(Timetable.timetable(0), id);
        }, update -> update.getMessage().getText().equals("Подписаться на курс"));
    }

    public Reply viewCourse() {
        return Reply.of(update -> {
            Long id = update.getMessage().getChatId();
            String[] myArray = getCourse(id);
            answer = "Ваши курсы:";
            silent.execute(Keyboard.addKeyboard(myArray, update, answer));
        }, update -> update.getMessage().getText().equals("Курсы"));
    }

    public String[] getCourse(Long id) {
        Student student = db.getStudent(id);
        Map<Integer, Course> course = db.getCourseMap();
        int q = 0;
        String[] myArray = new String[student.getCourses().size()];
        for (int i : student.getCourses()) {
            myArray[q] = course.get(i).getName();
            q++;
        }
        return myArray;
    }

    public Reply course() {
        return Reply.of(update -> {
            Long id = update.getMessage().getChatId();
            String text = update.getMessage().getText();
            Status status = new Status();
            status.setIdCourse(db.getIdByNameCourse(text, id));
            db.addUserStatus(id, status);
            answer = db.getCourse(status.getIdCourse()).getName() + "\n\n" + db.getCourse(status.getIdCourse()).getDescription();
            silent.execute(Keyboard.addKeyboard(new String[]{"Задания", "Ссылки", "Отписаться"}, update, answer));
        }, update -> Arrays.stream(getCourse(update.getMessage().getChatId())).anyMatch(update.getMessage().getText()::equals));
    }


    *//**...ЗАДАНИЯ.....................................................................................................*//*
    public Reply task() {
        return Reply.of(update -> {
            Long id = update.getMessage().getChatId();
            String text = update.getMessage().getText();
            Status status = db.getUserStatus(id);
            status.setIdTask(db.getIdByNameTask(text, status.getIdCourse()));
            db.addUserStatus(id, status);
            answer = db.getTask(status.getIdTask()).getName() + "\n" +
                    db.getTask(status.getIdTask()).getDescription() + "\n\nSoft dedline\n" +
                    db.getTask(status.getIdTask()).getSoft() + "\n\nHard dedline\n" +
                    db.getTask(status.getIdTask()).getHard() + "\n\n";
            silent.execute(Keyboard.addKeyboard(new String[]{"Отметить как сделанное", "Добавить комментарий", "История комментариев"}, update, answer));
        }, update -> Arrays.stream(getTask(update.getMessage().getChatId())).anyMatch(update.getMessage().getText()::equals));
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

    public Reply delCourse() {
        return Reply.of(update -> {
            Long id = update.getMessage().getChatId();
            int idCourse = db.getUserStatus().get(id).getIdCourse();
            db.removeCourse(idCourse, id);
            silent.send("Курс удален!", id);

            Status status = db.getUserStatus(id);
            status.setIdCourse(-1);
            db.addUserStatus(id, status);
            String[] myArray = getCourse(id);
            answer = "Ваши курсы:";
            silent.execute(Keyboard.addKeyboard(myArray, update, answer));
        }, update -> update.getMessage().getText().equals("Удалить курс"));
    }



    public Reply markTask() {
        return Reply.of(update -> {
            Long id = update.getMessage().getChatId();
            Student student = db.getStudent(id);
            Progress progress = student.getProgresses(db.getUserStatus(id).getIdCourse(), db.getUserStatus(id).getIdTask());
            progress.setMark(true);
            student.addProgress(progress);
            db.addStudent(id, student);
            silent.send("Отмеченно!", id);
        }, update -> update.getMessage().getText().equals("Отметить как сделанное"));
    }

    public Reply commentTask() {
        return Reply.of(update -> {
            Long id = update.getMessage().getChatId();
            Student student = db.getStudent(id);
            student.setStat("КОММЕНТ");
            db.addStudent(id, student);
            silent.send("Введите ваш комментарий", id);
        }, update -> update.getMessage().getText().equals("Добавить комментарий"));
    }

    public Reply addCommentTask() {
        return Reply.of(update -> {
            Long id = update.getMessage().getChatId();
            Student student = db.getStudent(id);
            if (student.getStat().equals("КОММЕНТ")) {
                Progress progress = student.getProgresses(db.getUserStatus(id).getIdCourse(), db.getUserStatus(id).getIdTask());
                progress.addComment(update.getMessage().getText());
                student.addProgress(progress);
                student.setStat("");
                db.addStudent(id, student);
                silent.send("Вы прокомментировали!", id);
            }
        }, Flag.TEXT);
    }

    public Reply allCommentTask() {
        return Reply.of(update -> {
            Long id = update.getMessage().getChatId();
            Student student = db.getStudent(id);
            Progress progress = student.getProgresses(db.getUserStatus(id).getIdCourse(), db.getUserStatus(id).getIdTask());
            String comment = "";
            for (String s : progress.getComment()) {
                comment += "- " + s + "\n\n";
            }
        }, update -> update.getMessage().getText().equals("История комментариев"));
    }*/
}
