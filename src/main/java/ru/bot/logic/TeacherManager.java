package ru.bot.logic;

import ru.bot.db.*;
import ru.bot.extension.ReportExcel;
import ru.bot.objects.*;
import ru.bot.view.ViewCourse;
import ru.bot.view.ViewAnswer;
import ru.bot.view.ViewTask;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

public class TeacherManager {

    private StorageTeacher storageTeacher;
    private StorageStudent storageStudent;
    private StorageCourses storageCourses;
    private StorageTasks storageTasks;
    private StorageContext storageContext;
    private StorageCreate storageCreate;
    private ContextAnswer contextAnswer = new ContextAnswer();

    public TeacherManager(StorageTeacher storageTeacher,
                          StorageStudent storageStudent,
                          StorageCourses storageCourses,
                          StorageTasks storageTasks,
                          StorageContext storageContext,
                          StorageCreate storageCreate) {
        this.storageTeacher = storageTeacher;
        this.storageStudent = storageStudent;
        this.storageCourses = storageCourses;
        this.storageTasks = storageTasks;
        this.storageContext = storageContext;
        this.storageCreate = storageCreate;
    }

    /**Начало.........................................................................................................*/

    public ContextAnswer start(Long id, String name) {
        storageCreate.removeCreateCourse(id);
        storageContext.set(id, new Context());

        if (storageTeacher.get(id)==null) {
            Teacher teacher = new Teacher();
            teacher.setName(name);
            storageTeacher.set(id, teacher);
        }

        contextAnswer.setAnswer("Добро пожаловать на главную страницу!");
        contextAnswer.setButtonsList(Arrays.asList("Добавить курс", "Посмотреть курсы", "Изменить профиль", "Помощь"));
        return contextAnswer;
    }

    public ContextAnswer back(Long id) {
        Context context = storageContext.get(id);
        if (context.getIdTask() == null && context.getIdCourse() == null) {
            contextAnswer.setAnswer("Добро пожаловать на главную страницу!");
            contextAnswer.setButtonsList(Arrays.asList("Добавить курс", "Посмотреть курсы", "Изменить профиль", "Помощь"));
        }
        if (context.getIdGroup() == null && context.getIdTask() == null && context.getIdCourse() != null) {
            context.setIdCourse(null);
            storageContext.set(id, context);
            contextAnswer.setAnswer("Ваши курсы:");
            contextAnswer.setButtonsList(getCourse(id));
        }
        if (context.getIdGroup() == null && context.getIdTask() != null) {
            context.setIdTask(null);
            storageContext.set(id, context);
            contextAnswer.setAnswer("Ваши задания:");
            contextAnswer.setButtonsList(getTask(id));
        }
        if (context.getIdGroup() != null) {
            storageContext.set(id, context);
            contextAnswer = viewGroup(id);
            context.setIdGroup(null);
        }
        return contextAnswer;
    }

    public ContextAnswer changeProfile() {
        contextAnswer.setAnswer("Редактирование профиля временно не доступно \nВаше имя берется из вашего ника в Telegram");
        contextAnswer.setButtonsList(null);
        return contextAnswer;
    }

    public ContextAnswer help() {
        ViewAnswer viewHelp = new ViewAnswer();
        contextAnswer.setAnswer(viewHelp.help());
        contextAnswer.setButtonsList(null);
        return contextAnswer;
    }

    /**Курсы..........................................................................................................*/
    public ContextAnswer addCourse(Long id) {
        Course course = new Course();
        course.setIdTeacher(id);
        if (storageContext.get(id).getIdCourse()!=null) {
            course.setCode(storageContext.get(id).getIdCourse());
        } else {
            course.setCode(UUID.randomUUID().toString().replace("-", ""));
        }
        storageCreate.setCreateCourse(course);

        contextAnswer.setAnswer("Введите название");
        return contextAnswer;
    }

    public ContextAnswer addNextCourse(Long id, String text) {
        Course newCourse = storageCreate.getCreateCourse().get(id);
        if(newCourse.getName()==null && getCourse(id).contains(text)) {
            storageCreate.removeCreateCourse(id);
            contextAnswer.setAnswer("Такой курс уже существует!");
            return contextAnswer;
        }
        if(text.equals("Назад")) {
            storageCreate.removeCreateCourse(id);
            contextAnswer.setAnswer("Редактирование курса отменено");
        } else {
            if (newCourse.getName() == null) {
                newCourse.setName(text);
                contextAnswer.setAnswer("Описание");
            } else if (newCourse.getDescription() == null) {
                newCourse.setDescription(text);
            }
            if (newCourse.check()) {
                Course course;
                if (storageCourses.get(newCourse.getCode())!=null) {
                    course = storageCourses.get(newCourse.getCode());
                    course.setName(newCourse.getName());
                    course.setDescription(newCourse.getDescription());
                } else {
                    course = newCourse;
                }
                storageCourses.set(course);
                Teacher teacher = storageTeacher.get(id);
                teacher.addCourses(course.getCode());
                storageTeacher.set(course.getIdTeacher(), teacher);
                storageCreate.removeCreateCourse(id);
                contextAnswer.setAnswer("Спасибо");
            } else {
                storageCreate.setCreateCourse(newCourse);
            }
        }
        return contextAnswer;
    }

    public ContextAnswer viewCourse(Long id) {
        contextAnswer.setAnswer("Ваши курсы");
        contextAnswer.setButtonsList(getCourse(id));
        return contextAnswer;
    }

    public ContextAnswer course(Long id, String text) {
        Context context = new Context();
        context.setIdCourse(storageCourses.getIdByName(text, id));
        storageContext.set(id, context);

        ViewCourse view = new ViewCourse();
        contextAnswer.setAnswer(view.make(storageCourses.get(context.getIdCourse()))+context.getIdCourse());
        contextAnswer.setButtonsList(Arrays.asList("Добавить задание", "Посмотреть задания", "Группы", "Ссылки", "Изменить курс", "Удалить курс"));
        return contextAnswer;
    }

    public ContextAnswer delCourse(Long id) {
        String idCourse = storageContext.get(id).getIdCourse();

        Teacher teacher = storageTeacher.get(id);
        teacher.removeCourse(idCourse);
        storageTeacher.set(id, teacher);

        Context context = storageContext.get(id);
        context.setIdCourse(null);
        storageContext.set(id, context);
        if (!getCourse(id).isEmpty()) {
            contextAnswer.setAnswer("Курс удален\nВаши курсы:");
            contextAnswer.setButtonsList(getCourse(id));
        } else {
            contextAnswer.setAnswer("Курс удален");
            contextAnswer.setButtonsList(Arrays.asList("Добавить курс", "Посмотреть курсы", "Изменить профиль", "Помощь"));
        }
        return contextAnswer;
    }

    public ContextAnswer setLink() {
        ViewAnswer viewHelp = new ViewAnswer();
        contextAnswer.setAnswer(viewHelp.setLink());
        contextAnswer.setButtonsList(null);
        return contextAnswer;
    }

    public ContextAnswer link(Long idUser, String linkName) {
        String idCourse = storageContext.get(idUser).getIdCourse();
        Course course = storageCourses.get(idCourse);
        course.setLinks(linkName);
        contextAnswer.setAnswer("Ссылка добавлена");
        contextAnswer.setButtonsList(null);
        return contextAnswer;
    }

    public List<String> getCourse(Long id) {
        Teacher teacher = storageTeacher.get(id);
        List<String> myArray = new ArrayList<String>(0);
        for (String i : teacher.getCourses()) {
            myArray.add(storageCourses.get(i).getName());
        }
        return myArray;
    }

    /**...............................................................................................................*/
    public ContextAnswer addTask(Long id) {
        Task task = new Task();
        if (storageContext.get(id).getIdTask()!=null) {
            task.setCode(storageContext.get(id).getIdTask());
        } else {
            task.setCode(UUID.randomUUID().toString().replace("-", ""));
        }
        task.setIdCourse(storageContext.get(id).getIdCourse());
        storageCreate.setCreateTask(id, task);

        contextAnswer.setAnswer("Введите название задания");
        contextAnswer.setButtonsList(null);
        return contextAnswer;
    }

    public ContextAnswer addNextTask(Long id, String text) {
        Task newTask = storageCreate.getCreateTask().get(id);
        if(text.equals("Назад")) {
            storageCreate.removeCreateTask(id);
            contextAnswer.setAnswer("Редактирование задания отменено");
        } else {
            if (newTask.getName() == null) {
                contextAnswer.setAnswer("Описание");
                newTask.setName(text);
            } else if (newTask.getDescription() == null) {
                newTask.setDescription(text);
                contextAnswer.setAnswer("введите soft в формате dd/mm/yyyy");
            } else if (newTask.getSoft() == null) {
                try {
                    newTask.setSoft(text);
                    contextAnswer.setAnswer("введите hard в формате dd/mm/yyyy");
                } catch (ParseException e) {
                    contextAnswer.setAnswer("Не правильная дата, введите в формате dd/mm/yyyy");
                }
            } else if (newTask.getHard() == null) {
                try {
                    newTask.setHard(text);
                } catch (ParseException e) {
                    contextAnswer.setAnswer("Не правильная дата, введите в формате dd/mm/yyyy");
                }
            }
            if (newTask.check()) {
                Task task;
                if (storageTasks.get(newTask.getCode())!=null) {
                    task = storageTasks.get(newTask.getCode());
                    task.setName(newTask.getName());
                    task.setDescription(newTask.getDescription());
                } else {
                    task = newTask;
                }
                storageTasks.set(task);
                Course course = storageCourses.get(task.getIdCourse());
                course.setIdTask(task.getCode());
                storageCourses.set(course);

                storageCreate.removeCreateTask(id);
                contextAnswer.setAnswer("Спасибо");
            } else {
                storageCreate.setCreateTask(id, newTask);
            }
        }
        return contextAnswer;
    }

    public ContextAnswer viewTask(Long id) {
        contextAnswer.setAnswer("Ваши задания:");
        contextAnswer.setButtonsList(getTask(id));
        return contextAnswer;
    }

    public ContextAnswer task(Long id, String text) {
        Context context = storageContext.get(id);
        context.setIdTask(storageTasks.getIdByName(text, context.getIdCourse()));
        storageContext.set(id, context);

        if (context.getIdStudent() != -1) {
            contextAnswer.setAnswer("Вы выбрали задание " + text);

            contextAnswer.setButtonsList(Arrays.asList("Снять отметку", "Изменить оценку", "Добавить комментарий"));
        } else {
            ViewTask view = new ViewTask();
            Task t = storageTasks.get(context.getIdTask());
            contextAnswer.setAnswer(view.make(storageTasks.get(context.getIdTask()))+context.getIdCourse());
            contextAnswer.setButtonsList(Arrays.asList("Изменить задание", "Удалить задание"));
        }
        return contextAnswer;
    }

    public ContextAnswer delTask(Long id) {
        Course course = storageCourses.get(storageContext.get(id).getIdCourse());
        course.removeTask(storageContext.get(id).getIdTask());
        storageCourses.set(course);

        Context context = storageContext.get(id);
        context.setIdTask(null);
        storageContext.set(id, context);

        contextAnswer.setAnswer("Задание удалено!\nВаши задания:");
        contextAnswer.setButtonsList(getTask(id));
    return contextAnswer;
    }

    public List<String> getTask(Long id) {
        List<String> myArray = new ArrayList<String>(0);
        if (storageContext.get(id).getIdCourse() == null) {
            return myArray;
        }
        Course idCourse = storageCourses.get(storageContext.get(id).getIdCourse());
        List<String> idTask = idCourse.getIdTasks();
        if (idTask != null) {
            for (String i : idTask) {
                myArray.add(storageTasks.get(i).getName());
            }
        } else {
            return myArray;
        }

        return myArray;
    }

    /**...............................................................................................................*/

    public ContextAnswer viewGroup(Long id) {
        contextAnswer.setButtonsList(new ArrayList<>(0));
        if (storageContext.get(id)==null) {
            return contextAnswer;
        }
        if (storageContext.get(id).getIdCourse()!=null) {
            Course course = storageCourses.get(storageContext.get(id).getIdCourse());
            contextAnswer.setAnswer("Группы");
            if (course.getGroups() == null) {
                contextAnswer.setButtonsList(new ArrayList<>(0));
            } else {
                contextAnswer.setButtonsList(course.getGroups());
            }
        }
        return contextAnswer;
    }

    public ContextAnswer group(Long id, String text) {
        Context context = storageContext.get(id);
        context.setIdGroup(text);
        storageContext.set(id, context);

        contextAnswer.setAnswer("1");
        contextAnswer.setButtonsList(Arrays.asList("Статистика", "Студенты"));
        return contextAnswer;
    }

    public List<String> getGroup(Long id) {
        Course course = storageCourses.get(storageContext.get(id).getIdCourse());
        return course.getGroups();
    }

    public File statistic(Long id, String text) throws IOException {
        String groupName = storageContext.get(id).getIdGroup();
        String idCourse = storageContext.get(id).getIdCourse();

        Context context = storageContext.get(id);
        context.setIdGroup(groupName);
        storageContext.set(id, context);

        /**имена студентов*/
        List<Long> studentList = new ArrayList<>(0);
        List<String> studentNameList = new ArrayList<>(0);
        for (Long idStudent: storageCourses.get(idCourse).getStudents()) {
            if (storageStudent.get(idStudent).getGroup().equals(groupName)) {
                studentList.add(idStudent);
                studentNameList.add(storageStudent.get(idStudent).getName());
            }
        }

        /**список прогрессов*/
        Map<String, List<Progress>> progressMap = new HashMap<>();
        for (Long idStudent: studentList) {
            List<Progress> progress = storageStudent.get(idStudent).getProgresses(idCourse);
            if (!(progress ==null)) {
                progressMap.put(storageStudent.get(idStudent).getName() ,progress);
            }
        }

        /**список названий заданий*/
        Map<String ,String> taskMap  = new HashMap<>();
        for (String s: storageCourses.get(context.getIdCourse()).getIdTasks()) {
            if (storageCourses.get(idCourse).getIdTasks().contains(s)) {
                taskMap.put(s, storageTasks.get(s).getName());
            }
        }

        ReportExcel reportExcel = new ReportExcel();

        return reportExcel.getReportFile(studentNameList, taskMap, progressMap);
    }

    public ContextAnswer viewStudent(Long id) {
        String idCourse = storageContext.get(id).getIdCourse();
        String nameGroup = storageContext.get(id).getIdGroup();
        List<String> myArray = new ArrayList<String>(0);

        Context context = storageContext.get(id);
        context.setIdGroup(nameGroup);
        storageContext.set(id, context);

        for (Long i : storageCourses.get(idCourse).getStudents()) {
            if (storageStudent.get(i).getGroup().equals(nameGroup)) {
                myArray.add(storageStudent.get(i).getName());
            }
        }
        contextAnswer.setAnswer("Студенты");
        contextAnswer.setButtonsList(myArray);
        return contextAnswer;
    }

    public ContextAnswer getStudent(Long id) {
        String idCourse = storageContext.get(id).getIdCourse();
        List<String> myArray = new ArrayList<String>(0);
        if (storageContext.get(id).getIdGroup() != null) {
            for (Long i : storageCourses.get(idCourse).getStudents()) {
                if (storageStudent.get(i).getGroup().equals(storageContext.get(id).getIdGroup())) {
                    myArray.add(storageStudent.get(i).getName());
                }
            }
        }
        contextAnswer.setAnswer("Студенты");
        contextAnswer.setButtonsList(myArray);
        return contextAnswer;
    }

    public ContextAnswer student(Long id, String text) {
        String idCourse = storageContext.get(id).getIdCourse();
        for (Long i : storageCourses.get(idCourse).getStudents()) {
            if (storageStudent.get(i).getGroup().equals(storageContext.get(id).getIdGroup()) &&
                    storageStudent.get(i).getName().equals(text)) {
                Context context = storageContext.get(id);
                context.setIdStudent(i);
                storageContext.set(id, context);
            }
        }
        contextAnswer.setAnswer("Выберете задание");
        contextAnswer.setButtonsList(getTask(id));
        return contextAnswer;
    }


    public ContextAnswer unmark(Long id) {
        Long idStudent = storageContext.get(id).getIdStudent();

        Student student = storageStudent.get(idStudent);
        Progress progress = student.getProgresses(storageContext.get(id).getIdCourse(), storageContext.get(id).getIdTask());
        progress.setMark(false);
        student.addProgress(progress);
        storageStudent.set(idStudent,student);

        contextAnswer.setAnswer("Отмеченно!");
        return contextAnswer;
    }

    public ContextAnswer grade(Long id, String text) {
        String massage = text.replaceAll("/grade ", "");

        Long idStudent = storageContext.get(id).getIdStudent();

        Student student = storageStudent.get(idStudent);
        Progress progress = student.getProgresses(storageContext.get(id).getIdCourse(), storageContext.get(id).getIdTask());
        progress.setGrade(massage);
        student.addProgress(progress);
        storageStudent.set(idStudent,student);

        contextAnswer.setAnswer("Оценено");
        return contextAnswer;
    }

    public ContextAnswer commentTask(Long id, String text) {
        Long idStudent = storageContext.get(id).getIdStudent();

        Student student = storageStudent.get(idStudent);
        Progress progress = student.getProgresses(storageContext.get(id).getIdCourse(), storageContext.get(id).getIdTask());
        String massage = text.replaceAll("/com ", "");
        progress.addComment(storageTeacher.get(id).getName() + ": " + massage + "\n");
        student.addProgress(progress);
        storageStudent.set(idStudent,student);

        contextAnswer.setAnswer("Ваш комментарий сохранен.");
        return contextAnswer;
    }

    /**...............................................................................................................*/
    /*public String delAll() {
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
    }*/

    /*public String db() {
        String answer = "База данных\n\n";

        answer = answer + "\nПреподаватели\n";
        for (Long i : storageTeacher.getMap().keySet()) {
            answer = answer + storageTeacher.get(i).getName() + "\n";
        }

        answer = answer + "\nСтуденты\n";
        for (Long i : storageStudent.getMap().keySet()) {
            answer = answer + storageStudent.get(i).getName() + "\n";
        }

        answer = answer + "\nКурсы\n";
        for (String i : storageCourses.getMap().keySet()) {
            answer = answer + storageCourses.get(i).getName() + "\n";
        }

        answer = answer + "\nЗадания\n";
        for (String i : storageTasks.getMap().keySet()) {
            answer = answer + storageTasks.get(i).getName() + "\n";
        }

        return answer;
    }*/
}
