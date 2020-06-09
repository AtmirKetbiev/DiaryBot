package ru.bot.view;

import ru.bot.objects.Task;

public class ViewTask implements StringMaker<Task> {

    @Override
    public String make(Task task) {
        return "�������� �������:\n" + task.getName() +
                "\n\n�������� �������:\n" + task.getDescription() +
                "\n\nSoft deadline:\n" + task.getSoft() +
                "\n\nHard deadline:\n" + task.getHard();
    }
}