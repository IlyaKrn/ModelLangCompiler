package com.ilyakrn.entities;

import com.ilyakrn.entities.items.*;

import java.util.ArrayList;

public final class InternalProgramPresentation {

    public static final int serviceTableId = 0;
    public static final int delimiterTableId = 1;
    public static final int identifierTableId = 2;
    public static final int numberTableId = 3;
    public static final int polizTableId = 4;
    public static final int polizPointerTableId = 5;

    private final ArrayList<ServiceItem> serviceTable;
    private final ArrayList<DelimiterItem> delimiterTable;
    private final ArrayList<IdentifierItem> identifierTable;
    private final ArrayList<NumberItem> numberTable;
    private final ArrayList<BinOperationItem> binOperationTable;
    private final ArrayList<PolizItem> polizTable;
    private final ArrayList<PolizPointerItem> polizPointerTable;

    private final ArrayList<LexemesSeqItem> lexemesSeqTable;

    public InternalProgramPresentation(ArrayList<ServiceItem> serviceTable, ArrayList<DelimiterItem> delimiterTable, ArrayList<IdentifierItem> identifierTable, ArrayList<NumberItem> numberTable, ArrayList<LexemesSeqItem> lexemesSeqTable, ArrayList<BinOperationItem> binOperationTable, ArrayList<PolizItem> polizTable, ArrayList<PolizPointerItem> polizPointerTable) {
        this.serviceTable = serviceTable;
        this.delimiterTable = delimiterTable;
        this.identifierTable = identifierTable;
        this.numberTable = numberTable;
        this.lexemesSeqTable = lexemesSeqTable;
        this.binOperationTable = binOperationTable;
        this.polizTable = polizTable;
        this.polizPointerTable = polizPointerTable;
    }

    public ArrayList<PolizItem> getPolizTable() {
        return polizTable;
    }

    public ArrayList<BinOperationItem> getBinOperationTable() {
        return binOperationTable;
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

    public ArrayList<PolizPointerItem> getPolizPointerTable() {
        return polizPointerTable;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("LexerOutput:\nTables:\n");
        sb.append("=================BIN_OPS=================\n");
        for (int i = 0; i < binOperationTable.size(); i++) {
            sb.append(String.format("%s\t%s\t%s\t%s\t%s\n", i, binOperationTable.get(i).getOperation(), binOperationTable.get(i).getOperand1(), binOperationTable.get(i).getOperand2(), binOperationTable.get(i).getResult()));
        }
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
            sb.append(String.format("%s\t%s\t\t\t\t%s\t%s\n", i, identifierTable.get(i).getLexeme(), identifierTable.get(i).getType() != null ? identifierTable.get(i).getType().name() : "null", identifierTable.get(i).isInit()));
        }
        sb.append("===================NUM===================\n");
        for (int i = 0; i < numberTable.size(); i++) {
            sb.append(String.format("%s\t%s\t%s\t%s\n", i, numberTable.get(i).getLexeme(), numberTable.get(i).getType().name(), numberTable.get(i).getDimensionCount()));
        }
        sb.append("=================POINTER=================\n");
        for (int i = 0; i < polizPointerTable.size(); i++) {
            sb.append(String.format("%s\t%s\n", i, polizPointerTable.get(i).getPolizIndex()));
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
                    tableName = "num ";
                    break;
            }
            sb.append(String.format("%s\t%s\t%s\t%s\t\t\t\t%s\n", i, lexemesSeqTable.get(i).getLexId(), lexemesSeqTable.get(i).getTableId(), tableName, lexemeName ));
        }
        sb.append("===================POLIZ===================\n");
        for (int i = 0; i < polizTable.size(); i++) {
            String lexemeName = "";
            String tableName = "";
            switch (polizTable.get(i).getTableId()){
                case serviceTableId:
                    lexemeName = serviceTable.get(polizTable.get(i).getLexId()).getLexeme();
                    tableName = "serv";
                    break;
                case delimiterTableId:
                    lexemeName = delimiterTable.get(polizTable.get(i).getLexId()).getLexeme();
                    tableName = "delim";
                    break;
                case identifierTableId:
                    lexemeName = identifierTable.get(polizTable.get(i).getLexId()).getLexeme();
                    tableName = "ident";
                    break;
                case numberTableId:
                    lexemeName = numberTable.get(polizTable.get(i).getLexId()).getLexeme();
                    tableName = "num ";
                    break;
                case polizPointerTableId:
                    lexemeName = String.valueOf(polizPointerTable.get(polizTable.get(i).getLexId()).getPolizIndex());
                    tableName = "point";
                    break;
            }
            sb.append(String.format("%s\t%s\t\t%s\n", i, tableName, lexemeName));
        }

        return sb.toString();

    }
}
