package com.formation.dto;

public record TaskDTO(
    Integer id,
    String title,
    String type, // "SIMPLE" ou "RECURRING"
    String priority, // null si RecurringTask
    Integer intervalDays, // null si SimpleTask
    boolean done,
    String createdAt, // ISO-8601 : "2024-01-15"
    String summary // résultat de getSummary()
) {}
