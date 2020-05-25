package ru.bot.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Course implements Serializable {

    //private static final long serialVersionUID = 1L;

    private String code;
    private String name;
    private String description;
    private Long idTeacher;
    private List<String> tasks;
    private List<String> group;
    private List<String> links;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public Long getIdTeacher() {
        return idTeacher;
    }

    public void setIdTeacher(Long idTeacher) {
        this.idTeacher = idTeacher;
    }

    public List<String> getIdTasks() {
        return tasks;
    }

    public void setIdTasks(String id) {
        if (tasks == null) {
            ArrayList<String> coue = new ArrayList<>();
            coue.add(id);
            this.tasks = coue;
        } else {
            tasks.add(id);
        }
    }

    public void setIdTasks(List<String> task) {
        this.tasks = task;
    }

    public void removeTask(String id) {
        tasks.remove(id);
    }

    public void addGroup(String str) {
        if (group == null) {
            List<String> newGroupList = new ArrayList<>();
            newGroupList.add(str);
            this.group = newGroupList;
        } else {
            group.add(str);
        }
    }

    public List<String> getGroup() {
        return group;
    }

    public void removeGrop(Integer id) {
        group.remove(id);
    }

    public List<String> getLinks() {
        return links;
    }

    public void setLinks(String links) {
        if (links == null) {
            ArrayList<String> array = new ArrayList<>();
            array.add(links);
            this.links = array;
        } else {
            tasks.add(links);
        }
    }

    public boolean check() {
        if (name == null)  return false;
        if (description == null)  return false;
        //if (idTasks == null)  return false;
        return true;
    }

}
