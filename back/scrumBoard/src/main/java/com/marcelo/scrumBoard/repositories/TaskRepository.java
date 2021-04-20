package com.marcelo.scrumBoard.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.marcelo.scrumBoard.models.Task;
@Repository
public interface TaskRepository extends CrudRepository<Task, Long>{

}
