package ru.bot.DB;

import org.telegram.abilitybots.api.db.DBContext;

import java.util.Map;

public class UserStatus {

    private Map<Long, String> userStatus;

    public UserStatus(DBContext db) {
        this.userStatus = db.getMap("userStatus");
    }

    public String getUserStatus(Long id) {
        return userStatus.get(id);
    }

    public void setUserStatus(Long id, String status) {
        this.userStatus.put(id, status);
    }

    public void remove(Long id) {
        userStatus.remove(id);
    }
}
