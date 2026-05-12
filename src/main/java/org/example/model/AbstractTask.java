package org.example.model;

import java.time.LocalDate;

public abstract class AbstractTask {
    public TaskId id;
    public String title;
    public boolean done;
    public LocalDate createAt;

    public AbstractTask() {
        this.id = new TaskId(0);
        this.title = "";
        this.done = false;
        this.createAt = LocalDate.now();
    }

    public TaskId getId() {
        return id;
    }

    public LocalDate getCreateAt() {
        return createAt;
    }

    public abstract String getSummary();

    public void markAsDone() {
        this.done = true;
    }

    @Override
    public String toString() {
        return super.toString() + "[id=" + id + ", hashCode=" + super.hashCode() + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        return false;
    }

}
