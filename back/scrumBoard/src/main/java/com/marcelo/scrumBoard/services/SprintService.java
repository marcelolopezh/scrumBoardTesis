package com.marcelo.scrumBoard.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.marcelo.scrumBoard.models.Project;
import com.marcelo.scrumBoard.models.Sprint;
import com.marcelo.scrumBoard.repositories.SprintRepository;

@Service
public class SprintService {
	@Autowired
	SprintRepository sprintRepository;
	
	public Sprint save(Sprint sprint) {
		return sprintRepository.save(sprint);
	}

	@Transactional
	public Sprint findById(Long id) {
		Optional<Sprint> u = sprintRepository.findById(id);
		if(u.isPresent()) {
			return u.get();
		}else {
			return null;
		}
	}

}
