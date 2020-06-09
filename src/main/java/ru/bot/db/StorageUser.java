package ru.bot.db;

public interface StorageUser<T, X> {
    T get(X x);
    void set(X x, T t);
}
