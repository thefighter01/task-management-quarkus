package org.learning.services;

import org.learning.models.TaskList;

import java.util.List;
import java.util.Optional;

public interface TaskListService {

    List<TaskList> listTaskLists();

    Optional<TaskList> getTaskList(Long id);

    TaskList createTaskList(TaskList taskList);

    TaskList updateTaskList(TaskList taskList);

    void deleteTaskList(Long id);
}
