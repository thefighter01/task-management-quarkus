package org.learning.services.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.learning.models.Task;
import org.learning.models.TaskList;
import org.learning.models.TaskPriority;
import org.learning.models.TaskStatus;
import org.learning.repositories.TaskListRepository;
import org.learning.repositories.TaskRepository;
import org.learning.services.TaskService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class TaskServiceImpl implements TaskService {

    @Inject
    TaskListRepository taskListRepository;
    @Inject
    private TaskRepository taskRepository;


    @Override
    public List<Task> listTasks(Long TaskListId) {
        return taskRepository.findByTaskListId(TaskListId);
    }

    @Override
    public Optional<Task> getTask(Long taskId, Long taskListId) {
        return taskRepository.findByTaskListIdAndTaskId(taskListId, taskId);
    }

    @Override
    @Transactional
    public Task createTask(Task task, Long TaskListId) {
        if (task == null ) {
            throw new IllegalArgumentException("Task must not be null");
        }

        TaskStatus taskStatus = TaskStatus.OPEN;

        TaskPriority priority = Optional.ofNullable(task.getPriority()).orElse(TaskPriority.MEDIUM);

        TaskList taskList = taskListRepository.findByIdOptional(TaskListId).orElseThrow(() -> new IllegalArgumentException("TaskList id must not be IN THE db"));
        LocalDateTime now = LocalDateTime.now();

        Task toBeSaved = new Task(null , task.getTitle() , task.getDescription() , priority , taskStatus , task.getDueDate() ,now , now , taskList);
        taskRepository.persistAndFlush(toBeSaved);
        return toBeSaved;

    }

    @Override
    @Transactional
    public Task updateTask(Task task, Long taskListId) {
        if (task == null || task.getId() == null) {
            throw new IllegalArgumentException("Task id must not be null");
        }
        if (null == task.getPriority()) throw new IllegalArgumentException("Task priority must be present");

        if (null == task.getTaskStatus()) throw new IllegalArgumentException("Task status must be present");


        Task existedTask = taskRepository.findByTaskListIdAndTaskId(taskListId, task.getId()).orElseThrow(() -> new IllegalArgumentException("Task id must not be IN THE db"));

        existedTask.setTitle(task.getTitle());
        existedTask.setDescription(task.getDescription());
        existedTask.setPriority(task.getPriority());
        existedTask.setTaskStatus(task.getTaskStatus());
        existedTask.setDueDate(task.getDueDate());
        taskRepository.persistAndFlush(existedTask);
        return existedTask;
    }

    @Override
    @Transactional
    public void deleteTask(Long taskId, Long taskListId) {
        taskRepository.deleteByTaskListIdAndTaskId(taskListId, taskId);
    }
}
