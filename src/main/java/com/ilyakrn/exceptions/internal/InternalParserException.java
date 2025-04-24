package com.ilyakrn.exceptions.internal;

public class InternalParserException extends CompilerInternalException {

    public InternalParserException(String message) {
        super("Parser internal error: " + message);
    }
}
