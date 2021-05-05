package com.marcelo.scrumBoard.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.marcelo.scrumBoard.models.SubTask;
import com.marcelo.scrumBoard.models.Task;
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

	@Transactional
	public SubTask findById(Long id) {
		Optional<SubTask> u = subTaskRepository.findById(id);
		if(u.isPresent()) {
			return u.get();
		}else {
			return null;
		}
	}
	
}
