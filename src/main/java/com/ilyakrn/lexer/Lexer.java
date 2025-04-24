package com.ilyakrn.lexer;

import com.ilyakrn.entities.InternalProgramPresentation;
import com.ilyakrn.entities.items.*;
import com.ilyakrn.exceptions.external.LexicalException;
import com.ilyakrn.exceptions.internal.InternalLexerException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Lexer {

    private enum STATE {
        READ,
        IDENT,
        NUM_BIN, NUM_BIN_FIN,
        NUM_OCT, NUM_OCT_FIN,
        NUM_DEC, NUM_DEC_FIN,
        NUM_HEX, NUM_HEX_FIN,
        NUM_REAL_POINT_1, NUM_REAL_POINT_2, NUM_REAL_POINT_ORDER_START_1, NUM_REAL_POINT_ORDER_START_2, NUM_REAL_POINT_ORDER,
        NUM_REAL_ORDER_OR_HEX, NUM_REAL_ORDER,
        DELIMITER,
        COMMENT,
        COMMENT_START,
        COMMENT_END,
        MORE_THEN_EQUAL,
        LESS_THEN_EQUAL,
        END,
        ERROR,
    }

    private String lexBuffer;
    private STATE currentState;
    private Character currentChar;
    private int curLexId;
    private int curTableId;

    private int curLine;
    private int curCol;

    private final ArrayList<ServiceItem> serviceTable;
    private final ArrayList<DelimiterItem> delimiterTable;
    private final ArrayList<IdentifierItem> identifierTable;
    private final ArrayList<NumberItem> numberTable;

    private final ArrayList<BinOperationItem> binOperationTable;

    private final ArrayList<LexemesSeqItem> lexemesSeqTable;

    private final Queue<Character> input;

    public Lexer() {
        input = new LinkedList<>();
        serviceTable = new ArrayList<>();
        delimiterTable = new ArrayList<>();
        identifierTable = new ArrayList<>();
        numberTable = new ArrayList<>();
        lexemesSeqTable = new ArrayList<>();
        binOperationTable = new ArrayList<>();
        curLine = 1;
        curCol = 1;

        serviceTable.add(new ServiceItem("true"));
        serviceTable.add(new ServiceItem("false"));
        serviceTable.add(new ServiceItem("int"));
        serviceTable.add(new ServiceItem("float"));
        serviceTable.add(new ServiceItem("bool"));
        serviceTable.add(new ServiceItem("if"));
        serviceTable.add(new ServiceItem("then"));
        serviceTable.add(new ServiceItem("else"));
        serviceTable.add(new ServiceItem("for"));
        serviceTable.add(new ServiceItem("while"));
        serviceTable.add(new ServiceItem("to"));
        serviceTable.add(new ServiceItem("do"));
        serviceTable.add(new ServiceItem("read"));
        serviceTable.add(new ServiceItem("write"));

        delimiterTable.add(new DelimiterItem("\n"));
        delimiterTable.add(new DelimiterItem(";"));
        delimiterTable.add(new DelimiterItem(":"));
        delimiterTable.add(new DelimiterItem(","));
        delimiterTable.add(new DelimiterItem(">"));
        delimiterTable.add(new DelimiterItem("<"));
        delimiterTable.add(new DelimiterItem("<>"));
        delimiterTable.add(new DelimiterItem(">="));
        delimiterTable.add(new DelimiterItem("<="));
        delimiterTable.add(new DelimiterItem("="));
        delimiterTable.add(new DelimiterItem("+"));
        delimiterTable.add(new DelimiterItem("-"));
        delimiterTable.add(new DelimiterItem("*"));
        delimiterTable.add(new DelimiterItem("/"));
        delimiterTable.add(new DelimiterItem("or"));
        delimiterTable.add(new DelimiterItem("and"));
        delimiterTable.add(new DelimiterItem("not"));
        delimiterTable.add(new DelimiterItem("ass"));
        delimiterTable.add(new DelimiterItem("("));
        delimiterTable.add(new DelimiterItem(")"));
        delimiterTable.add(new DelimiterItem("{"));
        delimiterTable.add(new DelimiterItem("}"));

        binOperationTable.add(new BinOperationItem(">", Type.INT, Type.INT, Type.INT));
        binOperationTable.add(new BinOperationItem("<", Type.INT, Type.INT, Type.INT));
        binOperationTable.add(new BinOperationItem("<>", Type.INT, Type.INT, Type.INT));
        binOperationTable.add(new BinOperationItem(">=", Type.INT, Type.INT, Type.INT));
        binOperationTable.add(new BinOperationItem("<=", Type.INT, Type.INT, Type.INT));
        binOperationTable.add(new BinOperationItem("=", Type.INT, Type.INT, Type.INT));
        binOperationTable.add(new BinOperationItem("+", Type.INT, Type.INT, Type.INT));
        binOperationTable.add(new BinOperationItem("-", Type.INT, Type.INT, Type.INT));
        binOperationTable.add(new BinOperationItem("*", Type.INT, Type.INT, Type.INT));
        binOperationTable.add(new BinOperationItem("/", Type.INT, Type.INT, Type.INT));

        binOperationTable.add(new BinOperationItem("or", Type.BOOL, Type.BOOL, Type.BOOL));
        binOperationTable.add(new BinOperationItem("and", Type.BOOL, Type.BOOL, Type.BOOL));
    }

    private boolean isBinAllow(){
        return currentChar >= '0' && currentChar <= '1';
    }
    private boolean isOctAllow(){
        return currentChar >= '0' && currentChar <= '7';
    }
    private boolean isDecAllow(){
        return currentChar >= '0' && currentChar <= '9';
    }
    private boolean isHexAllow(){
        return currentChar >= '0' && currentChar <= '9' || currentChar >= 'A' && currentChar <= 'F' || currentChar >= 'a' && currentChar <= 'f';
    }
    private boolean isNumber(){
        return currentChar >= '0' && currentChar <= '9';
    }
    private boolean isLetter(){
        return currentChar >= 'A' && currentChar <= 'Z' || currentChar >= 'a' && currentChar <= 'z';
    }

    private void clean(){
        lexBuffer = "";
    }

    private void add(){
        lexBuffer += currentChar;
    }

    private void read() {
        currentChar = input.poll();
        if (currentChar == '\n'){
            curLine++;
            curCol = 1;
        }
        else {
            curCol++;
        }
    }

    private void check(int tableId) throws InternalLexerException {
        switch (tableId){
            case InternalProgramPresentation.serviceTableId:
                curTableId = tableId;
                for (int i = 0; i < serviceTable.size(); i++) {
                    if(serviceTable.get(i).getLexeme().equals(lexBuffer)){
                        curLexId = i;
                        return;
                    }
                }
                curLexId = -1;
                break;
            case InternalProgramPresentation.delimiterTableId:
                curTableId = tableId;
                for (int i = 0; i < delimiterTable.size(); i++) {
                    if(delimiterTable.get(i).getLexeme().equals(lexBuffer)){
                        curLexId = i;
                        return;
                    }
                }
                curLexId = -1;
                break;
            case InternalProgramPresentation.identifierTableId:
                curTableId = tableId;
                for (int i = 0; i < identifierTable.size(); i++) {
                    if(identifierTable.get(i).getLexeme().equals(lexBuffer)){
                        curLexId = i;
                        return;
                    }
                }
                curLexId = -1;
                break;
            case InternalProgramPresentation.numberTableId:
                curTableId = tableId;
                for (int i = 0; i < numberTable.size(); i++) {
                    if(numberTable.get(i).getLexeme().equals(lexBuffer)){
                        curLexId = i;
                        return;
                    }
                }
                curLexId = -1;
                break;
            default:
                throw new InternalLexerException("table with id '" + tableId + "' not exists");
        }
    }

    private void put(int tableId) throws InternalLexerException {
        switch (tableId){
            case InternalProgramPresentation.serviceTableId:
            case InternalProgramPresentation.delimiterTableId:
                throw new InternalLexerException("table with id '" + tableId + "' can not modify");
            case InternalProgramPresentation.identifierTableId:
                curTableId = tableId;
                int findId = -1;
                for (int i = 0; i < identifierTable.size(); i++) {
                    if(identifierTable.get(i).getLexeme().equals(lexBuffer)){
                        findId = i;
                        break;
                    }
                }
                if(findId == -1){
                    identifierTable.add(new IdentifierItem(lexBuffer, false, null));
                    curLexId = identifierTable.size() - 1;
                }
                else {
                    curLexId = findId;
                }
                break;
            case InternalProgramPresentation.numberTableId:
                curTableId = tableId;
                findId = -1;
                for (int i = 0; i < numberTable.size(); i++) {
                    if(numberTable.get(i).getLexeme().equals(lexBuffer)){
                        findId = i;
                        break;
                    }
                }
                if(findId == -1){
                    numberTable.add(new NumberItem(lexBuffer));
                    curLexId = numberTable.size() - 1;
                }
                else {
                    curLexId = findId;
                }
                break;
            default:
                throw new InternalLexerException("table with id '" + tableId + "' not exists");
        }
    }

    private void write(int tableId, int lexId) throws InternalLexerException {
        switch (tableId){
            case InternalProgramPresentation.serviceTableId:
                if (lexId >= serviceTable.size() || lexId < 0)
                    throw new InternalLexerException("lexeme in table '" + tableId + "' with id '" + lexId + "' not exists");
                lexemesSeqTable.add(new LexemesSeqItem(tableId, lexId));
                break;
            case InternalProgramPresentation.delimiterTableId:
                if (lexId >= delimiterTable.size() || lexId < 0)
                    throw new InternalLexerException("lexeme in table '" + tableId + "' with id '" + lexId + "' not exists");
                lexemesSeqTable.add(new LexemesSeqItem(tableId, lexId));
                break;
            case InternalProgramPresentation.identifierTableId:
                if (lexId >= identifierTable.size() || lexId < 0)
                    throw new InternalLexerException("lexeme in table '" + tableId + "' with id '" + lexId + "' not exists");
                lexemesSeqTable.add(new LexemesSeqItem(tableId, lexId));
                break;
            case InternalProgramPresentation.numberTableId:
                if (lexId >= numberTable.size() || lexId < 0)
                    throw new InternalLexerException("lexeme in table '" + tableId + "' with id '" + lexId + "' not exists");
                lexemesSeqTable.add(new LexemesSeqItem(tableId, lexId));
                break;
            default:
                throw new InternalLexerException("table with id '" + tableId + "' not exists");
        }
    }

    public InternalProgramPresentation analyze(String progText) throws InternalLexerException, LexicalException {
        identifierTable.clear();
        numberTable.clear();
        lexemesSeqTable.clear();
        input.clear();
        lexBuffer = "";
        currentChar = null;
        String message = "";

        char[] progChars = progText.toCharArray();
        for (int i = 0; i < progChars.length; i++) {
            input.add(progChars[i]);
        }

        currentState = STATE.READ;
        while (currentState != STATE.END && currentState != STATE.ERROR) {
            switch (currentState) {
                case READ:
                    read();
                    if(currentChar == null){
                        message = "program end reached before '}'";
                        currentState = STATE.ERROR;
                    } else if (currentChar == ' ') {
                        clean();
                    } else if (isBinAllow()) {
                        currentState = STATE.NUM_BIN;
                        clean();
                        add();
                    } else if (isOctAllow()) {
                        currentState = STATE.NUM_OCT;
                        clean();
                        add();
                    } else if (isDecAllow()) {
                        currentState = STATE.NUM_DEC;
                        clean();
                        add();
                    } else if (isLetter()) {
                        currentState = STATE.IDENT;
                        clean();
                        add();
                    } else if (currentChar == '.') {
                        currentState = STATE.NUM_REAL_POINT_1;
                        clean();
                        add();
                    } else {
                        currentState = STATE.DELIMITER;
                        clean();
                        add();
                    }
                    break;
                case NUM_BIN:
                    read();
                    if(currentChar == null){
                        message = "program end reached before '}'";
                        currentState = STATE.ERROR;
                    } else if (isBinAllow()) {
                        add();
                    } else if (currentChar == '.') {
                        currentState = STATE.NUM_REAL_POINT_1;
                        add();
                    } else if (currentChar == 'E' || currentChar == 'e') {
                        currentState = STATE.NUM_REAL_ORDER_OR_HEX;
                        add();
                    } else if (currentChar == 'B' || currentChar == 'b') {
                        currentState = STATE.NUM_BIN_FIN;
                        add();
                    } else if (currentChar == 'O' || currentChar == 'o') {
                        currentState = STATE.NUM_OCT_FIN;
                        add();
                    } else if (currentChar == 'D' || currentChar == 'd') {
                        currentState = STATE.NUM_DEC_FIN;
                        add();
                    } else if (currentChar == 'H' || currentChar == 'h') {
                        currentState = STATE.NUM_HEX_FIN;
                        add();
                    } else if (isOctAllow()) {
                        currentState = STATE.NUM_OCT;
                        add();
                    } else if (isDecAllow()) {
                        currentState = STATE.NUM_DEC;
                        add();
                    } else if (isHexAllow()) {
                        currentState = STATE.NUM_HEX;
                        add();
                    } else if (isLetter()) {
                        message = "unresolved character '" + currentChar + "'";
                        currentState = STATE.ERROR;
                    } else {
                        currentState = STATE.DELIMITER;
                        put(InternalProgramPresentation.numberTableId);
                        write(InternalProgramPresentation.numberTableId, curLexId);
                        clean();
                        add();
                    }
                    break;
                case NUM_OCT:
                    read();
                    if(currentChar == null){
                        message = "program end reached before '}'";
                        currentState = STATE.ERROR;
                    } else if (isOctAllow()) {
                        add();
                    } else if (currentChar == '.') {
                        currentState = STATE.NUM_REAL_POINT_1;
                        add();
                    } else if (currentChar == 'E' || currentChar == 'e') {
                        currentState = STATE.NUM_REAL_ORDER_OR_HEX;
                        add();
                    } else if (currentChar == 'O' || currentChar == 'o') {
                        currentState = STATE.NUM_OCT_FIN;
                        add();
                    } else if (currentChar == 'D' || currentChar == 'd') {
                        currentState = STATE.NUM_DEC_FIN;
                        add();
                    } else if (currentChar == 'H' || currentChar == 'h') {
                        currentState = STATE.NUM_HEX_FIN;
                        add();
                    } else if (isDecAllow()) {
                        currentState = STATE.NUM_DEC;
                        add();
                    } else if (isHexAllow()) {
                        currentState = STATE.NUM_HEX;
                        add();
                    } else if (isLetter()) {
                        message = "unresolved character '" + currentChar + "'";
                        currentState = STATE.ERROR;
                    } else {
                        currentState = STATE.DELIMITER;
                        put(InternalProgramPresentation.numberTableId);
                        write(InternalProgramPresentation.numberTableId, curLexId);
                        clean();
                        add();
                    }
                    break;
                case NUM_DEC:
                    read();
                    if(currentChar == null){
                        message = "program end reached before '}'";
                        currentState = STATE.ERROR;
                    } else if (isDecAllow()) {
                        add();
                    } else if (currentChar == '.') {
                        currentState = STATE.NUM_REAL_POINT_1;
                        add();
                    } else if (currentChar == 'E' || currentChar == 'e') {
                        currentState = STATE.NUM_REAL_ORDER_OR_HEX;
                        add();
                    } else if (currentChar == 'D' || currentChar == 'd') {
                        currentState = STATE.NUM_DEC_FIN;
                        add();
                    } else if (currentChar == 'H' || currentChar == 'h') {
                        currentState = STATE.NUM_HEX_FIN;
                        add();
                    } else if (isHexAllow()) {
                        currentState = STATE.NUM_HEX;
                        add();
                    } else if (isLetter()) {
                        message = "unresolved character '" + currentChar + "'";
                        currentState = STATE.ERROR;
                    } else {
                        currentState = STATE.DELIMITER;
                        put(InternalProgramPresentation.numberTableId);
                        write(InternalProgramPresentation.numberTableId, curLexId);
                        clean();
                        add();
                    }
                    break;
                case NUM_HEX:
                    read();
                    if(currentChar == null){
                        message = "program end reached before '}'";
                        currentState = STATE.ERROR;
                    } else if (isHexAllow()) {
                        add();
                    } else if (currentChar == 'H' || currentChar == 'h') {
                        currentState = STATE.NUM_HEX_FIN;
                        add();
                    } else {
                        message = "unresolved character '" + currentChar + "'";
                        currentState = STATE.ERROR;
                    }
                    break;
                case IDENT:
                    read();
                    if(currentChar == null){
                        message = "program end reached before '}'";
                        currentState = STATE.ERROR;
                    } else if (isLetter()) {
                        add();
                    } else if (isNumber()) {
                        add();
                    } else {
                        currentState = STATE.DELIMITER;
                        check(InternalProgramPresentation.serviceTableId);
                        if (curLexId == -1){
                            check(InternalProgramPresentation.delimiterTableId);
                            if (curLexId == -1){
                                put(InternalProgramPresentation.identifierTableId);
                                write(InternalProgramPresentation.identifierTableId, curLexId);
                            }
                            else {
                                write(InternalProgramPresentation.delimiterTableId, curLexId);
                            }
                        }
                        else {
                            write(InternalProgramPresentation.serviceTableId, curLexId);
                        }
                        clean();
                        add();
                    }
                    break;
                case NUM_BIN_FIN:
                case NUM_DEC_FIN:
                    read();
                    if(currentChar == null){
                        message = "program end reached before '}'";
                        currentState = STATE.ERROR;
                    } else if (currentChar == 'H' || currentChar == 'h') {
                        currentState = STATE.NUM_HEX_FIN;
                        add();
                    } else if (isHexAllow()) {
                        currentState = STATE.NUM_HEX;
                        add();
                    } else if (isLetter() || isNumber()) {
                        message = "unresolved character '" + currentChar + "'";
                        currentState = STATE.ERROR;
                    } else {
                        currentState = STATE.DELIMITER;
                        put(InternalProgramPresentation.numberTableId);
                        write(InternalProgramPresentation.numberTableId, curLexId);
                        clean();
                        add();
                    }
                    break;
                case NUM_OCT_FIN:
                case NUM_HEX_FIN:
                    read();
                    if(currentChar == null){
                        message = "program end reached before '}'";
                        currentState = STATE.ERROR;
                    } else if (isLetter() || isNumber()) {
                        message = "unresolved character '" + currentChar + "'";
                        currentState = STATE.ERROR;
                    } else {
                        currentState = STATE.DELIMITER;
                        put(InternalProgramPresentation.numberTableId);
                        write(InternalProgramPresentation.numberTableId, curLexId);
                        clean();
                        add();
                    }
                    break;
                case NUM_REAL_POINT_1:
                    read();
                    if(currentChar == null){
                        message = "program end reached before '}'";
                        currentState = STATE.ERROR;
                    } else if (isNumber()) {
                        currentState = STATE.NUM_REAL_POINT_2;
                        add();
                    } else {
                        message = "unresolved character '" + currentChar + "'";
                        currentState = STATE.ERROR;
                    }
                    break;
                case NUM_REAL_POINT_2:
                    read();
                    if(currentChar == null){
                        message = "program end reached before '}'";
                        currentState = STATE.ERROR;
                    } else if (isNumber()) {
                        add();
                    } else if (currentChar == 'E' || currentChar == 'e') {
                        currentState = STATE.NUM_REAL_POINT_ORDER_START_1;
                        add();
                    } else if (isLetter() || currentChar == '.') {
                        message = "unresolved character '" + currentChar + "'";
                        currentState = STATE.ERROR;
                    } else {
                        currentState = STATE.DELIMITER;
                        put(InternalProgramPresentation.numberTableId);
                        write(InternalProgramPresentation.numberTableId, curLexId);
                        clean();
                        add();
                    }
                    break;
                case NUM_REAL_POINT_ORDER_START_1:
                    read();
                    if(currentChar == null){
                        message = "can not resolve real order";
                        currentState = STATE.ERROR;
                    } else if (isNumber()) {
                        currentState = STATE.NUM_REAL_POINT_ORDER;
                        add();
                    } else if (currentChar == '+' || currentChar == '-') {
                        currentState = STATE.NUM_REAL_POINT_ORDER_START_2;
                        add();
                    } else if (isLetter() || currentChar == '.') {
                        message = "unresolved character '" + currentChar + "'";
                        currentState = STATE.ERROR;
                    } else {
                        currentState = STATE.DELIMITER;
                        put(InternalProgramPresentation.numberTableId);
                        write(InternalProgramPresentation.numberTableId, curLexId);
                        clean();
                        add();
                    }
                    break;
                case NUM_REAL_POINT_ORDER_START_2:
                    read();
                    if(currentChar == null){
                        message = "can not resolve real order";
                        currentState = STATE.ERROR;
                    } else if (isNumber()) {
                        currentState = STATE.NUM_REAL_POINT_ORDER;
                        add();
                    } else if (isLetter() || currentChar == '.') {
                        message = "unresolved character '" + currentChar + "'";
                        currentState = STATE.ERROR;
                    } else {
                        currentState = STATE.DELIMITER;
                        put(InternalProgramPresentation.numberTableId);
                        write(InternalProgramPresentation.numberTableId, curLexId);
                        clean();
                        add();
                    }
                    break;
                case NUM_REAL_POINT_ORDER:
                    read();
                    if(currentChar == null){
                        message = "program end reached before '}'";
                        currentState = STATE.ERROR;
                    } else if (isNumber()) {
                        add();
                    } else if (isLetter() || currentChar == '.') {
                        message = "unresolved character '" + currentChar + "'";
                        currentState = STATE.ERROR;
                    } else {
                        currentState = STATE.DELIMITER;
                        put(InternalProgramPresentation.numberTableId);
                        write(InternalProgramPresentation.numberTableId, curLexId);
                        clean();
                        add();
                    }
                    break;
                case NUM_REAL_ORDER_OR_HEX:
                    read();
                    if(currentChar == null){
                        message = "program end reached before '}'";
                        currentState = STATE.ERROR;
                    } else if (isNumber()) {
                        add();
                    } else if (isHexAllow()) {
                        currentState = STATE.NUM_HEX;
                    } else if (currentChar == '+' || currentChar == '-') {
                        currentState = STATE.NUM_REAL_ORDER;
                        add();
                    } else if (currentChar == 'H' || currentChar == 'h') {
                        currentState = STATE.NUM_HEX_FIN;
                        add();
                    } else if (isLetter() || currentChar == '.') {
                        message = "unresolved character '" + currentChar + "'";
                        currentState = STATE.ERROR;
                    } else {
                        currentState = STATE.DELIMITER;
                        put(InternalProgramPresentation.numberTableId);
                        write(InternalProgramPresentation.numberTableId, curLexId);
                        clean();
                        add();
                    }
                    break;
                case NUM_REAL_ORDER:
                    read();
                    if(currentChar == null){
                        message = "program end reached before '}'";
                        currentState = STATE.ERROR;
                    } else if (isNumber()) {
                        add();
                    } else if (isLetter() || currentChar == '.' || currentChar == '+' || currentChar == '-') {
                        message = "unresolved character '" + currentChar + "'";
                        currentState = STATE.ERROR;
                    } else {
                        currentState = STATE.DELIMITER;
                        put(InternalProgramPresentation.numberTableId);
                        write(InternalProgramPresentation.numberTableId, curLexId);
                        clean();
                        add();
                    }
                    break;
                case DELIMITER:
                    if(currentChar == null){
                        message = "program end reached before '}'";
                        currentState = STATE.ERROR;
                    }
                    else if(currentChar == ' '){
                        currentState = STATE.READ;
                        clean();
                    }
                    else if (currentChar == '>') {
                        currentState = STATE.MORE_THEN_EQUAL;
                    }
                    else if (currentChar == '<') {
                        currentState = STATE.LESS_THEN_EQUAL;
                    }
                    else if (currentChar == '/') {
                        currentState = STATE.COMMENT_START;
                    }
                    else {
                        check(InternalProgramPresentation.delimiterTableId);
                        if (curLexId == -1){
                            currentState = STATE.ERROR;
                            message = "unresolved character '" + currentChar + "'";
                        }
                        else {
                            if (currentChar == '}'){
                                currentState = STATE.END;
                            }
                            else {
                                currentState = STATE.READ;
                            }
                            write(InternalProgramPresentation.delimiterTableId, curLexId);
                            clean();
                        }
                    }
                    break;
                case MORE_THEN_EQUAL:
                case LESS_THEN_EQUAL:
                    read();
                    if(currentChar == null){
                        message = "program end reached before '}'";
                        currentState = STATE.ERROR;
                    } else if(currentChar == '='){
                        add();
                        check(InternalProgramPresentation.delimiterTableId);
                        if(curLexId == -1){
                            currentState = STATE.ERROR;
                            message = "unresolved character '" + currentChar + "'";
                        }
                        else {
                            currentState = STATE.READ;
                            write(InternalProgramPresentation.delimiterTableId, curLexId);
                            clean();
                        }
                    } else if(currentChar == '>'){
                        add();
                        check(InternalProgramPresentation.delimiterTableId);
                        if(curLexId == -1){
                            currentState = STATE.ERROR;
                            message = "unresolved character '" + currentChar + "'";
                        }
                        else {
                            currentState = STATE.READ;
                            write(InternalProgramPresentation.delimiterTableId, curLexId);
                            clean();
                        }
                    } else if (isBinAllow()) {
                        currentState = STATE.NUM_BIN;
                        check(InternalProgramPresentation.delimiterTableId);
                        if(curLexId == -1){
                            currentState = STATE.ERROR;
                            message = "unresolved character '" + currentChar + "'";
                        }
                        else {
                            write(InternalProgramPresentation.delimiterTableId, curLexId);
                        }
                        clean();
                        add();
                    } else if (isOctAllow()) {
                        currentState = STATE.NUM_OCT;
                        check(InternalProgramPresentation.delimiterTableId);
                        if(curLexId == -1){
                            currentState = STATE.ERROR;
                            message = "unresolved character '" + currentChar + "'";
                        }
                        else {
                            write(InternalProgramPresentation.delimiterTableId, curLexId);
                        }
                        clean();
                        add();
                    } else if (isDecAllow()) {
                        currentState = STATE.NUM_DEC;
                        check(InternalProgramPresentation.delimiterTableId);
                        if(curLexId == -1){
                            currentState = STATE.ERROR;
                            message = "unresolved character '" + currentChar + "'";
                        }
                        else {
                            write(InternalProgramPresentation.delimiterTableId, curLexId);
                        }
                        clean();
                        add();
                    } else if (isLetter()) {
                        currentState = STATE.IDENT;
                        check(InternalProgramPresentation.delimiterTableId);
                        if(curLexId == -1){
                            currentState = STATE.ERROR;
                            message = "unresolved character '" + currentChar + "'";
                        }
                        else {
                            write(InternalProgramPresentation.delimiterTableId, curLexId);
                        }
                        clean();
                        add();
                    } else if (currentChar == '.') {
                        currentState = STATE.NUM_REAL_POINT_1;
                        if(curLexId == -1){
                            currentState = STATE.ERROR;
                            message = "unresolved character '" + currentChar + "'";
                        }
                        else {
                            write(InternalProgramPresentation.delimiterTableId, curLexId);
                        }
                        clean();
                        add();
                    } else {
                        check(InternalProgramPresentation.delimiterTableId);
                        if(curLexId == -1){
                            currentState = STATE.ERROR;
                            message = "unresolved character '" + currentChar + "'";
                        }
                        else {
                            currentState = STATE.DELIMITER;
                            write(InternalProgramPresentation.delimiterTableId, curLexId);
                            clean();
                            add();
                        }
                    }
                    break;
                case COMMENT_START:
                    read();
                    if(currentChar == null){
                        message = "program end reached before '}'";
                        currentState = STATE.ERROR;
                    } else if(currentChar == ' '){
                        currentState = STATE.READ;

                        check(InternalProgramPresentation.delimiterTableId);
                        if(curLexId == -1){
                            currentState = STATE.ERROR;
                            message = "unresolved character '" + currentChar + "'";
                        }
                        else {
                            write(InternalProgramPresentation.delimiterTableId, curLexId);
                        }
                        clean();
                    } else if(currentChar == '*'){
                        currentState = STATE.COMMENT;
                        clean();
                    } else if (isBinAllow()) {
                        currentState = STATE.NUM_BIN;
                        check(InternalProgramPresentation.delimiterTableId);
                        if(curLexId == -1){
                            currentState = STATE.ERROR;
                            message = "unresolved character '" + currentChar + "'";
                        }
                        else {
                            write(InternalProgramPresentation.delimiterTableId, curLexId);
                        }
                        clean();
                        add();
                    } else if (isOctAllow()) {
                        currentState = STATE.NUM_OCT;
                        check(InternalProgramPresentation.delimiterTableId);
                        if(curLexId == -1){
                            currentState = STATE.ERROR;
                            message = "unresolved character '" + currentChar + "'";
                        }
                        else {
                            write(InternalProgramPresentation.delimiterTableId, curLexId);
                        }
                        clean();
                        add();
                    } else if (isDecAllow()) {
                        currentState = STATE.NUM_DEC;
                        check(InternalProgramPresentation.delimiterTableId);
                        if(curLexId == -1){
                            currentState = STATE.ERROR;
                            message = "unresolved character '" + currentChar + "'";
                        }
                        else {
                            write(InternalProgramPresentation.delimiterTableId, curLexId);
                        }
                        clean();
                        add();
                    } else if (isLetter()) {
                        currentState = STATE.IDENT;
                        check(InternalProgramPresentation.delimiterTableId);
                        if(curLexId == -1){
                            currentState = STATE.ERROR;
                            message = "unresolved character '" + currentChar + "'";
                        }
                        else {
                            write(InternalProgramPresentation.delimiterTableId, curLexId);
                        }
                        clean();
                        add();
                    } else if (currentChar == '.') {
                        currentState = STATE.NUM_REAL_POINT_1;
                        if(curLexId == -1){
                            currentState = STATE.ERROR;
                            message = "unresolved character '" + currentChar + "'";
                        }
                        else {
                            write(InternalProgramPresentation.delimiterTableId, curLexId);
                        }
                        clean();
                        add();
                    } else {
                        check(InternalProgramPresentation.delimiterTableId);
                        if(curLexId == -1){
                            currentState = STATE.ERROR;
                            message = "unresolved character '" + currentChar + "'";
                        }
                        else {
                            currentState = STATE.DELIMITER;
                            write(InternalProgramPresentation.delimiterTableId, curLexId);
                            clean();
                            add();
                        }
                    }
                    break;
                case COMMENT:
                    read();
                    add();
                    if(currentChar == null){
                        message = "program end reached before '}'";
                        currentState = STATE.ERROR;
                    } else if(currentChar == '*') {
                        currentState = STATE.COMMENT_END;
                    }
                    break;
                case COMMENT_END:
                    read();
                    if(currentChar == null){
                        message = "program end reached before '}'";
                        currentState = STATE.ERROR;
                    } else if(currentChar == '/') {
                        currentState = STATE.READ;
                        clean();
                    }
                    else {
                        currentState = STATE.COMMENT;
                        add();
                    }
                    break;
                default:
                    throw new InternalLexerException("can not resolve state '" + currentState.name() + "'");

            }

        }

        if (currentState == STATE.ERROR) {
            throw new LexicalException(curLine + ":" + (curCol - 1) + "\t" + message);
        }

        return new InternalProgramPresentation(serviceTable, delimiterTable, identifierTable, numberTable, lexemesSeqTable, binOperationTable);
    }





}
