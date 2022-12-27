package org.example.service;

import jakarta.transaction.Transactional;
import org.example.model.Procedure;
import org.example.model.Purchase;
import org.example.model.PurchaseDetail;
import org.example.repository.PurchaseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.example.model.exception.PurchaseNotFoundException;

@Service
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final PurchaseDetailService purchaseDetailService;
    private final ProcedureService procedureService;

    public PurchaseService(PurchaseRepository purchaseRepository, PurchaseDetailService purchaseDetailService,
                           ProcedureService procedureService) {
        this.purchaseRepository = purchaseRepository;
        this.purchaseDetailService = purchaseDetailService;
        this.procedureService = procedureService;
    }

    @Transactional
    public Purchase addPurchase(Purchase purchase) {
        return purchaseRepository.save(purchase);
    }

    public List<Purchase> getPurchases() {
        return StreamSupport
                .stream(purchaseRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public Purchase getPurchase(Long id) {
        return purchaseRepository.findById(id).orElseThrow(() ->
                new PurchaseNotFoundException(id));
    }

    public Purchase deletePurchase(Long id) {
        Purchase purchase = getPurchase(id);
        purchaseRepository.delete(purchase);
        return purchase;
    }

    @Transactional
    public Purchase editPurchase(Long id, Purchase purchase) {
        Purchase purchaseToEdit = getPurchase(id);
        purchaseToEdit.setDate(purchase.getDate());
        return purchaseToEdit;
    }

    @Transactional
    public Purchase addProcedureToPurchase(Long purchaseId, Long procedureId, int count) {
        Purchase purchase = getPurchase(purchaseId);
        Procedure procedure = procedureService.getProcedure(procedureId);

        for (PurchaseDetail p : purchase.getProcedures()) {
            if (p.getProcedure().getId() == procedureId) {
                int prev_count = p.getCount();
                removeProcedureFromPurchase(purchaseId, procedureId);
                PurchaseDetail detail = new PurchaseDetail();
                detail.setPurchase(purchase);
                detail.setProcedure(procedure);
                detail.setCount(prev_count + count);
                detail.setSubtotal(procedure.getPrice() * (prev_count + count));
                purchase.addProcedurePurchase(detail);
                purchase.SumTotal();
                return purchase;
            }
        }

        PurchaseDetail detail = new PurchaseDetail();
        detail.setPurchase(purchase);
        detail.setProcedure(procedure);
        detail.setCount(count);
        detail.setSubtotal(procedure.getPrice() * count);
        purchase.addProcedurePurchase(detail);
        purchase.SumTotal();
        return purchase;
    }


    @Transactional
    public Purchase removeProcedureFromPurchase(Long purchaseId, Long procedureId) {
        Purchase purchase = getPurchase(purchaseId);
        PurchaseDetail detail = new PurchaseDetail();
        for (PurchaseDetail d : purchase.getProcedures()) {
            if (d.getProcedure().getId() == procedureId) {
                detail = d;
            }
        }
        purchase.removeProcedurePurchase(detail);
        purchaseDetailService.deletePurchaseDetail(detail.getId());
        purchase.SumTotal();
        return purchase;
    }
}
