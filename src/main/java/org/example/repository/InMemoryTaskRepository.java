package org.example.repository;

import org.example.model.AbstractTask;
import org.example.model.TaskId;
import org.example.model.TaskRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
        return Optional.empty();
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
