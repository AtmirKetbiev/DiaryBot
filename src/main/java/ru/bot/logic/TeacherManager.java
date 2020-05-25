package ru.bot.logic;

import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bot.DB.*;
import ru.bot.objects.Course;
import ru.bot.objects.Status;
import ru.bot.objects.Task;
import ru.bot.objects.Teacher;
import ru.bot.view.ViewCourse;
import ru.bot.view.ViewAnswer;
import ru.bot.view.ViewTask;

import java.text.ParseException;
import java.util.*;

public class TeacherManager {

    private StorageTeacher storageTeacher;
    private StorageStudent storageStudent;
    private StorageCourses storageCourses;
    private StorageTasks storageTasks;
    private StorageContext storageContext;
    private StorageCreate storageCreate;
    private DBContext db;
    private ContextAnswer contextAnswer = new ContextAnswer();

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

    public ContextAnswer start(Update update) {
        Long id = update.getMessage().getChatId();
        Teacher teacher = new Teacher();
        teacher.setName(update.getMessage().getChat().getFirstName() + " " +
                update.getMessage().getChat().getLastName());
        storageTeacher.set(id, teacher);
        contextAnswer.setAnswer("Добро пожаловать!");
        contextAnswer.setList(Arrays.asList("Добавить курс", "Посмотреть курсы", "Изменить профиль", "Помощь"));
        return contextAnswer;
    }

    public ContextAnswer back(Update update) {
        Long id = update.getMessage().getChatId();

        Status status = storageContext.get(id);
        if (status.getIdTask() == null && status.getIdCourse() == null) {
            status.setIdCourse(null);
            contextAnswer.setAnswer("Добро пожаловать!");
            contextAnswer.setList(Arrays.asList("Добавить курс", "Посмотреть курсы", "Изменить профиль", "Помощь"));
        }
        if (status.getIdTask() == null && status.getIdCourse() != null) {
            status.setIdCourse(null);
            storageContext.set(id, status);
            contextAnswer.setAnswer("Ваши курсы:");
            contextAnswer.setList(getCourse(id));
        }
        if (status.getIdTask() != null) {
            status.setIdTask(null);
            storageContext.set(id, status);
            contextAnswer.setAnswer("Ваши задания:");
            contextAnswer.setList(getTask(id));
        }
        return contextAnswer;
    }

    public ContextAnswer changeProfile() {
        contextAnswer.setAnswer("Редактирование профиля временно не доступно \nВаше имя берется из вашего ника в Telegram");
        contextAnswer.setList(null);
        return contextAnswer;
    }

    public ContextAnswer help() {
        ViewAnswer viewHelp = new ViewAnswer();
        contextAnswer.setAnswer(viewHelp.help());
        contextAnswer.setList(null);
        return contextAnswer;
    }

    /**Курсы..........................................................................................................*/
    public ContextAnswer addCourse(Update update) {
        Long id = update.getMessage().getChatId();
        Course course = new Course();
        course.setIdTeacher(id);
        course.setCode(UUID.randomUUID().toString().replace("-", ""));
        storageCreate.setCreateCourse(course);
        contextAnswer.setAnswer("Введите название");
        contextAnswer.setList(null);
        return contextAnswer;
    }

    public ContextAnswer addNextCourse(Update update) {
        String answer = "";
        Long id = update.getMessage().getChatId();
        Course course = storageCreate.getCreateCourse().get(id);
        if(getCourse(id).contains(update.getMessage().getText())) {
            storageCreate.removeCreateCourse(id);
            contextAnswer.setAnswer("Такой курс уже существует!");
            contextAnswer.setList(null);
            return contextAnswer;
        }
        if(update.getMessage().getText().equals("Назад")) {
            storageCreate.removeCreateCourse(id);
            contextAnswer.setAnswer("Редактирование курса отменено");
            contextAnswer.setList(null);
        } else {
            if (course.getName() == null) {
                course.setName(update.getMessage().getText());
                contextAnswer.setAnswer("Описание");
                contextAnswer.setList(null);
            } else if (course.getDescription() == null) {
                course.setDescription(update.getMessage().getText());
            }
            if (course.check()) {
                storageCourses.set(course);
                storageCreate.removeCreateCourse(id);
                contextAnswer.setAnswer("Спасибо");
                contextAnswer.setList(null);
            } else {
                storageCreate.setCreateCourse(course);
            }
        }
        return contextAnswer;
    }

    public ContextAnswer viewCourse(Update update) {
        Long id = update.getMessage().getChatId();
        contextAnswer.setAnswer("Ваши курсы");
        contextAnswer.setList(getCourse(id));
        return contextAnswer;
    }

    public ContextAnswer course(Update update) {
        String answer;
        Long id = update.getMessage().getChatId();
        String text = update.getMessage().getText();
        Status status = new Status();
        status.setIdCourse(storageCourses.getIdByName(text, id));
        storageContext.set(id, status);
        ViewCourse view = new ViewCourse();
        contextAnswer.setAnswer(view.make(storageCourses.get(status.getIdCourse()), db));
        contextAnswer.setList(Arrays.asList("Добавить задание", "Посмотреть задания", "Группы", "Ссылки", "Изменить курс", "Удалить курс"));
        return contextAnswer;
    }

    public ContextAnswer delCourse(Update update) {
        Long id = update.getMessage().getChatId();
        String idCourse = storageContext.get(id).getIdCourse();
        storageCourses.remove(idCourse, id);
        Status status = storageContext.get(id);
        status.setIdCourse(null);
        storageContext.set(id, status);
        contextAnswer.setAnswer("Курс удален\nВаши курсы:");
        contextAnswer.setList(getCourse(id));
        return contextAnswer;
    }

    public ContextAnswer setLink() {
        ViewAnswer viewHelp = new ViewAnswer();
        contextAnswer.setAnswer(viewHelp.setLink());
        contextAnswer.setList(null);
        return contextAnswer;
    }

    public ContextAnswer link(Long idUser, String linkName) {
        String idCourse = storageContext.get(idUser).getIdCourse();
        Course course = storageCourses.get(idCourse);
        course.setLinks(linkName);
        contextAnswer.setAnswer("Ссылка добавлена");
        contextAnswer.setList(null);
        return contextAnswer;
    }

    public List<String> getCourse(Long id) {
        Teacher teacher = storageTeacher.get(id);
        Map<String, Course> course = storageCourses.getMap();
        int q = 0;
        List<String> myArray = new ArrayList<String>(0);
        for (String i : teacher.getCourses()) {
            myArray.add(course.get(i).getName());
            q++;
        }
        return myArray;
    }

    /**...............................................................................................................*/
    public ContextAnswer addTask(Update update) {
        Long id = update.getMessage().getChatId();
        Task task = new Task();
        task.setIdCourse(storageContext.get(id).getIdCourse());
        storageCreate.setCreateTask(id, task);
        contextAnswer.setAnswer("Введите название задания");
        contextAnswer.setList(null);
        return contextAnswer;
    }

    public ContextAnswer addNextTask(Update update) {
        Long id = update.getMessage().getChatId();
        Task task = storageCreate.getCreateTask().get(id);
        if(update.getMessage().getText().equals("Назад")) {
            storageCreate.removeCreateTask(id);
            contextAnswer.setAnswer("Редактирование задания отменено");
            contextAnswer.setList(null);
        } else {
            if (task.getName() == null) {
                contextAnswer.setAnswer("Описание");
                contextAnswer.setList(null);
                task.setName(update.getMessage().getText());
            } else if (task.getDescription() == null) {
                task.setDescription(update.getMessage().getText());
                contextAnswer.setAnswer("введите soft в формате dd/mm/yyyy");
                contextAnswer.setList(null);
            } else if (task.getSoft() == null) {
                try {
                    task.setSoft(update.getMessage().getText());
                    contextAnswer.setAnswer("введите hard в формате dd/mm/yyyy");
                    contextAnswer.setList(null);
                } catch (ParseException e) {
                    contextAnswer.setAnswer("Не правильная дата, введите в формате dd/mm/yyyy");
                    contextAnswer.setList(null);
                    e.printStackTrace();
                }
            } else if (task.getHard() == null) {
                try {
                    task.setHard(update.getMessage().getText());
                } catch (ParseException e) {
                    contextAnswer.setAnswer("Не правильная дата, введите в формате dd/mm/yyyy");
                    contextAnswer.setList(null);
                    e.printStackTrace();
                }
            }
            if (task.check()) {
                storageTasks.set(task);
                storageCreate.removeCreateTask(id);
                contextAnswer.setAnswer("Спасибо");
                contextAnswer.setList(null);
            } else {
                storageCreate.setCreateTask(id, task);
            }
        }
        return contextAnswer;
    }

    public ContextAnswer viewTask(Update update) {
        Long id = update.getMessage().getChatId();
        contextAnswer.setAnswer("Ваши курсы:");
        contextAnswer.setList(getTask(id));
        return contextAnswer;
    }

    public ContextAnswer task(Update update) {
        Long id = update.getMessage().getChatId();
        String text = update.getMessage().getText();
        Status status = storageContext.get(id);
        status.setIdTask(storageTasks.getIdByName(text, status.getIdCourse()));
        storageContext.set(id, status);
        if (status.getIdStudent() != -1) {
            contextAnswer.setAnswer("Вы выбрали задание" + text);
            contextAnswer.setList(Arrays.asList("Снять отметку", "Изменить оценку", "Добавить комментарий"));
        } else {
            ViewTask view = new ViewTask();
            Task t = storageTasks.get(status.getIdTask());
            contextAnswer.setAnswer(view.make(storageTasks.get(status.getIdTask()), db));
            contextAnswer.setList(Arrays.asList("Изменить задание", "Удалить задание"));
        }
        return contextAnswer;
    }

    public ContextAnswer delTask(Update update) {
        Long id = update.getMessage().getChatId();
        Course course = storageCourses.get(storageContext.get(id).getIdCourse());
        course.removeTask(storageContext.get(id).getIdTask());
        storageCourses.set(course);
        Status status = storageContext.get(id);
        status.setIdTask(null);
        storageContext.set(id, status);
        contextAnswer.setAnswer("Задание удалено!\nВаши задания:");
        contextAnswer.setList(getTask(id));
    return contextAnswer;
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

    /**...............................................................................................................*/
    public String delAll(Update update) {
        for (String i : storageCourses.getMap().keySet()) {
            this.storageCourses.getMap().remove(i);
        }
        for (String i : storageTasks.getMap().keySet()) {
            this.storageTasks.getMap().remove(i);
        }
        for (Long i : storageTeacher.getMap().keySet()) {
            this.storageTeacher.getMap().remove(i);
        }
        for (Long i : storageStudent.getMap().keySet()) {
            this.storageStudent.getMap().remove(i);
        }
        for (Long i : storageCreate.getCreateTask().keySet()) {
            this.storageCreate.getCreateTask().remove(i);
        }
        for (Long i : storageCreate.getCreateCourse().keySet()) {
            this.storageCreate.getCreateCourse().remove(i);
        }
        for (Long i : storageContext.getMap().keySet()) {
            this.storageContext.getMap().remove(i);
        }
        return "Удалено";
    }
}
