package com.formation.service;

import com.formation.dto.TaskDTO;
import com.formation.dto.TaskMapper;
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

    /**
     * Les constantes sont généralement déclarées en premier dans la classe, avant les champs d'instance
     * et les constructeurs.
     */
    private static final Logger log = LoggerFactory.getLogger(DefaultTaskService.class);

    /**
     * problème d'encapsulation : le repository est public, n'importe qui peut le modifier. Il faut le rendre private
     */
    private TaskRepository taskRepository;

    /**
     * Inversion de controle : la classe ne s'occupe pas de creer son repository, elle le recoit en parametre
     */
    public DefaultTaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public TaskDTO addTask(AbstractTask task) {
        if (task == null) {
            throw new InvalidTaskException("task", "Task title cannot be empty");
        }

        if (task.getTitle() == null || task.getTitle().isEmpty()) {
            log.warn("Tentative de creation avec titre invalide");
        }

        log.info("Creation d'une nouvelle tache : {}", task.getTitle());

        this.taskRepository.save(task);
        // il faut retourner le DTO de la tache créée, pas null : return new TaskMapper().toDto(task);
        return new TaskMapper().toDto(task);
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
        // tu as déjà filtré les tasks actives dans le repository tu n'as plus besoin de filtrer ici
        //return this.taskRepository.findAllActiveTask().stream().filter(task -> !task.done).toList();
        return this.taskRepository.findAllActiveTask();
    }

    @Override
    public List<AbstractTask> getTasksByPriority(Priority priority) {
        return this.taskRepository.findAll().stream()                      // priority est un objet, tu ne peux pas utiliser priority == priority pour le comparer, 
                //tu dois utiliser equals() : task.priority.equals(priority)
                .filter(task -> task instanceof SimpleTask simpleTask && simpleTask.getPriority().equals(priority))
                .toList();
    }

    /**
     * il faut retourner un Optional<TaskDTO> et pas un Optional<AbstractTask>,
     * on n'expose pas la structure interne de nos antités.
     */
    @Override
    public TaskDTO findTask(TaskId id) {
        Optional<AbstractTask> task = this.taskRepository.findById(id);
        if (!task.isPresent()) {
            log.warn("Tache non trouvee : {}", id);
            throw new TaskNotFoundException(id.id());
        }
        return new TaskMapper().toDto(task.get());
    }

    @Override
    public Map<Boolean, List<AbstractTask>> partitionByStatus() {
        return this.taskRepository.findAll().stream()
                .collect(Collectors.partitioningBy(AbstractTask::isDone));
    }

    @Override
    public TaskDTO updateTask(AbstractTask task, int id) {
        Optional<AbstractTask> existedTask = this.taskRepository.findById(new TaskId(id));
        if (!existedTask.isPresent()) {
            log.warn("Tache non trouvee : {}", task.getId());
            throw new TaskNotFoundException(task.getId().id());
        }
        this.taskRepository.save(task);
        log.info("Tache mise a jour : {}", task.getId());
        return new TaskMapper().toDto(task);
    }
}
