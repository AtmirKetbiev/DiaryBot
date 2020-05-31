package ru.bot.DB;

import org.telegram.abilitybots.api.db.DBContext;
import ru.bot.objects.Context;
import ru.bot.objects.Student;

import java.util.Map;

public class StorageStudent implements StorageUser<Student, Long> {

    private Map<Long, Student> studentMap;
    private DBContext db;

    public StorageStudent(DBContext db) {
        this.studentMap = db.getMap("Students");
        this.db = db;
    }

    @Override
    public Map<Long, Student> getMap() {
        return studentMap;
    }

    @Override
    public Student get(Long id) {
        return studentMap.get(id);
    }

    @Override
    public void set(Long id, Student student) {
        StorageContext storageContext = new StorageContext(db);

        storageContext.set(id, new Context());
        if (studentMap.get(id) == null) {
            this.studentMap.put(id, student);
        }
    }
}
