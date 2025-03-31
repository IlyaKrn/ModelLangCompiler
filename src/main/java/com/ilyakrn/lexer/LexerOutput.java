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
        StringBuilder sb = new StringBuilder();
        sb.append("LexerOutput:\nTables:\n");
        for (ArrayList<Lex> table : lexTables) {
            sb.append("===================TABLE===================\n");
            for (Lex lex : table) {
                sb.append(String.format("%s\t%s\t%s\t\n", lex.getId(), lex.getTableId(), lex.getLexeme()));
            }
        }
            sb.append("==================LEXEMES==================\n");
        for (LexAndTable lex : lexemesList) {
            sb.append(String.format("%s\t%s\t\t%s\t\n", lex.getLexId(), lex.getTableId(), lexTables.get(lex.getTableId()).get(lex.getLexId()).getLexeme()));
        }
        return sb.toString();

    }
}
