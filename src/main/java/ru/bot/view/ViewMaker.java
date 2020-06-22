package ru.bot.view;

public interface ViewMaker <R, V> {
    R make(V v);
}