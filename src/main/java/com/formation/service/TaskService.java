package com.formation.service;

import com.formation.dto.TaskDTO;
import com.formation.model.AbstractTask;
import com.formation.model.Priority;
import com.formation.model.TaskId;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TaskService {
    TaskDTO addTask(AbstractTask task);
    TaskDTO updateTask(AbstractTask task, int id);
    void completeTask(TaskId id);
    void deleteTask(TaskId id);
    List<AbstractTask> getActiveTasks();
    List<AbstractTask> getTasksByPriority(Priority priority);
    TaskDTO findTask(TaskId id);
    Map<Boolean, List<AbstractTask>> partitionByStatus();
}
