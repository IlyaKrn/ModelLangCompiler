package com.ilyakrn.parser;

import com.ilyakrn.lexer.Lex;
import com.ilyakrn.lexer.LexAndTable;
import com.ilyakrn.lexer.LexerOutput;

import java.util.LinkedList;
import java.util.Queue;

public class Parser {

    public boolean analyze(LexerOutput lexerOutput) {
        Queue<Lex> input = new LinkedList<>();
        for (LexAndTable lexAndTable : lexerOutput.getLexemesList()){
            input.add(lexerOutput.getLexTables().get(lexAndTable.getTableId()).get(lexAndTable.getLexId()));
        }

        System.out.println(lexerOutput);
        for (int i = 0; i < 56; i++) {
            int res = EXPR(input);
            System.out.print(res + "\t");
            for (int j = 0; j < res + 1; j++) {
                System.out.print(input.poll().getLexeme() + " ");
            }
        }

        return true;//result == input.size();
    }

    private int nextLexemeIs(Queue<Lex> input, String lexeme){
        Queue<Lex> tempInput = new LinkedList<>(input);
        if (tempInput.isEmpty())
            return -1;
        else
            return tempInput.poll().getLexeme().equals(lexeme) ? 1 : -1;
    }
    private int isIdentifier(Queue<Lex> input){
        Queue<Lex> tempInput = new LinkedList<>(input);
        if (tempInput.isEmpty())
            return -1;
        else
            return tempInput.poll().getTableId() == LexerOutput.TABLE_IDENTIFIERS_ID ? 1 : -1;
    }
    private int isNumber(Queue<Lex> input) {
        Queue<Lex> tempInput = new LinkedList<>(input);
        if (tempInput.isEmpty())
            return -1;
        else
            return tempInput.poll().getTableId() == LexerOutput.TABLE_NUMBERS_ID ? 1 : -1;
    }

    private int OGO(Queue<Lex> input){
        Queue<Lex> tempInput = new LinkedList<>(input);
        if (tempInput.isEmpty())
            return -1;
        else {
            String lexeme = tempInput.poll().getLexeme();
            if(lexeme.equals("<>") ||
                    lexeme.equals("=") ||
                    lexeme.equals("<") ||
                    lexeme.equals("<=") ||
                    lexeme.equals(">") ||
                    lexeme.equals(">=")
            )
                return 1;
            else
                return -1;
        }
    }
    private int OGS(Queue<Lex> input){
        Queue<Lex> tempInput = new LinkedList<>(input);
        if (tempInput.isEmpty())
            return -1;
        else {
            String lexeme = tempInput.poll().getLexeme();
            if(lexeme.equals("+") ||
                    lexeme.equals("-") ||
                    lexeme.equals("or")
            )
                return 1;
            else
                return -1;
        }
    }
    private int OGU(Queue<Lex> input){
        Queue<Lex> tempInput = new LinkedList<>(input);
        if (tempInput.isEmpty())
            return -1;
        else {
            String lexeme = tempInput.poll().getLexeme();
            if(lexeme.equals("*") ||
                    lexeme.equals("/") ||
                    lexeme.equals("and")
            )
                return 1;
            else
                return -1;
        }
    }
    private int LC(Queue<Lex> input){
        Queue<Lex> tempInput = new LinkedList<>(input);
        if (tempInput.isEmpty())
            return -1;
        else {
            String lexeme = tempInput.poll().getLexeme();
            if(lexeme.equals("true") ||
                    lexeme.equals("false")
            )
                return 1;
            else
                return -1;
        }
    }
    private int UO(Queue<Lex> input){
        Queue<Lex> tempInput = new LinkedList<>(input);
        if (tempInput.isEmpty())
            return -1;
        else {
            String lexeme = tempInput.poll().getLexeme();
            if(lexeme.equals("not"))
                return 1;
            else
                return -1;
        }
    }
    private int T(Queue<Lex> input){
        Queue<Lex> tempInput = new LinkedList<>(input);
        if (tempInput.isEmpty())
            return -1;
        else {
            String lexeme = tempInput.poll().getLexeme();
            if(lexeme.equals("int") ||
                    lexeme.equals("float") ||
                    lexeme.equals("bool")
            )
                return 1;
            else
                return -1;
        }
    }

    private int EXPR(Queue<Lex> input){
        Queue<Lex> tempInput = new LinkedList<>(input);
        int result = OPRND(tempInput);
        if (result != -1){
            for (int i = 0; i < result; i++) {
                tempInput.poll();
            }
            int result1 = OGO(tempInput);
            while (result1 != -1) {
                for (int i = 0; i < result1; i++) {
                    tempInput.poll();
                }
                result += result1;
                int result2 = OPRND(tempInput);
                if (result2 != -1) {
                    for (int i = 0; i < result2; i++) {
                        tempInput.poll();
                    }
                    result1 = OGO(tempInput);
                    result += result2;
                }
                else {
                    result = -1;
                    break;
                }
            }
        }
        return result;
    }
    private int SLAG(Queue<Lex> input){
        Queue<Lex> tempInput = new LinkedList<>(input);
        int result = MNOZH(tempInput);
        if (result != -1){
            for (int i = 0; i < result; i++) {
                tempInput.poll();
            }
            int result1 = OGU(tempInput);
            while (result1 != -1) {
                for (int i = 0; i < result1; i++) {
                    tempInput.poll();
                }
                result += result1;
                int result2 = MNOZH(tempInput);
                if (result2 != -1) {
                    for (int i = 0; i < result2; i++) {
                        tempInput.poll();
                    }
                    result1 = OGU(tempInput);
                    result += result2;
                }
                else {
                    result = -1;
                    break;
                }
            }
        }
        return result;
    }
    private int OPRND(Queue<Lex> input){
        Queue<Lex> tempInput = new LinkedList<>(input);
        int result = SLAG(tempInput);
        if (result != -1){
            for (int i = 0; i < result; i++) {
                tempInput.poll();
            }
            int result1 = OGS(tempInput);
            while (result1 != -1) {
                for (int i = 0; i < result1; i++) {
                    tempInput.poll();
                }
                result += result1;
                int result2 = SLAG(tempInput);
                if (result2 != -1) {
                    for (int i = 0; i < result2; i++) {
                        tempInput.poll();
                    }
                    result1 = OGS(tempInput);
                    result += result2;
                }
                else {
                    result = -1;
                    break;
                }
            }
        }
        return result;
    }
    private int MNOZH(Queue<Lex> input){
        Queue<Lex> tempInput = new LinkedList<>(input);
        int result = -1;

        if (result == -1)
            result = isIdentifier(tempInput);
        if (result == -1)
            result = isNumber(tempInput);
        if (result == -1)
            result = LC(tempInput);
        if (result == -1) {
            result = UO(tempInput);
            if (result != -1) {
                for (int i = 0; i < result; i++) {
                    tempInput.poll();
                }
                int result1 = MNOZH(tempInput);
                result = result1 == -1 ? -1 : result + result1;
            }
        }
        if (result == -1)
            result = EXPR(tempInput);

        return result;
    }

/*
    /// ////////////////////////
    private int ENTER(Queue<Lex> input){
        if (input.isEmpty())
            return -1;
        else {
            String lexeme = input.poll().getLexeme();
            if(lexeme.equals("not"))
                return 1;
            else
                return -1;

            if(currentLexemeIs("read"))
                read();
            else
                error();

            if(currentLexemeIs("("))
                read();
            else
                error();

            if(isIdentifier())
                read();
            else
                error();

            while (currentLexemeIs(",")){
                read();
                if(isIdentifier())
                    read();
                else
                    error();
            }

            if(currentLexemeIs(")"))
                read();
            else
                error();
        }
    }
    private int OUT(Queue<Lex> input){
        if(currentLexemeIs("write"))
            read();
        else
            error();

        if(currentLexemeIs("("))
            read();
        else
            error();

        EXPR();

        while (currentLexemeIs(",")){
            read();
            EXPR();
        }

        if(currentLexemeIs(")"))
            read();
        else
            error();
    }
    private int SOSTAV(Queue<Lex> input){}
    private int PRISV(Queue<Lex> input){
        if(isIdentifier())
            read();
        else
            error();

        if(currentLexemeIs("ass"))
            read();
        else
            error();

        EXPR();
    }
    private int USLOV(Queue<Lex> input){}
    private int FIXLOOP(Queue<Lex> input){}
    private int USLLOOP(Queue<Lex> input){}

    private int OPERATOR(Queue<Lex> input){}
    private int DESC(Queue<Lex> input){
        T();

        if(isIdentifier())
            read();
        else
            error();

        while (currentLexemeIs(",")){
            read();
            if(isIdentifier())
                read();
            else
                error();
        }
    }

    private int PROG(Queue<Lex> input){
        read();

        if(currentLexemeIs("{"))
            read();
        else
            error();

        do {
            //(<описание> | <оператор>)
        } while (currentLexemeIs(";"));

        if(!currentLexemeIs("}"))
            error();
    }
/// ///////////////////////*/



/**   OGO                               операции_группы_отношения                      **/
/**   OGS                               операции_группы_сложения                       **/
/**   OGU                               операции_группы_умножения                      **/
/**   LC                                логическая_константа                           **/
/**   UO                                унарная_операция                               **/
/**   T                                 тип                                            **/

/**   MNOZH                             множитель                                           **/
/**   EXPR                              выражение                                           **/
/**   SLAG                              слагаемое                                           **/
/**   OPRND                             операнд                                             **/

/*    ENTER                             ввода                                               **/
/*    OUT                               вывода                                              **/
/*    SOSTAV                            составной                                           **/
/*    PRISV                             присваивания                                        **/
/*    USLOV                             условный                                            **/
/*    FIXLOOP                           фиксированного_цикла                                **/
/*    USLLOOP                           условного_цикла                                     **/

/*    OPERATOR                          оператор                                            **/
/*    DESC                              описание                                            **/

/*    PROG                              программа                                           **/


//          <операнд>::= <слагаемое> {<операции_группы_сложения> <слагаемое>}
//          <выражение>::= <операнд>{<операции_группы_отношения> <операнд>}
//          <множитель>::= (<идентификатор> | <число> | <логическая_константа> | <унарная_операция> <множитель> | <выражение>)
//          <слагаемое>::= <множитель> {<операции_группы_умножения> <множитель>}

//          <ввода>::= read (<идентификатор> {, <идентификатор> })
//          <вывода>::= write (<выражение> {, <выражение> })
//          <составной>::= <оператор> { ( : | перевод строки) <оператор> }
//          <присваивания>::= <идентификатор> ass <выражение>
//          <условный>::= if <выражение> then <оператор> [ else <оператор>]
//          <фиксированного_цикла>::= for <присваивания> to <выражение> do <оператор>
//          <условного_цикла>::= while <выражение> do <оператор>

//          <оператор>::= (<составной> | <присваивания> | <условный> | <фиксированного_цикла> | <условного_цикла> | <ввода> | <вывода>)
//          <описание>::= <тип> <идентификатор> { , <идентификатор> }

//          <программа>::= { {/ (<описание> | <оператор>) ; /} }

}
