package ru.bot.objects;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Task implements Serializable {

    private String code;
    private String idCourse;
    private String name;
    private String description;
    private Date soft;
    private Date hard;

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }

    public String getIdCourse() {
        return idCourse;
    }
    public void setIdCourse(String idCourse) {
        this.idCourse = idCourse;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public Date getSoft() {
        return soft;
    }
    public void setSoft(String soft) throws ParseException {
        this.soft = new SimpleDateFormat("dd/MM/yyyy").parse(soft);
    }

    public Date getHard() {
        return hard;
    }
    public void setHard(String hard) throws ParseException {
        this.hard = new SimpleDateFormat("dd/MM/yyyy").parse(hard);
    }

    public boolean check() {
        if (name == null)  return false;
        if (description == null)  return false;
        if (soft == null)  return false;
        return hard != null;
    }
}
