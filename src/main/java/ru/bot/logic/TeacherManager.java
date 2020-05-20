package ru.bot.logic;

import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.abilitybots.api.objects.Reply;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bot.DB.*;
import ru.bot.extension.Keyboard;
import ru.bot.objects.Course;
import ru.bot.objects.Status;
import ru.bot.objects.Task;
import ru.bot.objects.Teacher;
import ru.bot.view.ViewCourse;
import ru.bot.view.ViewTask;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public class TeacherManager {

    private StorageTeacher storageTeacher;
    private StorageStudent storageStudent;
    private StorageCourses storageCourses;
    private StorageTasks storageTasks;
    private StorageContext storageContext;
    private StorageCreate storageCreate;
    private DBContext db;

    public TeacherManager(DBContext db) {
        this.storageTeacher = new StorageTeacher(db);
        this.storageStudent = new StorageStudent(db);
        this.storageCourses = new StorageCourses(db);
        this.storageTasks = new StorageTasks(db);
        this.storageContext = new StorageContext(db);
        this.storageCreate = new StorageCreate(db);
        this.db = db;
    }

    /**Начало.........................................................................................................*/

    public String start(Update update) {
        Long id = update.getMessage().getChatId();
        Teacher teacher = new Teacher();
        teacher.setName("препод");
        storageTeacher.set(id, teacher);
        return "Добро пожаловать!";
    }

    public String[] back(Update update) {
        String[] answer = new String[0];
        Long id = update.getMessage().getChatId();

        Status status = storageContext.get(id);
        if (status.getIdTask() == null && status.getIdCourse() == null) {
            status.setIdCourse(null);
            answer = new String[]{"Добавить курс", "Посмотреть курсы", "Изменить профиль", "Помощь"};
            //silent.execute(Keyboard.addKeyboard(new String[]{"Добавить курс", "Посмотреть курсы", "Изменить профиль", "Помощь"}, update, answer));
        }
        if (status.getIdTask() == null && status.getIdCourse() != null) {
            status.setIdCourse(null);
            storageContext.set(id, status);
            String[] myArray = getCourse(id);
            answer = myArray;
            //silent.execute(Keyboard.addKeyboard(myArray, update, answer));
        }
        if (status.getIdTask() != null) {
            status.setIdTask(null);
            storageContext.set(id, status);
            String[] myArray = getTask(id);
            answer = myArray;
            //silent.execute(Keyboard.addKeyboard(myArray, update, answer));
        }
        return answer;
    }

    /**Курсы..........................................................................................................*/
    public String course(Update update) {
        String answer;
        Long id = update.getMessage().getChatId();
        String text = update.getMessage().getText();
        Status status = new Status();
        status.setIdCourse(storageCourses.getIdByName(text, id));
        storageContext.set(id, status);
        ViewCourse view = new ViewCourse();
        answer = view.make(storageCourses.get(status.getIdCourse()), db);
        return answer;
    }

    public String addCourse(Update update) {
        Long id = update.getMessage().getChatId();
        Course course = new Course();
        course.setIdTeacher(id);
        storageCreate.setCreateCourse(course);
        return "Введите название";
    }

    public String[] viewCourse(Update update) {
        Long id = update.getMessage().getChatId();
        String[] myArray = getCourse(id);
        return myArray;
    }

    public String addNextCourse(Update update) {
        String answer = "";
        Long id = update.getMessage().getChatId();
        Course course = storageCreate.getCreateCourse().get(id);
        if(update.getMessage().getText().equals("Назад")) {
            answer = "Редактирование курса отменено";
            storageCreate.removeCreateCourse(id);
        } else {
            if (course.getName() == null) {
                answer = "Описание";
                course.setName(update.getMessage().getText());
            } else if (course.getDescription() == null) {
                course.setDescription(update.getMessage().getText());
            }
            if (course.check()) {
                storageCourses.set(course);
                storageCreate.removeCreateCourse(id);
                answer = "Спасибо";
            } else {
                storageCreate.setCreateCourse(course);
            }
        }
        return answer;
    }

    public String[] delCourse(Update update) {
        Long id = update.getMessage().getChatId();
        String idCourse = storageContext.get(id).getIdCourse();
        storageCourses.remove(idCourse, id);
        Status status = storageContext.get(id);
        status.setIdCourse(null);
        storageContext.set(id, status);
        return getCourse(id);
    }

    public String[] getCourse(Long id) {
        Teacher teacher = storageTeacher.get(id);
        Map<String, Course> course = storageCourses.getMap();
        int q = 0;
        String[] myArray = new String[teacher.getCourses().size()];
        for (String i : teacher.getCourses()) {
            myArray[q] = course.get(i).getName();
            q++;
        }
        return myArray;
    }

    /**...............................................................................................................*/

    public String task(Update update) {
        Long id = update.getMessage().getChatId();
        String text = update.getMessage().getText();
        Status status = storageContext.get(id);
        status.setIdTask(storageTasks.getIdByName(text, status.getIdCourse()));
        storageContext.set(id, status);
        String answer = "Вы выбрали задание" + text;
        if (status.getIdStudent() != -1) {
            //silent.execute(Keyboard.addKeyboard(new String[]{"Снять отметку", "Изменить оценку", "Добавить комментарий"}, update, answer));
        } else {
            ViewTask view = new ViewTask();
            answer = view.make(storageTasks.get(status.getIdTask()), db);
        }
        return answer;
    }

    public String addTask(Update update) {
            Long id = update.getMessage().getChatId();
            Task task = new Task();
            task.setIdCourse(storageContext.get(id).getIdCourse());
            storageCreate.setCreateTask(id, task);
            return "Введите название задания";
    }

    public String addNextTask(Update update) {
        String answer = "";
        Long id = update.getMessage().getChatId();
        Task task = storageCreate.getCreateTask().get(id);
        if(update.getMessage().getText().equals("Назад")) {
            answer = "Редактирование задания отменено";
            storageCreate.removeCreateTask(id);
        } else {
            if (task.getName() == null) {
                answer = "Описание";
                task.setName(update.getMessage().getText());
            } else if (task.getDescription() == null) {
                task.setDescription(update.getMessage().getText());
                answer = "введите soft в формате dd/mm/yyyy";
            } else if (task.getSoft() == null) {
                try {
                    task.setSoft(update.getMessage().getText());
                    answer = "введите hard в формате dd/mm/yyyy";
                } catch (ParseException e) {
                    answer = "Не правильная дата, введите в формате dd/mm/yyyy";
                    e.printStackTrace();
                }
            } else if (task.getHard() == null) {
                try {
                    task.setHard(update.getMessage().getText());
                } catch (ParseException e) {
                    answer = "Не правильная дата, введите в формате dd/mm/yyyy";
                    e.printStackTrace();
                }
            }
            if (task.check()) {
                storageTasks.set(task);
                storageCreate.removeCreateTask(id);
                answer = "Спасибо";
            } else {
                storageCreate.setCreateTask(id, task);
            }
        }
        return answer;
    }

    public String[] viewTask(Update update) {
        Long id = update.getMessage().getChatId();
        return getTask(id);
    }

    public String[] delTask(Update update) {
        Long id = update.getMessage().getChatId();
        Course course = storageCourses.get(storageContext.get(id).getIdCourse());
        course.removeTask(storageContext.get(id).getIdTask());
        storageCourses.set(course);
        Status status = storageContext.get(id);
        status.setIdTask(null);
        storageContext.set(id, status);
    return getTask(id);
    }


    public String[] getTask(Long id) {
        if (storageContext.get(id).getIdCourse() == null) {
            return new String[0];
        }
        Map<String, Task> task = storageTasks.getMap();
        Map<String, Course> course = storageCourses.getMap();
        Course idCourse = course.get(storageContext.get(id).getIdCourse());
        List<String> idTask = idCourse.getIdTasks();
        int q = 0;
        String[] myArray;
        if (idTask != null) {
            myArray = new String[idTask.size()];

        } else {
            myArray = new String[0];
            return myArray;
        }
        for (String i : idTask) {
            myArray[q] = task.get(i).getName();
            q++;
        }
        return myArray;
    }
}
