package com.ilyakrn.entities.items;

public class ParserQueueItem {
    private final int lexId;
    private final int tableId;
    private final String lexeme;
    private final int row;
    private final int col;

    public ParserQueueItem(int lexId, int tableId, String lexeme, int row, int col) {
        this.lexId = lexId;
        this.tableId = tableId;
        this.lexeme = lexeme;
        this.row = row;
        this.col = col;
    }

    public int getLexId() {
        return lexId;
    }

    public int getTableId() {
        return tableId;
    }

    public String getLexeme() {
        return lexeme;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
