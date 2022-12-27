package org.example.service;

import jakarta.transaction.Transactional;
import org.example.model.PurchaseDetail;
import org.example.repository.PurchaseDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.example.model.exception.ProcedureNotFoundException;

@Service
public class PurchaseDetailService {
    private final PurchaseDetailRepository purchaseDetailRepository;
    private final ProcedureService procedureService;

    @Autowired
    public PurchaseDetailService(PurchaseDetailRepository purchaseDetailRepository, ProcedureService procedureService) {
        this.purchaseDetailRepository = purchaseDetailRepository;
        this.procedureService = procedureService;
    }

    @Transactional
    public PurchaseDetail addPurchaseDetail(PurchaseDetail purchaseDetail) {
        return purchaseDetailRepository.save(purchaseDetail);
    }

    public List<PurchaseDetail> getPurchaseDetails() {
        return StreamSupport
                .stream(purchaseDetailRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public PurchaseDetail getPurchaseDetail(Long id) {
        return purchaseDetailRepository.findById(id).orElseThrow(() ->
                new ProcedureNotFoundException(id));
    }

    public PurchaseDetail deletePurchaseDetail(Long id) {
        PurchaseDetail purchaseDetail = getPurchaseDetail(id);
        purchaseDetailRepository.delete(purchaseDetail);
        return purchaseDetail;
    }

    @Transactional
    public PurchaseDetail editPurchaseDetail(Long id, PurchaseDetail purchaseDetail) {
        PurchaseDetail purchaseDetailToEdit = getPurchaseDetail(id);
        purchaseDetailToEdit.setProcedure(purchaseDetail.getProcedure());
        purchaseDetailToEdit.setPurchase(purchaseDetail.getPurchase());
        purchaseDetailToEdit.setSubtotal(purchaseDetail.getCount());
        return purchaseDetailToEdit;
    }
}
