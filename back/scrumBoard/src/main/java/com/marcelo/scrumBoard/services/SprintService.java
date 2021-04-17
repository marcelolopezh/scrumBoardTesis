package com.marcelo.scrumBoard.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.marcelo.scrumBoard.models.Sprint;
import com.marcelo.scrumBoard.repositories.SprintRepository;

@Service
public class SprintService {
	@Autowired
	SprintRepository sprintRepository;
	
	public Sprint save(Sprint sprint) {
		return sprintRepository.save(sprint);
	}

}
