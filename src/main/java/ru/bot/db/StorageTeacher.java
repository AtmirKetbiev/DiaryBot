package ru.bot.db;

import org.telegram.abilitybots.api.db.DBContext;
import ru.bot.objects.Teacher;

import java.util.Map;

public class StorageTeacher implements StorageUser<Teacher, Long> {

    private Map<Long, Teacher> teacherMap;

    public StorageTeacher(DBContext db) {
        this.teacherMap = db.getMap("Teachers");
    }

    @Override
    public Teacher get(Long id) {
        return teacherMap.get(id);
    }

    @Override
    public void set(Long id, Teacher teacher) {
            this.teacherMap.put(id, teacher);
    }
}
