package Sulekhaai.WHBRD.controller;

import Sulekhaai.WHBRD.model.Task;
import Sulekhaai.WHBRD.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskRepository taskRepo;

    // GET tasks securely with user validation
    @GetMapping
    public List<Task> getTasks(@RequestParam(required = false) String date) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String authenticatedEmail = auth.getName();

        if (date != null && !date.isBlank()) {
            return taskRepo.findByUserEmailAndDate(authenticatedEmail, LocalDate.parse(date));
        } else {
            return taskRepo.findByUserEmail(authenticatedEmail);
        }
    }


    // POST add task
    @PostMapping
    public Task addTask(@RequestBody Task task) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String authenticatedEmail = auth.getName();

        // Forcefully set the authenticated user's email to the task
        task.setUserEmail(authenticatedEmail);

        return taskRepo.save(task);
    }

    // PUT update task
    @PutMapping("/{id}")
    public Task updateTask(@PathVariable Long id, @RequestBody Task updated) {
        Optional<Task> opt = taskRepo.findById(id);
        if (opt.isPresent()) {
            Task t = opt.get();
            t.setText(updated.getText());
            t.setDone(updated.isDone());
            t.setDate(updated.getDate());
            return taskRepo.save(t);
        } else {
            throw new RuntimeException("Task not found");
        }
    }

    // DELETE task
    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskRepo.deleteById(id);
    }
}