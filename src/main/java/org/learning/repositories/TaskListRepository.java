package org.learning.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.learning.models.TaskList;

@ApplicationScoped
public class TaskListRepository implements PanacheRepositoryBase<TaskList , Long> {
}
