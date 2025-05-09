package com.ilyakrn.entities.items;

public class LexemesSeqItem {
    private final int tableId;
    private final int lexId;
    private final int row;
    private final int col;

    public LexemesSeqItem(int tableId, int lexId, int row, int col) {
        this.tableId = tableId;
        this.lexId = lexId;
        this.row = row;
        this.col = col;
    }

    public int getTableId() {
        return tableId;
    }

    public int getLexId() {
        return lexId;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
