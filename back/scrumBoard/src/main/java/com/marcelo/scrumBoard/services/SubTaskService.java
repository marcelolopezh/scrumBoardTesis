package com.marcelo.scrumBoard.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.marcelo.scrumBoard.models.SubTask;
import com.marcelo.scrumBoard.repositories.SubTaskRepository;

@Service
public class SubTaskService {
	@Autowired
	SubTaskRepository subTaskRepository;
	public SubTask save(SubTask subTask) {
		return subTaskRepository.save(subTask);
	}
	
	public void deleteById(Long id) {
		subTaskRepository.deleteById(id);
	}

}
