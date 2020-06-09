package ru.bot.view;

import ru.bot.objects.Task;

public class ViewTask implements StringMaker<Task> {

    @Override
    public String make(Task task) {
        return "Название задания:\n" + task.getName() +
                "\n\nОписание задания:\n" + task.getDescription() +
                "\n\nSoft deadline:\n" + task.getSoft() +
                "\n\nHard deadline:\n" + task.getHard();
    }
}