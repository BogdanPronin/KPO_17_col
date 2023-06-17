package com.github.bogdan.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;
import java.util.Objects;

@DatabaseTable(tableName = "project")
public class Project {
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String name;

    @DatabaseField
    private String task;


    @DatabaseField
    private String deadline;

    private List<User> users;
    public Project() {
    }

    public Project(String name, String task, String deadline, List<User> users) {
        this.name = name;
        this.task = task;
        this.deadline = deadline;
        this.users = users;
    }

    public Project(String name, String task, String deadline) {
        this.name = name;
        this.task = task;
        this.deadline = deadline;
    }

    public Project(int id, String name, String task, String deadline, List<User> users) {
        this.id = id;
        this.name = name;
        this.task = task;
        this.deadline = deadline;
        this.users = users;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return id == project.id && Objects.equals(name, project.name) && Objects.equals(task, project.task) && Objects.equals(deadline, project.deadline) && Objects.equals(users, project.users);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, task, deadline, users);
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", task='" + task + '\'' +
                ", deadline='" + deadline + '\'' +
                ", users=" + users +
                '}';
    }
}
