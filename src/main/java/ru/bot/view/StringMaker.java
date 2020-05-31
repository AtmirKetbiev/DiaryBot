package ru.bot.view;

public interface StringMaker<String, V> extends ViewMaker<String, V> {
    @Override
    String make(V v);
}