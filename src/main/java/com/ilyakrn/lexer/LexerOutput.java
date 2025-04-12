package com.ilyakrn.lexer;

import java.util.ArrayList;

public final class LexerOutput {

    public static final int TABLE_SERVICE_ID = 0;
    public static final int TABLE_DELIMITERS_ID = 1;
    public static final int TABLE_IDENTIFIERS_ID = 2;
    public static final int TABLE_NUMBERS_ID = 3;

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
        int tableId = 0;
        for (ArrayList<Lex> table : lexTables) {
            String tableName = "(UNDEFINED)";
            if(tableId == TABLE_SERVICE_ID) tableName = "(SERVICE)";
            if(tableId == TABLE_DELIMITERS_ID) tableName = "(DELIM)";
            if(tableId == TABLE_IDENTIFIERS_ID) tableName = "(IDENT)";
            if(tableId == TABLE_NUMBERS_ID) tableName = "(NUMBER)";
            sb.append(String.format("==============TABLE %s==============\n", tableName));
            for (Lex lex : table) {
                sb.append(String.format("%s\t%s\t%s\t\n", lex.getId(), lex.getTableId(), lex.getLexeme()));
            }
            tableId++;
        }
        sb.append("==================LEXEMES==================\n");
        for (LexAndTable lex : lexemesList) {
            String tableName = "(UNDEFINED)";
            if(lex.getTableId() == TABLE_SERVICE_ID) tableName = "(SERVICE)";
            if(lex.getTableId() == TABLE_DELIMITERS_ID) tableName = "(DELIM)";
            if(lex.getTableId() == TABLE_IDENTIFIERS_ID) tableName = "(IDENT)";
            if(lex.getTableId() == TABLE_NUMBERS_ID) tableName = "(NUMBER)";
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
