package com.ilyakrn.lexer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Lexer {

    public final int TABLE_SERVICE_ID = 0;
    public final int TABLE_DELIMITERS_ID = 1;
    public final int TABLE_IDENTIFIERS_ID = 2;
    public final int TABLE_NUMBERS_ID = 3;

    private enum STATE {
        READ,
        IDENT,
        NUM_BIN, NUM_BIN_FIN,
        NUM_OCT, NUM_OCT_FIN,
        NUM_DEC, NUM_DEC_FIN,
        NUM_HEX, NUM_HEX_FIN,
        COMMENT_START,
        COMMENT_END,
        MORE_THEN_EQUAL,
        LESS_THEN_EQUAL,
        COMMENT,
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

        lexTables.get(TABLE_SERVICE_ID).add(new Lex(0, TABLE_SERVICE_ID, "true"));
        lexTables.get(TABLE_SERVICE_ID).add(new Lex(1, TABLE_SERVICE_ID, "false"));
        lexTables.get(TABLE_SERVICE_ID).add(new Lex(2, TABLE_SERVICE_ID, "int"));
        lexTables.get(TABLE_SERVICE_ID).add(new Lex(3, TABLE_SERVICE_ID, "float"));
        lexTables.get(TABLE_SERVICE_ID).add(new Lex(4, TABLE_SERVICE_ID, "bool"));
        lexTables.get(TABLE_SERVICE_ID).add(new Lex(5, TABLE_SERVICE_ID, "if"));
        lexTables.get(TABLE_SERVICE_ID).add(new Lex(6, TABLE_SERVICE_ID, "then"));
        lexTables.get(TABLE_SERVICE_ID).add(new Lex(7, TABLE_SERVICE_ID, "else"));
        lexTables.get(TABLE_SERVICE_ID).add(new Lex(8, TABLE_SERVICE_ID, "for"));
        lexTables.get(TABLE_SERVICE_ID).add(new Lex(9, TABLE_SERVICE_ID, "while"));
        lexTables.get(TABLE_SERVICE_ID).add(new Lex(10, TABLE_SERVICE_ID, "to"));
        lexTables.get(TABLE_SERVICE_ID).add(new Lex(11, TABLE_SERVICE_ID, "do"));
        lexTables.get(TABLE_SERVICE_ID).add(new Lex(12, TABLE_SERVICE_ID, "read"));
        lexTables.get(TABLE_SERVICE_ID).add(new Lex(13, TABLE_SERVICE_ID, "write"));

        lexTables.get(TABLE_DELIMITERS_ID).add(new Lex(0, TABLE_DELIMITERS_ID, "\n"));
        lexTables.get(TABLE_DELIMITERS_ID).add(new Lex(1, TABLE_DELIMITERS_ID, ";"));
        lexTables.get(TABLE_DELIMITERS_ID).add(new Lex(2, TABLE_DELIMITERS_ID, ":"));
        lexTables.get(TABLE_DELIMITERS_ID).add(new Lex(3, TABLE_DELIMITERS_ID, ","));
        lexTables.get(TABLE_DELIMITERS_ID).add(new Lex(4, TABLE_DELIMITERS_ID, "."));
        lexTables.get(TABLE_DELIMITERS_ID).add(new Lex(5, TABLE_DELIMITERS_ID, ">"));
        lexTables.get(TABLE_DELIMITERS_ID).add(new Lex(6, TABLE_DELIMITERS_ID, "<"));
        lexTables.get(TABLE_DELIMITERS_ID).add(new Lex(7, TABLE_DELIMITERS_ID, ">="));
        lexTables.get(TABLE_DELIMITERS_ID).add(new Lex(8, TABLE_DELIMITERS_ID, "<="));
        lexTables.get(TABLE_DELIMITERS_ID).add(new Lex(9, TABLE_DELIMITERS_ID, "="));
        lexTables.get(TABLE_DELIMITERS_ID).add(new Lex(10, TABLE_DELIMITERS_ID, "+"));
        lexTables.get(TABLE_DELIMITERS_ID).add(new Lex(11, TABLE_DELIMITERS_ID, "-"));
        lexTables.get(TABLE_DELIMITERS_ID).add(new Lex(12, TABLE_DELIMITERS_ID, "*"));
        lexTables.get(TABLE_DELIMITERS_ID).add(new Lex(13, TABLE_DELIMITERS_ID, "/"));
        lexTables.get(TABLE_DELIMITERS_ID).add(new Lex(14, TABLE_DELIMITERS_ID, "or"));
        lexTables.get(TABLE_DELIMITERS_ID).add(new Lex(15, TABLE_DELIMITERS_ID, "and"));
        lexTables.get(TABLE_DELIMITERS_ID).add(new Lex(16, TABLE_DELIMITERS_ID, "not"));
        lexTables.get(TABLE_DELIMITERS_ID).add(new Lex(17, TABLE_DELIMITERS_ID, "ass"));
        lexTables.get(TABLE_DELIMITERS_ID).add(new Lex(18, TABLE_DELIMITERS_ID, "("));
        lexTables.get(TABLE_DELIMITERS_ID).add(new Lex(19, TABLE_DELIMITERS_ID, ")"));
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
        if(tableId == TABLE_SERVICE_ID || tableId == TABLE_DELIMITERS_ID){
            throw new Exception("table with id '" + tableId + "' read only");
        }
        lexTables.get(tableId).add(new Lex(lexTables.get(tableId).size(), tableId, lexBuffer));
        curLexId = lexTables.get(tableId).size() - 1;
        curTableId = tableId;
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
        lexTables.get(TABLE_IDENTIFIERS_ID).clear();
        lexTables.get(TABLE_NUMBERS_ID).clear();
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
                        message = "LEXER COMPLETE SUCCESSFUL";
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
                    } else {
                        currentState = STATE.READ;
                        clean();
////////////
//                        add();
//                        switch (currentChar){
//                            case '/':
//                                currentState = STATE.COMMENT_START;
//                                break;
//                            case '>':
//                                currentState = STATE.MORE_THEN_EQUAL;
//                                break;
//                            case '<':
//                                currentState = STATE.LESS_THEN_EQUAL;
//                                break;
//                            default:
//                                currentState = STATE.READ;
//                                check(TABLE_DELIMITERS_ID);
//                                if(curLexId != -1){
//                                    write(TABLE_DELIMITERS_ID, curLexId);
//                                }
//                                else{
//                                    currentState = STATE.ERROR;
//                                }
//                                break;
//                        }
//                        clean();
////////////
                    }
                    break;
                case NUM_BIN:
                    read();
                    if(currentChar == null){
                        message = "LEXER COMPLETE SUCCESSFUL";
                        currentState = STATE.ERROR;
                    } else if (isBinAllow()) {
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
                        currentState = STATE.READ;
                        put(TABLE_NUMBERS_ID);
                        write(TABLE_NUMBERS_ID, curLexId);
                        clean();
                    }
                    break;
                case NUM_OCT:
                    read();
                    if(currentChar == null){
                        message = "LEXER COMPLETE SUCCESSFUL";
                        currentState = STATE.ERROR;
                    } else if (isOctAllow()) {
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
                        currentState = STATE.READ;
                        put(TABLE_NUMBERS_ID);
                        write(TABLE_NUMBERS_ID, curLexId);
                        clean();
                    }
                    break;
                case NUM_DEC:
                    read();
                    if(currentChar == null){
                        message = "LEXER COMPLETE SUCCESSFUL";
                        currentState = STATE.ERROR;
                    } else if (isDecAllow()) {
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
                        currentState = STATE.READ;
                        put(TABLE_NUMBERS_ID);
                        write(TABLE_NUMBERS_ID, curLexId);
                        clean();
                    }
                    break;
                case NUM_HEX:
                    read();
                    if(currentChar == null){
                        message = "LEXER COMPLETE SUCCESSFUL";
                        currentState = STATE.ERROR;
                    } else if (isHexAllow()) {
                        add();
                    } else if (currentChar == 'H' || currentChar == 'h') {
                        currentState = STATE.NUM_HEX_FIN;
                        add();
                    } else if (isLetter()) {
                        message = "UNRESOLVED CHAR '"+currentChar+"'";
                        currentState = STATE.ERROR;
                    } else {
                        currentState = STATE.READ;
                        put(TABLE_NUMBERS_ID);
                        write(TABLE_NUMBERS_ID, curLexId);
                        clean();
                    }
                    break;
                case IDENT:
                    read();
                    if(currentChar == null){
                        message = "LEXER COMPLETE SUCCESSFUL";
                        currentState = STATE.ERROR;
                    } else if (isLetter()) {
                        add();
                    } else if (isNumber()) {
                        add();
                    } else {
                        currentState = STATE.READ;
                        check(TABLE_SERVICE_ID);
                        if (curLexId == -1){
                            put(TABLE_IDENTIFIERS_ID);
                            write(TABLE_IDENTIFIERS_ID, curLexId);
                        }
                        else {
                            write(TABLE_SERVICE_ID, curLexId);
                        }
                        clean();
                    }
                    break;
                case NUM_BIN_FIN:
                case NUM_DEC_FIN:
                    read();
                    if(currentChar == null){
                        message = "LEXER COMPLETE SUCCESSFUL";
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
                        currentState = STATE.READ;
                        put(TABLE_NUMBERS_ID);
                        write(TABLE_NUMBERS_ID, curLexId);
                        clean();
                    }
                    break;
                case NUM_OCT_FIN:
                case NUM_HEX_FIN:
                    read();
                    if(currentChar == null){
                        message = "LEXER COMPLETE SUCCESSFUL";
                        currentState = STATE.ERROR;
                    } else if (isLetter() || isNumber()) {
                        message = "UNRESOLVED CHAR '"+currentChar+"'";
                        currentState = STATE.ERROR;
                    } else {
                        currentState = STATE.READ;
                        put(TABLE_NUMBERS_ID);
                        write(TABLE_NUMBERS_ID, curLexId);
                        clean();
                    }
                    break;
////////////
//                case COMMENT_START:
//                    read();
//                    if(currentChar == null){
//                        message = "LEXER COMPLETE SUCCESSFUL";
//                        currentState = STATE.ERROR;
//                    } else if (currentChar == '*') {
//                        currentState = STATE.COMMENT;
//                    }
//                    else {
//                        currentState = STATE.READ;
//                        check(TABLE_DELIMITERS_ID);
//                        if(curLexId != -1) {
//                            write(TABLE_DELIMITERS_ID, curLexId);
//                        }
//                        else {
//                            currentState = STATE.ERROR;
//                        }
//                    }
//                    clean();
//                    break;
//                case COMMENT:
//                    read();
//                    if(currentChar == null){
//                        message = "LEXER COMPLETE SUCCESSFUL";
//                        currentState = STATE.ERROR;
//                    } else if (currentChar == '*') {
//                        currentState = STATE.COMMENT_END;
//                    }
//                    else {
//                    }
//                    clean();
//                    break;
//                case COMMENT_END:
//                    read();
//                    if(currentChar == null){
//                        message = "LEXER COMPLETE SUCCESSFUL";
//                        currentState = STATE.ERROR;
//                    } else if (currentChar == '/') {
//                        currentState = STATE.READ;
//                    }
//                    else {
//                    }
//                    clean();
//                    break;
//                case MORE_THEN_EQUAL:
//                    read();
//                    if(currentChar == null){
//                        message = "LEXER COMPLETE SUCCESSFUL";
//                        currentState = STATE.ERROR;
//                    } else if (currentChar == '=') {
//                        write(TABLE_DELIMITERS_ID, 7);
//                    }
//                    else {
//                        currentState = STATE.READ;
//                        check(TABLE_DELIMITERS_ID);
//                        if(curLexId != -1) {
//                            write(TABLE_DELIMITERS_ID, curLexId);
//                        }
//                        else {
//                            currentState = STATE.ERROR;
//                        }
//                    }
//                    clean();
//                    break;
//                case LESS_THEN_EQUAL:
//                    read();
//                    if(currentChar == null){
//                        message = "LEXER COMPLETE SUCCESSFUL";
//                        currentState = STATE.ERROR;
//                    } else if (currentChar == '=') {
//                        write(TABLE_DELIMITERS_ID, 8);
//                    }
//                    else {
//                        currentState = STATE.READ;
//                        check(TABLE_DELIMITERS_ID);
//                        if(curLexId != -1) {
//                            write(TABLE_DELIMITERS_ID, curLexId);
//                        }
//                        else {
//                            currentState = STATE.ERROR;
//                        }
//                    }
//                    clean();
//                    break;
////////////
                default:
                    throw new Exception("can not resolve state '" + currentState.name() + "'");

            }

        }

        return new LexerOutput(lexTables, lexemesList, currentState == STATE.ERROR, message);
    }





}
