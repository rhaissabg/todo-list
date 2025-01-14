package com.rhaissalima.todosimple.services;

import com.rhaissalima.todosimple.models.Task;
import com.rhaissalima.todosimple.models.User;
import com.rhaissalima.todosimple.repositories.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;

    public TaskService(TaskRepository taskRepository, UserService userService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
    }

    public Task findById(Long id) {
        Optional<Task> task = taskRepository.findById(id);
        return task.orElseThrow(() -> new RuntimeException("Task not found!"));
    }

    public List<Task> findAllByUserId(Long userId) {
        List<Task> tasks = taskRepository.findByUser_Id(userId);
        return tasks;
    }

    @Transactional
    public Task create(Task obj) {
        User user = userService.findById(obj.getUser().getId());
        obj.setId(null);
        obj.setUser(user);
        obj = taskRepository.save(obj);
        return obj;
    }

    @Transactional
    public Task update(Task obj) {
        Task newObj = findById(obj.getId());
        newObj.setDescription(obj.getDescription());
        return taskRepository.save(newObj);
    }

    public void delete(Long id) {
        findById(id);
        try {
            taskRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Impossible to delete task!");
        }
    }

}
