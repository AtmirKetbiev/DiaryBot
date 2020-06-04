package ru.bot.logic;

import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.bot.DB.*;
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

    /**������.........................................................................................................*/

    public ContextAnswer start(Update update) {
        Long id = update.getMessage().getChatId();
        storageCreate.removeCreateCourse(id);

        StorageContext storageContext = new StorageContext(db);
        storageContext.set(id, new Context());

        Teacher teacher = new Teacher();
        teacher.setName(update.getMessage().getChat().getFirstName() + " " +
                update.getMessage().getChat().getLastName());
        storageTeacher.set(id, teacher);

        contextAnswer.setAnswer("����� ���������� �� ������� ��������!");
        contextAnswer.setButtonsList(Arrays.asList("�������� ����", "���������� �����", "�������� �������", "������"));
        return contextAnswer;
    }

    public ContextAnswer back(Update update) {
        Long id = update.getMessage().getChatId();
        Context context = storageContext.get(id);
        if (context.getIdTask() == null && context.getIdCourse() == null) {
            contextAnswer.setAnswer("����� ���������� �� ������� ��������!");
            contextAnswer.setButtonsList(Arrays.asList("�������� ����", "���������� �����", "�������� �������", "������"));
        }
        if (context.getIdTask() == null && context.getIdCourse() != null) {
            context.setIdCourse(null);
            storageContext.set(id, context);
            contextAnswer.setAnswer("���� �����:");
            contextAnswer.setButtonsList(getCourse(id));
        }
        if (context.getIdTask() != null) {
            context.setIdTask(null);
            storageContext.set(id, context);
            contextAnswer.setAnswer("���� �������:");
            contextAnswer.setButtonsList(getTask(id));
        }
        return contextAnswer;
    }

    public ContextAnswer changeProfile() {
        contextAnswer.setAnswer("�������������� ������� �������� �� �������� \n���� ��� ������� �� ������ ���� � Telegram");
        contextAnswer.setButtonsList(null);
        return contextAnswer;
    }

    public ContextAnswer help() {
        ViewAnswer viewHelp = new ViewAnswer();
        contextAnswer.setAnswer(viewHelp.help());
        contextAnswer.setButtonsList(null);
        return contextAnswer;
    }

    /**�����..........................................................................................................*/
    public ContextAnswer addCourse(Update update) {
        Long id = update.getMessage().getChatId();

        Course course = new Course();
        course.setIdTeacher(id);
        course.setCode(UUID.randomUUID().toString().replace("-", ""));
        storageCreate.setCreateCourse(course);

        contextAnswer.setAnswer("������� ��������");
        contextAnswer.setButtonsList(null);
        return contextAnswer;
    }

    public ContextAnswer addNextCourse(Update update) {
        Long id = update.getMessage().getChatId();
        Course course = storageCreate.getCreateCourse().get(id);
        if(getCourse(id).contains(update.getMessage().getText())) {
            storageCreate.removeCreateCourse(id);
            contextAnswer.setAnswer("����� ���� ��� ����������!");
            contextAnswer.setButtonsList(null);
            return contextAnswer;
        }
        if(update.getMessage().getText().equals("�����")) {
            storageCreate.removeCreateCourse(id);
            contextAnswer.setAnswer("�������������� ����� ��������");
            contextAnswer.setButtonsList(null);
        } else {
            if (course.getName() == null) {
                course.setName(update.getMessage().getText());
                contextAnswer.setAnswer("��������");
                contextAnswer.setButtonsList(null);
            } else if (course.getDescription() == null) {
                course.setDescription(update.getMessage().getText());
            }
            if (course.check()) {
                storageCourses.set(course);
                //
                Teacher teacher = storageTeacher.getMap().get(id);
                teacher.addCourses(course.getCode());
                storageTeacher.getMap().put(course.getIdTeacher(), teacher);
                //
                storageCreate.removeCreateCourse(id);
                contextAnswer.setAnswer("�������");
                contextAnswer.setButtonsList(null);
            } else {
                storageCreate.setCreateCourse(course);
            }
        }
        return contextAnswer;
    }

    public ContextAnswer viewCourse(Update update) {
        Long id = update.getMessage().getChatId();
        contextAnswer.setAnswer("���� �����");
        contextAnswer.setButtonsList(getCourse(id));
        return contextAnswer;
    }

    public ContextAnswer course(Update update) {
        Long id = update.getMessage().getChatId();
        String text = update.getMessage().getText();

        Context context = new Context();
        context.setIdCourse(storageCourses.getIdByName(text, id));
        storageContext.set(id, context);

        ViewCourse view = new ViewCourse();
        contextAnswer.setAnswer(view.make(storageCourses.get(context.getIdCourse()))+context.getIdCourse());
        contextAnswer.setButtonsList(Arrays.asList("�������� �������", "���������� �������", "������", "������", "�������� ����", "������� ����"));
        return contextAnswer;
    }

    public ContextAnswer delCourse(Update update) {
        Long id = update.getMessage().getChatId();

        String idCourse = storageContext.get(id).getIdCourse();
        storageCourses.remove(idCourse, id);

        Context context = storageContext.get(id);
        context.setIdCourse(null);
        storageContext.set(id, context);

        contextAnswer.setAnswer("���� ������\n���� �����:");
        contextAnswer.setButtonsList(getCourse(id));
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
        contextAnswer.setAnswer("������ ���������");
        contextAnswer.setButtonsList(null);
        return contextAnswer;
    }

    public List<String> getCourse(Long id) {
        Teacher teacher = storageTeacher.get(id);
        Map<String, Course> course = storageCourses.getMap();
        //int q = 0;
        List<String> myArray = new ArrayList<String>(0);
        List<String> a = teacher.getCourses();
        for (String i : teacher.getCourses()) {
            myArray.add(course.get(i).getName());
            //q++;
        }
        return myArray;
    }

    /**...............................................................................................................*/
    public ContextAnswer addTask(Update update) {
        Long id = update.getMessage().getChatId();

        Task task = new Task();
        task.setIdCourse(storageContext.get(id).getIdCourse());
        storageCreate.setCreateTask(id, task);

        contextAnswer.setAnswer("������� �������� �������");
        contextAnswer.setButtonsList(null);
        return contextAnswer;
    }

    public ContextAnswer addNextTask(Update update) {
        Long id = update.getMessage().getChatId();
        Task task = storageCreate.getCreateTask().get(id);
        if(update.getMessage().getText().equals("�����")) {
            storageCreate.removeCreateTask(id);
            contextAnswer.setAnswer("�������������� ������� ��������");
            contextAnswer.setButtonsList(null);
        } else {
            if (task.getName() == null) {
                contextAnswer.setAnswer("��������");
                contextAnswer.setButtonsList(null);
                task.setName(update.getMessage().getText());
            } else if (task.getDescription() == null) {
                task.setDescription(update.getMessage().getText());
                contextAnswer.setAnswer("������� soft � ������� dd/mm/yyyy");
                contextAnswer.setButtonsList(null);
            } else if (task.getSoft() == null) {
                try {
                    task.setSoft(update.getMessage().getText());
                    contextAnswer.setAnswer("������� hard � ������� dd/mm/yyyy");
                    contextAnswer.setButtonsList(null);
                } catch (ParseException e) {
                    contextAnswer.setAnswer("�� ���������� ����, ������� � ������� dd/mm/yyyy");
                    contextAnswer.setButtonsList(null);
                    //e.printStackTrace();
                }
            } else if (task.getHard() == null) {
                try {
                    task.setHard(update.getMessage().getText());
                } catch (ParseException e) {
                    contextAnswer.setAnswer("�� ���������� ����, ������� � ������� dd/mm/yyyy");
                    contextAnswer.setButtonsList(null);
                    //e.printStackTrace();
                }
            }
            if (task.check()) {
                storageTasks.set(task);
                //
                /*Course course = storageCourses.get(task.getIdCourse());
                course.setIdTasks(id);
                storageCourses.getMap().put(task.getIdCourse(), course);*/
                //
                storageCreate.removeCreateTask(id);
                contextAnswer.setAnswer("�������");
                contextAnswer.setButtonsList(null);
            } else {
                storageCreate.setCreateTask(id, task);
            }
        }
        return contextAnswer;
    }

    public ContextAnswer viewTask(Update update) {
        Long id = update.getMessage().getChatId();
        contextAnswer.setAnswer("���� �������:");
        contextAnswer.setButtonsList(getTask(id));
        return contextAnswer;
    }

    public ContextAnswer task(Update update) {
        Long id = update.getMessage().getChatId();
        String text = update.getMessage().getText();

        Context context = storageContext.get(id);
        context.setIdTask(storageTasks.getIdByName(text, context.getIdCourse()));
        storageContext.set(id, context);

        if (context.getIdStudent() != -1) {
            contextAnswer.setAnswer("�� ������� �������" + text);
            contextAnswer.setButtonsList(Arrays.asList("����� �������", "�������� ������", "�������� �����������"));
        } else {
            ViewTask view = new ViewTask();
            Task t = storageTasks.get(context.getIdTask());
            contextAnswer.setAnswer(view.make(storageTasks.get(context.getIdTask()))+context.getIdCourse());
            contextAnswer.setButtonsList(Arrays.asList("�������� �������", "������� �������"));
        }
        return contextAnswer;
    }

    public ContextAnswer delTask(Update update) {
        Long id = update.getMessage().getChatId();

        Course course = storageCourses.get(storageContext.get(id).getIdCourse());
        course.removeTask(storageContext.get(id).getIdTask());
        storageCourses.set(course);

        Context context = storageContext.get(id);
        context.setIdTask(null);
        storageContext.set(id, context);

        contextAnswer.setAnswer("������� �������!\n���� �������:");
        contextAnswer.setButtonsList(getTask(id));
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
        //int q = 0;
        if (idTask != null) {
            for (String i : idTask) {
                myArray.add(task.get(i).getName());
                //q++;
            }
        } else {
            return myArray;
        }

        return myArray;
    }

    /**...............................................................................................................*/

    public ContextAnswer viewGroup(Update update) {
        Long id = update.getMessage().getChatId();
        contextAnswer.setButtonsList(new ArrayList<>(0));
        if (storageContext.get(id).getIdCourse()!=null) {
            Course course = storageCourses.get(storageContext.get(id).getIdCourse());
            contextAnswer.setAnswer("������");
            if (course.getGroup() == null) {
                contextAnswer.setButtonsList(new ArrayList<>(0));
            } else {
                contextAnswer.setButtonsList(course.getGroup());
            }
        }
        return contextAnswer;
    }

    public ContextAnswer group(Update update) {
        Long id = update.getMessage().getChatId();

        Context context = storageContext.get(id);
        context.setIdGroup(update.getMessage().getText());
        storageContext.set(id, context);

        contextAnswer.setAnswer("1");
        contextAnswer.setButtonsList(Arrays.asList("����������", "��������"));
        return contextAnswer;
    }

    public List<String> getGroup(Update update) {
        Long id = update.getMessage().getChatId();
        Course course = storageCourses.get(storageContext.get(id).getIdCourse());
        return course.getGroup();
    }

    public File statistic(Update update) throws IOException {
        Long id = update.getMessage().getChatId();
        //String groupName = update.getMessage().getText();
        String groupName = storageContext.get(id).getIdGroup();
        String idCourse = storageContext.get(id).getIdCourse();

        Context context = storageContext.get(id);
        context.setIdGroup(groupName);
        storageContext.set(id, context);

        /**����� ���������*/
        List<Long> studentList = new ArrayList<>(0);
        List<String> studentNameList = new ArrayList<>(0);
        for (Long idStudent: storageStudent.getMap().keySet()) {
            if (storageStudent.get(idStudent).getGroup().equals(groupName)) {
                studentList.add(idStudent);
                studentNameList.add(storageStudent.get(idStudent).getName());
            }
        }

        /**������ ����������*/
        Map<String, List<Progress>> progressMap = new HashMap<>();
        for (Long idStudent: studentList) {
            List<Progress> progress = storageStudent.get(idStudent).getProgresses(idCourse);
            if (!progress.isEmpty()) {
                progressMap.put(storageStudent.get(idStudent).getName() ,progress);
            }
        }

        /**������ �������� �������*/
        Map<String ,String> taskMap  = new HashMap<>();
        for (String s: storageTasks.getMap().keySet()) {
            if (storageCourses.get(idCourse).getIdTasks().contains(s)) {
                taskMap.put(s, storageTasks.get(s).getName());
            }
        }

        ReportExcel reportExcel = new ReportExcel();

        return reportExcel.getReportFile(studentNameList, taskMap, progressMap);
    }

    public ContextAnswer viewStudent(Update update) {
        Long id = update.getMessage().getChatId();
        //String nameGroup = update.getMessage().getText();
        String nameGroup = storageContext.get(id).getIdGroup();
        List<String> myArray = new ArrayList<String>(0);

        Context context = storageContext.get(id);
        context.setIdGroup(nameGroup);
        storageContext.set(id, context);

        for (Long i : storageStudent.getMap().keySet()) {
            if (storageStudent.getMap().get(i).getGroup().equals(nameGroup)) {
                myArray.add(storageStudent.getMap().get(i).getName());
            }
        }
        contextAnswer.setAnswer("��������");
        contextAnswer.setButtonsList(myArray);
        return contextAnswer;
    }

    public ContextAnswer getStudent(Update update) {
        Long id = update.getMessage().getChatId();
        List<String> myArray = new ArrayList<String>(0);
        if (storageContext.get(id).getIdGroup() != null) {
            for (Long i : storageStudent.getMap().keySet()) {
                if (storageStudent.getMap().get(i).getGroup().equals(storageContext.get(id).getIdGroup())) {
                    myArray.add(storageStudent.getMap().get(i).getName());
                }
            }
        }
        contextAnswer.setAnswer("��������");
        contextAnswer.setButtonsList(myArray);
        return contextAnswer;
    }

    public ContextAnswer student(Update update) {
        Long id = update.getMessage().getChatId();

        for (Long i : storageStudent.getMap().keySet()) {
            if (storageStudent.getMap().get(i).getGroup().equals(storageContext.get(id).getIdGroup()) &&
                    storageStudent.getMap().get(i).getName().equals(update.getMessage().getText())) {
                Context context = storageContext.get(id);
                context.setIdStudent(i);
                storageContext.set(id, context);
            }
        }

        //Student student = storageStudent.get(storageContext.get(id).getIdStudent());
        //Progress progress = student.getProgresses(storageContext.get(id).getIdCourse(), storageContext.get(id).getIdTask());

        contextAnswer.setAnswer("�������� �������");
        contextAnswer.setButtonsList(getTask(id));
        return contextAnswer;
    }


    public ContextAnswer unmark(Update update) {
        Long id = update.getMessage().getChatId();
        Long idStudent = storageContext.get(id).getIdStudent();

        Student student = storageStudent.get(idStudent);
        Progress progress = student.getProgresses(storageContext.get(id).getIdCourse(), storageContext.get(id).getIdTask());
        progress.setMark(false);
        student.addProgress(progress);
        storageStudent.getMap().put(idStudent,student);

        contextAnswer.setAnswer("���������!");
        return contextAnswer;
    }

    public ContextAnswer grade(Update update) {
        Long id = update.getMessage().getChatId();
        String massage = update.getMessage().getText().replaceAll("/grade ", "");

        Long idStudent = storageContext.get(id).getIdStudent();

        Student student = storageStudent.get(idStudent);
        Progress progress = student.getProgresses(storageContext.get(id).getIdCourse(), storageContext.get(id).getIdTask());
        progress.setGrade(massage);
        student.addProgress(progress);
        storageStudent.getMap().put(idStudent,student);

        contextAnswer.setAnswer("�������");
        return contextAnswer;
    }

    public ContextAnswer commentTask(Update update) {
        Long id = update.getMessage().getChatId();
        Long idStudent = storageContext.get(id).getIdStudent();

        Student student = storageStudent.get(idStudent);
        Progress progress = student.getProgresses(storageContext.get(id).getIdCourse(), storageContext.get(id).getIdTask());
        String massage = update.getMessage().getText().replaceAll("/com ", "");
        progress.addComment(update.getMessage().getChat().getFirstName() + ": " + massage + "\n");
        student.addProgress(progress);
        storageStudent.getMap().put(idStudent,student);

        contextAnswer.setAnswer("��� ����������� ��������.");
        return contextAnswer;
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
        return "�������";
    }
}
