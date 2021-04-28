package com.marcelo.scrumBoard.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.marcelo.scrumBoard.models.Project;
import com.marcelo.scrumBoard.models.Task;
import com.marcelo.scrumBoard.repositories.TaskRepository;

@Service
public class TaskService {
	@Autowired
	TaskRepository taskRepository;
	public Task save(Task task) {
		return taskRepository.save(task);
	}
	
	public void deleteById(Long id) {
		taskRepository.deleteById(id);
	}

	@Transactional
	public Task findById(Long id) {
		Optional<Task> u = taskRepository.findById(id);
		if(u.isPresent()) {
			return u.get();
		}else {
			return null;
		}
	}
	

}
