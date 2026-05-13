package com.formation.model;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    void save(AbstractTask task);
    Optional<AbstractTask> findById(TaskId id);
    List<AbstractTask> findAll();
    void delete(TaskId id);
}
