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
    /*Старт преподавателя*/
    /*public Reply start() {
        return Reply.of(update -> {
            Long id = update.getMessage().getChatId();
            Teacher teacher = new Teacher();
            teacher.setName("препод");
            db.addTeacher(id, teacher);
            answer = "Выберете: add course";
            silent.execute(Keyboard.addKeyboard(new String[]{"Добавить курс", "Посмотреть курсы", "Изменить профиль", "Помощь"}, update, answer));
        }, update -> update.getMessage().getText().equals("/start"));
    }

    public Reply back() {
        return Reply.of(update -> {
            Long id = update.getMessage().getChatId();
            Status status = db.getUserStatus(id);
            if (status.getIdTask() == -1 && status.getIdCourse() == -1) {
                status.setIdCourse(-1);
                silent.execute(Keyboard.addKeyboard(new String[]{"Добавить курс", "Посмотреть курсы", "Изменить профиль", "Помощь"}, update, answer));
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
                answer = "Ваши задания:";
                silent.execute(Keyboard.addKeyboard(myArray, update, answer));
            }
        }, update -> update.getMessage().getText().equals("Назад"));
    }
*/
    /*...КУРС........................................................................................................*/
   /* public Reply course() {
        return Reply.of(update -> {
            Long id = update.getMessage().getChatId();
            String text = update.getMessage().getText();
            Status status = new Status();
            status.setIdCourse(db.getIdByNameCourse(text, id));
            db.addUserStatus(id, status);
            answer = db.getCourse(status.getIdCourse()).getName() + "\n\n" + db.getCourse(status.getIdCourse()).getDescription();
            silent.execute(Keyboard.addKeyboard(new String[]{"Добавить задание", "Посмотреть задания", "Группы", "Ссылки", "Изменить курс", "Удалить курс"}, update, answer));
        }, update -> Arrays.stream(getCourse(update.getMessage().getChatId())).anyMatch(update.getMessage().getText()::equals));
    }

    public Reply viewCourse() {
        return Reply.of(update -> {
            Long id = update.getMessage().getChatId();
            String[] myArray = getCourse(id);
            answer = "Ваши курсы:";
            silent.execute(Keyboard.addKeyboard(myArray, update, answer));
        }, update -> update.getMessage().getText().equals("Посмотреть курсы"));
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
            silent.send("Введите название", update.getMessage().getChatId());
        }, update -> (update.getMessage().getText().equals("Добавить курс")
                || update.getMessage().getText().equals("Изменить курс")));
    }

    public Reply addNextCourse() {
        return Reply.of(update -> {
            Long id = update.getMessage().getChatId();
            Course course = db.getCreateCourse().get(id);
            if(update.getMessage().getText().equals("Назад")) {
                silent.send("Редактирование курса отменено", id);
                db.removeCreateCourse(id);
            } else {
                if (course.getName() == null) {
                    silent.send("Описание", id);
                    course.setName(update.getMessage().getText());
                } else if (course.getDescription() == null) {
                    course.setDescription(update.getMessage().getText());
                }
                if (course.check()) {
                    db.addCourseMap(course);
                    db.removeCreateCourse(id);
                    silent.send("Спасибо", id);
                } else {
                    db.setCreateCourse(course);
                }
            }
        }, update -> db.getCreateCourse().get(update.getMessage().getChatId()) != null
                && !update.getMessage().getText().equals("Добавить курс")
                && !update.getMessage().getText().equals("Изменить курс"));
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

    *//*...ЗАДАНИЯ.....................................................................................................*//*
    public Reply task() {
        return Reply.of(update -> {
            Long id = update.getMessage().getChatId();
            String text = update.getMessage().getText();
            Status status = db.getUserStatus(id);
            status.setIdTask(db.getIdByNameTask(text, status.getIdCourse()));
            db.addUserStatus(id, status);
            //answer = "Вы выбрали задание" + text;
            if (status.getIdStudent() != -1) {
                silent.execute(Keyboard.addKeyboard(new String[]{"Снять отметку", "Изменить оценку", "Добавить комментарий"}, update, answer));
            } else {
                answer = db.getTask(status.getIdTask()).getName() + "\n" +
                        db.getTask(status.getIdTask()).getDescription() + "\n\nSoft dedline\n" +
                        db.getTask(status.getIdTask()).getSoft() + "\n\nHard dedline\n" +
                        db.getTask(status.getIdTask()).getHard() + "\n\n";
                silent.execute(Keyboard.addKeyboard(new String[]{"Изменить задание", "Удалить задание"}, update, answer));
            }
        }, update -> Arrays.stream(getTask(update.getMessage().getChatId())).anyMatch(update.getMessage().getText()::equals));
    }

    public Reply viewTask() {
        return Reply.of(update -> {
            Long id = update.getMessage().getChatId();
            String[] myArray = getTask(id);
            answer = "Ваши задания:";
            silent.execute(Keyboard.addKeyboard(myArray, update, answer));
        }, update -> update.getMessage().getText().equals("Посмотреть задания"));
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
            silent.send("Введите название задания", update.getMessage().getChatId());
        }, update -> (update.getMessage().getText().equals("Добавить задание")
                || update.getMessage().getText().equals("Изменить задание")));
    }

    public Reply addNextTask() {
        return Reply.of(update -> {
            Long id = update.getMessage().getChatId();
            Task task = db.getCreateTask().get(id);
            if(update.getMessage().getText().equals("Назад")) {
                silent.send("Редактирование задания отменено", id);
                db.removeCreateTask(id);
            } else {
                if (task.getName() == null) {
                    silent.send("Описание", id);
                    task.setName(update.getMessage().getText());
                } else if (task.getDescription() == null) {
                    task.setDescription(update.getMessage().getText());
                    silent.send("введите soft в формате dd/mm/yyyy", id);
                } else if (task.getSoft() == null) {
                    try {
                        task.setSoft(update.getMessage().getText());
                        silent.send("введите hard в формате dd/mm/yyyy", id);
                    } catch (ParseException e) {
                        silent.send("Не правильная дата, введите в формате dd/mm/yyyy", id);
                        e.printStackTrace();
                    }
                } else if (task.getHard() == null) {
                    try {
                        task.setHard(update.getMessage().getText());
                    } catch (ParseException e) {
                        silent.send("Не правильная дата, введите в формате dd/mm/yyyy", id);
                        e.printStackTrace();
                    }
                }
                if (task.check()) {
                    db.addTaskMap(task);
                    db.removeCreateTask(id);
                    silent.send("Спасибо", id);
                } else {
                    db.setCreateTask(id, task);
                }
            }
        }, update ->
                db.getCreateTask().get(update.getMessage().getChatId()) != null
                        && !update.getMessage().getText().equals("Добавить задание")
                        && !update.getMessage().getText().equals("Изменить задание"));
    }

    public Reply delTask() {
        return Reply.of(update -> {
            Long id = update.getMessage().getChatId();
            Course course = db.getCourse(db.getUserStatus().get(id).getIdCourse());
            course.removeTask(db.getUserStatus().get(id).getIdTask());
            db.addCourseMap(course);
            silent.send("Задание удалено!", id);

            Status status = db.getUserStatus(id);
            status.setIdTask(-1);
            db.addUserStatus(id, status);
            String[] myArray = getTask(id);
            answer = "Ваши задания:";
            silent.execute(Keyboard.addKeyboard(myArray, update, answer));
        }, update -> update.getMessage().getText().equals("Удалить задание"));
    }
*/

    /*...ГРУППЫ......................................................................................................*/
/*    public Reply viewGroup() {
        return Reply.of(update -> {
            Long id = update.getMessage().getChatId();
            String[] myArray = getGroup(id);
            answer = "Ваши задания:";
            silent.execute(ru.bot.extension.Keyboard.addKeyboard(myArray, update, answer));
        }, update -> update.getMessage().getText().equals("Группы"));
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
            silent.send("Введите группу", update.getMessage().getChatId());
        }, update -> update.getMessage().getText().startsWith("Добавить группу"));
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
                silent.send(("Группа добавлена"), update.getMessage().getChatId());
            } else {
                silent.send(("Такой группы не найдено " +
                        "вводите название группы так как это указано " +
                        "на официальном сайте вуза"), update.getMessage().getChatId());
            }
        }, update -> db.getCreateAddGroupInCourse().get(update.getMessage().getChatId()) != null);
    }

    public Reply groups() {
        return Reply.of(update -> {
            Long id = update.getMessage().getChatId();
            ru.bot.objects.Status status = db.getUserStatus(id);
            status.setIdGroup(db.getIdByNameGroup(update.getMessage().getText()));
            db.addUserStatus(id, status);
            answer = "Группа " + update.getMessage().getText();
            silent.execute(ru.bot.extension.Keyboard.addKeyboard(new String[]{"Статистика", "Участники"}, update, answer));
        }, update -> Arrays.stream(getGroup(update.getMessage().getChatId())).anyMatch(update.getMessage().getText()::equals));
    }*/


    /*...Студенты.....................................................................................................*/
//    public Reply student() {
//        return Reply.of(update -> {
//            Long id = update.getMessage().getChatId();
//            String text = update.getMessage().getText();
//            ru.bot.objects.Status status = db.getUserStatus(id);
//            status.setIdTask(db.getIdByNameTask(text, status.getIdCourse()));
//            db.addUserStatus(id, status);
//            //answer = "Вы выбрали задание" + text;
//            answer = db.getTask(status.getIdTask()).getName() + "\n" +
//                    db.getTask(status.getIdTask()).getDescription() + "\n\nSoft dedline\n" +
//                    db.getTask(status.getIdTask()).getSoft() + "\n\nHard dedline\n" +
//                    db.getTask(status.getIdTask()).getHard() + "\n\n";
//            silent.execute(ru.bot.extension.Keyboard.addKeyboard(new String[]{"Изменить задание", "Удалить задание"}, update, answer));
//        }, update -> Arrays.stream(getStudent(update.getMessage().getChatId())).anyMatch(update.getMessage().getText()::equals));
//    }

    /*public Reply viewStudent() {
        return Reply.of(update -> {
            Long id = update.getMessage().getChatId();
            String[] myArray = getStudent(id);
            answer = "Студенты:";
            silent.execute(ru.bot.extension.Keyboard.addKeyboard(myArray, update, answer));
        }, update -> update.getMessage().getText().equals("Участники"));
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


    /*...РАЗНОЕ......................................................................................................*/

/*    public Reply allDB() {
        return Reply.of(update -> {
            Map<Long, ru.bot.diary.objects.ru.bot.objects.Teacher> teacherMap = db.getTeacherMap();
            Map<Long, ru.bot.diary.objects.ru.bot.objects.Student> studentMap = db.getStudentMap();
            Map<Integer, ru.bot.diary.objects.ru.bot.objects.Course> courseMap = db.getCourseMap();
            Map<Integer, ru.bot.diary.objects.ru.bot.objects.Task> taskMap = db.getTaskMap();

            Map<Long, ru.bot.diary.objects.ru.bot.objects.Student> createStudent = db.getCreateStudent();
            Map<Long, ru.bot.diary.objects.ru.bot.objects.Course> createCourse = db.getCreateCourse();
            Map<Long, ru.bot.diary.objects.ru.bot.objects.Task> createTask = db.getCreateTask();

            silent.send("Стоп", update.getMessage().getChatId());
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
            silent.send("Все удалено", update.getMessage().getChatId());
            start();
        }, update -> update.getMessage().getText().equals("del"));
    }

    public Reply ssss() {
        return Reply.of(update -> {
            String str = "1-ПМИ-4";
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
                    silent.send(("Группа добавлена"), id);
                */
/*} else {
                    silent.send(("Такой группы не найдено \n" +
                            "пожалуйста, введите название группы в том виде " +
                            "в котором она записана на официальном сайте вуза"), id);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }*//*

        }, update -> update.getMessage().getText().equals("s"));
    }*/
}

