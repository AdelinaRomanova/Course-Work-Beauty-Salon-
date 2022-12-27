package org.example.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import org.example.model.Procedure;

@Repository
public interface ProcedureRepository extends CrudRepository<Procedure, Long> {
}
