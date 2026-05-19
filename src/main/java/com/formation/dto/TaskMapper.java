package com.formation.dto;

import com.formation.model.AbstractTask;
import com.formation.model.Priority;
import com.formation.model.RecurringTask;
import com.formation.model.SimpleTask;
import com.formation.service.DefaultTaskService;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.logging.Logger;

public class TaskMapper {

    private static final Logger log = (Logger) LoggerFactory.getLogger(TaskMapper.class);

    public TaskDTO toDto(AbstractTask task) {
        if (task == null) {
            throw new IllegalArgumentException("task ne peut pas etre null");
        }


        if (task instanceof SimpleTask simpleTask) {
            log.config("Mapping entité -> DTO (SimpleTask)");
            return new TaskDTO(
                    task.getId().id(),
                    task.title,
                    "SIMPLE",
                    simpleTask.priority.name(),
                    null,
                    task.done,
                    task.getCreateAt().toString(),
                    task.getSummary()
            );
        }

        if (task instanceof RecurringTask recurringTask) {
            log.config("Mapping entité -> DTO (RecurringTask)");
            return new TaskDTO(
                    task.getId().id(),
                    task.title,
                    "RECURRING",
                    null,
                    recurringTask.intervalDays,
                    task.done,
                    task.getCreateAt().toString(),
                    task.getSummary()
            );
        }

        throw new IllegalArgumentException("Type de task non supporté : " + task.getClass().getName());
    }

    public List<TaskDTO> toDtoList(List<AbstractTask> tasks) {
        return tasks.stream().map(this::toDto).toList();
    }

    public SimpleTask fromDto(TaskDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("dto ne peut pas etre null");
        }

        if (dto.title() == null || dto.title().isBlank()) {
            throw new IllegalArgumentException("title ne peut pas etre blank");
        }

        SimpleTask task = new SimpleTask(Priority.valueOf(dto.priority()));
        task.title = dto.title();
        return task;
    }
}
