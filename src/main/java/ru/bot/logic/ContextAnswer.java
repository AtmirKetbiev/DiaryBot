package ru.bot.logic;

import java.util.List;

public class ContextAnswer {
    private List<String> buttonsList;
    private String answer;

    public List<String> getButtonsList() {
        return buttonsList;
    }

    public void setButtonsList(List<String> buttonsList) {
        this.buttonsList = buttonsList;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
