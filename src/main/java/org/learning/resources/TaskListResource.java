package org.learning.resources;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.learning.dtos.TaskListDto;
import org.learning.mappers.TaskListMapper;
import org.learning.models.TaskList;
import org.learning.services.TaskListService;

import java.util.List;

@Path("/task-lists")
public class TaskListResource {

    @Inject
    private TaskListService taskListService;

    @Inject
    private TaskListMapper taskListMapper;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<TaskListDto> getTaskLists() {
        List<TaskList> taskLists = taskListService.listTaskLists();
        return taskLists.stream().map(taskListMapper::toDto).toList();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TaskListDto createTaskList(TaskListDto taskListDto) {
        TaskList savedTaskList = taskListService.createTaskList(taskListMapper.fromDto(taskListDto));
        return taskListMapper.toDto(savedTaskList);
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public TaskListDto getTaskList(@PathParam("id") Long id) {
        TaskList taskList = taskListService.getTaskList(id).orElseThrow();
        return taskListMapper.toDto(taskList);
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public TaskListDto updateTaskList(TaskListDto taskListDto) {
        TaskList updatedTaskList = taskListService.updateTaskList(taskListMapper.fromDto(taskListDto));
        return taskListMapper.toDto(updatedTaskList);
    }

    @DELETE
    @Path("/{id}")
    public void deleteTaskList(@PathParam("id") Long id) {
        taskListService.deleteTaskList(id);
    }
}
