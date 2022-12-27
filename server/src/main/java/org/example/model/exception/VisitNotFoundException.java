package org.example.model.exception;

import java.text.MessageFormat;

public class VisitNotFoundException extends RuntimeException{

    public VisitNotFoundException(final Long id) {
        super(MessageFormat.format("Could not find visit with id: {0}", id));
    }

}
