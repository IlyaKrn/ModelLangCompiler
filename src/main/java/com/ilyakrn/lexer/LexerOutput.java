package com.ilyakrn.lexer;

import com.ilyakrn.lexer.items.*;

import java.util.ArrayList;

public final class LexerOutput {

    public static final int serviceTableId = 0;
    public static final int delimiterTableId = 1;
    public static final int identifierTableId = 2;
    public static final int numberTableId = 3;

    private final ArrayList<ServiceItem> serviceTable;
    private final ArrayList<DelimiterItem> delimiterTable;
    private final ArrayList<IdentifierItem> identifierTable;
    private final ArrayList<NumberItem> numberTable;

    private final ArrayList<LexemesSeqItem> lexemesSeqTable;

    private final boolean isError;
    private final String message;

    public LexerOutput(ArrayList<ServiceItem> serviceTable, ArrayList<DelimiterItem> delimiterTable, ArrayList<IdentifierItem> identifierTable, ArrayList<NumberItem> numberTable, ArrayList<LexemesSeqItem> lexemesSeqTable, boolean isError, String message) {
        this.serviceTable = serviceTable;
        this.delimiterTable = delimiterTable;
        this.identifierTable = identifierTable;
        this.numberTable = numberTable;
        this.lexemesSeqTable = lexemesSeqTable;
        this.isError = isError;
        this.message = message;
    }

    public ArrayList<ServiceItem> getServiceTable() {
        return serviceTable;
    }

    public ArrayList<DelimiterItem> getDelimiterTable() {
        return delimiterTable;
    }

    public ArrayList<IdentifierItem> getIdentifierTable() {
        return identifierTable;
    }

    public ArrayList<NumberItem> getNumberTable() {
        return numberTable;
    }

    public ArrayList<LexemesSeqItem> getLexemesSeqTable() {
        return lexemesSeqTable;
    }

    public boolean isError() {
        return isError;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("LexerOutput:\nTables:\n");
        sb.append("=================SERVICE=================\n");
        for (int i = 0; i < serviceTable.size(); i++) {
            sb.append(String.format("%s\t%s\n", i, serviceTable.get(i).getLexeme()));
        }
        sb.append("==================DELIM==================\n");
        for (int i = 0; i < delimiterTable.size(); i++) {
            sb.append(String.format("%s\t%s\n", i, delimiterTable.get(i).getLexeme()));
        }
        sb.append("==================IDENT==================\n");
        for (int i = 0; i < identifierTable.size(); i++) {
            sb.append(String.format("%s\t%s\t\t\t\t%s\t%s\n", i, identifierTable.get(i).getLexeme(), identifierTable.get(i).getType(), identifierTable.get(i).isInit()));
        }
        sb.append("===================NUM===================\n");
        for (int i = 0; i < numberTable.size(); i++) {
            sb.append(String.format("%s\t%s\n", i, numberTable.get(i).getLexeme()));
        }
        sb.append("==================LEXEMES==================\n");
        for (int i = 0; i < lexemesSeqTable.size(); i++) {
            String lexemeName = "";
            String tableName = "";
            switch (lexemesSeqTable.get(i).getTableId()){
                case serviceTableId:
                    lexemeName = serviceTable.get(lexemesSeqTable.get(i).getLexId()).getLexeme();
                    tableName = "serv";
                    break;
                case delimiterTableId:
                    lexemeName = delimiterTable.get(lexemesSeqTable.get(i).getLexId()).getLexeme();
                    tableName = "delim";
                    break;
                case identifierTableId:
                    lexemeName = identifierTable.get(lexemesSeqTable.get(i).getLexId()).getLexeme();
                    tableName = "ident";
                    break;
                case numberTableId:
                    lexemeName = numberTable.get(lexemesSeqTable.get(i).getLexId()).getLexeme();
                    tableName = "num";
                    break;
            }
            sb.append(String.format("%s\t%s\t%s\t%s\t\t\t\t%s\n", i, lexemesSeqTable.get(i).getLexId(), lexemesSeqTable.get(i).getTableId(), tableName, lexemeName ));
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
