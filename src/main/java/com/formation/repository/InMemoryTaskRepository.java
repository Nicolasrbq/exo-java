package com.formation.repository;

import com.formation.model.AbstractTask;
import com.formation.model.TaskId;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryTaskRepository implements TaskRepository {

    public Map<TaskId, AbstractTask> saved;

    public InMemoryTaskRepository() {
        saved = new HashMap<>();
    }

    public void save(AbstractTask task) {
        saved.put(task.getId(), task);
    }

    @Override
    public Optional<AbstractTask> findById(TaskId id) {
        return Optional.ofNullable(saved.get(id));
    }

    @Override
    public List<AbstractTask> findAll() {
        return this.saved.values().stream()
                .filter(task -> !task.done)
                .toList();
    }

    @Override
    public void delete(TaskId id) {
        saved.remove(id);
    }
}
