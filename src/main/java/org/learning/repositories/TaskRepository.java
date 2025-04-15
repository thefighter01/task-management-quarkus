package org.learning.repositories;


import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.learning.models.Task;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class TaskRepository implements PanacheRepositoryBase<Task , Long> {

    public List<Task> findByTaskListId(Long taskListId) {
        return find("taskList.id", taskListId).list();
    }

    public Optional<Task> findByTaskListIdAndTaskId(Long taskListId , Long taskId) {
        return find("taskList.id = ?1 and id = ?2" , taskListId , taskId).firstResultOptional();
    }

    public void deleteByTaskListIdAndTaskId(Long taskListId , Long taskId) {
        delete("taskList.id = ?1 and id = ?2" , taskListId , taskId);
    }



}
