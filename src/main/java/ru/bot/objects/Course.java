package ru.bot.objects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Course implements Serializable {

    private String code;
    private String name;
    private String description;
    private Long idTeacher;
    private List<String> tasks = new ArrayList<>();
    private List<String> groups = new ArrayList<>();
    private List<String> links = new ArrayList<>();
    private List<Long> students = new ArrayList<>();

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
    public void setIdTask(String idTask) {
        if (!tasks.contains(idTask)) {
            tasks.add(idTask);
        }

    }
    public void setIdTask(List<String> task) {
        this.tasks = task;
    }
    public void removeTask(String id) {
        tasks.remove(id);
    }

    public void addGroup(String str) {
        if (!groups.contains(str)) {
            groups.add(str);
        }
    }
    public List<String> getGroups() {
        return groups;
    }
    public void removeGrop(Integer id) {
        groups.remove(id);
    }

    public List<String> getLinks() {
        return links;
    }
    public void setLinks(String link) {
        links.add(link);
    }

    public List<Long> getStudents() {
        return students;
    }
    public void setStudents(Long idStudents) {
        if (!groups.contains(idStudents)) {
            this.students.add(idStudents);
        }
    }
    public void removeStudents(Long idStudents) {
        this.students.remove(idStudents);
    }

    public boolean check() {
        if (name == null)  return false;
        if (description == null)  return false;
        return tasks != null;
    }

}