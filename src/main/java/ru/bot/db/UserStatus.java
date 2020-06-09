package ru.bot.db;

import org.telegram.abilitybots.api.db.DBContext;
import ru.bot.extension.Constants;

import java.util.Map;

public class UserStatus {

    private Map<Long, String> userStatus;

    public UserStatus(DBContext db) {
        this.userStatus = db.getMap("userStatus");
    }

    public void setUserStatus(Long id, String status) {
        this.userStatus.put(id, status);
    }

    public void remove(Long id) {
        if (!userStatus.isEmpty()) {
            userStatus.remove(id);
        }
    }

    public boolean isTeacher(Long id) {
        return userStatus.get(id) != null && userStatus.get(id).equals(Constants.TEACHER);
    }

    public boolean isStudent(Long id) {
        return userStatus.get(id) != null && userStatus.get(id).equals(Constants.STUDENT);
    }
}
