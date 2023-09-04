package edu.http_server.app.controller;

import edu.http_server.app.model.entity.Task;
import edu.http_server.app.service.TaskService;
import edu.http_server.server.di.Controller;
import edu.http_server.server.http.mapping.PathVariable;
import edu.http_server.server.http.mapping.RequestBody;
import edu.http_server.server.http.mapping.RequestMapping;
import edu.http_server.server.http.model.HttpMethod;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Controller
@Slf4j
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @RequestMapping(method = HttpMethod.POST, url = "/api/tasks")
    public Task createTask(@RequestBody Task task){
        log.debug("Request caught, task value = {}", task);
        return taskService.createTask(task);
    }

    @RequestMapping(method = HttpMethod.GET, url = "/api/tasks/{id}")
    public Task findById(@PathVariable(name = "id") Long id){
        log.debug("Request caught, id value = {}", id);
        return taskService.findTaskById(id);
    }

    @RequestMapping(method = HttpMethod.GET, url = "/api/tasks")
    public List<Task> findAll(){
        log.debug("findAll() proceeded");
        return taskService.findAll();
    }

    @RequestMapping(method = HttpMethod.PUT, url = "/api/tasks/{id}")
    public Task updateById(@PathVariable(name = "id") Long id, @RequestBody Task task){
        log.debug("Request caught, task value = {}", task);
        return taskService.updateTask(id, task);
    }

    @RequestMapping(method = HttpMethod.DELETE, url = "/api/tasks/{id}")
    public boolean deleteById(@PathVariable(name = "id") Long id){
        log.debug("Request caught, id value = {}", id);
        return taskService.deleteById(id);
    }
}
