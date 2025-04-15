package org.learning.mappers;

import org.learning.dtos.TaskDto;
import org.learning.models.Task;

public interface TaskMapper {

    TaskDto toDto(Task task);

    Task fromDto(TaskDto taskDto);
}
