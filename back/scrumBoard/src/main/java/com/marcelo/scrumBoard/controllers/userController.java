package com.marcelo.scrumBoard.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.marcelo.scrumBoard.models.User;
import com.marcelo.scrumBoard.services.UserService;

@RestController
@CrossOrigin({"http://localhost:8081"})
public class userController {
	@Autowired
	UserService userService;
	
	@GetMapping("/getAllMembers")
	public ResponseEntity<List<User> >getAllMembers(){
		List<User> allUsers = userService.findAll();
		return ResponseEntity.status(HttpStatus.OK).body(allUsers);
	}
}
