package com.marcelo.scrumBoard.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
