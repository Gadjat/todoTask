package com.example.todo.service;

import com.example.todo.dto.TaskDto;
import com.example.todo.entity.Task;
import com.example.todo.mapper.TaskMapper;
import com.example.todo.repo.TaskRepo;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class TaskService {

    private final TaskRepo taskRepo;
    private final TaskMapper taskMapper;

    public List<TaskDto> get(Integer pageNumber, Integer pageSize){
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return taskRepo.getAll(pageable).stream().map(taskMapper::toDTO).collect(Collectors.toList());
    }

    public Long getAllCount(){
        return taskRepo.count();
    }

    public Optional<TaskDto> getById(Long id){
        return taskRepo.findById(id).map(taskMapper::toDTO);
    }
    @Transactional
    public Optional<Long> create(TaskDto taskDto){
        return Optional.of(taskRepo.save(taskMapper.toModel(taskDto)).getId());
    }

    @Transactional
    public void delete(Long id){
        taskRepo.deleteById(id);
    }

    @Transactional
    public void update(Long id, TaskDto taskDto){
        Task task = taskRepo.findById(id).get();
        if(nonNull(taskDto.getDescription())){
            task.setDescription(taskDto.getDescription());
        }
        if(nonNull(taskDto.getStatus())){
            task.setStatus(taskDto.getStatus());
        }
        taskRepo.save(task);

    }
}
