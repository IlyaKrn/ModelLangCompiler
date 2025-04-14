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
        if(currentLexemeIs("<>") ||
                currentLexemeIs("=") ||
                currentLexemeIs("<") ||
                currentLexemeIs("<=") ||
                currentLexemeIs(">") ||
                currentLexemeIs(">=")
        )
            read();
        else
            error();
    }
    private void OGS(){
        if(currentLexemeIs("+") ||
                currentLexemeIs("-") ||
                currentLexemeIs("or")
        )
            read();
        else
            error();
    }
    private void OGU(){
        if(currentLexemeIs("*") ||
                currentLexemeIs("/") ||
                currentLexemeIs("and")
        )
            read();
        else
            error();
    }
    private void LC(){
        if(currentLexemeIs("true") ||
                currentLexemeIs("false")
        )
            read();
        else
            error();
    }
    private void UO(){
        if(currentLexemeIs("not"))
            read();
        else
            error();
    }
    private void T(){
        if(currentLexemeIs("int") ||
                currentLexemeIs("float") ||
                currentLexemeIs("bool")
        )
            read();
        else
            error();
    }

    private void MNOZH(){}
    private void EXPR(){}
    private void SLAG(){}
    private void OPRND(){}

    private void ENTER(){
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
    private void OUT(){
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
    private void SOSTAV(){}
    private void PRISV(){
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
    private void USLOV(){}
    private void FIXLOOP(){}
    private void USLLOOP(){}

    private void OPERATOR(){}
    private void DESC(){
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

    private void PROG(){
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



/**   OGO                               операции_группы_отношения                      **/
/**   OGS                               операции_группы_сложения                       **/
/**   OGU                               операции_группы_умножения                      **/
/**   LC                                логическая_константа                           **/
/**   UO                                унарная_операция                               **/
/**   T                                 тип                                            **/

/*    MNOZH                             множитель                                           **/
/*    EXPR                              выражение                                           **/
/*    SLAG                              слагаемое                                           **/
/*    OPRND                             операнд                                             **/

/**   ENTER                             ввода                                               **/
/**   OUT                               вывода                                              **/
/*    SOSTAV                            составной                                           **/
/*    PRISV                             присваивания                                        **/
/*    USLOV                             условный                                            **/
/*    FIXLOOP                           фиксированного_цикла                                **/
/*    USLLOOP                           условного_цикла                                     **/

/*    OPERATOR                          оператор                                            **/
/**   DESC                              описание                                            **/

/*    PROG                              программа                                           **/


//          <множитель>::= (<идентификатор> | <число> | <логическая_константа> | <унарная_операция> <множитель> | <выражение>)
//          <выражение>::= <операнд>{<операции_группы_отношения> <операнд>}
//          <слагаемое>::= <множитель> {<операции_группы_умножения> <множитель>}
//          <операнд>::= <слагаемое> {<операции_группы_сложения> <слагаемое>}
//          <составной>::= <оператор> { ( : | перевод строки) <оператор> }
//          <условный>::= if <выражение> then <оператор> [ else <оператор>]
//          <фиксированного_цикла>::= for <присваивания> to <выражение> do <оператор>
//          <условного_цикла>::= while <выражение> do <оператор>
//          <оператор>::= (<составной> | <присваивания> | <условный> | <фиксированного_цикла> | <условного_цикла> | <ввода> | <вывода>)
//          <программа>::= { {/ (<описание> | <оператор>) ; /} }



}
