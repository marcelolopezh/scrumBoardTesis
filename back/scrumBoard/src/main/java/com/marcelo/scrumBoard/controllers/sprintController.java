package com.marcelo.scrumBoard.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.marcelo.scrumBoard.models.Project;
import com.marcelo.scrumBoard.models.Sprint;
import com.marcelo.scrumBoard.services.ProjectService;
import com.marcelo.scrumBoard.services.SprintService;

@Controller
@CrossOrigin(origins = "http://localhost:8081")
public class sprintController {
	@Autowired
	SprintService sprintService;
	@Autowired
	ProjectService projectService;

	@PostMapping("/createSprint")
	private ResponseEntity<Project> createSprint(
			@RequestParam("name") String name, 
			@RequestParam("objetive") String objetive,
			@RequestParam("projectId") Long projectId) {
		Sprint sprint = new Sprint();
		sprint.setName(name);
		sprint.setObjetive(objetive);
		Project project =projectService.findById(projectId);
		sprint = sprintService.save(sprint);
		sprint.setProject(project);
		project = projectService.createProject(project);
		return ResponseEntity.status(HttpStatus.OK).body(project);
	}

}
