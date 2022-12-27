package org.example.controller;

import org.example.model.Visit;
import org.example.model.VisitDetail;
import org.example.model.dto.VisitDetailDto;
import org.example.model.dto.VisitDto;
import org.example.service.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/visits")
public class VisitController {
    private final VisitService visitService;

    @Autowired
    public VisitController(VisitService visitService) {
        this.visitService = visitService;
    }

    @PostMapping
    public ResponseEntity<VisitDto> addVisit(@RequestBody final VisitDto visitDto) {
        Visit visit = visitService.addVisit(Visit.from(visitDto));
        return new ResponseEntity<>(VisitDto.from(visit), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<VisitDto>> getVisits() {
        List<Visit> visits = visitService.getVisits();
        List<VisitDto> visitDtos = visits.stream().map(VisitDto::from).collect(Collectors.toList());
        return new ResponseEntity<>(visitDtos, HttpStatus.OK);
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<VisitDto> getVisit(@PathVariable final Long id) {
        Visit visit = visitService.getVisit(id);
        return new ResponseEntity<>(VisitDto.from(visit), HttpStatus.OK);
    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<VisitDto> deleteVisit(@PathVariable final Long id) {
        Visit visit = visitService.deleteVisit(id);
        return new ResponseEntity<>(VisitDto.from(visit), HttpStatus.OK);
    }

    @PutMapping(value = "{id}")
    public ResponseEntity<VisitDto> editVisit(@PathVariable final Long id,
                                              @RequestBody final VisitDto visitDto) {
        Visit visit = visitService.editVisit(id, Visit.from(visitDto));
        return new ResponseEntity<>(VisitDto.from(visit), HttpStatus.OK);
    }

    @PostMapping(value = "{visitId}/procedures/add/{procedureId}")
    public ResponseEntity<VisitDto> addProcedureToVisit(@PathVariable final Long visitId,
                                                        @PathVariable final Long procedureId) {
        Visit visit = visitService.addProcedureToVisit(visitId, procedureId);
        return new ResponseEntity<>(VisitDto.from(visit), HttpStatus.OK);
    }

    @GetMapping(value = "{id}/procedures")
    public ResponseEntity<VisitDetailDto> getVisitDetails(@PathVariable final Long id) {
        List<VisitDetail> details = visitService.getVisit(id).getProcedures();
        List<VisitDetailDto> detailsDto = details.stream().map(VisitDetailDto::from).collect(Collectors.toList());
        return new ResponseEntity(detailsDto, HttpStatus.OK);
    }

    @DeleteMapping(value = "{visitId}/procedures/remove/{procedureId}")
    public ResponseEntity<VisitDto> removeProcedureFromVisit(@PathVariable final Long visitId,
                                                             @PathVariable final Long procedureId) {
        Visit visit = visitService.removeProcedureFromVisit(visitId, procedureId);
        return new ResponseEntity<>(VisitDto.from(visit), HttpStatus.OK);
    }


}
