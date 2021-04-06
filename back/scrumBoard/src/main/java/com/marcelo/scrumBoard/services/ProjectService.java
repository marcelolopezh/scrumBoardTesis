package com.marcelo.scrumBoard.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.marcelo.scrumBoard.models.Project;
import com.marcelo.scrumBoard.repositories.ProjectRepository;

@Service
public class ProjectService {
	@Autowired
	ProjectRepository projectRepository;
	
	@Transactional
	public List<Project> findAll(){
		return projectRepository.findAll();
	}
	
	@Transactional
	public Project createProject(Project project) {
		return projectRepository.save(project);
	}

	@Transactional
	public Project findById(Long id) {
		Optional<Project> u = projectRepository.findById(id);
		if(u.isPresent()) {
			return u.get();
		}else {
			return null;
		}
	}
	
	@Transactional
	public void deleteProject(Long id) {
		projectRepository.deleteById(id);
	}
}
