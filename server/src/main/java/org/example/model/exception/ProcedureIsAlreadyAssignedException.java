package org.example.model.exception;

import java.text.MessageFormat;

public class ProcedureIsAlreadyAssignedException extends RuntimeException {
    public ProcedureIsAlreadyAssignedException(final Long procedureId, final Long purchaseId) {
        super(MessageFormat.format("Procedure: {0} is already assigned to purchase: {1}", procedureId, purchaseId));
    }
}
