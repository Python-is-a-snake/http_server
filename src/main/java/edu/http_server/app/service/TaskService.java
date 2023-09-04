package edu.http_server.app.service;

import edu.http_server.app.dao.TaskDao;
import edu.http_server.app.dao.UserDao;
import edu.http_server.app.model.entity.Task;
import edu.http_server.server.di.Service;

import java.util.List;

@Service
public class TaskService {
    private final TaskDao taskDao;
    private final UserDao userDao;

    public TaskService(TaskDao taskDao, UserDao userDao) {
        this.taskDao = taskDao;
        this.userDao = userDao;
    }

    public Task createTask(Task task) {
        return taskDao.save(task);
    }
    public Task findTaskById(Long id) {
        return taskDao.findById(id).orElseThrow();
    }
    public boolean deleteById(Long id) {
        return taskDao.deleteById(id);
    }

    public List<Task> findAll() {
        return taskDao.findAll();
    }

    public Task updateTask(Long id, Task dataToUpdate) {
        Task task = taskDao.findById(id).orElseThrow();

        if (dataToUpdate.getName() != null) {
            task.setName(dataToUpdate.getName());
        }

        if (dataToUpdate.getDescription() != null) {
            task.setDescription(dataToUpdate.getDescription());
        }

        if(dataToUpdate.getUser().getId() != null){
            task.setUser(userDao.findById(dataToUpdate.getUser().getId()).get());
        }

        if(task.isDone() != dataToUpdate.isDone()){
            task.setDone(dataToUpdate.isDone());
        }

        return taskDao.update(task);
    }
}
