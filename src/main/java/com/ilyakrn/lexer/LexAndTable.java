package com.ilyakrn.lexer;

public final class LexAndTable {
    private final int tableId;
    private final int lexId;

    public LexAndTable(int tableId, int lexId) {
        this.tableId = tableId;
        this.lexId = lexId;
    }

    public int getTableId() {
        return tableId;
    }

    public int getLexId() {
        return lexId;
    }

    @Override
    public String toString() {
        return "LexAndTable{" +
                "tableId=" + tableId +
                ", lexId=" + lexId +
                '}';
    }
}
