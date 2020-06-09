package ru.bot.db;

import org.telegram.abilitybots.api.db.DBContext;
import ru.bot.objects.Student;

import java.util.Map;

public class StorageStudent implements StorageUser<Student, Long> {

    private Map<Long, Student> studentMap;

    public StorageStudent(DBContext db) {
        this.studentMap = db.getMap("Students");
    }

    @Override
    public Student get(Long id) {
        return studentMap.get(id);
    }

    @Override
    public void set(Long id, Student student) {
        this.studentMap.put(id, student);
    }
}
