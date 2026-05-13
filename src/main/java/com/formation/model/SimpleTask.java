package com.formation.model;

public class SimpleTask extends AbstractTask {
    public Priority priority;

    public SimpleTask(Priority priority) {
        this.priority = priority;
    }

    public String getSummary() {
        return "[SIMPLE]" + super.title + "(" + priority + ")";
    }
}
