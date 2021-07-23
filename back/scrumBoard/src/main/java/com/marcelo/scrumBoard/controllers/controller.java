package com.marcelo.scrumBoard.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.marcelo.scrumBoard.models.User;
import com.marcelo.scrumBoard.services.UserService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Controller
@CrossOrigin(origins = "http://localhost:8081")
public class controller {
	@Autowired
	UserService userService;
	
	@GetMapping("/")
	public ResponseEntity<Boolean> confirmToken() {
		return ResponseEntity.status(HttpStatus.OK).body(true);
	}
	
	@PostMapping("/login")
	public ResponseEntity<HashMap<String, String>> loginPost(
			@RequestParam("email") String email,
			@RequestParam("password") String password,
			@RequestParam("date") String date
			){
		Boolean isDataCorrect = userService.authenticateUser(email, password);
		if(isDataCorrect) {
			HashMap<String,String> userObj = new HashMap<>();
			User user = userService.findByEmail(email);
			userObj.put("id",user.getId().toString());
			userObj.put("completeName", user.getName()+" "+user.getLastName());
			userObj.put("token", getJWTToken(email+" "+date));
			return ResponseEntity.status(HttpStatus.OK).body(userObj);
		}
		else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	}
	
	@PostMapping("/createUser")
	public ResponseEntity<User> registerPost(
			@RequestParam("name") String name,
			@RequestParam("lastName") String lastName,
			@RequestParam("email") String email,
			@RequestParam("password") String password
			) {
		User isUserRegistered = userService.findByEmail(email.toLowerCase());
		if(isUserRegistered==null) {
			User registerUserDB = new User();
			registerUserDB.setName(name);
			registerUserDB.setLastName(lastName);
			registerUserDB.setEmail(email);
			String hashed = userService.hashedPassword(password);
			registerUserDB.setPassword(hashed);			
			registerUserDB = userService.save(registerUserDB);
			return ResponseEntity.status(HttpStatus.OK).body(registerUserDB);
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	}
	private String getJWTToken(String email) {
		String secretKey = "uctTemuco";
		List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER");

		String token = Jwts.builder().setId(UUID.randomUUID().toString()).setSubject(email)
				.claim("authorities",
						grantedAuthorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 43200000)) // 12 horas 43200000
				.signWith(SignatureAlgorithm.HS512, secretKey.getBytes()).compact();

		return "Bearer " + token;
	}


}
