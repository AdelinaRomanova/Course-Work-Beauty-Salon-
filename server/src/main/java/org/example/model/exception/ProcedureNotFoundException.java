package org.example.model.exception;

import java.text.MessageFormat;

public class ProcedureNotFoundException extends RuntimeException{

    public ProcedureNotFoundException(final Long id) {
        super(MessageFormat.format("Could not find procedure with id: {0}", id));
    }
}
