package com.marcelo.scrumBoard.controllers;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
	private ResponseEntity<Project> createSprint(@RequestParam("name") String name,
			@RequestParam("objetive") String objetive, @RequestParam("projectId") Long projectId,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") Date startDate,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") Date endDate) {

		Sprint sprint = new Sprint();
		sprint.setName(name);
		sprint.setObjetive(objetive);
		sprint.setState("No Iniciado");
		sprint.setStartDate(startDate);
		sprint.setEndDate(endDate);
		Project project = projectService.findById(projectId);
		sprint = sprintService.save(sprint);
		sprint.setProject(project);
		project = projectService.createProject(project);
		return ResponseEntity.status(HttpStatus.OK).body(project);
	}

}
