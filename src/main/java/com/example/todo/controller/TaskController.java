package com.example.todo.controller;

import com.example.todo.dto.TaskDto;
import com.example.todo.exception.CustomException;
import com.example.todo.exception.NotFoundException;
import com.example.todo.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Objects.isNull;

@Controller
@RequiredArgsConstructor
@RequestMapping("/todo")
public class TaskController {
    private final TaskService taskService;

    @GetMapping(value = "/")
    public String tasks(Model model,
                         @RequestParam(required = false, defaultValue = "1") Integer pageNumber,
                         @RequestParam(required = false, defaultValue = "10") Integer pageSize){
        pageNumber = isNull(pageNumber) ? 0 : pageNumber;
        pageSize = isNull(pageSize) ? 3 : pageSize;
        model.addAttribute("tasks", taskService.get(pageNumber-1, pageSize));
        model.addAttribute("currentPage", pageNumber);
        int totalPage = (int)Math.ceil(1.0* taskService.getAllCount()/pageSize);
        if(totalPage>1){
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPage).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "tasks";
    }

    @PostMapping(value = "/")
    public String add(Model model,
                       @RequestBody TaskDto taskDto) throws CustomException {
        if (StringUtils.isEmpty(taskDto.getDescription()) || taskDto.getDescription().length() > 100)
            throw new CustomException("description incorrect");
        if (StringUtils.isEmpty(taskDto.getStatus()))
            taskDto.setDescription("CREATED");
        taskService.create(taskDto);
        return tasks(model, 1, 10);

    }

    @PostMapping (value = "/{id}")
    public String edit(Model model,
                           @PathVariable Long id,
                           @RequestBody TaskDto taskDto) throws NotFoundException {
        if(taskService.getById(id).isPresent()){
            taskService.update(id, taskDto);
            return tasks(model, 1, 10);
        }
        throw new NotFoundException("invalid id");
    }



    @DeleteMapping(value = "/{id}")
    public String delete(Model model,
                         @PathVariable Long id) throws NotFoundException {
        if(taskService.getById(id).isPresent()){
            taskService.delete(id);
            return tasks(model, 1, 10);
        }
        throw new NotFoundException("Not found");
    }

}
