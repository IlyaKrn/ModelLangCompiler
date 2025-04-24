package com.ilyakrn.exceptions.external;

public class SemanticException extends CompilerExternalException {

    public SemanticException(String message) {
        super("Semantic error: " + message);
    }
}
