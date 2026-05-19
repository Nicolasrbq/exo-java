package fr.formation.taskmanager.service;

import com.formation.dto.TaskDTO;
import com.formation.exception.InvalidTaskException;
import com.formation.exception.TaskNotFoundException;
import com.formation.model.*;
import com.formation.repository.InMemoryTaskRepository;
import com.formation.repository.TaskRepository;
import com.formation.service.DefaultTaskService;
import com.formation.service.TaskService;
import org.junit.jupiter.api.*;

import org.slf4j.Logger;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DefaultTaskServiceTest {
    private TaskRepository repository;
    private TaskService service;
    @BeforeEach // Exécuté avant chaque test ® isolation garantie
    void setUp() {
        repository = new InMemoryTaskRepository();
        service = new DefaultTaskService(repository);
    }
    @Test
    @DisplayName("Doit créer une tâche et la retrouver par id")
    void shouldCreateAndFindTask() {
// Given — contexte
        var task = new SimpleTask(new TaskId(1), "Rédiger les tests", Priority.HIGH);
// When — action
        service.addTask(task);
// Then — assertions
        var found = service.findTask(new TaskId(1));
        assertTrue(found.isPresent());
        assertEquals("Rédiger les tests", found.get().getTitle());
    }

    @Test
    @DisplayName("Doit appeler addTask() avec titre vide")
    void shouldThrowWhenTitleIsBlank() {
        SimpleTask task = new SimpleTask(new TaskId(1), "", Priority.HIGH);
        service.addTask(task);

        assertThrows(InvalidTaskException.class, () -> service.addTask(task));
    }

    @Test
    @DisplayName("Doit appeler addTask() avec null")
    void shouldThrowWhenTitleIsNull() {
        service.addTask(null);

        assertThrows(InvalidTaskException.class, () -> service.addTask(null));
    }

    @Test
    @DisplayName("Doit créer une tâche et appeler completeTask")
    void shouldCompleteTask() {
        SimpleTask task = new SimpleTask(new TaskId(1), "Cool", Priority.HIGH);
        service.addTask(task);
        service.completeTask(task.getId());

        assertTrue(task.done);
    }

    @Test
    @DisplayName("Doit appeler completeTask avec un id inexistant")
    void shouldThrowWhenTaskNotFound() {
        SimpleTask task = new SimpleTask(new TaskId(1), "Cool", Priority.HIGH);
        service.addTask(task);
        TaskId fakeId = new TaskId(190);
        service.completeTask(fakeId);

        assertThrows(TaskNotFoundException.class, () -> service.findTask(fakeId));
    }

    @Test
    @DisplayName("getActiveTasks() Doit retourner 2 éléments")
    void shouldReturnOnlyActiveTasks() {
        SimpleTask task1 = new SimpleTask(new TaskId(1), "Cool", Priority.HIGH);
        task1.markAsDone();
        new RecurringTask();
        new SimpleTask(new TaskId(3), "Cooool", Priority.LOW);

        assertEquals(2, service.getActiveTasks().toArray().length);
    }

    @Test
    @DisplayName("partitionByStatus() Doit retourner la bonne liste")
    void shouldPartitionTasksByStatus() {
        new SimpleTask(new TaskId(1), "Cool", Priority.HIGH);
        RecurringTask task2 = new RecurringTask();
        task2.markAsDone();
        new RecurringTask();

        assertEquals(1, service.partitionByStatus().get(true).toArray().length);
    }

    @Test
    @DisplayName("getTasksByPriority(HIGH) Doit retourner les bonnes valeurs")
    void shouldFilterByPriority() {
        String expectedTitle = "Très Cool";
        new SimpleTask(new TaskId(1), "Cool", Priority.LOW);
        new SimpleTask(new TaskId(2), "Moyen cool", Priority.MEDIUM);
        new SimpleTask(new TaskId(3), expectedTitle, Priority.HIGH);


        List<AbstractTask> foundTask = service.getTasksByPriority(Priority.HIGH);

        /**
         *  utilise get(0) pour accéder à la première tâche de la liste, au lieu de getFirst() qui n'existe pas 
         *  : foundTask.get(0)
         */
        assertEquals(expectedTitle, foundTask.get(0).getTitle());
        assertEquals(3, foundTask.get(0).getId().id());
    }
}
