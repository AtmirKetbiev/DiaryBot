import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.abilitybots.api.util.AbilityExtension;

import java.util.ArrayList;
import java.util.Map;

public class DBManager implements AbilityExtension {

    private DBContext db;
    private Map<Long, Teacher> teacherMap;
    private Map<Long, Student> studentMap;
    private Map<Integer, Course> courseMap;
    private Map<Integer, Task> taskMap;

    private Map<Long, Status> userStatus;

    private Map<Long, Teacher> createTeacher;
    private Map<Long, Student> createStudent;
    private Map<Long, Course> createCourse;
    private Map<Long, Task> createTask;

    public DBManager() {
    }

    DBManager(DBContext db) {
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

    /** Статус юзера */

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


    /** Курсы */
    public Map<Integer, Course> getCourseMap() {
        return courseMap;
    }
    public void setCourseMap(Map<Integer, Course> courseMap) {
        this.courseMap = courseMap;
    }
    public void addCourseMap(Course course) {
        this.courseMap.put(courseMap.size(), course);
        Teacher teacher = teacherMap.get(course.getIdTeacher());
        int i = courseMap.size();
        teacher.addCourses(courseMap.size()-1);
        this.teacherMap.put(course.getIdTeacher(), teacher);
    }
    public  int getIdByNameCourse(String name, Long idTeacher) {
        for (int i : courseMap.keySet()) {
            if (courseMap.get(i).getName().equals(name) && courseMap.get(i).getIdTeacher().equals(idTeacher)) {
                return i;
            }
        }
        return 0;
    }

    /** Задания */
    public Map<Integer, Task> getTaskMap() {
        return taskMap;
    }
    public void setTaskMap(Map<Integer, Task> taskMap) {
        this.taskMap = taskMap;
    }
    public void addTaskMap(Task task) {
        this.taskMap.put(taskMap.size(), task);
        Course course = courseMap.get(task.getIdCourse());
        course.addTask(taskMap.size()-1);
        this.courseMap.put(task.getIdCourse(), course);
    }


    /** Преподаватели */
    public void addTeacher(Long id, Teacher teacher) {
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
    /** Студенты */
    public Map<Long, Student> getStudentMap() {
        return studentMap;
    }
    public void setStudentMap(Map<Long, Student> studentMap) {
        this.studentMap = studentMap;
    }




    /** Добавление студентов */
    public Map<Long, Student> getCreateStudent() {
        return createStudent;
    }
    public void setCreateStudent(Map<Long, Student> createStudent) {
        this.createStudent = createStudent;
    }

    /** Создание курса */
    public Map<Long, Course> getCreateCourse() {
        return createCourse;
    }
    public void setCreateCourse(Course course) {
        this.createCourse.put(course.getIdTeacher(), course);
    }
    public void removeCreateCourse(Long id) {
        createCourse.remove(id);
    }

    /** Создание задания */
    public Map<Long, Task> getCreateTask() {
        return createTask;
    }
    public void setCreateTask(Long id, Task task) {
        this.createTask.put(id, task);
    }
    public void removeCreateTask(Long id) {
        createTask.remove(id);
    }





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
