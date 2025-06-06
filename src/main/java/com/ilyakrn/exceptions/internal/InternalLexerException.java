package com.ilyakrn.exceptions.internal;

public class InternalLexerException extends CompilerInternalException {

    public InternalLexerException(String message) {
        super("Lexer internal error: " + message);
    }
}
