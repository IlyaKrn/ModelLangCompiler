package com.ilyakrn.exceptions.internal;

public class InternalInterpreterException extends CompilerInternalException {

    public InternalInterpreterException(String message) {
        super("Interpreter internal error: " + message);
    }
}
