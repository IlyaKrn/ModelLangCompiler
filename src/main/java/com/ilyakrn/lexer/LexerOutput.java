package com.ilyakrn.lexer;

import java.util.ArrayList;

public final class LexerOutput {

    private final ArrayList<ArrayList<Lex>> lexTables;
    private final ArrayList<LexAndTable> lexemesList;
    private final boolean isError;
    private final String message;

    public LexerOutput(ArrayList<ArrayList<Lex>> lexTables, ArrayList<LexAndTable> lexemesList, boolean isError, String message) {
        this.lexTables = lexTables;
        this.lexemesList = lexemesList;
        this.isError = isError;
        this.message = message;
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
            String tableName = "(UNDEFINED)";
            if(lex.getTableId() == Lexer.TABLE_SERVICE_ID) tableName = "(SERVICE)";
            if(lex.getTableId() == Lexer.TABLE_DELIMITERS_ID) tableName = "(DELIM)";
            if(lex.getTableId() == Lexer.TABLE_IDENTIFIERS_ID) tableName = "(IDENT)";
            if(lex.getTableId() == Lexer.TABLE_NUMBERS_ID) tableName = "(NUMBER)";
            String lexemeText = lexTables.get(lex.getTableId()).get(lex.getLexId()).getLexeme();
            switch (lexemeText){
                case "\n":
                    lexemeText = "\\n";
                    break;
            }
            sb.append(String.format("%s\t%s %s\t\t\t\t%s\t\n", lex.getLexId(), lex.getTableId(), tableName, lexemeText));
        }
        sb.append(isError ? "===================ERROR===================\n" : "====================OK=====================\n");
        if(!message.isEmpty()){
            sb.append(message+"\n");
        }
        else {
            sb.append("NO LEXER MESSAGE\n");
        }

        return sb.toString();

    }
}
