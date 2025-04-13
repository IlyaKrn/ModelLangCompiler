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

    private void error(){
        System.out.println("PARSER DETECTED ERROR");
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

    private void OGO(){
        if(!currentLexemeIs("<>") &
                !currentLexemeIs("=") &
                !currentLexemeIs("<") &
                !currentLexemeIs("<=") &
                !currentLexemeIs(">") &
                !currentLexemeIs(">=")
        ){
            error();
        }
    }
    private void OGS(){
        if(!currentLexemeIs("+") &&
                !currentLexemeIs("-") &&
                !currentLexemeIs("or")
        ){
            error();
        }
    }
    private void OGU(){
        if(!currentLexemeIs("*") &&
                !currentLexemeIs("/") &&
                !currentLexemeIs("and")
        ){
            error();
        }
    }
    private void LC(){
        if(!currentLexemeIs("true") &&
                !currentLexemeIs("false")
        ){
            error();
        }
    }
    private void UO(){
        if(!currentLexemeIs("not")
        ) {
            error();
        }
    }
    private void T(){
        if(!currentLexemeIs("int") &&
                !currentLexemeIs("float") &&
                !currentLexemeIs("bool")
        ) {
            error();
        }
    }
    private void ENTER(){

    }
    private void DESC(){

    }
    private void MNOZH(){

    }
    private void SLAG(){

    }
    private void OPRND(){

    }
    private void EXPR(){

    }
    private void SOSTAV(){

    }
    private void PRISV(){

    }
    private void USLOV(){

    }
    private void FIXLOOP(){

    }
    private void USLLOOP(){

    }
    private void OUT(){

    }
    private void OPERATOR(){

    }
    private void PROG(){
        read();
        if(currentLexemeIs("{"))
            read();
        else
            error();


        if(!currentLexemeIs("}"))
            error();
    }



// OGO                               операции_группы_отношения
// OGS                               операции_группы_сложения
// OGU                               операции_группы_умножения
// LC                                логическая_константа
// UO                                унарная_операция
// T                                 тип
// ENTER                             ввода
// DESC                              описание
// MNOZH                             множитель
// SLAG                              слагаемое
// OPRND                             операнд
// EXPR                              выражение
// SOSTAV                            составной
// PRISV                             присваивания
// USLOV                             условный
// FIXLOOP                           фиксированного_цикла
// USLLOOP                           условного_цикла
// OUT                               вывода
// OPERATOR                          оператор
// PROG                              программа


//          <ввода>::= read (<идентификатор> {, <идентификатор> })
//          <описание>::= <тип> <идентификатор> { , <идентификатор> }
//          <множитель>::= (<идентификатор> | <число> | <логическая_константа> | <унарная_операция> <множитель> | <выражение>)
//          <слагаемое>::= <множитель> {<операции_группы_умножения> <множитель>}
//          <операнд>::= <слагаемое> {<операции_группы_сложения> <слагаемое>}
//          <выражение>::= <операнд>{<операции_группы_отношения> <операнд>}
//          <составной>::= <оператор> { ( : | перевод строки) <оператор> }
//          <присваивания>::= <идентификатор> ass <выражение>
//          <условный>::= if <выражение> then <оператор> [ else <оператор>]
//          <фиксированного_цикла>::= for <присваивания> to <выражение> do <оператор>
//          <условного_цикла>::= while <выражение> do <оператор>
//          <вывода>::= write (<выражение> {, <выражение> })
//          <оператор>::= (<составной> | <присваивания> | <условный> | <фиксированного_цикла> | <условного_цикла> | <ввода> | <вывода>)
//          <программа>::= { {/ (<описание> | <оператор>) ; /} }



}
