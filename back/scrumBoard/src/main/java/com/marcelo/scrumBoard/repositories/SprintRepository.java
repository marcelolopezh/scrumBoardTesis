package com.marcelo.scrumBoard.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.marcelo.scrumBoard.models.Sprint;


@Repository
public interface SprintRepository extends CrudRepository<Sprint,Long>{
	List<Sprint> findAll();
	Optional<Sprint> findById(Long id);
}
