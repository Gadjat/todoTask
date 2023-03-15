package com.example.todo.mapper;

import com.example.todo.dto.TaskDto;
import com.example.todo.entity.Task;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    TaskDto toDTO(Task task);
    Task toModel(TaskDto taskDto);
}
