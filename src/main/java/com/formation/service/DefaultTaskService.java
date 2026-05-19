package com.formation.service;

import com.formation.dto.TaskDTO;
import com.formation.exception.InvalidTaskException;
import com.formation.exception.TaskNotFoundException;
import com.formation.model.AbstractTask;
import com.formation.model.Priority;
import com.formation.model.SimpleTask;
import com.formation.model.TaskId;
import com.formation.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class DefaultTaskService implements TaskService {

    public TaskRepository taskRepository;

    public DefaultTaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    private static final Logger log = LoggerFactory.getLogger(DefaultTaskService.class);

    @Override
    public TaskDTO addTask(AbstractTask task) {
        if (task == null) {
            throw new InvalidTaskException("task", "Task title cannot be empty");
        }

        if (task.title == null || task.title.isEmpty()) {
            log.warn("Tentative de creation avec titre invalide");
        }

        log.info("Creation d'une nouvelle tache : {}", task.title);

        this.taskRepository.save(task);
        return null;
    }

    @Override
    public void completeTask(TaskId id) {
        AbstractTask task = this.taskRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Tache non trouvee");
                    return new TaskNotFoundException(id.id());
                });
        task.markAsDone();
        log.info("Tache marquee comme faite : {}", task.getId());
    }

    @Override
    public void deleteTask(TaskId id) {
        this.taskRepository.delete(id);
        log.info("Tache supprimee : {}", id);
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
        return this.taskRepository.findById(id);
    }

    @Override
    public Map<Boolean, List<AbstractTask>> partitionByStatus() {
        return this.taskRepository.findAll().stream()
                .collect(Collectors.partitioningBy(task -> task.done));
    }
}
