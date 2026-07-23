/**
 * Généralement dans une application avec une architecture ne couche on sépare le package des interfaces(contrat) de celui des implémentations.
 */
package com.formation.repository;

import com.formation.model.*;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class InMemoryTaskRepository implements TaskRepository {

    /**
     * problème d'encapsulation. utilise private au lieu du public
     * le nom de la variable n'est pas pertinent. il faudrait plutôt "tasks" ou "savedTasks"
     * <p>
     * vue que saved est static il est partagé entre toutes les instances de InMemoryTaskRepository, donc si on crée plusieurs instances de InMemoryTaskRepository,
     * on n'a pas besoin du mot clé this pour accéder à saved, on peut y accéder directement par son nom.
     * <p>
     * this est réservé pour les variables et les méthodes d'instance.
     */
    private static final Map<TaskId, AbstractTask> saved = new HashMap<>();

    public InMemoryTaskRepository() {
        // initialisation de quelques taches pour les tests

        save(new SimpleTask(new TaskId(1), "Tache 1", Priority.HIGH));
        save(new SimpleTask(new TaskId(2), "Tache 2", Priority.MEDIUM));
        save(new SimpleTask(new TaskId(3), "Tache 3", Priority.LOW));
        save(new SimpleTask(new TaskId(4), "Tache 4", Priority.HIGH));
        save(new RecurringTask(new TaskId(5), "Tache 5", 7));
        save(new RecurringTask(new TaskId(6), "Tache 6", 3));
        save(new RecurringTask(new TaskId(7), "Tache 7", 5));
    }


    public void save(AbstractTask task) {
        if (task.getId() == null || task.getId().id() == 0) {
            task.setId(new TaskId(generateNextId()));
            saved.put(task.getId(), task);
        } else {
            saved.put(task.getId(), task);
        }
    }

    @Override
    public Optional<AbstractTask> findById(TaskId id) {
        return Optional.ofNullable(saved.get(id));
    }

    /**
     * le nom de la méthode findAll() n'est pas pertinent,
     * car elle ne retourne que les tâches non terminées.
     */
    @Override
    public List<AbstractTask> findAllActiveTask() {
        return saved.values().stream()
                .filter(task -> !task.isDone())
                .toList();
    }

    /**
     * comme tu utilise un RECORD pour TaskId, java génère automatiquement les méthodes equals() et hashCode()
     * basées sur les champs du record. Mais si ce  n'était pas le cas, vu que id est un Object, il faudrait
     * rédéfinir les méthodes equal() et hashCode() dans la class Task. sinon la méthode delete() ne fonctionnera pas correctement,
     * HashMap utilise deux étapes pour localiser une clé :
     * 1. hashCode()  →  trouve le "bucket" (compartiment)
     * 2. equals()    →  trouve la bonne clé dans ce bucket
     */
    @Override
    public void delete(TaskId id) {
        saved.remove(id);
    }

    /**
     * la méthode generateNextId() génère un nouvel identifiant en trouvant le maximum des identifiants existants dans la collection saved et en ajoutant 1.
     *
     * @return
     */
    private int generateNextId() {
        return saved.keySet().stream().mapToInt(TaskId::id).max().orElse(0) + 1;
    }


    @Override
    public List<AbstractTask> findAll() {
        return saved.values().stream().toList();
    }
}
