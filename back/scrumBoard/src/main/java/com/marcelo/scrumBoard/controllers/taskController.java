package com.marcelo.scrumBoard.controllers;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.marcelo.scrumBoard.models.Sprint;
import com.marcelo.scrumBoard.models.Task;
import com.marcelo.scrumBoard.models.User;
import com.marcelo.scrumBoard.services.ProjectService;
import com.marcelo.scrumBoard.services.SprintService;
import com.marcelo.scrumBoard.services.SubTaskService;
import com.marcelo.scrumBoard.services.TaskService;
import com.marcelo.scrumBoard.services.UserService;

@Controller
public class taskController {
	@Autowired
	SprintService sprintService;
	@Autowired
	ProjectService projectService;
	@Autowired
	UserService userService;
	@Autowired
	TaskService taskService;
	@Autowired
	SubTaskService subTaskService;

	@PostMapping("/createTask")
	private ResponseEntity<Task> createTask(@RequestParam("name") String name,
			@RequestParam("description") String description, @RequestParam("taskEstimatedHours") Integer estimatedHours,
			@RequestParam("priority") String priority, @RequestParam("responsable") Long userId,
			@RequestParam("sprint") Long sprintId) {
		if(description==null || estimatedHours == null || priority == null || userId == null || sprintId == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		Task task = new Task();
		task.setName(name);
		task.setDescription(description);
		task.setEstimatedHours(estimatedHours);
		task.setPriority(priority);
		User user = userService.findById(userId);
		Sprint sprint = sprintService.findById(sprintId);
		task.setUser(user);
		task.setSprint(sprint);
		task.setState("Pendiente");
		task = taskService.save(task);
		return ResponseEntity.status(HttpStatus.OK).body(task);
	}

	@PutMapping("/editTask")
	private ResponseEntity<Task> editTask(@RequestParam("name") String name,
			@RequestParam("description") String description, 
			@RequestParam("estimatedHours") Integer estimatedHours,
			@RequestParam("priority") String priority, 
			@RequestParam("responsable") Long userId,
			@RequestParam("task_id") Long task_id,
			@RequestParam("state") String state) {
		Task task = taskService.findById(task_id);
		if("En Curso".equals(state) && task.getStarted_at()==null) {
			LocalDateTime localDateTime = LocalDateTime.now();
			task.setStarted_at(localDateTime);
		}
		if("Terminado".equals(state)) {
			LocalDateTime localDateTime = LocalDateTime.now();
			task.setFinished_at(localDateTime);
		}
		task.setName(name);
		task.setDescription(description);
		task.setEstimatedHours(estimatedHours);
		task.setPriority(priority);
		task.setUser(userService.findById(userId));
		task.setState(state);
		task = taskService.save(task);
		return ResponseEntity.status(HttpStatus.OK).body(task);
	}

	@DeleteMapping("/deleteTask/{id}")
	private ResponseEntity<Task> deleteTask(@PathVariable("id") Long id) {
		taskService.deleteById(id);
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}

}
