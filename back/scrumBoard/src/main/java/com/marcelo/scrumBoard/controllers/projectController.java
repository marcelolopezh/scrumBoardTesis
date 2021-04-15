package com.marcelo.scrumBoard.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.marcelo.scrumBoard.models.Project;
import com.marcelo.scrumBoard.models.User;
import com.marcelo.scrumBoard.services.ProjectService;
import com.marcelo.scrumBoard.services.UserService;

@Controller
@CrossOrigin(origins = "http://localhost:8081")
public class projectController {
	@Autowired
	UserService userService;
	@Autowired
	ProjectService projectService;

	@PostMapping("/createProject")
	public ResponseEntity<Project> createProject(
			@RequestParam("name") String name, 
			@RequestParam("description") String description,
			@RequestParam("selectedMembers") List<String> selectedMembers,
			@RequestParam("selectedClients") List<String> selectedClients,
			@RequestParam("email") String email) {
		if(name!=null && description != null && selectedMembers.size()!=0 && selectedClients.size()!=0 && email != null) {
			Project project = new Project();
			project.setUser(userService.findByEmail(email));
			project.setName(name);
			List<User> selectedMembersArray = new ArrayList<User>();
			User user = new User();
			for(int i = 0; i<selectedMembers.size(); i++) {
				user = userService.findById( Long.parseLong(selectedMembers.get(i)) );
				selectedMembersArray.add(user);
			}
			project.setMembers(selectedMembersArray);
			List<User> selectedClientsArray = new ArrayList<User>();
			for(int i = 0; i<selectedClients.size(); i++) {
				user = userService.findById( Long.parseLong(selectedMembers.get(i)) );
				selectedClientsArray.add(user);
			}
			project.setClients(selectedClientsArray);
			project.setDescription(description);
			project = projectService.createProject(project);
			return ResponseEntity.status(HttpStatus.OK).body(project);
		}else{
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}
	
	@PostMapping("/getMyProjects")
	public ResponseEntity<List<Project>> getMyProjects(@RequestParam("email") String email){
		List<Project> AllProjects = projectService.findAll();
		List<Project> MyProj = new ArrayList<Project>();
		User user = userService.findByEmail(email);
		for(int i  = 0 ; i< AllProjects.size(); i++) {
			if(AllProjects.get(i).getUser()==user) MyProj.add(AllProjects.get(i));
		}
	
		return ResponseEntity.status(HttpStatus.OK).body(MyProj);
	}
	
	@GetMapping("/getInfoProject/{id}")
	public  ResponseEntity<Project> getInfoProject(@PathVariable("id") Long id) {
		return ResponseEntity.status(HttpStatus.OK).body(projectService.findById(id));
	}

}
