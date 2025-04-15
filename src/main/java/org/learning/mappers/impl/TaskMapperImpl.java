package org.learning.mappers.impl;

import jakarta.enterprise.context.ApplicationScoped;
import org.learning.dtos.TaskDto;
import org.learning.mappers.TaskMapper;
import org.learning.models.Task;

@ApplicationScoped
public class TaskMapperImpl implements TaskMapper {
    @Override
    public TaskDto toDto(Task task) {
        return new TaskDto(task.getId(), task.getTitle(), task.getDescription(), task.getPriority(), task.getTaskStatus(), task.getDueDate());
    }

    @Override
    public Task fromDto(TaskDto taskDto) {
        return new Task(taskDto.id(), taskDto.title(), taskDto.description(), taskDto.priority(), taskDto.taskStatus(), taskDto.dueDate() , null , null , null);
    }
}
