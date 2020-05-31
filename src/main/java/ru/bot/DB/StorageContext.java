package ru.bot.DB;

import org.telegram.abilitybots.api.db.DBContext;
import ru.bot.objects.Context;

import java.util.Map;

public class StorageContext implements StorageUser<Context, Long> {

    private Map<Long, Context> userContext;

    public StorageContext(DBContext db) {
        this.userContext = db.getMap("userContext");
    }

    public void contextNull(Long id) {
        this.userContext.put(id, new Context());
    }

    @Override
    public Map<Long, Context> getMap() {
        return userContext;
    }

    @Override
    public Context get(Long id) {
        return userContext.get(id);
    }

    @Override
    public void set(Long id, Context status) {
        this.userContext.put(id, status);
    }
}
