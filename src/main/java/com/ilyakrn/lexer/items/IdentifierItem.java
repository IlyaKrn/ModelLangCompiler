package com.ilyakrn.lexer.items;

public class IdentifierItem {

    public enum TYPE {
        INT, FLOAT, BOOL
    }

    private final String lexeme;
    private boolean isInit;
    private TYPE type;

    public void setInit(boolean init) {
        isInit = init;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

    public String getLexeme() {
        return lexeme;
    }

    public boolean isInit() {
        return isInit;
    }

    public TYPE getType() {
        return type;
    }

    public IdentifierItem(String lexeme, boolean isInit, TYPE type) {
        this.lexeme = lexeme;
        this.isInit = isInit;
        this.type = type;
    }
}