package org.example.controller;

import org.example.model.Procedure;
import org.example.model.dto.ProcedureDto;
import org.example.service.ProcedureService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/procedures")
public class ProcedureController {

    private final ProcedureService procedureService;

    @Autowired
    public ProcedureController(ProcedureService procedureService) {
        this.procedureService = procedureService;
    }

    @PostMapping
    public ResponseEntity<ProcedureDto> addProcedure(@RequestBody final ProcedureDto procedureDto) {
        Procedure procedure = procedureService.addProcedure(Procedure.from(procedureDto));
        return new ResponseEntity<>(ProcedureDto.from(procedure), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ProcedureDto>> getProcedures() {
        List<Procedure> procedures = procedureService.getProcedures();
        List<ProcedureDto> cosmeticsDto = procedures.stream().map(ProcedureDto::from).collect(Collectors.toList());
        return new ResponseEntity<>(cosmeticsDto, HttpStatus.OK);
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<ProcedureDto> getProcedure(@PathVariable final Long id) {
        Procedure procedure = procedureService.getProcedure(id);
        return new ResponseEntity<>(ProcedureDto.from(procedure), HttpStatus.OK);
    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<ProcedureDto> deleteProcedure(@PathVariable final Long id) {
        Procedure procedure = procedureService.deleteProcedure(id);
        return new ResponseEntity<>(ProcedureDto.from(procedure), HttpStatus.OK);
    }

    @PutMapping(value = "{id}")
    public ResponseEntity<ProcedureDto> editProcedure(@PathVariable final Long id,
                                                      @RequestBody final ProcedureDto procedureDto) {
        Procedure editedProcedure = procedureService.editProcedure(id, Procedure.from(procedureDto));
        return new ResponseEntity<>(ProcedureDto.from(editedProcedure), HttpStatus.OK);
    }
}
