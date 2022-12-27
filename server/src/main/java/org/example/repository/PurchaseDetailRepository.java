package org.example.repository;

import org.example.model.PurchaseDetail;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseDetailRepository extends CrudRepository<PurchaseDetail, Long> {
}
