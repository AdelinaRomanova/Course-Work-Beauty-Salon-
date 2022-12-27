package org.example.service;

import jakarta.transaction.Transactional;
import org.example.model.Procedure;
import org.example.model.Visit;
import org.example.model.VisitDetail;
import org.example.model.exception.VisitNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.example.repository.VisitRepository;

@Service
public class VisitService {
    private final VisitRepository visitRepository;
    private final VisitDetailService visitDetailService;
    private final ProcedureService procedureService;

    public VisitService(VisitRepository visitRepository, VisitDetailService visitDetailService,
                        ProcedureService procedureService) {
        this.visitRepository = visitRepository;
        this.visitDetailService = visitDetailService;
        this.procedureService = procedureService;
    }

    @Transactional
    public Visit addVisit(Visit visit) {
        return visitRepository.save(visit);
    }

    public List<Visit> getVisits() {
        return StreamSupport
                .stream(visitRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public Visit getVisit(Long id) {
        return visitRepository.findById(id).orElseThrow(() ->
                new VisitNotFoundException(id));
    }

    public Visit deleteVisit(Long id) {
        Visit visit = getVisit(id);
        visitRepository.delete(visit);
        return visit;
    }

    @Transactional
    public Visit editVisit(Long id, Visit visit) {
        Visit visitToEdit = getVisit(id);
        visitToEdit.setProcedures(visit.getProcedures());
        visitToEdit.setDate(visit.getDate());
        return visitToEdit;
    }

    @Transactional
    public Visit addProcedureToVisit(Long visitId, Long procedureId) {
        Visit visit = getVisit(visitId);

        Procedure procedure = procedureService.getProcedure(procedureId);
        VisitDetail detail = new VisitDetail();
        detail.setVisit(visit);
        detail.setProcedure(procedure);

        visit.addProcedureVisit(detail);
        return visit;
    }

    @Transactional
    public Visit removeProcedureFromVisit(Long visitId, Long procedureId) {
        Visit visit = getVisit(visitId);
        VisitDetail detail = new VisitDetail();
        for (VisitDetail d : visit.getProcedures()) {
            if (d.getProcedure().getId() == procedureId) {
                detail = d;
            }
        }
        visit.removeProcedureVisit(detail);
        visitDetailService.deleteVisitDetail(detail.getId());
        return visit;
    }

}
