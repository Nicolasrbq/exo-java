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
        List<AbstractTask> doneTasks = tasks.stream().filter(task -> task.done).toList();
        return doneTasks.size();
    }

    public static List<String> getTitleUpperCase(List<AbstractTask> tasks) {
        return tasks.stream()
                .map(task -> task.title)
                .sorted()
                .map(String::toUpperCase)
                .toList();
    }

    public static Optional<AbstractTask> findMostUrgent(List<AbstractTask> tasks) {
        Optional<AbstractTask> countTasks = tasks.stream()
                .filter(task -> !task.done)
                .filter(task -> task instanceof SimpleTask simpleTask && simpleTask.priority == Priority.HIGH)
                .max(Comparator.comparing(AbstractTask::getCreateAt));

        countTasks.ifPresentOrElse(AbstractTask::getSummary, () -> System.out.println("Aucune tâche urgente"));
        return countTasks;
    }

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
