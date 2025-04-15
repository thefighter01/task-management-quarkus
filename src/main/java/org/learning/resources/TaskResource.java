package org.learning.resources;


import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.learning.dtos.TaskDto;
import org.learning.mappers.TaskMapper;
import org.learning.models.Task;
import org.learning.services.TaskService;

import java.util.List;

@Path("/task-lists/{task_list_id}/tasks")
public class TaskResource {

    @Inject
    private TaskService taskService;

    @Inject
    private TaskMapper taskMapper;


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTasks(@PathParam("task_list_id") Long taskListId) {
        List<TaskDto> tasksDtos = taskService.listTasks(taskListId).stream().map(taskMapper::toDto).toList();
        return Response.ok().entity(tasksDtos).build();
    }


    @GET
    @Path("/{task_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTask(@PathParam("task_list_id") Long taskListId,@PathParam("task_id") Long id) {
        TaskDto taskDto = taskMapper.toDto(taskService.getTask(id, taskListId).orElse(null));
        return Response.ok().entity(taskDto).build();
    }


    @PUT
    @Path("/{task_id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateTask(@PathParam("task_list_id") Long taskListId,@PathParam("task_id") Long id, TaskDto taskDto) {
        Task updatedTask = taskService.updateTask(taskMapper.fromDto(taskDto), taskListId);
        return Response.ok().entity(taskMapper.toDto(updatedTask)).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createTask(@PathParam("task_list_id") Long taskListId, TaskDto taskDto) {
        Task createdTask = taskService.createTask(taskMapper.fromDto(taskDto), taskListId);
        return Response.status(Response.Status.CREATED).entity(taskMapper.toDto(createdTask)).build();
    }

    @DELETE
    @Path("/{task_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteTask(@PathParam("task_list_id") Long taskListId,@PathParam("task_id") Long id) {
        taskService.deleteTask(id, taskListId);
        return Response.noContent().build();
    }


}
