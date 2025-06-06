package org.example.tasklist.web.mappers;

import org.example.tasklist.domain.task.Task;
import org.example.tasklist.web.dto.task.TaskDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    TaskDto toDto(Task task);
    Task toEntity(TaskDto taskDto);
    List<TaskDto> toDto(List<Task> tasks);
}
