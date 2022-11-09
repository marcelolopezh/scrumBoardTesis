package com.marcelo.scrumBoard.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.marcelo.scrumBoard.models.ResponsePokeApi;
import com.marcelo.scrumBoard.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.marcelo.scrumBoard.models.User;
import com.marcelo.scrumBoard.services.UserService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Controller
public class controller {
	@Autowired
	UserService userService;

	@Autowired
	ProjectService projectService;
	
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
			String token = getJWTToken(email+" "+date);
			user.setToken(token);
			userService.save(user);
			userObj.put("id",user.getId().toString());
			userObj.put("completeName", user.getName()+" "+user.getLastName());
			userObj.put("token", token);
			return ResponseEntity.status(HttpStatus.OK).body(userObj);
		}
		else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	}
	
	@PostMapping("/createUser")
	public ResponseEntity<User> registerPost(
			@RequestBody User user
			) {
		User _user_ = userService.findByEmail(user.getEmail());
		Boolean isValid = _user_ == null ? true : false;
		User dbUser = isValid ? userService.save(user) : null;
		return dbUser != null && isValid
				? ResponseEntity.status(HttpStatus.OK).body(dbUser)
				: ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
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



	@GetMapping("/pokemon-api")
	public ResponseEntity<ResponsePokeApi> getPokemon(
			@RequestParam("pokemon") String pokemon
 	) throws Exception {
		return projectService.getInfoFromApi(pokemon);
	}
}
