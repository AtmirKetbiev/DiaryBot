package ru.bot.DB;

import org.telegram.abilitybots.api.db.DBContext;
import ru.bot.objects.Status;
import ru.bot.objects.Teacher;

import java.util.Map;

public class StorageTeacher implements StorageUser<Teacher, Long> {

    private Map<Long, Teacher> teacherMap;
    private DBContext db;

    public StorageTeacher(DBContext db) {
        this.teacherMap = db.getMap("Teachers");
        this.db = db;
    }

    @Override
    public Map<Long, Teacher> getMap() {
        return teacherMap;
    }

    @Override
    public Teacher get(Long id) {
        return teacherMap.get(id);
    }

    @Override
    public void set(Long id, Teacher teacher) {

        StorageContext storageContext = new StorageContext(db);

        storageContext.set(id, new Status());
        if (teacherMap.get(id) == null) {
            this.teacherMap.put(id, teacher);
        }
    }
}
