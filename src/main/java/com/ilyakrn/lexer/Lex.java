package com.ilyakrn.lexer;

public final class Lex {
    private final int id;
    private final int tableId;
    private final String lexeme;

    public Lex(int id, int tableId, String lexeme) {
        this.id = id;
        this.tableId = tableId;
        this.lexeme = lexeme;
    }

    public int getId() {
        return id;
    }

    public int getTableId() {
        return tableId;
    }

    public String getLexeme() {
        return lexeme;
    }

    @Override
    public String toString() {
        return "Lex{" +
                "id=" + id +
                ", tableId=" + tableId +
                ", lexeme='" + lexeme + '\'' +
                '}';
    }
}
