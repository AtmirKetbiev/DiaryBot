package ru.bot.view;

import org.telegram.abilitybots.api.db.DBContext;

public interface ViewMaker <V> {
    String make(V v, DBContext db);
}