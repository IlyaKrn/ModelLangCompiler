package com.ilyakrn.lexer;

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

    private final ArrayList<ArrayList<Lex>> lexTables;
    ArrayList<LexAndTable> lexemesList = new ArrayList<>();

    private final Queue<Character> input;

    public Lexer() {
        input = new LinkedList<>();
        lexTables = new ArrayList<>();
        lexTables.add(new ArrayList<>());
        lexTables.add(new ArrayList<>());
        lexTables.add(new ArrayList<>());
        lexTables.add(new ArrayList<>());

        lexTables.get(LexerOutput.TABLE_SERVICE_ID).add(new Lex(0, LexerOutput.TABLE_SERVICE_ID, "true"));
        lexTables.get(LexerOutput.TABLE_SERVICE_ID).add(new Lex(1, LexerOutput.TABLE_SERVICE_ID, "false"));
        lexTables.get(LexerOutput.TABLE_SERVICE_ID).add(new Lex(2, LexerOutput.TABLE_SERVICE_ID, "int"));
        lexTables.get(LexerOutput.TABLE_SERVICE_ID).add(new Lex(3, LexerOutput.TABLE_SERVICE_ID, "float"));
        lexTables.get(LexerOutput.TABLE_SERVICE_ID).add(new Lex(4, LexerOutput.TABLE_SERVICE_ID, "bool"));
        lexTables.get(LexerOutput.TABLE_SERVICE_ID).add(new Lex(5, LexerOutput.TABLE_SERVICE_ID, "if"));
        lexTables.get(LexerOutput.TABLE_SERVICE_ID).add(new Lex(6, LexerOutput.TABLE_SERVICE_ID, "then"));
        lexTables.get(LexerOutput.TABLE_SERVICE_ID).add(new Lex(7, LexerOutput.TABLE_SERVICE_ID, "else"));
        lexTables.get(LexerOutput.TABLE_SERVICE_ID).add(new Lex(8, LexerOutput.TABLE_SERVICE_ID, "for"));
        lexTables.get(LexerOutput.TABLE_SERVICE_ID).add(new Lex(9, LexerOutput.TABLE_SERVICE_ID, "while"));
        lexTables.get(LexerOutput.TABLE_SERVICE_ID).add(new Lex(10, LexerOutput.TABLE_SERVICE_ID, "to"));
        lexTables.get(LexerOutput.TABLE_SERVICE_ID).add(new Lex(11, LexerOutput.TABLE_SERVICE_ID, "do"));
        lexTables.get(LexerOutput.TABLE_SERVICE_ID).add(new Lex(12, LexerOutput.TABLE_SERVICE_ID, "read"));
        lexTables.get(LexerOutput.TABLE_SERVICE_ID).add(new Lex(13, LexerOutput.TABLE_SERVICE_ID, "write"));

        lexTables.get(LexerOutput.TABLE_DELIMITERS_ID).add(new Lex(0, LexerOutput.TABLE_DELIMITERS_ID, "\n"));
        lexTables.get(LexerOutput.TABLE_DELIMITERS_ID).add(new Lex(1, LexerOutput.TABLE_DELIMITERS_ID, ";"));
        lexTables.get(LexerOutput.TABLE_DELIMITERS_ID).add(new Lex(2, LexerOutput.TABLE_DELIMITERS_ID, ":"));
        lexTables.get(LexerOutput.TABLE_DELIMITERS_ID).add(new Lex(3, LexerOutput.TABLE_DELIMITERS_ID, ","));
        lexTables.get(LexerOutput.TABLE_DELIMITERS_ID).add(new Lex(4, LexerOutput.TABLE_DELIMITERS_ID, ">"));
        lexTables.get(LexerOutput.TABLE_DELIMITERS_ID).add(new Lex(5, LexerOutput.TABLE_DELIMITERS_ID, "<"));
        lexTables.get(LexerOutput.TABLE_DELIMITERS_ID).add(new Lex(6, LexerOutput.TABLE_DELIMITERS_ID, "<>"));
        lexTables.get(LexerOutput.TABLE_DELIMITERS_ID).add(new Lex(7, LexerOutput.TABLE_DELIMITERS_ID, ">="));
        lexTables.get(LexerOutput.TABLE_DELIMITERS_ID).add(new Lex(8, LexerOutput.TABLE_DELIMITERS_ID, "<="));
        lexTables.get(LexerOutput.TABLE_DELIMITERS_ID).add(new Lex(9, LexerOutput.TABLE_DELIMITERS_ID, "="));
        lexTables.get(LexerOutput.TABLE_DELIMITERS_ID).add(new Lex(10, LexerOutput.TABLE_DELIMITERS_ID, "+"));
        lexTables.get(LexerOutput.TABLE_DELIMITERS_ID).add(new Lex(11, LexerOutput.TABLE_DELIMITERS_ID, "-"));
        lexTables.get(LexerOutput.TABLE_DELIMITERS_ID).add(new Lex(12, LexerOutput.TABLE_DELIMITERS_ID, "*"));
        lexTables.get(LexerOutput.TABLE_DELIMITERS_ID).add(new Lex(13, LexerOutput.TABLE_DELIMITERS_ID, "/"));
        lexTables.get(LexerOutput.TABLE_DELIMITERS_ID).add(new Lex(14, LexerOutput.TABLE_DELIMITERS_ID, "or"));
        lexTables.get(LexerOutput.TABLE_DELIMITERS_ID).add(new Lex(15, LexerOutput.TABLE_DELIMITERS_ID, "and"));
        lexTables.get(LexerOutput.TABLE_DELIMITERS_ID).add(new Lex(16, LexerOutput.TABLE_DELIMITERS_ID, "not"));
        lexTables.get(LexerOutput.TABLE_DELIMITERS_ID).add(new Lex(17, LexerOutput.TABLE_DELIMITERS_ID, "ass"));
        lexTables.get(LexerOutput.TABLE_DELIMITERS_ID).add(new Lex(18, LexerOutput.TABLE_DELIMITERS_ID, "("));
        lexTables.get(LexerOutput.TABLE_DELIMITERS_ID).add(new Lex(19, LexerOutput.TABLE_DELIMITERS_ID, ")"));
        lexTables.get(LexerOutput.TABLE_DELIMITERS_ID).add(new Lex(20, LexerOutput.TABLE_DELIMITERS_ID, "{"));
        lexTables.get(LexerOutput.TABLE_DELIMITERS_ID).add(new Lex(21, LexerOutput.TABLE_DELIMITERS_ID, "}"));
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

    private void check(int tableId) throws Exception {
        if(tableId >= lexTables.size()){
            throw new Exception("table with id '" + tableId + "' not exists");
        }
        curTableId = tableId;
        for (int i = 0; i < lexTables.get(tableId).size(); i++) {
            if(lexTables.get(tableId).get(i).getLexeme().equals(lexBuffer)){
                curLexId = lexTables.get(tableId).get(i).getId();
                return;
            }
        }
        curLexId = -1;
    }

    private void put(int tableId) throws Exception {
        if(tableId >= lexTables.size()){
            throw new Exception("table with id '" + tableId + "' not exists");
        }
        if(tableId == LexerOutput.TABLE_SERVICE_ID || tableId == LexerOutput.TABLE_DELIMITERS_ID){
            throw new Exception("table with id '" + tableId + "' read only");
        }
        curTableId = tableId;
        int findId = -1;
        for (Lex lex : lexTables.get(tableId)) {
            if(lex.getLexeme().equals(lexBuffer)){
                findId = lex.getId();
                break;
            }
        }
        if(findId == -1){
            lexTables.get(tableId).add(new Lex(lexTables.get(tableId).size(), tableId, lexBuffer));
            curLexId = lexTables.get(tableId).size() - 1;
        }
        else {
            curLexId = findId;
        }
    }

    private void clean(){
        lexBuffer = "";
    }

    private void add(){
        lexBuffer += currentChar;
    }

    private void read() {
        currentChar = input.poll();
    }

    private void write(int tableId, int lexId) throws Exception {
        if(tableId >= lexTables.size()){
            throw new Exception("table with id '" + tableId + "' not exists");
        }
        if(lexId >= lexTables.get(tableId).size()){
            throw new Exception("lexeme in table '" + tableId + "' with id '" + lexId + "' not exists");
        }
        lexemesList.add(new LexAndTable(tableId, lexId));
    }

    public LexerOutput analyze(String progText) throws Exception {
        lexTables.get(LexerOutput.TABLE_IDENTIFIERS_ID).clear();
        lexTables.get(LexerOutput.TABLE_NUMBERS_ID).clear();
        lexemesList.clear();
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
                        message = "PROGRAM END REACHED BEFORE '}'";
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
                        message = "PROGRAM END REACHED BEFORE '}'";
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
                        message = "UNRESOLVED CHAR '"+currentChar+"'";
                        currentState = STATE.ERROR;
                    } else {
                        currentState = STATE.DELIMITER;
                        put(LexerOutput.TABLE_NUMBERS_ID);
                        write(LexerOutput.TABLE_NUMBERS_ID, curLexId);
                        clean();
                        add();
                    }
                    break;
                case NUM_OCT:
                    read();
                    if(currentChar == null){
                        message = "PROGRAM END REACHED BEFORE '}'";
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
                        message = "UNRESOLVED CHAR '"+currentChar+"'";
                        currentState = STATE.ERROR;
                    } else {
                        currentState = STATE.DELIMITER;
                        put(LexerOutput.TABLE_NUMBERS_ID);
                        write(LexerOutput.TABLE_NUMBERS_ID, curLexId);
                        clean();
                        add();
                    }
                    break;
                case NUM_DEC:
                    read();
                    if(currentChar == null){
                        message = "PROGRAM END REACHED BEFORE '}'";
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
                        message = "UNRESOLVED CHAR '"+currentChar+"'";
                        currentState = STATE.ERROR;
                    } else {
                        currentState = STATE.DELIMITER;
                        put(LexerOutput.TABLE_NUMBERS_ID);
                        write(LexerOutput.TABLE_NUMBERS_ID, curLexId);
                        clean();
                        add();
                    }
                    break;
                case NUM_HEX:
                    read();
                    if(currentChar == null){
                        message = "PROGRAM END REACHED BEFORE '}'";
                        currentState = STATE.ERROR;
                    } else if (isHexAllow()) {
                        add();
                    } else if (currentChar == 'H' || currentChar == 'h') {
                        currentState = STATE.NUM_HEX_FIN;
                        add();
                    } else {
                        message = "UNRESOLVED CHAR '" + currentChar + "'";
                        currentState = STATE.ERROR;
                    }
                    break;
                case IDENT:
                    read();
                    if(currentChar == null){
                        message = "PROGRAM END REACHED BEFORE '}'";
                        currentState = STATE.ERROR;
                    } else if (isLetter()) {
                        add();
                    } else if (isNumber()) {
                        add();
                    } else {
                        currentState = STATE.DELIMITER;
                        check(LexerOutput.TABLE_SERVICE_ID);
                        if (curLexId == -1){
                            check(LexerOutput.TABLE_DELIMITERS_ID);
                            if (curLexId == -1){
                                put(LexerOutput.TABLE_IDENTIFIERS_ID);
                                write(LexerOutput.TABLE_IDENTIFIERS_ID, curLexId);
                            }
                            else {
                                write(LexerOutput.TABLE_DELIMITERS_ID, curLexId);
                            }
                        }
                        else {
                            write(LexerOutput.TABLE_SERVICE_ID, curLexId);
                        }
                        clean();
                        add();
                    }
                    break;
                case NUM_BIN_FIN:
                case NUM_DEC_FIN:
                    read();
                    if(currentChar == null){
                        message = "PROGRAM END REACHED BEFORE '}'";
                        currentState = STATE.ERROR;
                    } else if (currentChar == 'H' || currentChar == 'h') {
                        currentState = STATE.NUM_HEX_FIN;
                        add();
                    } else if (isHexAllow()) {
                        currentState = STATE.NUM_HEX;
                        add();
                    } else if (isLetter() || isNumber()) {
                        message = "UNRESOLVED CHAR '"+currentChar+"'";
                        currentState = STATE.ERROR;
                    } else {
                        currentState = STATE.DELIMITER;
                        put(LexerOutput.TABLE_NUMBERS_ID);
                        write(LexerOutput.TABLE_NUMBERS_ID, curLexId);
                        clean();
                        add();
                    }
                    break;
                case NUM_OCT_FIN:
                case NUM_HEX_FIN:
                    read();
                    if(currentChar == null){
                        message = "PROGRAM END REACHED BEFORE '}'";
                        currentState = STATE.ERROR;
                    } else if (isLetter() || isNumber()) {
                        message = "UNRESOLVED CHAR '"+currentChar+"'";
                        currentState = STATE.ERROR;
                    } else {
                        currentState = STATE.DELIMITER;
                        put(LexerOutput.TABLE_NUMBERS_ID);
                        write(LexerOutput.TABLE_NUMBERS_ID, curLexId);
                        clean();
                        add();
                    }
                    break;
                case NUM_REAL_POINT_1:
                    read();
                    if(currentChar == null){
                        message = "PROGRAM END REACHED BEFORE '}'";
                        currentState = STATE.ERROR;
                    } else if (isNumber()) {
                        currentState = STATE.NUM_REAL_POINT_2;
                        add();
                    } else {
                        message = "CAN NOT RESOLVE CHAR '"+currentChar+"'";
                        currentState = STATE.ERROR;
                    }
                    break;
                case NUM_REAL_POINT_2:
                    read();
                    if(currentChar == null){
                        message = "PROGRAM END REACHED BEFORE '}'";
                        currentState = STATE.ERROR;
                    } else if (isNumber()) {
                        add();
                    } else if (currentChar == 'E' || currentChar == 'e') {
                        currentState = STATE.NUM_REAL_POINT_ORDER_START_1;
                        add();
                    } else if (isLetter() || currentChar == '.') {
                        message = "CAN NOT RESOLVE CHAR '"+currentChar+"'";
                        currentState = STATE.ERROR;
                    } else {
                        currentState = STATE.DELIMITER;
                        put(LexerOutput.TABLE_NUMBERS_ID);
                        write(LexerOutput.TABLE_NUMBERS_ID, curLexId);
                        clean();
                        add();
                    }
                    break;
                case NUM_REAL_POINT_ORDER_START_1:
                    read();
                    if(currentChar == null){
                        message = "CAN NOT RESOLVE REAL'S ORDER";
                        currentState = STATE.ERROR;
                    } else if (isNumber()) {
                        currentState = STATE.NUM_REAL_POINT_ORDER;
                        add();
                    } else if (currentChar == '+' || currentChar == '-') {
                        currentState = STATE.NUM_REAL_POINT_ORDER_START_2;
                        add();
                    } else if (isLetter() || currentChar == '.') {
                        message = "CAN NOT RESOLVE CHAR '"+currentChar+"'";
                        currentState = STATE.ERROR;
                    } else {
                        currentState = STATE.DELIMITER;
                        put(LexerOutput.TABLE_NUMBERS_ID);
                        write(LexerOutput.TABLE_NUMBERS_ID, curLexId);
                        clean();
                        add();
                    }
                    break;
                case NUM_REAL_POINT_ORDER_START_2:
                    read();
                    if(currentChar == null){
                        message = "CAN NOT RESOLVE REAL'S ORDER";
                        currentState = STATE.ERROR;
                    } else if (isNumber()) {
                        currentState = STATE.NUM_REAL_POINT_ORDER;
                        add();
                    } else if (isLetter() || currentChar == '.') {
                        message = "CAN NOT RESOLVE CHAR '"+currentChar+"'";
                        currentState = STATE.ERROR;
                    } else {
                        currentState = STATE.DELIMITER;
                        put(LexerOutput.TABLE_NUMBERS_ID);
                        write(LexerOutput.TABLE_NUMBERS_ID, curLexId);
                        clean();
                        add();
                    }
                    break;
                case NUM_REAL_POINT_ORDER:
                    read();
                    if(currentChar == null){
                        message = "PROGRAM END REACHED BEFORE '}'";
                        currentState = STATE.ERROR;
                    } else if (isNumber()) {
                        add();
                    } else if (isLetter() || currentChar == '.') {
                        message = "CAN NOT RESOLVE CHAR '"+currentChar+"'";
                        currentState = STATE.ERROR;
                    } else {
                        currentState = STATE.DELIMITER;
                        put(LexerOutput.TABLE_NUMBERS_ID);
                        write(LexerOutput.TABLE_NUMBERS_ID, curLexId);
                        clean();
                        add();
                    }
                    break;
                case NUM_REAL_ORDER_OR_HEX:
                    read();
                    if(currentChar == null){
                        message = "PROGRAM END REACHED BEFORE '}'";
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
                        message = "CAN NOT RESOLVE CHAR '"+currentChar+"'";
                        currentState = STATE.ERROR;
                    } else {
                        currentState = STATE.DELIMITER;
                        put(LexerOutput.TABLE_NUMBERS_ID);
                        write(LexerOutput.TABLE_NUMBERS_ID, curLexId);
                        clean();
                        add();
                    }
                    break;
                case NUM_REAL_ORDER:
                    read();
                    if(currentChar == null){
                        message = "PROGRAM END REACHED BEFORE '}'";
                        currentState = STATE.ERROR;
                    } else if (isNumber()) {
                        add();
                    } else if (isLetter() || currentChar == '.' || currentChar == '+' || currentChar == '-') {
                        message = "CAN NOT RESOLVE CHAR '"+currentChar+"'";
                        currentState = STATE.ERROR;
                    } else {
                        currentState = STATE.DELIMITER;
                        put(LexerOutput.TABLE_NUMBERS_ID);
                        write(LexerOutput.TABLE_NUMBERS_ID, curLexId);
                        clean();
                        add();
                    }
                    break;
                case DELIMITER:
                    if(currentChar == null){
                        message = "PROGRAM END REACHED BEFORE '}'";
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
                        check(LexerOutput.TABLE_DELIMITERS_ID);
                        if (curLexId == -1){
                            currentState = STATE.ERROR;
                            message = "CAN NOT RESOLVE CHAR '"+currentChar+"'";
                        }
                        else {
                            if (currentChar == '}'){
                                currentState = STATE.END;
                            }
                            else {
                                currentState = STATE.READ;
                            }
                            write(LexerOutput.TABLE_DELIMITERS_ID, curLexId);
                            clean();
                        }
                    }
                    break;
                case MORE_THEN_EQUAL:
                case LESS_THEN_EQUAL:
                    read();
                    if(currentChar == null){
                        message = "PROGRAM END REACHED BEFORE '}'";
                        currentState = STATE.ERROR;
                    } else if(currentChar == '='){
                        add();
                        check(LexerOutput.TABLE_DELIMITERS_ID);
                        if(curLexId == -1){
                            currentState = STATE.ERROR;
                            message = "CAN NOT RESOLVE CHAR '"+currentChar+"'";
                        }
                        else {
                            currentState = STATE.READ;
                            write(LexerOutput.TABLE_DELIMITERS_ID, curLexId);
                            clean();
                        }
                    } else if(currentChar == '>'){
                        add();
                        check(LexerOutput.TABLE_DELIMITERS_ID);
                        if(curLexId == -1){
                            currentState = STATE.ERROR;
                            message = "CAN NOT RESOLVE CHAR '"+currentChar+"'";
                        }
                        else {
                            currentState = STATE.READ;
                            write(LexerOutput.TABLE_DELIMITERS_ID, curLexId);
                            clean();
                        }
                    } else if (isBinAllow()) {
                        currentState = STATE.NUM_BIN;
                        check(LexerOutput.TABLE_DELIMITERS_ID);
                        if(curLexId == -1){
                            currentState = STATE.ERROR;
                            message = "CAN NOT RESOLVE CHAR '"+currentChar+"'";
                        }
                        else {
                            write(LexerOutput.TABLE_DELIMITERS_ID, curLexId);
                        }
                        clean();
                        add();
                    } else if (isOctAllow()) {
                        currentState = STATE.NUM_OCT;
                        check(LexerOutput.TABLE_DELIMITERS_ID);
                        if(curLexId == -1){
                            currentState = STATE.ERROR;
                            message = "CAN NOT RESOLVE CHAR '"+currentChar+"'";
                        }
                        else {
                            write(LexerOutput.TABLE_DELIMITERS_ID, curLexId);
                        }
                        clean();
                        add();
                    } else if (isDecAllow()) {
                        currentState = STATE.NUM_DEC;
                        check(LexerOutput.TABLE_DELIMITERS_ID);
                        if(curLexId == -1){
                            currentState = STATE.ERROR;
                            message = "CAN NOT RESOLVE CHAR '"+currentChar+"'";
                        }
                        else {
                            write(LexerOutput.TABLE_DELIMITERS_ID, curLexId);
                        }
                        clean();
                        add();
                    } else if (isLetter()) {
                        currentState = STATE.IDENT;
                        check(LexerOutput.TABLE_DELIMITERS_ID);
                        if(curLexId == -1){
                            currentState = STATE.ERROR;
                            message = "CAN NOT RESOLVE CHAR '"+currentChar+"'";
                        }
                        else {
                            write(LexerOutput.TABLE_DELIMITERS_ID, curLexId);
                        }
                        clean();
                        add();
                    } else if (currentChar == '.') {
                        currentState = STATE.NUM_REAL_POINT_1;
                        if(curLexId == -1){
                            currentState = STATE.ERROR;
                            message = "CAN NOT RESOLVE CHAR '"+currentChar+"'";
                        }
                        else {
                            write(LexerOutput.TABLE_DELIMITERS_ID, curLexId);
                        }
                        clean();
                        add();
                    } else {
                        check(LexerOutput.TABLE_DELIMITERS_ID);
                        if(curLexId == -1){
                            currentState = STATE.ERROR;
                            message = "CAN NOT RESOLVE CHAR '"+currentChar+"'";
                        }
                        else {
                            currentState = STATE.DELIMITER;
                            write(LexerOutput.TABLE_DELIMITERS_ID, curLexId);
                            clean();
                            add();
                        }
                    }
                    break;
                case COMMENT_START:
                    read();
                    if(currentChar == null){
                        message = "PROGRAM END REACHED BEFORE '}'";
                        currentState = STATE.ERROR;
                    } else if(currentChar == ' '){
                        currentState = STATE.READ;

                        check(LexerOutput.TABLE_DELIMITERS_ID);
                        if(curLexId == -1){
                            currentState = STATE.ERROR;
                            message = "CAN NOT RESOLVE CHAR '"+currentChar+"'";
                        }
                        else {
                            write(LexerOutput.TABLE_DELIMITERS_ID, curLexId);
                        }
                        clean();
                    } else if(currentChar == '*'){
                        currentState = STATE.COMMENT;
                        clean();
                    } else if (isBinAllow()) {
                        currentState = STATE.NUM_BIN;
                        check(LexerOutput.TABLE_DELIMITERS_ID);
                        if(curLexId == -1){
                            currentState = STATE.ERROR;
                            message = "CAN NOT RESOLVE CHAR '"+currentChar+"'";
                        }
                        else {
                            write(LexerOutput.TABLE_DELIMITERS_ID, curLexId);
                        }
                        clean();
                        add();
                    } else if (isOctAllow()) {
                        currentState = STATE.NUM_OCT;
                        check(LexerOutput.TABLE_DELIMITERS_ID);
                        if(curLexId == -1){
                            currentState = STATE.ERROR;
                            message = "CAN NOT RESOLVE CHAR '"+currentChar+"'";
                        }
                        else {
                            write(LexerOutput.TABLE_DELIMITERS_ID, curLexId);
                        }
                        clean();
                        add();
                    } else if (isDecAllow()) {
                        currentState = STATE.NUM_DEC;
                        check(LexerOutput.TABLE_DELIMITERS_ID);
                        if(curLexId == -1){
                            currentState = STATE.ERROR;
                            message = "CAN NOT RESOLVE CHAR '"+currentChar+"'";
                        }
                        else {
                            write(LexerOutput.TABLE_DELIMITERS_ID, curLexId);
                        }
                        clean();
                        add();
                    } else if (isLetter()) {
                        currentState = STATE.IDENT;
                        check(LexerOutput.TABLE_DELIMITERS_ID);
                        if(curLexId == -1){
                            currentState = STATE.ERROR;
                            message = "CAN NOT RESOLVE CHAR '"+currentChar+"'";
                        }
                        else {
                            write(LexerOutput.TABLE_DELIMITERS_ID, curLexId);
                        }
                        clean();
                        add();
                    } else if (currentChar == '.') {
                        currentState = STATE.NUM_REAL_POINT_1;
                        if(curLexId == -1){
                            currentState = STATE.ERROR;
                            message = "CAN NOT RESOLVE CHAR '"+currentChar+"'";
                        }
                        else {
                            write(LexerOutput.TABLE_DELIMITERS_ID, curLexId);
                        }
                        clean();
                        add();
                    } else {
                        check(LexerOutput.TABLE_DELIMITERS_ID);
                        if(curLexId == -1){
                            currentState = STATE.ERROR;
                            message = "CAN NOT RESOLVE CHAR '"+currentChar+"'";
                        }
                        else {
                            currentState = STATE.DELIMITER;
                            write(LexerOutput.TABLE_DELIMITERS_ID, curLexId);
                            clean();
                            add();
                        }
                    }
                    break;
                case COMMENT:
                    read();
                    add();
                    if(currentChar == null){
                        message = "PROGRAM END REACHED BEFORE '}'";
                        currentState = STATE.ERROR;
                    } else if(currentChar == '*') {
                        currentState = STATE.COMMENT_END;
                    }
                    break;
                case COMMENT_END:
                    read();
                    if(currentChar == null){
                        message = "PROGRAM END REACHED BEFORE '}'";
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
                    throw new Exception("can not resolve state '" + currentState.name() + "'");

            }

        }

        return new LexerOutput(lexTables, lexemesList, currentState == STATE.ERROR, message);
    }





}
