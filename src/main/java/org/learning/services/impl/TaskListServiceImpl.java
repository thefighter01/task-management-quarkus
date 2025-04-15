package org.learning.services.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.learning.models.TaskList;
import org.learning.repositories.TaskListRepository;
import org.learning.services.TaskListService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class TaskListServiceImpl implements TaskListService {

    @Inject
    private TaskListRepository taskListRepository;


    @Override
    public List<TaskList> listTaskLists() {
        return taskListRepository.findAll().list();
    }

    @Override
    public Optional<TaskList> getTaskList(Long id) {
        return taskListRepository.findByIdOptional(id);
    }

    @Override
    @Transactional
    public TaskList createTaskList(TaskList taskList) {
        if (null != taskList.getId()) {
            throw new IllegalArgumentException("TaskList id must be null");
        }

        LocalDateTime now = LocalDateTime.now();
        var newTaskList = new TaskList(null, taskList.getTitle(), taskList.getDescription(), now, now, taskList.getTasks());

         taskListRepository.persistAndFlush(newTaskList);
         return newTaskList;
    }

    @Override
    @Transactional
    public TaskList updateTaskList(TaskList taskList) {
        if (null == taskList || null == taskList.getId()) {
            throw new IllegalArgumentException("TaskList id must not be null");
        }

        TaskList existingTaskList = taskListRepository.findById(taskList.getId());
        existingTaskList.setTitle(taskList.getTitle());
        existingTaskList.setDescription(taskList.getDescription());
        existingTaskList.setTasks(taskList.getTasks());
        existingTaskList.setUpdated(LocalDateTime.now());
        taskListRepository.persistAndFlush(existingTaskList);


        return existingTaskList;
    }

    @Override
    @Transactional
    public void deleteTaskList(Long id) {
        taskListRepository.deleteById(id);
    }
}
