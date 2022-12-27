package org.example.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import org.example.model.Client;

@Repository
public interface ClientRepository extends CrudRepository<Client, Long> {
}
