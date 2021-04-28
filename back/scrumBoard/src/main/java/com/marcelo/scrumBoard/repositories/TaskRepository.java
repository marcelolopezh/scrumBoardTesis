package com.marcelo.scrumBoard.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.marcelo.scrumBoard.models.Task;
@Repository
public interface TaskRepository extends CrudRepository<Task, Long>{
	Optional<Task> findById(Long id);
}
