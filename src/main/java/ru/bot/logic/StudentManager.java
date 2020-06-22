package ru.bot.logic;

import ru.bot.db.*;
import ru.bot.extension.Timetable;
import ru.bot.objects.*;
import ru.bot.view.ViewCourse;
import ru.bot.view.ViewTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class StudentManager {

    private StorageStudent storageStudent;
    private StorageCourses storageCourses;
    private StorageTasks storageTasks;
    private StorageProgress storageProgress;
    private StorageContext storageContext;
    private ContextAnswer contextAnswer = new ContextAnswer();

    public StudentManager(StorageStudent storageStudent,
                          StorageCourses storageCourses,
                          StorageTasks storageTasks,
                          StorageProgress storageProgress,
                          StorageContext storageContext) {
        this.storageStudent = storageStudent;
        this.storageCourses = storageCourses;
        this.storageTasks = storageTasks;
        this.storageProgress = storageProgress;
        this.storageContext = storageContext;
    }

    public ContextAnswer start(Long id, String name) {
        storageContext.set(id, new Context());
        if (storageStudent.get(id)==null) {
            Student student = new Student();
            student.setName("Студент " + name);
            student.setGroup("1-пми");
            storageStudent.set(id, student);
        }
        contextAnswer.setAnswer("Добро пожаловать! Чтобы подписаться на курс введите команду /newCourse *id*");
        contextAnswer.setButtonsList(Arrays.asList("Расписание", "Курсы", "Подписаться на курс", "Помощь"));
        return contextAnswer;
    }
    public ContextAnswer back(Long id) {
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

    public ContextAnswer timetable() {
        contextAnswer.setAnswer(Timetable.timetable(0));
        contextAnswer.setButtonsList(null);
        return contextAnswer;
    }

    public List<String> getCourse(Long id) {
        Student student = storageStudent.get(id);
        List<String> myArray = new ArrayList<String>(0);
        for (String i : student.getCourses()) {
            myArray.add(storageCourses.get(i).getName());
        }
        return myArray;
    }

    public List<String> getTask(Long id) {
        List<String> myArray = new ArrayList<String>(0);
        if (storageContext.get(id).getIdCourse() == null) {
            return myArray;
        }
        Course idCourse = storageCourses.get(storageContext.get(id).getIdCourse());
        List<String> idTask = idCourse.getIdTasks();
        int q = 0;
        if (idTask != null) {
            for (String i : idTask) {
                myArray.add(storageTasks.get(i).getName());
                q++;
            }
        } else {
            return myArray;
        }
        return myArray;
    }

    public ContextAnswer course(Long id, String text) {
        Student student = storageStudent.get(id);
        String idCourse = "";
        for (String idC: student.getCourses()) {
            if (storageCourses.get(idC).getName().equals(text)) {
                idCourse = idC;
            }
        }

        Context context = new Context();
        context.setIdCourse(idCourse);
        //context.setIdCourse(storageCourses.getIdByName(text, id));
        storageContext.set(id, context);
        ViewCourse view = new ViewCourse();
        contextAnswer.setAnswer(view.make(storageCourses.get(idCourse)));
        contextAnswer.setButtonsList(Arrays.asList("Задания", "Ссылки", "Отписаться"));
        return contextAnswer;
    }

    public ContextAnswer viewCourse(Long id) {
        contextAnswer.setAnswer("Ваши курсы");
        contextAnswer.setButtonsList(getCourse(id));
        return contextAnswer;
    }

    public ContextAnswer addCourse(Long id, String text) {
        String idCourse = text.replaceAll("/newCourse ", "");

        Student student = storageStudent.get(id);

        Course course = storageCourses.get(idCourse);
        course.addGroup(student.getGroup());
        course.setStudents(id);
        storageCourses.set(course);

        student.setCourses(idCourse);
        for (String idTask: course.getIdTasks()) {
            if (storageProgress.getId(id, idCourse, idTask).equals("")) {
                Progress progress = new Progress();
                progress.setIdProgress(UUID.randomUUID().toString().replace("-", ""));
                progress.setIdStudent(id);
                progress.setIdCourse(idCourse);
                progress.setIdTask(idTask);
                storageProgress.set(progress);
                student.setProgresses(progress.getIdProgress());
            }
        }

        storageStudent.set(id,student);

        contextAnswer.setAnswer("Добавлены");
        return contextAnswer;
    }

    public ContextAnswer delCourse(Long id) {
        String idCourse = storageContext.get(id).getIdCourse();
        Student student = storageStudent.get(id);
        student.removeCourse(idCourse);
        storageStudent.set(id,student);
        Context context = storageContext.get(id);
        context.setIdCourse(null);
        storageContext.set(id, context);
        contextAnswer.setAnswer("Курс удален\nВаши курсы:");
        contextAnswer.setButtonsList(getCourse(id));
        return contextAnswer;
    }

    /**...............................................................................................................*/

    public ContextAnswer task(Long id, String text) {
        Context context = storageContext.get(id);
        context.setIdTask(storageTasks.getIdByName(text, context.getIdCourse()));
        storageContext.set(id, context);
        ViewTask view = new ViewTask();
        contextAnswer.setAnswer(view.make(storageTasks.get(context.getIdTask())));
        contextAnswer.setButtonsList(Arrays.asList("Отметить как сделанное", "Добавить комментарий", "История комментариев"));
        return contextAnswer;
    }

    public ContextAnswer viewTask(Long id) {
        contextAnswer.setAnswer("Ваши задания:");
        contextAnswer.setButtonsList(getTask(id));
        return contextAnswer;
    }

    public ContextAnswer markTask(Long id) {
        String idProgress = storageProgress.getId(id, storageContext.get(id).getIdCourse(), storageContext.get(id).getIdTask());
        Progress progress = storageProgress.get(idProgress);
        progress.setMark(true);
        storageProgress.set(progress);
        contextAnswer.setAnswer("Отмеченно!");
        contextAnswer.setButtonsList(null);
        return contextAnswer;
    }

    public ContextAnswer commentTask(Long id, String text) {
        String idProgress = storageProgress.getId(id, storageContext.get(id).getIdCourse(), storageContext.get(id).getIdTask());
        Progress progress = storageProgress.get(idProgress);
        String massage = text.replaceAll("/com ", "");
        progress.setComment(storageStudent.get(id).getName() + ": " + massage + "\n");
        storageProgress.set(progress);
        contextAnswer.setAnswer("Ваш комментарий сохранен.");
        return contextAnswer;
    }

    public ContextAnswer viewComment(Long id) {
        String idProgress = storageProgress.getId(id, storageContext.get(id).getIdCourse(), storageContext.get(id).getIdTask());
        Progress progress = storageProgress.get(idProgress);
        String comments = "";
        for (String s : progress.getComment()) {
            comments = comments + s;
        }
        contextAnswer.setAnswer(comments);
        return contextAnswer;
    }

}