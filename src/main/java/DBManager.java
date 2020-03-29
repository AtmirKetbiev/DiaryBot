import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.abilitybots.api.util.AbilityExtension;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class DBManager implements AbilityExtension {

    private DBContext db;
    private Map<Long, Teacher> teacherMap;
    private Map<Long, Student> studentMap;
    private Map<Integer, Course> courseMap;
    private Map<Integer, Task> taskMap;

    private Map<Long, Status> userStatus;

    private Map<Integer, Group> groups;

    private Map<Long, Teacher> createTeacher;
    private Map<Long, Student> createStudent;
    private Map<Long, Course> createCourse;
    private Map<Long, Task> createTask;

    public DBManager(DBContext db) {
        this.db = db;
        this.teacherMap = db.getMap("Teachers");
        this.studentMap = db.getMap("Students");
        this.courseMap = db.getMap("Courses");
        this.taskMap = db.getMap("Tasks");

        this.userStatus = db.getMap("userStatus");

        this.createTeacher = db.getMap("createTeacher");
        this.createStudent = db.getMap("createStudent");
        this.createCourse = db.getMap("createCourse");
        this.createTask = db.getMap("createTask");
    }


    /** Статус юзера..................................................................................................*/
    public void statusNull(Long id) {
        this.userStatus.put(id, new Status());
    }

    public Map<Long, Status> getUserStatus() {
        return userStatus;
    }

    public Status getUserStatus(Long id) {
        return userStatus.get(id);
    }

    public void addUserStatus(Long id, Status status) {
        this.userStatus.put(id, status);
    }

    public void removeUserStatus(Long id) {
        this.userStatus.remove(id);
    }


    /** Курсы.........................................................................................................*/
    public Map<Integer, Course> getCourseMap() {
        return courseMap;
    }

    public Course getCourse(Integer id) {
        return courseMap.get(id);
    }

    public void setCourseMap(Map<Integer, Course> courseMap) {
        this.courseMap = courseMap;
    }

    public void addCourseMap(Course course) {
        Teacher teacher = teacherMap.get(course.getIdTeacher());
        int idCourse = userStatus.get(course.getIdTeacher()).getIdCourse();
        if (idCourse != -1) {
            if (userStatus.get(course.getIdTeacher()).getIdTask() == -1) {
                course.addTask(this.courseMap.get(idCourse).getIdTasks());
            }
            this.courseMap.put(idCourse, course);
        } else {
            this.courseMap.put(courseMap.size(), course);
            teacher.addCourses(courseMap.size()-1);
            this.teacherMap.put(course.getIdTeacher(), teacher);
        }
    }

    public void removeCourse(int idCourse, Long idTeacher) {
        Teacher teacher = teacherMap.get(idTeacher);
        teacher.removeCourse(idCourse);
        this.teacherMap.put(idTeacher, teacher);
    }

    public  int getIdByNameCourse(String name, Long idTeacher) {
        for (int i : courseMap.keySet()) {
            if (courseMap.get(i).getName().equals(name) && courseMap.get(i).getIdTeacher().equals(idTeacher)) {
                return i;
            }
        }
        return 0;
    }


    /** Задания ......................................................................................................*/
    public Map<Integer, Task> getTaskMap() {
        return taskMap;
    }

    public Task getTask(Integer id) {
        return taskMap.get(id);
    }

    public void setTaskMap(Map<Integer, Task> taskMap) {
        this.taskMap = taskMap;
    }

    public void addTaskMap(Task task) {
        Course course = courseMap.get(task.getIdCourse());
        if (userStatus.get(course.getIdTeacher()).getIdTask() != -1) {
            this.taskMap.put(userStatus.get(course.getIdTeacher()).getIdTask(), task);
        } else {
            this.taskMap.put(taskMap.size(), task);
            course.addTask(taskMap.size()-1);
            this.courseMap.put(task.getIdCourse(), course);
        }
    }

    public int getIdByNameTask(String name, int idCourse) {
        for (int i : taskMap.keySet()) {
            if (taskMap.get(i).getName().equals(name) && taskMap.get(i).getIdCourse() == (idCourse)) {
                return i;
            }
        }
        return 0;
    }


    /** Преподы .....................................................................................................*/
    public void addTeacher(Long id, Teacher teacher) {
        this.userStatus.put(id, new Status());
        if (teacherMap.get(id) == null) {
            this.teacherMap.put(id, teacher);
        }
    }

    public Teacher getTeacher(long id) {
        return teacherMap.get(id);
    }

    public Map<Long, Teacher> getTeacherMap() {
        return teacherMap;
    }


    /** Студенты .....................................................................................................*/
    public void addStudent(Long id, Student student) {
        if (teacherMap.get(id) == null) {
            this.studentMap.put(id, student);
        }
    }

    public Student getStudent(long id) {
        return studentMap.get(id);
    }

    public Map<Long, Student> getStudentMap() {
        return studentMap;
    }


    /** Добавление студентов..........................................................................................*/
    public Map<Long, Student> getCreateStudent() {
        return createStudent;
    }

    public void setCreateStudent(Map<Long, Student> createStudent) {
        this.createStudent = createStudent;
    }

    /** Создание курса................................................................................................*/
    public Map<Long, Course> getCreateCourse() {
        return createCourse;
    }

    public void setCreateCourse(Course course) {
        this.createCourse.put(course.getIdTeacher(), course);
    }

    public void removeCreateCourse(Long id) {
        createCourse.remove(id);
    }

    /** Создание задания..............................................................................................*/
    public Map<Long, Task> getCreateTask() {
        return createTask;
    }

    public void setCreateTask(Long id, Task task) {
        this.createTask.put(id, task);
    }

    public void removeCreateTask(Long id) {
        createTask.remove(id);
    }



    /** Группы........................................................................................................*/
    private Map<Integer,Group> group() throws Exception {
        FileReader fileReader = new FileReader("group.txt");
        Scanner scanner = new Scanner(fileReader);
        Group group = new Group();
        while (scanner.hasNextLine()) {
            group.setGroup(scanner.nextLine().split(".")[0], scanner.nextLine().split(".")[1]);
            groups.put(groups.size(), group);
        }
        fileReader.close();
        return groups;
    }

    public Group getGroup(long id) {
        return groups.get(id);
    }

    public int getIdByNameGroup(String name) {
        for (int i : groups.keySet()) {
            if (groups.get(i).getName().equals(name)) {
                return i;
            }
        }
        return -1;
    }

//    public ArrayList<Long> getStudentByNameGroup(String name, Long id) {
//    }

    public void deleteCourses() {
        for (int i : courseMap.keySet()) {
            this.courseMap.remove(i);
        }
    }
    public void deleteTasks() {
        for (int i : taskMap.keySet()) {
            this.taskMap.remove(i);
        }
    }
    public void deleteTeacher() {
        for (Long i : teacherMap.keySet()) {
            this.teacherMap.remove(i);
        }
    }
    public void deleteStudent() {
        for (Long i : studentMap.keySet()) {
            this.studentMap.remove(i);
        }
    }
    public void deleteCreate() {
        for (Long i : createTask.keySet()) {
            this.createTask.remove(i);
        }
        for (Long i : createCourse.keySet()) {
            this.createCourse.remove(i);
        }
        for (Long i : createStudent.keySet()) {
            this.createStudent.remove(i);
        }
        for (Long i : createTeacher.keySet()) {
            this.createTeacher.remove(i);
        }
    }
    public void deleteStatus() {
        for (Long i : userStatus.keySet()) {
            this.userStatus.remove(i);
        }
    }

}
