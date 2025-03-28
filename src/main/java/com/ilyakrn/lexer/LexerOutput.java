package com.ilyakrn.lexer;

import java.util.ArrayList;

public final class LexerOutput {

    private final ArrayList<ArrayList<Lex>> lexTables;
    private final ArrayList<LexAndTable> lexemesList;

    public LexerOutput(ArrayList<ArrayList<Lex>> lexTables, ArrayList<LexAndTable> lexemesList) {
        this.lexTables = lexTables;
        this.lexemesList = lexemesList;
    }

    @Override
    public String toString() {
        return "LexerOutput{" +
                "lexTables=" + lexTables +
                ", lexemesList=" + lexemesList +
                '}';
    }
}
