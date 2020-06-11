package ru.bot.db;

import org.telegram.abilitybots.api.db.DBContext;
import ru.bot.objects.Progress;

import java.util.Map;

public class StorageProgress implements Storage<Progress, String, Long> {

    private Map<String, Progress> progressMap;

    public StorageProgress(DBContext db){
        this.progressMap = db.getMap("Progresses");
    }

    @Override
    public Progress get(String s) {
        return progressMap.get(s);
    }

    @Override
    public void set(Progress progress) {
        progressMap.put(progress.getIdProgress(), progress);
    }

    @Override
    public void remove(Progress progress) {
        this.progressMap.remove(progress.getIdProgress());
    }

    @Override
    public String getIdByName(String name, Long aLong) {
        return null;
    }

    public String getId(Long idStudent, String idCourse, String idTask) {
        for (String i : progressMap.keySet()) {
            if (progressMap.get(i).getIdStudent().equals(idStudent) &&
                    progressMap.get(i).getIdCourse().equals(idCourse) &&
                    progressMap.get(i).getIdTask().equals(idTask)) {
                return i;
            }
        }
        return "";
    }
}
