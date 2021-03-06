package com.marcelo.scrumBoard.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.marcelo.scrumBoard.models.Project;
import com.marcelo.scrumBoard.models.Sprint;
import com.marcelo.scrumBoard.services.ProjectService;
import com.marcelo.scrumBoard.services.SprintService;

@Controller
public class sprintController {
	@Autowired
	SprintService sprintService;
	@Autowired
	ProjectService projectService;

	@PostMapping("/createSprint")
	private ResponseEntity<Project> createSprint(@RequestParam("name") String name,
			@RequestParam("objetive") String objetive, @RequestParam("projectId") Long projectId,
			@RequestParam  String startDate,
			@RequestParam  String endDate) throws ParseException {
		if(name == null || objetive == null || projectId == null || startDate == null || endDate == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startDateF = sdf.parse(startDate);
		Date endDateF   = sdf.parse(endDate);
		Sprint sprint = new Sprint();
		sprint.setName(name);
		sprint.setObjetive(objetive);
		sprint.setState("No Iniciado");
		sprint.setStartDate(startDateF);
		sprint.setEndDate(endDateF);
		Project project = projectService.findById(projectId);
		sprint = sprintService.save(sprint);
		sprint.setProject(project);
		project = projectService.createProject(project);
		return ResponseEntity.status(HttpStatus.OK).body(project);
	}
	
	@PutMapping("/editSprint")
	private ResponseEntity<Project> editSprint(@RequestParam("name") String name,
			@RequestParam("objetive") String objetive,
			@RequestParam  String startDate,
			@RequestParam  String endDate,
			@RequestParam Long id) throws ParseException {
		if(name == null || name == "" || objetive == null || objetive == "" || id == null || startDate == null || endDate == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startDateF = sdf.parse(startDate);
		Date endDateF   = sdf.parse(endDate);
		Sprint sprint = sprintService.findById(id);
		sprint.setName(name);
		sprint.setObjetive(objetive);
		sprint.setStartDate(startDateF);
		sprint.setEndDate(endDateF);
		sprint = sprintService.save(sprint);
		return ResponseEntity.status(HttpStatus.OK).body(sprint.getProject());
	}

	@GetMapping("/getInfoSprint/{id}")
	private ResponseEntity<Sprint> getInfoSprint(@PathVariable("id") Long id) {
		return ResponseEntity.status(HttpStatus.OK).body(sprintService.findById(id));
	}
	
	@DeleteMapping("/deleteSprint/{id}")
	private ResponseEntity<Sprint> deleteTask(@PathVariable("id") Long id){
		sprintService.deleteById(id);
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}
}

