package ru.bot.DB;

import java.util.Map;

public interface StorageUser<T, X> {
    Map<X, T> getMap();
    T get(X x);
    void set(X x, T t);
}
