package ru.bot.logic;

import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bot.DB.*;
import ru.bot.extension.Timetable;
import ru.bot.objects.*;
import ru.bot.view.ViewCourse;
import ru.bot.view.ViewTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class StudentManager {

    private StorageTeacher storageTeacher;
    private StorageStudent storageStudent;
    private StorageCourses storageCourses;
    private StorageTasks storageTasks;
    private StorageContext storageContext;
    private StorageCreate storageCreate;
    private DBContext db;
    private ContextAnswer contextAnswer = new ContextAnswer();

    public StudentManager(DBContext db) {
        this.storageTeacher = new StorageTeacher(db);
        this.storageStudent = new StorageStudent(db);
        this.storageCourses = new StorageCourses(db);
        this.storageTasks = new StorageTasks(db);
        this.storageContext = new StorageContext(db);
        this.storageCreate = new StorageCreate(db);
        this.db = db;
    }

    public ContextAnswer start(Update update) {
        Long id = update.getMessage().getChatId();
        Student student = new Student();
        student.setName("Студент");
        student.setGroup("1-пми");
        storageStudent.set(id, student);
        storageContext.set(id, new Context());
        /*if (storageStudent.get(id).getName()==null) {
            contextAnswer.setAnswer("Введите ваше имя в формате\n/name Иванов Иван");
            contextAnswer.setList(Arrays.asList("Зачем мне это?"));
        } else if(storageStudent.get(id).getGroup()==null) {
            contextAnswer.setAnswer("Введите вашу группу в формате\n/group 1-пми-4");
            contextAnswer.setList(Arrays.asList("Зачем мне это?"));
        } else {*/
            contextAnswer.setAnswer("Добро пожаловать!");
            contextAnswer.setButtonsList(Arrays.asList("Расписание", "Курсы", "Подписаться на курс", "Помощь"));
        //}
        return contextAnswer;
    }

    public ContextAnswer back(Update update) {
        Long id = update.getMessage().getChatId();

        Context context = storageContext.get(id);
        if (context.getIdTask() == null && context.getIdCourse() == null) {
            context.setIdCourse(null);
            contextAnswer.setAnswer("Добро пожаловать!");
            contextAnswer.setButtonsList(Arrays.asList("Расписание", "Курсы", "Подписаться на курс", "Помощь"));
        }
        if (context.getIdTask() == null && context.getIdCourse() != null) {
            context.setIdCourse(null);
            storageContext.set(id, context);
            contextAnswer.setAnswer("Ваши курсы:");
            contextAnswer.setButtonsList(getCourse(id));
        }
        if (context.getIdTask() != null) {
            context.setIdTask(null);
            storageContext.set(id, context);
            contextAnswer.setAnswer("Ваши задания:");
            contextAnswer.setButtonsList(getTask(id));
        }
        return contextAnswer;
    }

    public ContextAnswer timetable(Update update) {
        contextAnswer.setAnswer(Timetable.timetable(0));
        contextAnswer.setButtonsList(null);
        return contextAnswer;
    }

    public List<String> getCourse(Long id) {
        Student student = storageStudent.get(id);
        Map<String, Course> course = storageCourses.getMap();
        int q = 0;
        List<String> myArray = new ArrayList<String>(0);
        List<String> a = student.getCourses();
        for (String i : student.getCourses()) {
            myArray.add(course.get(i).getName());
            q++;
        }
        return myArray;
    }

    public List<String> getTask(Long id) {
        List<String> myArray = new ArrayList<String>(0);
        if (storageContext.get(id).getIdCourse() == null) {
            return myArray;
        }
        Map<String, Task> task = storageTasks.getMap();
        Map<String, Course> course = storageCourses.getMap();
        Course idCourse = course.get(storageContext.get(id).getIdCourse());
        List<String> idTask = idCourse.getIdTasks();
        int q = 0;
        if (idTask != null) {
            for (String i : idTask) {
                myArray.add(task.get(i).getName());
                q++;
            }
        } else {
            return myArray;
        }
        return myArray;
    }

    public ContextAnswer course(Update update) {
        Long id = update.getMessage().getChatId();
        String text = update.getMessage().getText();
        Context context = new Context();
        context.setIdCourse(storageCourses.getIdByName(text, id));
        storageContext.set(id, context);
        ViewCourse view = new ViewCourse();
        contextAnswer.setAnswer(view.make(storageCourses.get(context.getIdCourse())));
        contextAnswer.setButtonsList(Arrays.asList("Задания", "Ссылки", "Отписаться"));
        return contextAnswer;
    }

    public ContextAnswer viewCourse(Update update) {
        Long id = update.getMessage().getChatId();
        contextAnswer.setAnswer("Ваши курсы");
        contextAnswer.setButtonsList(getCourse(id));
        return contextAnswer;
    }

    public ContextAnswer addCourse(Long idStudent, String idCourse) {
        Student student = storageStudent.get(idStudent);
        student.setCourses(idCourse);
        storageStudent.getMap().put(idStudent,student);
        Course course = storageCourses.get(idCourse);
        course.addGroup(student.getGroup());
        storageCourses.set(course);
        //storageStudent.set(idStudent, student);
        contextAnswer.setAnswer("Добавлены");
        return contextAnswer;
    }

    public ContextAnswer delCourse(Update update) {
        Long id = update.getMessage().getChatId();
        String idCourse = storageContext.get(id).getIdCourse();
        Student student = storageStudent.get(id);
        student.removeCourse(idCourse);
        storageStudent.getMap().put(id,student);
        //storageCourses.remove(idCourse, id);
        Context context = storageContext.get(id);
        context.setIdCourse(null);
        storageContext.set(id, context);
        contextAnswer.setAnswer("Курс удален\nВаши курсы:");
        contextAnswer.setButtonsList(getCourse(id));
        return contextAnswer;
    }

    /**...............................................................................................................*/

    public ContextAnswer task(Update update) {
        Long id = update.getMessage().getChatId();
        String text = update.getMessage().getText();
        Context context = storageContext.get(id);
        context.setIdTask(storageTasks.getIdByName(text, context.getIdCourse()));
        storageContext.set(id, context);
        ViewTask view = new ViewTask();
        contextAnswer.setAnswer(view.make(storageTasks.get(context.getIdTask())));
        contextAnswer.setButtonsList(Arrays.asList("Отметить как сделанное", "Добавить комментарий", "История комментариев"));
        return contextAnswer;
    }

    public ContextAnswer viewTask(Update update) {
        Long id = update.getMessage().getChatId();
        contextAnswer.setAnswer("Ваши задания:");
        contextAnswer.setButtonsList(getTask(id));
        return contextAnswer;
    }

    public ContextAnswer markTask(Update update) {
        Long id = update.getMessage().getChatId();
        Student student = storageStudent.get(id);
        Progress progress = student.getProgresses(storageContext.get(id).getIdCourse(), storageContext.get(id).getIdTask());
        progress.setMark(true);
        student.addProgress(progress);
        storageStudent.getMap().put(id,student);
        //storageStudent.set(id, student);
        contextAnswer.setAnswer("Отмеченно!");
        contextAnswer.setButtonsList(null);
        return contextAnswer;
    }

    public ContextAnswer commentTask(Update update) {
        Long id = update.getMessage().getChatId();
        Student student = storageStudent.get(id);
        Progress progress = student.getProgresses(storageContext.get(id).getIdCourse(), storageContext.get(id).getIdTask());
        String massage = update.getMessage().getText().replaceAll("/add ", "");
        progress.addComment(update.getMessage().getChat().getFirstName() + ": " + massage + "\n");
        student.addProgress(progress);
        storageStudent.getMap().put(id,student);
        contextAnswer.setAnswer("Ваш комментарий сохранен.");
        return contextAnswer;
    }

    public ContextAnswer viewComment(Update update) {
        Long id = update.getMessage().getChatId();
        Student student = storageStudent.get(id);
        Progress progress = student.getProgresses(storageContext.get(id).getIdCourse(), storageContext.get(id).getIdTask());
        String comments = "";
        for (String s : progress.getComment()) {
            comments = comments + s;
        }
        contextAnswer.setAnswer(comments);
        return contextAnswer;
    }

    public ContextAnswer commentTask2(Update update) {
        Long id = update.getMessage().getChatId();
        Student student = storageStudent.get(id);
        student.setStat("КОММЕНТ");
        storageStudent.getMap().put(id,student);
        //storageStudent.set(id, student);
        contextAnswer.setAnswer("Введите ваш комментарий");
        contextAnswer.setButtonsList(null);
        return contextAnswer;
    }

    public ContextAnswer addCommentTask(Update update) {
            Long id = update.getMessage().getChatId();
            Student student = storageStudent.get(id);
        //student.setStat("");
        //storageStudent.getMap().put(id,student);
            if (student.getStat().equals("КОММЕНТ")) {
                Progress progress = student.getProgresses(storageContext.get(id).getIdCourse(), storageContext.get(id).getIdTask());
                progress.addComment(update.getMessage().getText());
                student.addProgress(progress);
                student.setStat("");
                storageStudent.set(id, student);
                contextAnswer.setAnswer("Комментарий сохранен!");
                contextAnswer.setButtonsList(null);
            } else {
                contextAnswer.setAnswer("");
                contextAnswer.setButtonsList(null);
            }
            return contextAnswer;
    }
}