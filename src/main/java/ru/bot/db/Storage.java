package ru.bot.db;

public interface Storage <T, X, Y> {
    T get(X x);
    void set(T t);
    void remove(T t);
    X getIdByName(String name, Y y);
}