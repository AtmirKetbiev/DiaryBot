package ru.bot.DB;

import org.telegram.abilitybots.api.db.DBContext;
import ru.bot.objects.Status;

import java.util.Map;

public class StorageContext implements StorageUser<Status, Long> {

    private Map<Long, Status> userContext;

    public StorageContext(DBContext db) {
        this.userContext = db.getMap("userContext");
    }

    public void contextNull(Long id) {
        this.userContext.put(id, new Status());
    }

    @Override
    public Map<Long, Status> getMap() {
        return userContext;
    }

    @Override
    public Status get(Long id) {
        return userContext.get(id);
    }

    @Override
    public void set(Long id, Status status) {
        this.userContext.put(id, status);
    }
}
