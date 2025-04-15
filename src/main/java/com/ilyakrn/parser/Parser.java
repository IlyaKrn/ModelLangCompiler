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
        int result = PROG(input);
        System.out.print(result + "\t");

        return result == input.size();
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
//        if (result == -1)
//            result = MNOZH(tempInput);

        return result;
    }

    private int ENTER(Queue<Lex> input){
        Queue<Lex> tempInput = new LinkedList<>(input);

        int result = nextLexemeIs(tempInput,"read");
        if (result == -1)
            return -1;
        for (int i = 0; i < result; i++) {
            tempInput.poll();
        }

        int result1 = nextLexemeIs(tempInput,"(");
        if (result1 == -1)
            return -1;
        for (int i = 0; i < result1; i++) {
            tempInput.poll();
        }
        result += result1;

        int result2 = isIdentifier(tempInput);
        if (result2 == -1)
            return -1;
        for (int i = 0; i < result2; i++) {
            tempInput.poll();
        }
        result += result2;

        int result3 = nextLexemeIs(tempInput,",");
        while (result3 != -1) {
            for (int i = 0; i < result3; i++) {
                tempInput.poll();
            }
            result += result3;

            int result4 = isIdentifier(tempInput);
            if (result4 == -1)
                return -1;
            for (int i = 0; i < result4; i++) {
                tempInput.poll();
            }
            result += result4;

            result3 = nextLexemeIs(tempInput,",");

        }

        int result5 = nextLexemeIs(tempInput,")");
        if (result5 == -1)
            return -1;
        for (int i = 0; i < result5; i++) {
            tempInput.poll();
        }
        result += result5;
        return result;
    }
    private int OUT(Queue<Lex> input){
        Queue<Lex> tempInput = new LinkedList<>(input);

        int result = nextLexemeIs(tempInput,"write");
        if (result == -1)
            return -1;
        for (int i = 0; i < result; i++) {
            tempInput.poll();
        }

        int result1 = nextLexemeIs(tempInput,"(");
        if (result1 == -1)
            return -1;
        for (int i = 0; i < result1; i++) {
            tempInput.poll();
        }
        result += result1;

        int result2 = EXPR(tempInput);
        if (result2 == -1)
            return -1;
        for (int i = 0; i < result2; i++) {
            tempInput.poll();
        }
        result += result2;

        int result3 = nextLexemeIs(tempInput,",");
        while (result3 != -1) {
            for (int i = 0; i < result3; i++) {
                tempInput.poll();
            }
            result += result3;

            int result4 = EXPR(tempInput);
            if (result4 == -1)
                return -1;
            for (int i = 0; i < result4; i++) {
                tempInput.poll();
            }
            result += result4;

            result3 = nextLexemeIs(tempInput,",");

        }

        int result5 = nextLexemeIs(tempInput,")");
        if (result5 == -1)
            return -1;
        for (int i = 0; i < result5; i++) {
            tempInput.poll();
        }
        result += result5;
        return result;
    }
    private int PRISV(Queue<Lex> input){
        Queue<Lex> tempInput = new LinkedList<>(input);
        int result = isIdentifier(tempInput);
        if (result == -1)
            return -1;
        for (int i = 0; i < result; i++) {
            tempInput.poll();
        }

        int result1 = nextLexemeIs(tempInput, "ass");
        if (result1 == -1)
            return -1;
        for (int i = 0; i < result1; i++) {
            tempInput.poll();
        }
        result += result1;

        int result2 = EXPR(tempInput);
        if (result2 == -1)
            return -1;
        for (int i = 0; i < result2; i++) {
            tempInput.poll();
        }
        result += result2;
        return result;
    }
    private int USLOV(Queue<Lex> input){
        Queue<Lex> tempInput = new LinkedList<>(input);
        int result = nextLexemeIs(tempInput, "if");
        if (result == -1)
            return -1;
        for (int i = 0; i < result; i++) {
            tempInput.poll();
        }

        int result1 = EXPR(tempInput);
        if (result1 == -1)
            return -1;
        for (int i = 0; i < result1; i++) {
            tempInput.poll();
        }
        result += result1;

        int result2 = nextLexemeIs(tempInput, "then");
        if (result2 == -1)
            return -1;
        for (int i = 0; i < result2; i++) {
            tempInput.poll();
        }
        result += result2;

        int result3 = OPERATOR(tempInput);
        if (result3 == -1)
            return -1;
        for (int i = 0; i < result3; i++) {
            tempInput.poll();
        }
        result += result3;

        int result4 = nextLexemeIs(tempInput, "else");
        if (result4 != -1) {
            for (int i = 0; i < result4; i++) {
                tempInput.poll();
            }
            result += result4;

            int result5 = OPERATOR(tempInput);
            if (result5 == -1)
                return -1;
            for (int i = 0; i < result5; i++) {
                tempInput.poll();
            }
            result += result5;
        }
        return result;
    }
    private int FIXLOOP(Queue<Lex> input){
        Queue<Lex> tempInput = new LinkedList<>(input);
        int result = nextLexemeIs(tempInput, "for");
        if (result == -1)
            return -1;
        for (int i = 0; i < result; i++) {
            tempInput.poll();
        }

        int result1 = PRISV(tempInput);
        if (result1 == -1)
            return -1;
        for (int i = 0; i < result1; i++) {
            tempInput.poll();
        }
        result += result1;

        int result2 = nextLexemeIs(tempInput, "to");
        if (result2 == -1)
            return -1;
        for (int i = 0; i < result2; i++) {
            tempInput.poll();
        }
        result += result2;

        int result3 = EXPR(tempInput);
        if (result3 == -1)
            return -1;
        for (int i = 0; i < result3; i++) {
            tempInput.poll();
        }
        result += result3;

        int result4 = nextLexemeIs(tempInput, "do");
        if (result4 == -1)
            return -1;
        for (int i = 0; i < result4; i++) {
            tempInput.poll();
        }
        result += result4;

        int result5 = OPERATOR(tempInput);
        if (result5 == -1)
            return -1;
        for (int i = 0; i < result5; i++) {
            tempInput.poll();
        }
        result += result5;
        return result;
    }
    private int USLLOOP(Queue<Lex> input){
        Queue<Lex> tempInput = new LinkedList<>(input);
        int result = nextLexemeIs(tempInput, "while");
        if (result == -1)
            return -1;
        for (int i = 0; i < result; i++) {
            tempInput.poll();
        }

        int result1 = EXPR(tempInput);
        if (result1 == -1)
            return -1;
        for (int i = 0; i < result1; i++) {
            tempInput.poll();
        }
        result += result1;

        int result2 = nextLexemeIs(tempInput, "do");
        if (result2 == -1)
            return -1;
        for (int i = 0; i < result2; i++) {
            tempInput.poll();
        }
        result += result2;

        int result3 = OPERATOR(tempInput);
        if (result3 == -1)
            return -1;
        for (int i = 0; i < result3; i++) {
            tempInput.poll();
        }
        result += result3;

        return result;
    }

    private int OPERATOR(Queue<Lex> input){
        Queue<Lex> tempInput = new LinkedList<>(input);
        int result = -1;

        if (result == -1)
            result = PRISV(tempInput);
        if (result == -1)
            result = USLOV(tempInput);
        if (result == -1)
            result = FIXLOOP(tempInput);
        if (result == -1)
            result = USLLOOP(tempInput);
        if (result == -1)
            result = ENTER(tempInput);
        if (result == -1)
            result = OUT(tempInput);

        if (result != -1){
            for (int i = 0; i < result; i++) {
                tempInput.poll();
            }

            int result1 = nextLexemeIs(tempInput, ":");
            if (result1 == -1)
                result1 = nextLexemeIs(tempInput, "\n");

            while (result1 != -1) {
                for (int i = 0; i < result1; i++) {
                    tempInput.poll();
                }
                result += result1;

                int result2 = OPERATOR(tempInput);
                if (result2 == -1)
                    return -1;
                for (int i = 0; i < result2; i++) {
                    tempInput.poll();
                }
                result += result2;

                result1 = nextLexemeIs(tempInput, ":");
                if (result1 == -1)
                    result1 = nextLexemeIs(tempInput, "\n");
            }
        }

        return result;
    }

    private int DESC(Queue<Lex> input){
        Queue<Lex> tempInput = new LinkedList<>(input);
        int result = T(tempInput);
        if (result == -1)
            return -1;
        for (int i = 0; i < result; i++) {
            tempInput.poll();
        }

        int result1 = isIdentifier(tempInput);
        if (result1 == -1)
            return -1;
        for (int i = 0; i < result1; i++) {
            tempInput.poll();
        }
        result += result1;

        int result2 = nextLexemeIs(tempInput, ",");

        while (result2 != -1) {
            for (int i = 0; i < result2; i++) {
                tempInput.poll();
            }
            result += result1;
            int result3 = isIdentifier(tempInput);
            if (result3 == -1)
                return -1;
            for (int i = 0; i < result3; i++) {
                tempInput.poll();
            }
            result += result3;

            result2 = nextLexemeIs(tempInput, ",");
        }

        return result;
    }
    private int PROG(Queue<Lex> input){
        Queue<Lex> tempInput = new LinkedList<>(input);
        int result = nextLexemeIs(tempInput, "{");
        if (result == -1)
            return -1;
        for (int i = 0; i < result; i++) {
            tempInput.poll();
        }


        int result1 = DESC(tempInput);
        if (result1 == -1)
            result1 = OPERATOR(tempInput);
        if (result1 == -1)
            return -1;
        for (int i = 0; i < result1; i++) {
            tempInput.poll();
        }
        result += result1;

        int result2 = nextLexemeIs(tempInput, ";");
        if (result2 == -1)
            return -1;
        for (int i = 0; i < result2; i++) {
            tempInput.poll();
        }
        result += result2;

        int result3 = DESC(tempInput);
        if (result3 == -1)
            result3 = OPERATOR(tempInput);

        while (result3 != -1) {
            for (int i = 0; i < result3; i++) {
                tempInput.poll();
            }
            result += result3;

            int result4 = nextLexemeIs(tempInput, ";");
            if (result4 == -1)
                return -1;
            for (int i = 0; i < result4; i++) {
                tempInput.poll();
            }
            result += result4;

            result3 = DESC(tempInput);
            if (result3 == -1)
                result3 = OPERATOR(tempInput);
        }

        int result5 = nextLexemeIs(tempInput, "}");
        if (result5 == -1)
            return -1;
        for (int i = 0; i < result5; i++) {
            tempInput.poll();
        }
        result += result5;
        return result;
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

/**   ENTER                             ввода                                               **/
/**   OUT                               вывода                                              **/
/**   PRISV                             присваивания                                        **/
/**   USLOV                             условный                                            **/
/**   FIXLOOP                           фиксированного_цикла                                **/
/**   USLLOOP                           условного_цикла                                     **/
/**   SOSTAV                            составной                                           **/

/**   OPERATOR                          оператор                                            **/
/**   DESC                              описание                                            **/

/**   PROG                              программа                                           **/


//          <операнд>::= <слагаемое> {<операции_группы_сложения> <слагаемое>}
//          <выражение>::= <операнд>{<операции_группы_отношения> <операнд>}
//          <множитель>::= (<идентификатор> | <число> | <логическая_константа> | <унарная_операция> <множитель> | <выражение>)
//          <слагаемое>::= <множитель> {<операции_группы_умножения> <множитель>}

//          <ввода>::= read (<идентификатор> {, <идентификатор> })
//          <вывода>::= write (<выражение> {, <выражение> })
//          <присваивания>::= <идентификатор> ass <выражение>
//          <условный>::= if <выражение> then <оператор> [ else <оператор>]
//          <фиксированного_цикла>::= for <присваивания> to <выражение> do <оператор>
//          <условного_цикла>::= while <выражение> do <оператор>
//          ---------<составной>::= <оператор> { ( : | перевод строки) <оператор> }

//          <оператор>::= (<присваивания> | <условный> | <фиксированного_цикла> | <условного_цикла> | <ввода> | <вывода>) { ( : | перевод строки) <оператор> }
//          <описание>::= <тип> <идентификатор> { , <идентификатор> }

//          <программа>::= { {/ (<описание> | <оператор>) ; /} }

}
