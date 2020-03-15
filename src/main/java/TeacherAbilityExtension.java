import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.abilitybots.api.objects.Reply;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.abilitybots.api.util.AbilityExtension;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Map;

public class TeacherAbilityExtension implements AbilityExtension {

    private String answer = "";
    private SilentSender silent;
    private DBManager db;

    TeacherAbilityExtension(SilentSender silent, DBContext db) {

        this.silent = silent;
        DBManager ddb = new DBManager(db);
        this.db = ddb;
    }

    /**Старт преподавателя*/
    public Reply start() {
        return Reply.of(update -> {
            Long id = update.getMessage().getChatId();
            Teacher teacher = new Teacher();
            teacher.setName("препод");
            db.addTeacher(id, teacher);
            answer = "Выберете: add course";
            silent.execute(Keyboard.addKeyboard(new String[]{"add course", "view course", "delete course"}, update, answer));
        }, update -> update.getMessage().getText().equals("/start"));
    }

    public Reply course() {
        return Reply.of(update -> {
            Long id = update.getMessage().getChatId();

            String text = update.getMessage().getText();
            silent.send("Вы выбрали курс" + text, id);
            Status status = new Status();
            int t = db.getIdByNameCourse(text, id);
            status.setIdCourse(db.getIdByNameCourse(text, id));
            db.addUserStatus(id, status);

            answer = "ваши курсы";
            silent.execute(Keyboard.addKeyboard(new String[]{"add task", "view course", "delete course"}, update, answer));
        }, update -> Arrays.stream(getCourse(update.getMessage().getChatId())).anyMatch(update.getMessage().getText()::equals));

    }

    /**Вывод курсов*/

    public String[] getCourse(Long id) {
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

    public Reply viewCourse() {
        return Reply.of(update -> {
            Long id = update.getMessage().getChatId();
            String[] myArray = getCourse(id);
            answer = "Ваши курсы:";
            silent.execute(Keyboard.addKeyboard(myArray, update, answer));
        }, update -> update.getMessage().getText().equals("view course"));
    }

    /**Добавление курса*/

    public Reply addCourse() {
        return Reply.of(update -> {

            Long id = update.getMessage().getChatId();
            Course course = new Course();
            course.setIdTeacher(id);
            db.setCreateCourse(course);
            silent.send("Введите название", update.getMessage().getChatId());
        }, update -> update.getMessage().getText().equals("add course"));
    }

    public Reply addNextCourse() {
        return Reply.of(update -> {
            Long id = update.getMessage().getChatId();
            Course course = db.getCreateCourse().get(id);
            if(course.getName() == null) {
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
        }, update -> db.getCreateCourse().get(update.getMessage().getChatId()) != null && !update.getMessage().getText().equals("add course"));
    }

    public Reply addTask() {
        return Reply.of(update -> {
            Long id = update.getMessage().getChatId();
            Task task = new Task();
            task.setIdCourse(db.getUserStatus(id).getIdCourse());
            db.setCreateTask(id, task);
            silent.send("Введите название задания", update.getMessage().getChatId());
        }, update -> update.getMessage().getText().equals("add task"));
    }

    public Reply addNextTask() {
        return Reply.of(update -> {
            Long id = update.getMessage().getChatId();
            Task task = db.getCreateTask().get(id);
            if(task.getName() == null) {
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
                    e.printStackTrace();
                }
            } else if (task.getHard() == null) {
                try {
                    task.setHard(update.getMessage().getText());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if (task.check()) {
                db.addTaskMap(task);
                db.removeCreateCourse(id);
                silent.send("Спасибо", id);
            } else {
                db.setCreateTask(id, task);
            }
        }, update ->
                db.getCreateTask().get(update.getMessage().getChatId()) != null && !update.getMessage().getText().equals("add task"));
    }




    public Reply allDB() {
        return Reply.of(update -> {
            Map<Long, Teacher> teacherMap = db.getTeacherMap();
            Map<Long, Student> studentMap = db.getStudentMap();
            Map<Integer, Course> courseMap = db.getCourseMap();
            Map<Integer, Task> taskMap = db.getTaskMap();

            Map<Long, Student> createStudent = db.getCreateStudent();
            Map<Long, Course> createCourse = db.getCreateCourse();
            Map<Long, Task> createTask = db.getCreateTask();

            silent.send("Стоп", update.getMessage().getChatId());
        }, update -> update.getMessage().getText().equals("q"));
    }

    public Reply del() {
        return Reply.of(update -> {
            db.deleteCourses();
            db.deleteTasks();
            db.deleteTeacher();
            db.deleteStudent();
            db.deleteCreate();
            db.deleteStatus();
            silent.send("КУрсы удалены", update.getMessage().getChatId());
        }, update -> update.getMessage().getText().equals("del"));
    }


}
