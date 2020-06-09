package ru.bot.view;

public interface StringMaker<V> extends ViewMaker<String, V> {
    @Override
    String make(V v);
}