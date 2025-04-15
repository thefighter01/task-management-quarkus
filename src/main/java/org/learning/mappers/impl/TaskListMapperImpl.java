package org.learning.mappers.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.learning.dtos.TaskListDto;
import org.learning.mappers.TaskListMapper;
import org.learning.mappers.TaskMapper;
import org.learning.models.Task;
import org.learning.models.TaskList;
import org.learning.models.TaskStatus;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class TaskListMapperImpl implements TaskListMapper {

    @Inject
    private TaskMapper taskMapper;

    @Override
    public TaskListDto toDto(TaskList taskList) {
        return new TaskListDto(taskList.getId(), taskList.getTitle(), taskList.getDescription(),
                Optional.ofNullable(taskList.getTasks()).map(List::size).orElse(null), calculateProgress(taskList.getTasks()),
                Optional.ofNullable(taskList.getTasks()).map(tasks -> tasks.stream().map(taskMapper::toDto).toList()).orElse(null));
    }

    @Override
    public TaskList fromDto(TaskListDto taskListDto) {
        return new TaskList(taskListDto.id(), taskListDto.title(),
                taskListDto.description(), null , null,
                Optional.ofNullable(taskListDto.tasks()).map(tasks -> tasks.stream().map(taskMapper::fromDto).toList()).orElse(null));
    }

    private Double calculateProgress(List<Task> tasks) {
        if (null == tasks ) return null;
        return ( tasks.stream().filter(task -> TaskStatus.CLOSED.equals(task.getTaskStatus())).count() * 1.0 / tasks.size());
    }
}
