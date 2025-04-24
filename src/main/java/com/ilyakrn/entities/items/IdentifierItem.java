package com.ilyakrn.entities.items;

public class IdentifierItem {

    private final String lexeme;
    private boolean isInit;
    private Type type;

    public void setInit(boolean init) {
        isInit = init;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getLexeme() {
        return lexeme;
    }

    public boolean isInit() {
        return isInit;
    }

    public Type getType() {
        return type;
    }

    public IdentifierItem(String lexeme, boolean isInit, Type type) {
        this.lexeme = lexeme;
        this.isInit = isInit;
        this.type = type;
    }
}