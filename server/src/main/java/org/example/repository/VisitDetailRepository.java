package org.example.repository;

import org.example.model.VisitDetail;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VisitDetailRepository extends CrudRepository<VisitDetail, Long> {
}
