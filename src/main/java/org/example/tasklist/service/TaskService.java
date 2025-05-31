package org.example.tasklist.service;
import java.util.List;
import org.example.tasklist.domain.task.Task;
public interface TaskService {
    Task getById(Long id);
    List<Task> getAllByUserId(Long id);
    Task update(Task task);
    Task create(Task task, Long userId);
    void delete(Long id);

}
