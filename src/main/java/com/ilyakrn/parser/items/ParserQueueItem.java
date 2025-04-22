package com.ilyakrn.parser.items;

public class ParserQueueItem {
    private final int lexId;
    private final int tableId;
    private final String lexeme;

    public int getLexId() {
        return lexId;
    }

    public int getTableId() {
        return tableId;
    }

    public String getLexeme() {
        return lexeme;
    }

    public ParserQueueItem(int lexId, int tableId, String lexeme) {
        this.lexId = lexId;
        this.tableId = tableId;
        this.lexeme = lexeme;
    }
}
