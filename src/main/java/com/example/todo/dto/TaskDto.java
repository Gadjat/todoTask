package com.example.todo.dto;

import com.example.todo.entity.Status;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TaskDto {
    private Long id;
    private String description;
    private Status status;

}
