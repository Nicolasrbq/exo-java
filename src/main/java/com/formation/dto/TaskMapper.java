package com.formation.dto;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.formation.model.AbstractTask;
import com.formation.model.Priority;
import com.formation.model.RecurringTask;
import com.formation.model.SimpleTask;
import com.formation.model.TaskId;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    private static final Logger log = LoggerFactory.getLogger(TaskMapper.class);

    public TaskDTO toDto(AbstractTask task) {
        if (task == null) {
            throw new IllegalArgumentException("task ne peut pas etre null");
        }


        if (task instanceof SimpleTask simpleTask) {
            log.info("Mapping entité -> DTO (SimpleTask)");
            return new TaskDTO(
                    task.getId().id(),
                    task.getTitle(),
                    "SIMPLE",
                    simpleTask.getPriority().name(),
                    null,
                    task.isDone(),
                    task.getCreateAt().toString(),
                    task.getSummary()
            );
        }

        if (task instanceof RecurringTask recurringTask) {
            log.info("Mapping entité -> DTO (RecurringTask)");
            return new TaskDTO(
                    task.getId().id(),
                    task.getTitle(),
                    "RECURRING",
                    null,
                    recurringTask.getIntervalDays(),
                    task.isDone(),
                    task.getCreateAt().toString(),
                    task.getSummary()
            );
        }

        throw new IllegalArgumentException("Type de task non supporté : " + task.getClass().getName());
    }

    public List<TaskDTO> toDtoList(List<AbstractTask> tasks) {
        return tasks.stream().map(this::toDto).toList();
    }

    // il faut retourner un AbstractTask car un DTO Task peut être un SimpleTask ou un RecurringTask, il faut vérifier le type avant de faire le mapping,
	public AbstractTask fromDto(TaskDTO dto) {
		if (dto == null) {
			throw new IllegalArgumentException("dto ne peut pas etre null");
		}

		if (dto.title() == null || dto.title().isBlank()) {
			throw new IllegalArgumentException("title ne peut pas etre blank");
		}

		AbstractTask task = null;
		// un DTO Task peut être un SimpleTask ou un RecurringTask, il faut vérifier le
		// type avant de faire le mapping,
		if (dto.priority() != null && !dto.priority().isBlank()) {
			log.info("Mapping DTO -> entité (SimpleTask)");
			task = new SimpleTask(new TaskId(dto.id()), dto.title(), Priority.valueOf(dto.priority()));
		} else if (dto.intervalDays() != null) {
			log.info("Mapping DTO -> entité (RecurringTask)");
			task = new RecurringTask(new TaskId(dto.id()), dto.title(), dto.intervalDays());
		}
		return task;
    }
}
