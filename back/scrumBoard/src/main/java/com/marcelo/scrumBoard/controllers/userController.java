package com.marcelo.scrumBoard.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.marcelo.scrumBoard.models.Project;
import com.marcelo.scrumBoard.models.User;
import com.marcelo.scrumBoard.services.ProjectService;
import com.marcelo.scrumBoard.services.UserService;

@RestController
@CrossOrigin({ "http://localhost:8081" })
public class userController {
	@Autowired
	UserService userService;
	@Autowired
	ProjectService projectService;
	@GetMapping("/getAllMembers")
	public ResponseEntity<List<User>> getAllMembers() {
		List<User> allUsers = userService.findAll();
		return ResponseEntity.status(HttpStatus.OK).body(allUsers);
	}

	@PostMapping("/getAllData")
	public ResponseEntity<List<Project>> getAllData(@RequestParam("email") String email) {
		// RETORNA PROYECTOS DE LOS CUAL EL EMAIL PARAM ES OWNER
		User user = userService.findByEmail(email);
		List<Project> allProjects = projectService.findAll();
		List<Project> projectsToReturn = new ArrayList<Project>();
		for(int i = 0 ; i<allProjects.size();i++ ) {
			if(allProjects.get(i).getUser().equals(user)) projectsToReturn.add(allProjects.get(i));
		}
		return ResponseEntity.status(HttpStatus.OK).body(projectsToReturn);
	}
}
