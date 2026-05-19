/**
 * Généralement dans une application avec une architecture ne couche on sépare le package des interfaces(contrat) de celui des implémentations.
 */
package com.formation.repository;

import com.formation.model.AbstractTask;
import com.formation.model.TaskId;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryTaskRepository implements TaskRepository {

	/**
	 * problème d'encapsulation. utilise private au lieu du public
	 *  le nom de la variable n'est pas pertinent. il faudrait plutôt "tasks" ou "savedTasks"
	 */
    public Map<TaskId, AbstractTask> saved;

    public InMemoryTaskRepository() {
        saved = new HashMap<>();
    }

 
    public void save(AbstractTask task) {
        saved.put(task.getId(), task);
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
    public List<AbstractTask> findAll() {
        return this.saved.values().stream()
                .filter(task -> !task.done)
                .toList();
    }

    /**
     * 	comme tu utilise un RECORD pour TaskId, java génère automatiquement les méthodes equals() et hashCode()
	 *  basées sur les champs du record. Mais si ce  n'était pas le cas, vu que id est un Object, il faudrait 
	 *  rédéfinir les méthodes equal() et hashCode() dans la class Task. sinon la méthode delete() ne fonctionnera pas correctement, 
     * HashMap utilise deux étapes pour localiser une clé :
		1. hashCode()  →  trouve le "bucket" (compartiment)
		2. equals()    →  trouve la bonne clé dans ce bucket
     */
    @Override
    public void delete(TaskId id) {
        saved.remove(id);
    }
}
