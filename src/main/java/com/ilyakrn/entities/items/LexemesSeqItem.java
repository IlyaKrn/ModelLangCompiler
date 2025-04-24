package com.ilyakrn.entities.items;

public class LexemesSeqItem {
    private final int tableId;
    private final int lexId;

    public int getTableId() {
        return tableId;
    }

    public int getLexId() {
        return lexId;
    }

    public LexemesSeqItem(int tableId, int lexId) {
        this.tableId = tableId;
        this.lexId = lexId;
    }
}
