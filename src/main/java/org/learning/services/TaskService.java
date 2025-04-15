package org.learning.services;

import org.learning.models.Task;

import java.util.List;
import java.util.Optional;

public interface TaskService {

    List<Task> listTasks(Long taskListId);

    Optional<Task> getTask(Long taskId , Long taskListId);

    Task createTask(Task task , Long TaskListId);

    Task updateTask(Task task , Long taskListId);

    void deleteTask(Long taskId , Long taskListId);



}
