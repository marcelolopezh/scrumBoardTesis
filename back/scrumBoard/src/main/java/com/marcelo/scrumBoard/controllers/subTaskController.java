package com.marcelo.scrumBoard.controllers;

import java.util.List;

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
	private ResponseEntity<SubTask> createSubTask(@RequestParam("name") String name,
			@RequestParam("description") String description, @RequestParam("estimatedHours") Integer estimatedHours,
			@RequestParam("priority") String priority, @RequestParam("taskId") Long taskId) {
		SubTask subTask = new SubTask();
		subTask.setName(name);
		subTask.setDescription(description);
		subTask.setEstimatedHours(estimatedHours);
		subTask.setPriority(priority);
		Task task = taskService.findById(taskId);
		subTask.setTask(task);
		subTask.setState("Pendiente");
		subTask = subTaskService.save(subTask);
		return ResponseEntity.status(HttpStatus.OK).body(subTask);
	}

	@DeleteMapping("/deleteSubTask/{id}")
	private ResponseEntity<SubTask> deleteSubTask(@PathVariable("id") Long id) {
		subTaskService.deleteById(id);
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}

	// NO ME FUNCIONO CON PUTMAPPING
	@PostMapping("/editSubTask/{id}")
	private ResponseEntity<SubTask> editSubTask(@PathVariable("id") Long id, @RequestParam("name") String name,
			@RequestParam("description") String description, @RequestParam("estimatedHours") Integer estimatedHours,
			@RequestParam("priority") String priority, @RequestParam("state") String state) {
		SubTask subTask = subTaskService.findById(id);
		subTask.setName(name);
		subTask.setDescription(description);
		subTask.setEstimatedHours(estimatedHours);
		subTask.setPriority(priority);
		subTask.setState(state);
		subTask = subTaskService.save(subTask);
		Task task = subTask.getTask();
		int isTerminado = 0;
		int isEnCurso = 0;
		int isPendiente = 0;
		int tamaño = 0;

		List<SubTask> subTaskList = task.getSubtasks();
		for (int i = 0; i < subTaskList.size(); i++) {
			tamaño++;
			if ((subTaskList.get(i).getState().equals("Terminado")))
				isTerminado++;
			if ((subTaskList.get(i).getState().equals("En Curso")))
				isEnCurso++;
			if ((subTaskList.get(i).getState().equals("Pendiente")))
				isPendiente++;
		}
		if (tamaño == isTerminado)
			task.setState("Terminado");
		if (tamaño == isEnCurso || (isPendiente==0 && isEnCurso>=1))
			task.setState("En Curso");
		if (isPendiente >= 1)
			task.setState("Pendiente");

		
		taskService.save(task);
		return ResponseEntity.status(HttpStatus.OK).body(subTask);
	}

}
