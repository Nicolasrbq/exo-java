package com.formation.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TaskService {
    void addTask(AbstractTask task);
    void completeTask(TaskId id);
    List<AbstractTask> getActiveTasks();
    List<AbstractTask> getTasksByPriority(Priority priority);
    Optional<AbstractTask> findTask(TaskId id);
    Map<Boolean, List<AbstractTask>> partitionByStatus();
}
