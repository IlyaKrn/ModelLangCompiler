package com.ilyakrn.entities.items;

public class InterpreterStackItem {
    private final String lexeme;
    private final int lexId;
    private final int tableId;

    public String getLexeme() {
        return lexeme;
    }

    public int getLexId() {
        return lexId;
    }

    public int getTableId() {
        return tableId;
    }

    public InterpreterStackItem(String lexeme, int lexId, int tableId) {
        this.lexeme = lexeme;
        this.lexId = lexId;
        this.tableId = tableId;
    }
}
