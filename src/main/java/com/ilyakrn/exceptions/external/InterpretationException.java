package com.ilyakrn.exceptions.external;

public class InterpretationException extends CompilerExternalException {

    public InterpretationException(String message) {
        super("Interpretation error: " + message);
    }
}
