package com.formation.repository;

import com.formation.model.AbstractTask;
import com.formation.model.TaskId;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    void save(AbstractTask task);
    Optional<AbstractTask> findById(TaskId id);
    List<AbstractTask> findAll();
    void delete(TaskId id);
}
