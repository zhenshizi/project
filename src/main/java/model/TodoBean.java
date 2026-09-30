package model;

import java.io.Serializable;

public class TodoBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private int userId;
    private String todo;
    private String idea;
    private boolean status;

    public TodoBean() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTodo() {
        return todo;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }

    public String getIdea() {
        return idea;
    }

    public void setIdea(String idea) {
        this.idea = idea;
    }

    public boolean isStatus() {
        return status;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
