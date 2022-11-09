package com.marcelo.scrumBoard.services;

import java.util.List;
import java.util.Optional;

import com.marcelo.scrumBoard.models.ResponsePokeApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.marcelo.scrumBoard.models.Project;
import com.marcelo.scrumBoard.repositories.ProjectRepository;
import org.springframework.web.client.RestTemplate;

@Service
public class ProjectService {
	@Autowired
	RestTemplate restTemplate;
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	private final ProjectRepository projectRepository;
	public ProjectService(ProjectRepository projectRepository){
		this.projectRepository = projectRepository;
	}
	
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

	public ResponseEntity<ResponsePokeApi> getInfoFromApi(String pokemon) throws Exception{
		String urlApi = "https://pokeapi.co/api/v2/pokemon/{pokemon}";
		HttpHeaders requestHeaders = new HttpHeaders();
		HttpEntity entity = new HttpEntity<Object>(requestHeaders);
		HttpMethod methodGet = HttpMethod.GET;

		try {
			ResponseEntity<ResponsePokeApi> apiData = restTemplate.exchange(
					urlApi,
					methodGet,
					entity,
					ResponsePokeApi.class,
					pokemon
			);
			return apiData;
		} catch(Exception e){
			throw new Exception("Error al consultar a la api pokemon",e.getCause());
		}


	}
}
