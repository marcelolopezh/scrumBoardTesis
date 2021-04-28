package com.marcelo.scrumBoard.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.marcelo.scrumBoard.models.SubTask;
import com.marcelo.scrumBoard.models.Task;
import com.marcelo.scrumBoard.services.ProjectService;
import com.marcelo.scrumBoard.services.SprintService;
import com.marcelo.scrumBoard.services.SubTaskService;
import com.marcelo.scrumBoard.services.TaskService;
import com.marcelo.scrumBoard.services.UserService;

@Controller
@CrossOrigin(origins = "http://localhost:8081")
public class subTaskController {
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
	@PostMapping("/createSubTask")
	private ResponseEntity<SubTask> createSubTask(
			@RequestParam("name") String name, 
			@RequestParam("description") String description,
			@RequestParam("estimatedHours") Integer estimatedHours,
			@RequestParam("priority") String priority,
			@RequestParam("taskId") Long taskId) {
		SubTask subTask = new SubTask();
		subTask.setName(name);
		subTask.setDescription(description);
		subTask.setEstimatedHours(estimatedHours);
		subTask.setPriority(priority);
		Task task = taskService.findById(taskId);
		subTask.setTask(task);
		subTask.setState("No Iniciado");
		subTask = subTaskService.save(subTask);
		return ResponseEntity.status(HttpStatus.OK).body(subTask);
	}

	@DeleteMapping("/deleteSubTask/{id}")
	private ResponseEntity<SubTask> deleteTask(@PathVariable("id") Long id){
		subTaskService.deleteById(id);
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}
	
}
