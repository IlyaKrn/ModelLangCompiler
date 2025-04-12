package com.ilyakrn.parser;

import com.ilyakrn.lexer.Lex;
import com.ilyakrn.lexer.LexAndTable;
import com.ilyakrn.lexer.LexerOutput;

import java.util.LinkedList;
import java.util.Queue;

public class Parser {

    private Lex currentLex;
    private final Queue<Lex> input;

    public Parser() {
        this.input = new LinkedList<>();
    }

    private void read(){
        currentLex = input.poll();
    }

    private boolean currentLexemeIs(String lexeme){
        return currentLex.getLexeme().equals(lexeme);
    }

    private boolean isIdentifier(){
        return currentLex.getTableId() == LexerOutput.TABLE_IDENTIFIERS_ID;
    }

    private boolean isNumber(){
        return currentLex.getTableId() == LexerOutput.TABLE_NUMBERS_ID;
    }

    public boolean analyze(LexerOutput lexerOutput) {
        currentLex = null;
        input.clear();
        for (LexAndTable lexAndTable : lexerOutput.getLexemesList()){
            input.add(lexerOutput.getLexTables().get(lexAndTable.getTableId()).get(lexAndTable.getLexId()));
        }

        return false;
    }



}
