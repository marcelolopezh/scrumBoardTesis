package com.marcelo.scrumBoard.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.marcelo.scrumBoard.models.Sprint;
import com.marcelo.scrumBoard.models.Task;
import com.marcelo.scrumBoard.models.User;
import com.marcelo.scrumBoard.services.ProjectService;
import com.marcelo.scrumBoard.services.SprintService;
import com.marcelo.scrumBoard.services.TaskService;
import com.marcelo.scrumBoard.services.UserService;

@Controller
@CrossOrigin(origins = "http://localhost:8081")
public class taskController {
	@Autowired
	SprintService sprintService;
	@Autowired
	ProjectService projectService;
	@Autowired
	UserService userService;
	@Autowired
	TaskService taskService;
	@PostMapping("/createTask")
	private ResponseEntity<Task> createTask(
			@RequestParam("name") String name, 
			@RequestParam("description") String description,
			@RequestParam("taskEstimatedHours") Integer estimatedHours,
			@RequestParam("priority") String priority,
			@RequestParam("responsable") Long userId,
			@RequestParam("sprint") Long sprintId) {
		Task task = new Task();
		task.setName(name);
		task.setDescription(description);
		task.setEstimatedHours(estimatedHours);
		task.setPriority(priority);
		User user = userService.findById(userId);
		Sprint sprint = sprintService.findById(sprintId);
		task.setUser(user);
		task.setSprint(sprint);
		task.setState("No Iniciado");
		task = taskService.save(task);
		return ResponseEntity.status(HttpStatus.OK).body(task);
	}

	@DeleteMapping("/deleteTask/{id}")
	private ResponseEntity<Task> deleteTask(@PathVariable("id") Long id){
		taskService.deleteById(id);
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}
	
}