package com.formation.controller;

import com.formation.dto.TaskDTO;
import com.formation.dto.TaskMapper;
import com.formation.model.AbstractTask;
import com.formation.model.TaskId;
import com.formation.service.TaskService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;
    private final TaskMapper taskMapper;

    public TaskController(TaskService taskService, TaskMapper taskMapper) {
        this.taskService = taskService;
        this.taskMapper = taskMapper;
    }

    public List<TaskDTO> getAllActiveTasks() {
        return taskService.getActiveTasks().stream().map(taskMapper::toDto).toList();
    }

    /**
     * Ne jamais retourner une entité du domaine vers l'extérieur, toujours retourner un DTO
     * on ne retourne pas un optional vers l'extérieur, on à géré l'absence de tache dans le service avec une exception TaskNotFoundException, 
     * donc ici on peut retourner directement un TaskDTO, 
     * @param id
     * @return
     */
    public TaskDTO getTaskById(int id) { 
        return taskService.findTask(new TaskId(id));
    }

    public TaskDTO createTask(TaskDTO dto) {
        return taskService.addTask(taskMapper.fromDto(dto));
    }
    
    public TaskDTO updateTask(TaskDTO dto, int id) {
        return taskService.updateTask(taskMapper.fromDto(dto), id);
    }

    public void completeTask(int id) {
        taskService.completeTask(new TaskId(id));
    }
    
    public void deleteTask(int id) {
        taskService.deleteTask(new TaskId(id));
    }
}
