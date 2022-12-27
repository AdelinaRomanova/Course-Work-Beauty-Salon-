package org.example.service;

import jakarta.transaction.Transactional;
import org.example.model.VisitDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.example.model.exception.ProcedureNotFoundException;
import org.example.repository.VisitDetailRepository;

@Service
public class VisitDetailService {
    private final VisitDetailRepository visitDetailRepository;
    private final ProcedureService procedureService;

    @Autowired
    public VisitDetailService(VisitDetailRepository visitDetailRepository, ProcedureService procedureService) {
        this.visitDetailRepository = visitDetailRepository;
        this.procedureService = procedureService;
    }

    @Transactional
    public VisitDetail addPurchaseDetail(VisitDetail visitDetail) {
        return visitDetailRepository.save(visitDetail);
    }

    public List<VisitDetail> getVisitDetails() {
        return StreamSupport
                .stream(visitDetailRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public VisitDetail getVisitDetail(Long id) {
        return visitDetailRepository.findById(id).orElseThrow(() ->
                new ProcedureNotFoundException(id));
    }

    @Transactional
    public VisitDetail deleteVisitDetail(Long id) {
        VisitDetail visitDetail = getVisitDetail(id);
        visitDetailRepository.delete(visitDetail);
        return visitDetail;
    }

    @Transactional
    public VisitDetail editReceiptDetail(Long id, VisitDetail visitDetail) {
        VisitDetail receiptDetailToEdit = getVisitDetail(id);
        receiptDetailToEdit.setProcedure(visitDetail.getProcedure());
        receiptDetailToEdit.setVisit(visitDetail.getVisit());
        return receiptDetailToEdit;
    }
}
