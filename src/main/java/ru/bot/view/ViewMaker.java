package ru.bot.view;

import org.telegram.abilitybots.api.db.DBContext;
import ru.bot.objects.Course;

public interface ViewMaker <V> {
    String make(V v, DBContext db);
}