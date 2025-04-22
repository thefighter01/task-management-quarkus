package org.learning.services.impl;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.learning.models.TaskList;
import org.learning.repositories.TaskListRepository;
import org.learning.services.TaskListService;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
class TaskListServiceImplTest {


    @InjectMock
    private TaskListRepository taskListRepository;

    @Inject
    private TaskListService taskListService; // underTest

    @Test
    void givenNoTaskLists_whenGetListTaskLists_thenEmpty() {

        final List<TaskList> taskLists = new ArrayList<>();


        @SuppressWarnings("unchecked")
        PanacheQuery<TaskList> query = (PanacheQuery<TaskList>)mock(PanacheQuery.class);
        when(taskListRepository.findAll()).thenReturn(query);
        when(query.list()).thenReturn(taskLists);

        List<TaskList> taskListsResult = taskListService.listTaskLists();

        verify(taskListRepository).findAll();

        assert taskListsResult.isEmpty();

        verifyNoMoreInteractions(taskListRepository);


    }



    @Test
    void givenTaskLists_whenGetListTaskLists_thenMatchExpectedResult() {
        // given

        final List<TaskList> taskLists = new ArrayList<>();
        TaskList taskList = new TaskList();
        taskList.setId(1L);
        taskList.setTitle("TaskList 1");
        taskList.setDescription("Description 1");
        taskLists.add(taskList);


        @SuppressWarnings("unchecked")
        PanacheQuery<TaskList> query = (PanacheQuery<TaskList>) mock(PanacheQuery.class);
        when(taskListRepository.findAll()).thenReturn(query);
        when(query.list()).thenReturn(taskLists);

        // act
        List<TaskList> taskListsResult = taskListService.listTaskLists();

        // assert
        verify(taskListRepository).findAll();

        assert taskListsResult.size() == 1;
        assert taskListsResult.get(0).getId() == 1L;
        assert taskListsResult.get(0).getTitle().equals("TaskList 1");

        verifyNoMoreInteractions(taskListRepository);
    }



    @Test
    void givenTaskListId_whenGetTaskList_thenMatchExpectedResult() {
        // given
        Long taskListId = 1L;
        TaskList taskList = new TaskList();
        taskList.setId(taskListId);
        taskList.setTitle("TaskList 1");
        taskList.setDescription("Description 1");

        when(taskListRepository.findByIdOptional(taskListId)).thenReturn(Optional.of(taskList));

        // act
        TaskList taskListResult = taskListService.getTaskList(taskListId).get();


        // assert
        verify(taskListRepository).findByIdOptional(taskListId);

        assert taskListResult.getId() == taskListId;
        assert taskListResult.getTitle().equals(taskList.getTitle());
        assert taskListResult.getDescription().equals(taskList.getDescription());

        ArgumentCaptor<Long> taskListIdArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(taskListRepository).findByIdOptional(taskListIdArgumentCaptor.capture());
        assert taskListId == taskListIdArgumentCaptor.getValue();

        verifyNoMoreInteractions(taskListRepository);
    }

    @Test
    void givenTaskListId_whenGetTaskList_thenTaskListNotFound() {
        // given
        Long taskListId = 1L;


        when(taskListRepository.findByIdOptional(taskListId)).thenReturn(Optional.ofNullable(null));

        // act
        TaskList taskListResult = taskListService.getTaskList(taskListId).orElse(null);


        // assert
        verify(taskListRepository).findByIdOptional(taskListId);

        assertNull(taskListResult);

        ArgumentCaptor<Long> taskListIdArgumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(taskListRepository).findByIdOptional(taskListIdArgumentCaptor.capture());
        assert taskListId == taskListIdArgumentCaptor.getValue();

        verifyNoMoreInteractions(taskListRepository);
    }

    @Test
    void givenTaskList_whenCreateTaskList_thenMatchExpectedResult() {
        // given
        TaskList taskList = new TaskList();
        taskList.setTitle("TaskList 1");
        taskList.setDescription("Description 1");
        taskList.setTasks(new ArrayList<>());

        doNothing().when(taskListRepository).persistAndFlush(any(TaskList.class));

        // act
        TaskList taskListResult = taskListService.createTaskList(taskList);

        // assert
        assertEquals(taskListResult.getTitle() , taskList.getTitle());
        assertEquals(taskListResult.getDescription() , taskList.getDescription());
        assertNotNull(taskListResult.getCreated());
        assertNotNull(taskListResult.getUpdated());
        assertNull(taskListResult.getId());

        ArgumentCaptor<TaskList> taskListArgumentCaptor = ArgumentCaptor.forClass(TaskList.class);
        verify(taskListRepository).persistAndFlush(taskListArgumentCaptor.capture());
        TaskList taskListCaptorValue = taskListArgumentCaptor.getValue();

        assertNull(taskListCaptorValue.getId());
        assertEquals(taskListCaptorValue.getTitle(), taskList.getTitle());
        assertEquals(taskListCaptorValue.getDescription(), taskList.getDescription());
        assertEquals(taskListCaptorValue.getCreated() , taskListResult.getCreated());
        assertEquals(taskListCaptorValue.getUpdated() , taskListResult.getUpdated());


       verifyNoMoreInteractions(taskListRepository);
    }
    @Test
    void givenTaskListWithId_whenCreateTaskList_thenExceptionThrown() {
        // given
        Long taskListId = 34L;
        TaskList taskList = new TaskList();
        taskList.setId(taskListId);
        taskList.setTitle("TaskList 1");
        taskList.setDescription("Description 1");
        taskList.setTasks(new ArrayList<>());

        IllegalArgumentException toBeThrown = new IllegalArgumentException("TaskList id must be null");



        // act
        IllegalArgumentException exceptionResult = assertThrows(IllegalArgumentException.class, () -> taskListService.createTaskList(taskList));

        // assert
        assertEquals(toBeThrown.getMessage() , exceptionResult.getMessage());
        assertInstanceOf(IllegalArgumentException.class, exceptionResult);
        verifyNoInteractions(taskListRepository);

    }
    @Test
    void givenTaskList_whenUpdateTaskList_thenMatchExpectedResult() {
        // given

        TaskList taskList = new TaskList();
        taskList.setId(null);
        taskList.setTitle("TaskList 1");
        taskList.setDescription("Description 1");
        taskList.setTasks(new ArrayList<>());

        IllegalArgumentException toBeThrown = new IllegalArgumentException("TaskList id must not be null");


        // act
        IllegalArgumentException exceptionResult = assertThrows(IllegalArgumentException.class, () -> taskListService.updateTaskList(taskList));

        // assert
        assertEquals(toBeThrown.getMessage() , exceptionResult.getMessage());
        assertInstanceOf(IllegalArgumentException.class, exceptionResult);
        verifyNoInteractions(taskListRepository);
    }

    @Test
    void givenTaskListId_whenUpdateTaskList_thenExceptionThrown() {
        // given
       Long taskListId = 43L;
       TaskList taskListUpdates = new TaskList();
       taskListUpdates.setTitle("updated title");
       taskListUpdates.setId(taskListId);
       taskListUpdates.setDescription("updated description");
       taskListUpdates.setTasks(new ArrayList<>());


       TaskList existingTaskList = new TaskList();
       existingTaskList.setId(taskListId);
       existingTaskList.setTitle("existing title");
       existingTaskList.setDescription("existing description");
       existingTaskList.setTasks(new ArrayList<>());

       when(taskListRepository.findById(taskListId)).thenReturn(existingTaskList);

       // act
       TaskList taskListUpdateResult = taskListService.updateTaskList(taskListUpdates);

       // assert
       verify(taskListRepository).findById(taskListId);
       assertAll(
               () -> assertEquals(taskListId , taskListUpdateResult.getId()),
               () -> assertEquals(taskListUpdates.getTitle() , taskListUpdateResult.getTitle()),
               () -> assertEquals(taskListUpdates.getDescription() , taskListUpdateResult.getDescription()),
               () -> assertEquals(taskListUpdates.getTasks() , taskListUpdateResult.getTasks())
       );

       ArgumentCaptor<TaskList> argumentCaptor = ArgumentCaptor.forClass(TaskList.class);
       verify(taskListRepository).persistAndFlush(argumentCaptor.capture());
       TaskList taskListCaptorValue = argumentCaptor.getValue();

       assertAll(
               () -> assertEquals(taskListId , taskListCaptorValue.getId()),
               () -> assertEquals(taskListUpdates.getTitle() , taskListCaptorValue.getTitle()),
               () -> assertEquals(taskListUpdates.getDescription() , taskListCaptorValue.getDescription()),
               () -> assertEquals(taskListUpdates.getTasks() , taskListCaptorValue.getTasks())
       );

    }


    @Test
    void WhenDeleteTaskList_ThenTaskListDeleted() {
        // given
        Long taskListId = 1L;
        ArgumentCaptor<Long> taskListIdArgumentCaptor = ArgumentCaptor.forClass(Long.class);

        // act
        taskListService.deleteTaskList(taskListId);

        // assert

        verify(taskListRepository).deleteById(taskListId);
        verify(taskListRepository).deleteById(taskListIdArgumentCaptor.capture());

        assert taskListId == taskListIdArgumentCaptor.getValue();

        verifyNoMoreInteractions(taskListRepository);
    }

    @Test
    void WhenDeleteTaskList_ThenRepositoryThrowsException() {
        // given
        Long taskListId = 1L;
        RuntimeException dbException = new RuntimeException("DB Error");
        doThrow(dbException).when(taskListRepository).deleteById(taskListId);
        // act

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> taskListService.deleteTaskList(taskListId));

        // assert
        assertSame(dbException, thrown);
        assertEquals("DB Error", thrown.getMessage());
        verify(taskListRepository , times(1)).deleteById(taskListId);
        verifyNoMoreInteractions(taskListRepository);
    }
}

