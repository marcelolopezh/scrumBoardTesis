package com.marcelo.scrumBoard.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.marcelo.scrumBoard.models.SubTask;
@Repository
public interface SubTaskRepository extends CrudRepository<SubTask, Long>{

}
