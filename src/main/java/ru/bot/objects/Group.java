package ru.bot.objects;

public class Group {

    private String kafedra;
    private String name;

    public Group(String kafedra, String name) {
        this.kafedra =  kafedra;
        this.name = name;
    }

    public String getKafedra() {
        return kafedra;
    }

    public void setKafedra(String kafedra) {
        this.kafedra = kafedra;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
