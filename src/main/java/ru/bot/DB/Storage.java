package ru.bot.DB;

import java.util.Map;

public interface Storage <T, X, Y> {
    Map<X, T> getMap();
    T get(X x);
    void set(T t);
    void remove(X x, Y y);
    X getIdByName(String name, Y y);
}