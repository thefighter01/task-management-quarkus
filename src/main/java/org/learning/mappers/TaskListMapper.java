package org.learning.mappers;

import org.learning.dtos.TaskListDto;
import org.learning.models.TaskList;

public interface TaskListMapper {

    TaskListDto toDto(TaskList taskList);

    TaskList fromDto(TaskListDto taskListDto);
}
