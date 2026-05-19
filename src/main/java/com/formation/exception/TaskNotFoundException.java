package com.formation.exception;

public class TaskNotFoundException extends RuntimeException {
    private final int taskId;

    public TaskNotFoundException(int id) {
        super("Tâche introuvable avec l'id : " + id);
        this.taskId = id;
    }

    public int getTaskId() { return taskId; }
}
