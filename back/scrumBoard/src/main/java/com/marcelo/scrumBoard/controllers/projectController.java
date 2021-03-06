package com.marcelo.scrumBoard.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import com.marcelo.scrumBoard.models.User;
import com.marcelo.scrumBoard.services.ProjectService;
import com.marcelo.scrumBoard.services.UserService;

@Controller
public class projectController {
	@Autowired
	UserService userService;
	@Autowired
	ProjectService projectService;

	@PostMapping("/createProject")
	public ResponseEntity<Project> createProject(@RequestParam("name") String name,
			@RequestParam("description") String description, @RequestParam("objetive") String objetive,
			@RequestParam("selectedMembers") List<String> selectedMembers,
			@RequestParam("selectedInteresteds") List<String> selectedInteresteds,
			@RequestParam("email") String email) {
		if (name == null || description == null || objetive == null || email == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		Project project = new Project();
		project.setUser(userService.findByEmail(email));
		project.setName(name);
		List<User> selectedMembersArray = new ArrayList<User>();
		User user = new User();
		for (int i = 0; i < selectedMembers.size(); i++) {
			user = userService.findById(Long.parseLong(selectedMembers.get(i)));
			selectedMembersArray.add(user);
		}
		project.setMembers(selectedMembersArray);
		List<User> selectedInterestedsArray = new ArrayList<User>();
		for (int i = 0; i < selectedInteresteds.size(); i++) {
			user = userService.findById(Long.parseLong(selectedInteresteds.get(i)));
			selectedInterestedsArray.add(user);
		}
		project.setInteresteds(selectedInterestedsArray);
		project.setDescription(description);
		project = projectService.createProject(project);
		return ResponseEntity.status(HttpStatus.OK).body(project);

	}

	@GetMapping("/getAllProjects")
	public ResponseEntity<List<Project>> getAllProjects() {
		List<Project> allProjects = projectService.findAll();
		return ResponseEntity.status(HttpStatus.OK).body(allProjects);
	}

	@PostMapping("/getMyProjects")
	public ResponseEntity<List<Project>> getMyProjects(@RequestParam("email") String email,
			@RequestParam("token") String token) {
		User user = userService.findByEmail(email);
		if (!token.equals(user.getToken())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		List<Project> AllProjects = projectService.findAll();
		List<Project> MyProj = new ArrayList<Project>();
		for (int i = 0; i < AllProjects.size(); i++) {
			if (AllProjects.get(i).getUser() == user)
				MyProj.add(AllProjects.get(i));
		}
		return ResponseEntity.status(HttpStatus.OK).body(MyProj);
	}

	@PostMapping("/getMyProjectsAsMember")
	public ResponseEntity<List<Project>> getMyProjectsAsMember(@RequestParam("email") String email,
			@RequestParam("token") String token) {
		User user = userService.findByEmail(email);
		if (!token.equals(user.getToken())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		List<Project> AllProjects = projectService.findAll();
		List<Project> MyProj = new ArrayList<Project>();
		for (int i = 0; i < AllProjects.size(); i++) {
			if (AllProjects.get(i).getMembers().contains(user))
				MyProj.add(AllProjects.get(i));
		}
		return ResponseEntity.status(HttpStatus.OK).body(MyProj);
	}

	@PostMapping("/getMyProjectsAsInterested")
	public ResponseEntity<List<Project>> getMyProjectsAsInterested(@RequestParam("email") String email,
			@RequestParam("token") String token) {
		User user = userService.findByEmail(email);
		if (!token.equals(user.getToken())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		List<Project> AllProjects = projectService.findAll();
		List<Project> MyProj = new ArrayList<Project>();
		for (int i = 0; i < AllProjects.size(); i++) {
			if (AllProjects.get(i).getInteresteds().contains(user))
				MyProj.add(AllProjects.get(i));
		}
		return ResponseEntity.status(HttpStatus.OK).body(MyProj);
	}

	@PostMapping("/getInfoProject/{id}")
	public ResponseEntity<Object> getInfoProject(@PathVariable("id") Long id, @RequestParam("email") String email,
			@RequestParam("token") String token) {
		User user = userService.findByEmail(email);
		if (!token.equals(user.getToken())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		HashMap<String,Object> responseData = new HashMap<>();
		responseData.put("user", user);
		responseData.put("project", projectService.findById(id));
		return ResponseEntity.status(HttpStatus.OK).body(responseData);
	}

	@DeleteMapping("/deleteProject/{id}")
	public ResponseEntity<Project> deleteProject(@PathVariable("id") Long id) {
		Project project = projectService.findById(id);
		if (project == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		} else {
			projectService.deleteProject(id);
			return ResponseEntity.status(HttpStatus.OK).body(null);
		}
	}

	@PutMapping("/updateProject/{id}")
	public ResponseEntity<Project> updateProject(@PathVariable("id") Long id, @RequestParam("name") String name,
			@RequestParam("description") String description,
			@RequestParam("selectedMembers") List<String> selectedMembers,
			@RequestParam("selectedInteresteds") List<String> selectedInteresteds, @RequestParam("email") String email,
			@RequestParam("objetive") String objetive) {
		Project project = projectService.findById(id);
		project.setName(name);
		project.setDescription(description);
		List<User> selectedMembersArray = new ArrayList<User>();
		User user = new User();
		for (int i = 0; i < selectedMembers.size(); i++) {
			user = userService.findById(Long.parseLong(selectedMembers.get(i)));
			selectedMembersArray.add(user);
		}
		project.setMembers(selectedMembersArray);
		List<User> selectedInterestedsArray = new ArrayList<User>();
		for (int i = 0; i < selectedInteresteds.size(); i++) {
			user = userService.findById(Long.parseLong(selectedInteresteds.get(i)));
			selectedInterestedsArray.add(user);
		}
		project.setInteresteds(selectedInterestedsArray);
		project.setObjetive(objetive);
		projectService.createProject(project);
		return ResponseEntity.status(HttpStatus.OK).body(project);
	}

	@DeleteMapping("/deleteMemberFromTheProject/{project_id}/{member_id}")
	public ResponseEntity<List<User>> deleteMemberFromTheProject(@PathVariable("project_id") Long project_id,
			@PathVariable("member_id") Long member_id) {

		Project project = projectService.findById(project_id);
		List<User> memberList = project.getMembers();
		memberList.remove(userService.findById(member_id));
		project.setMembers(memberList);
		projectService.createProject(project);
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}

	@DeleteMapping("/deleteInterestedFromTheProject/{project_id}/{member_id}")
	public ResponseEntity<List<User>> deleteInterestedFromTheProject(@PathVariable("project_id") Long project_id,
			@PathVariable("member_id") Long interested_id) {

		Project project = projectService.findById(project_id);
		List<User> interestedList = project.getInteresteds();
		interestedList.remove(userService.findById(interested_id));
		project.setInteresteds(interestedList);
		projectService.createProject(project);
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}

	@PutMapping("/addMemberToProject")
	public ResponseEntity<List<User>> addMemberToProeject(@RequestParam("newMemberList") List<Long> newMemberList,
			@RequestParam("project_id") Long project_id) {
		System.out.println(newMemberList);
		Project project = projectService.findById(project_id);
		List<User> memberList = project.getMembers();
		for (int i = 0; i < newMemberList.size(); i++) {
			User user = userService.findById(newMemberList.get(i));
			if (!memberList.contains(user))
				memberList.add(userService.findById(newMemberList.get(i)));
		}
		project.setMembers(memberList);
		projectService.createProject(project);
		return ResponseEntity.status(HttpStatus.OK).body(memberList);
	}

	@PutMapping("/addInterestedToProject")
	public ResponseEntity<List<User>> addInterestedToProject(
			@RequestParam("newInterestedList") List<Long> newMemberList, @RequestParam("project_id") Long project_id) {
		System.out.println(newMemberList);
		Project project = projectService.findById(project_id);
		List<User> memberList = project.getInteresteds();
		for (int i = 0; i < newMemberList.size(); i++) {
			User user = userService.findById(newMemberList.get(i));
			if (!memberList.contains(user))
				memberList.add(userService.findById(newMemberList.get(i)));
		}
		project.setInteresteds(memberList);
		projectService.createProject(project);
		return ResponseEntity.status(HttpStatus.OK).body(memberList);
	}

}
