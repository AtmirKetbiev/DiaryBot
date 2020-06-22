package ru.bot.extension;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class Keyboard {

    private static ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

    public static SendMessage listKeyboard(List<String> text, Update update, String answer) {
        SendMessage sendMessage = new SendMessage().setChatId(update.getMessage().getChatId());
        sendMessage.setText("Пусто");

        ArrayList<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();

        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        for (String s : text) {
            keyboardRow.add(s);
            keyboard.add(keyboardRow);
            keyboardRow = new KeyboardRow();
        }

        if (!text.isEmpty() ) {
            keyboardRow.add("Назад");
            keyboard.add(keyboardRow);
            sendMessage.setText(answer);
        }

        replyKeyboardMarkup.setKeyboard(keyboard);

        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        return sendMessage;
    }

    public static SendMessage constantKeyboard(String[][] text, Update update, String answer) {
        SendMessage sendMessage = new SendMessage().setChatId(update.getMessage().getChatId());
        ArrayList<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardRow;

        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        for (String[] array : text) {
            keyboardRow = new KeyboardRow();
            for (String s : array) {
                keyboardRow.add(s);
            }
            keyboard.add(keyboardRow);
        }

        if (!text[0][0].equals("Добавить курс")) {
            keyboardRow = new KeyboardRow();
            keyboardRow.add("Назад");
            keyboard.add(keyboardRow);
        }

        replyKeyboardMarkup.setKeyboard(keyboard);
        sendMessage.setText(answer);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        return sendMessage;
    }
}
