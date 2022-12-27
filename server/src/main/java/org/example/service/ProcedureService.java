package org.example.service;

import jakarta.transaction.Transactional;
import org.example.model.Procedure;
import org.example.model.exception.ProcedureNotFoundException;
import org.example.repository.ProcedureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ProcedureService {

    private final ProcedureRepository procedureRepository;

    @Autowired
    public ProcedureService(ProcedureRepository procedureRepository) {
        this.procedureRepository = procedureRepository;
    }

    @Transactional
    public Procedure addProcedure(Procedure procedure) {
        return procedureRepository.save(procedure);
    }

    public List<Procedure> getProcedures() {
        return StreamSupport
                .stream(procedureRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public Procedure getProcedure(Long id) {
        return procedureRepository.findById(id).orElseThrow(() ->
                new ProcedureNotFoundException(id));
    }

    public Procedure deleteProcedure(Long id) {
        Procedure procedure = getProcedure(id);
        procedureRepository.delete(procedure);
        return procedure;
    }

    @Transactional
    public Procedure editProcedure(Long id, Procedure procedure) {
        Procedure procedureToEdit = getProcedure(id);
        procedureToEdit.setName(procedure.getName());
        procedureToEdit.setMaster(procedure.getMaster());
        procedureToEdit.setPrice(procedure.getPrice());
        return procedureToEdit;
    }
}
