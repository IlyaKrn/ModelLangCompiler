package com.ilyakrn.entities.items;

public class PolizItem {

    private final int lexId;
    private final int tableId;

    public PolizItem(int lexId, int tableId) {
        this.lexId = lexId;
        this.tableId = tableId;
    }

    public int getLexId() {
        return lexId;
    }

    public int getTableId() {
        return tableId;
    }
}
