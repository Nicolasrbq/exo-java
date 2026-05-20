package com.formation.service;

import com.formation.model.AbstractTask;
import com.formation.model.Priority;
import com.formation.model.SimpleTask;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class TaskAnalyser {

    public static long countDone(List<AbstractTask> tasks) {
    	/**
    	 *  l'api stream de compter directement les éléments qui correspondent à un prédicat
    	 *  tu peux retourner directement tasks.stream().filter(task -> task.done).count();
    	 */
        List<AbstractTask> doneTasks = tasks.stream().filter(task -> task.isDone()).toList();
        return doneTasks.size();
    }

    //GOOD
    public static List<String> getTitleUpperCase(List<AbstractTask> tasks) {
        return tasks.stream()
                .map(task -> task.getTitle())
                .sorted()
                .map(String::toUpperCase)
                .toList();
    }

    //GOOD
    public static Optional<AbstractTask> findMostUrgent(List<AbstractTask> tasks) {
        Optional<AbstractTask> countTasks = tasks.stream()
                .filter(task -> !task.isDone())
                .filter(task -> task instanceof SimpleTask simpleTask && simpleTask.priority == Priority.HIGH)
                .max(Comparator.comparing(AbstractTask::getCreateAt));

        // utilise un logger à la place de System.out.println pour afficher les messages d'information
        countTasks.ifPresentOrElse(AbstractTask::getSummary, () -> System.out.println("Aucune tâche urgente"));
        return countTasks;
    }

    //GOOD
    public static Map<Priority, Long> countByPriority(List<AbstractTask> tasks) {
        return tasks.stream()
                .filter(task -> task instanceof SimpleTask)
                .map(task -> (SimpleTask) task)
                .collect(Collectors.groupingBy(task -> task.priority, Collectors.counting()));
    }

    public static String buildReport(List<AbstractTask> tasks) {
        return tasks.stream()
                .map(AbstractTask::getSummary)
                .collect(Collectors.joining("\\n"));
    }
}
