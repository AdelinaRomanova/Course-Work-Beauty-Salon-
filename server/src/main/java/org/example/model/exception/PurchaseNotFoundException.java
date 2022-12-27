package org.example.model.exception;

import java.text.MessageFormat;

public class PurchaseNotFoundException extends RuntimeException {

    public PurchaseNotFoundException(final Long id) {
        super(MessageFormat.format("Could not find purchase with id: {0}", id));
    }
}