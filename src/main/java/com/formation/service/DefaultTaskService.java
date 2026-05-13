package com.formation.service;

import com.formation.model.*;
import org.example.model.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DefaultTaskService implements TaskService {

    public TaskRepository taskRepository;

    public DefaultTaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public void addTask(AbstractTask task) {
        this.taskRepository.save(task);
    }

    @Override
    public void completeTask(TaskId id) {
        this.taskRepository.findById(id).ifPresent(task -> {
            try {
                task.markAsDone();
            } catch (Exception e) {
                throw new Exception("Task could not be marked as done", e);
            }
        });
    }

    @Override
    public List<AbstractTask> getActiveTasks() {
        return this.taskRepository.findAll();
    }

    @Override
    public List<AbstractTask> getTasksByPriority(Priority priority) {
        return List.of();
    }

    @Override
    public Optional<AbstractTask> findTask(TaskId id) {
        return Optional.empty();
    }

    @Override
    public Map<Boolean, List<AbstractTask>> partitionByStatus() {
        return Map.of();
    }
}
