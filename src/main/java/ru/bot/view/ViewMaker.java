package ru.bot.view;

import org.telegram.abilitybots.api.db.DBContext;

public interface ViewMaker <R, V> {
    R make(V v);
}