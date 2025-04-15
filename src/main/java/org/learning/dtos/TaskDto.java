package org.learning.dtos;

import org.learning.models.TaskPriority;
import org.learning.models.TaskStatus;

import java.time.LocalDateTime;

public record TaskDto(
    Long id,
    String title,
    String description,
    TaskPriority priority,
    TaskStatus taskStatus,
    LocalDateTime dueDate) {
}
