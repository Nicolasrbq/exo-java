package com.formation.service;

import com.formation.model.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class DefaultTaskService implements TaskService {

    public TaskRepository taskRepository;

    public DefaultTaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public void addTask(AbstractTask task) {
        if (task == null) {
            throw new IllegalArgumentException("Task title cannot be empty");
        }

        this.taskRepository.save(task);
    }

    @Override
    public void completeTask(TaskId id) {
        AbstractTask task = this.taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found: " + id));
        task.markAsDone();
    }

    @Override
    public List<AbstractTask> getActiveTasks() {
        return this.taskRepository.findAll().stream().filter(task -> !task.done).toList();
    }

    @Override
    public List<AbstractTask> getTasksByPriority(Priority priority) {
        return this.taskRepository.findAll().stream()
                .filter(task -> task instanceof SimpleTask simpleTask && simpleTask.priority == priority)
                .toList();
    }

    @Override
    public Optional<AbstractTask> findTask(TaskId id) {
        return Optional.empty();
    }

    @Override
    public Map<Boolean, List<AbstractTask>> partitionByStatus() {
        return this.taskRepository.findAll().stream()
                .collect(Collectors.partitioningBy(task -> task.done));
    }
}
