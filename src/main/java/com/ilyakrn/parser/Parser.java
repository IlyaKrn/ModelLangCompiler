package com.ilyakrn.parser;

import com.ilyakrn.entities.items.*;
import com.ilyakrn.entities.InternalProgramPresentation;
import com.ilyakrn.exceptions.external.SemanticException;
import com.ilyakrn.exceptions.external.SyntaxException;
import com.ilyakrn.exceptions.internal.InternalParserException;

import java.util.*;

public class Parser {

    private ArrayList<ServiceItem> serviceTable;
    private ArrayList<DelimiterItem> delimiterTable;
    private ArrayList<IdentifierItem> identifierTable;
    private ArrayList<NumberItem> numberTable;

    private ArrayList<BinOperationItem> binOperationTable;

    private Stack<String> exprStack;

    public boolean analyze(InternalProgramPresentation internalProgramPresentation) throws InternalParserException, SyntaxException, SemanticException {
        serviceTable = internalProgramPresentation.getServiceTable();
        delimiterTable = internalProgramPresentation.getDelimiterTable();
        identifierTable = internalProgramPresentation.getIdentifierTable();
        numberTable = internalProgramPresentation.getNumberTable();
        binOperationTable = internalProgramPresentation.getBinOperationTable();
        exprStack = new Stack<>();

        Queue<ParserQueueItem> input = new LinkedList<>();
        for (int i = 0; i < internalProgramPresentation.getLexemesSeqTable().size(); i++) {
            String lexemeText = "";
            switch (internalProgramPresentation.getLexemesSeqTable().get(i).getTableId()){
                case InternalProgramPresentation.serviceTableId:
                    lexemeText = serviceTable.get(internalProgramPresentation.getLexemesSeqTable().get(i).getLexId()).getLexeme();
                    break;
                case InternalProgramPresentation.delimiterTableId:
                    lexemeText = delimiterTable.get(internalProgramPresentation.getLexemesSeqTable().get(i).getLexId()).getLexeme();
                    break;
                case InternalProgramPresentation.identifierTableId:
                    lexemeText = identifierTable.get(internalProgramPresentation.getLexemesSeqTable().get(i).getLexId()).getLexeme();
                    break;
                case InternalProgramPresentation.numberTableId:
                    lexemeText = numberTable.get(internalProgramPresentation.getLexemesSeqTable().get(i).getLexId()).getLexeme();
                    break;
                default:
                    throw new InternalParserException("Invalid table id: " + internalProgramPresentation.getLexemesSeqTable().get(i).getTableId());
            }
            input.add(new ParserQueueItem(internalProgramPresentation.getLexemesSeqTable().get(i).getLexId(), internalProgramPresentation.getLexemesSeqTable().get(i).getTableId(), lexemeText));
        }

        int result = PROG(input);
        if(result == -1)
            throw new SyntaxException("Syntax error (no more info)");
        return result == input.size();
    }

    private int nextLexemeIs(Queue<ParserQueueItem> input, String lexeme){
        Queue<ParserQueueItem> tempInput = new LinkedList<>(input);
        if (tempInput.isEmpty())
            return -1;
        else{
            ParserQueueItem next = tempInput.poll();
            int skipped = 0;
            while (next.getLexeme().equals("\n")){
                next = tempInput.poll();
                skipped++;
            }
            if (skipped > 0 && lexeme.equals("\n"))
                return skipped;
            return next.getLexeme().equals(lexeme) ? 1 + skipped : -1;
        }
    }
    private int isIdentifier(Queue<ParserQueueItem> input){
        Queue<ParserQueueItem> tempInput = new LinkedList<>(input);
        if (tempInput.isEmpty())
            return -1;
        else{
            ParserQueueItem next = tempInput.poll();
            int skipped = 0;
            while (next.getLexeme().equals("\n")){
                next = tempInput.poll();
                skipped++;
            }
            return next.getTableId() == InternalProgramPresentation.identifierTableId ? 1 + skipped : -1;
        }
    }
    private int isNumber(Queue<ParserQueueItem> input) {
        Queue<ParserQueueItem> tempInput = new LinkedList<>(input);
        if (tempInput.isEmpty())
            return -1;
        else{
            ParserQueueItem next = tempInput.poll();
            int skipped = 0;
            while (next.getLexeme().equals("\n")){
                next = tempInput.poll();
                skipped++;
            }
            return next.getTableId() == InternalProgramPresentation.numberTableId ? 1 + skipped : -1;
        }
    }

    private int OGO(Queue<ParserQueueItem> input){
        Queue<ParserQueueItem> tempInput = new LinkedList<>(input);
        if (tempInput.isEmpty())
            return -1;
        else{
            ParserQueueItem next = tempInput.poll();
            int skipped = 0;
            while (next.getLexeme().equals("\n")){
                next = tempInput.poll();
                skipped++;
            }
            return (next.getLexeme().equals("<>") ||
                    next.getLexeme().equals("=") ||
                    next.getLexeme().equals("<") ||
                    next.getLexeme().equals("<=") ||
                    next.getLexeme().equals(">") ||
                    next.getLexeme().equals(">=")
            )? 1 + skipped : -1;
        }
    }
    private int OGS(Queue<ParserQueueItem> input){
        Queue<ParserQueueItem> tempInput = new LinkedList<>(input);
        if (tempInput.isEmpty())
            return -1;
        else{
            ParserQueueItem next = tempInput.poll();
            int skipped = 0;
            while (next.getLexeme().equals("\n")){
                next = tempInput.poll();
                skipped++;
            }
            return (next.getLexeme().equals("+") ||
                    next.getLexeme().equals("-") ||
                    next.getLexeme().equals("or")
            )? 1 + skipped : -1;
        }
    }
    private int OGU(Queue<ParserQueueItem> input){
        Queue<ParserQueueItem> tempInput = new LinkedList<>(input);
        if (tempInput.isEmpty())
            return -1;
        else{
            ParserQueueItem next = tempInput.poll();
            int skipped = 0;
            while (next.getLexeme().equals("\n")){
                next = tempInput.poll();
                skipped++;
            }
            return (next.getLexeme().equals("*") ||
                    next.getLexeme().equals("/") ||
                    next.getLexeme().equals("and")
            )? 1 + skipped : -1;
        }
    }
    private int LC(Queue<ParserQueueItem> input){
        Queue<ParserQueueItem> tempInput = new LinkedList<>(input);
        if (tempInput.isEmpty())
            return -1;
        else{
            ParserQueueItem next = tempInput.poll();
            int skipped = 0;
            while (next.getLexeme().equals("\n")){
                next = tempInput.poll();
                skipped++;
            }
            return (next.getLexeme().equals("true") ||
                    next.getLexeme().equals("false")
            )? 1 + skipped : -1;
        }
    }
    private int UO(Queue<ParserQueueItem> input){
        Queue<ParserQueueItem> tempInput = new LinkedList<>(input);
        if (tempInput.isEmpty())
            return -1;
        else{
            ParserQueueItem next = tempInput.poll();
            int skipped = 0;
            while (next.getLexeme().equals("\n")){
                next = tempInput.poll();
                skipped++;
            }
            return (next.getLexeme().equals("not")
            )? 1 + skipped : -1;
        }
    }
    private int T(Queue<ParserQueueItem> input){
        Queue<ParserQueueItem> tempInput = new LinkedList<>(input);
        if (tempInput.isEmpty())
            return -1;
        else{
            ParserQueueItem next = tempInput.poll();
            int skipped = 0;
            while (next.getLexeme().equals("\n")){
                next = tempInput.poll();
                skipped++;
            }
            return (next.getLexeme().equals("int") ||
                    next.getLexeme().equals("float") ||
                    next.getLexeme().equals("bool")
            )? 1 + skipped : -1;
        }
    }

    private int EXPR(Queue<ParserQueueItem> input){
        Queue<ParserQueueItem> tempInput = new LinkedList<>(input);
        exprStack.clear();
        int result = OPRND(tempInput);
        if (result != -1){
            for (int i = 0; i < result; i++) {
                tempInput.poll();
            }
            int result1 = OGO(tempInput);
            while (result1 != -1) {
                for (int i = 0; i < result1 - 1; i++) {
                    tempInput.poll();
                }
                exprStack.add(tempInput.poll().getLexeme());
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

        System.out.print(exprStack + "        ");
        Type t = getExprType(exprStack);

        System.out.println(t.name());
        return result;
    }
    private int SLAG(Queue<ParserQueueItem> input){
        Queue<ParserQueueItem> tempInput = new LinkedList<>(input);
        int result = MNOZH(tempInput);
        if (result != -1){
            for (int i = 0; i < result; i++) {
                tempInput.poll();
            }
            int result1 = OGU(tempInput);
            while (result1 != -1) {
                for (int i = 0; i < result1 - 1; i++) {
                    tempInput.poll();
                }
                exprStack.add(tempInput.poll().getLexeme());
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
    private int OPRND(Queue<ParserQueueItem> input){
        Queue<ParserQueueItem> tempInput = new LinkedList<>(input);
        int result = SLAG(tempInput);
        if (result != -1){
            for (int i = 0; i < result; i++) {
                tempInput.poll();
            }
            int result1 = OGS(tempInput);
            while (result1 != -1) {
                for (int i = 0; i < result1 - 1; i++) {
                    tempInput.poll();
                }
                exprStack.add(tempInput.poll().getLexeme());
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
    private int MNOZH(Queue<ParserQueueItem> input){
        Queue<ParserQueueItem> tempInput = new LinkedList<>(input);
        int result = -1;

        if (result == -1) {
            result = UO(tempInput);
            if (result != -1) {
                for (int i = 0; i < result; i++) {
                    tempInput.poll();
                }
                int result1 = MNOZH(tempInput);
                if(result1 != -1){
                    for (int i = 0; i < result1 - 1; i++) {
                        tempInput.poll();
                    }
                    Type type = getType(tempInput.poll());
                    if(type != Type.BOOL)
                        throw new SemanticException("'not' can apply to bool only");
                    exprStack.add(type.name());
                    result += result1;
                    return result;
                }
                else {
                    throw new SemanticException("'not' statement not completed");
                }
            }
        }
        if (result == -1)
            result = isIdentifier(tempInput);
        if (result == -1)
            result = isNumber(tempInput);
        if (result == -1)
            result = LC(tempInput);
//        if (result == -1)
//            result = MNOZH(tempInput);

        if(result != -1){
            for (int i = 0; i < result - 1; i++) {
                tempInput.poll();
            }
            Type type = getType(tempInput.poll());
            exprStack.add(type.name());
        }

        return result;
    }

    private int ENTER(Queue<ParserQueueItem> input){
        Queue<ParserQueueItem> tempInput = new LinkedList<>(input);

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
    private int OUT(Queue<ParserQueueItem> input){
        Queue<ParserQueueItem> tempInput = new LinkedList<>(input);

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
    private int PRISV(Queue<ParserQueueItem> input){
        Queue<ParserQueueItem> tempInput = new LinkedList<>(input);
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
    private int USLOV(Queue<ParserQueueItem> input){
        Queue<ParserQueueItem> tempInput = new LinkedList<>(input);
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
    private int FIXLOOP(Queue<ParserQueueItem> input){
        Queue<ParserQueueItem> tempInput = new LinkedList<>(input);
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
    private int USLLOOP(Queue<ParserQueueItem> input){
        Queue<ParserQueueItem> tempInput = new LinkedList<>(input);
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

    private int OPERATOR(Queue<ParserQueueItem> input){
        Queue<ParserQueueItem> tempInput = new LinkedList<>(input);
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

            boolean isLn = false;
            int result1 = nextLexemeIs(tempInput, ":");
            if (result1 == -1) {
                result1 = nextLexemeIs(tempInput, "\n");
                if (result1 != -1)
                    isLn = true;
            }

            while (result1 != -1) {
                for (int i = 0; i < result1; i++) {
                    tempInput.poll();
                }
                result += result1;

                int result2 = OPERATOR(tempInput);
                if (result2 == -1 && !isLn)
                    return -1;
                else if (result2 == -1 && isLn){
                    result -= result1;
                    break;
                }
                for (int i = 0; i < result2; i++) {
                    tempInput.poll();
                }
                result += result2;

                isLn = false;
                result1 = nextLexemeIs(tempInput, ":");
                if (result1 == -1) {
                    result1 = nextLexemeIs(tempInput, "\n");
                    if (result1 != -1)
                        isLn = true;
                }
            }
        }

        return result;
    }

    private int DESC(Queue<ParserQueueItem> input) throws InternalParserException {
        Queue<ParserQueueItem> tempInput = new LinkedList<>(input);
        int result = T(tempInput);
        if (result == -1)
            return -1;
        Type type = null;
        for (int i = 0; i < result - 1; i++) {
            tempInput.poll();
        }
        switch (tempInput.poll().getLexeme()){
            case "int":
                type = Type.INT;
                break;
            case "float":
                type = Type.FLOAT;
                break;
            case "bool":
                type = Type.BOOL;
                break;
            default:
                throw new InternalParserException("unresolved type");
        }
        ArrayList<Integer> ids = new ArrayList<>();

        int result1 = isIdentifier(tempInput);
        if (result1 == -1)
            return -1;
        for (int i = 0; i < result1 - 1; i++) {
            tempInput.poll();
        }
        ids.add(tempInput.poll().getLexId());
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
            for (int i = 0; i < result3 - 1; i++) {
                tempInput.poll();
            }
            ids.add(tempInput.poll().getLexId());
            result += result3;

            result2 = nextLexemeIs(tempInput, ",");
        }
        for (Integer id : ids) {
            if(identifierTable.get(id).isInit())
                throw new SemanticException(id + " has already been initialized");
            identifierTable.get(id).setInit(true);
            identifierTable.get(id).setType(type);
        }

        return result;
    }

    private int PROG(Queue<ParserQueueItem> input) throws InternalParserException, SyntaxException, SemanticException {
        Queue<ParserQueueItem> tempInput = new LinkedList<>(input);
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

    Type getType(ParserQueueItem parserQueueItem) {
        Type result = null;
        switch (parserQueueItem.getTableId()){
            case InternalProgramPresentation.serviceTableId:
                switch (parserQueueItem.getLexeme()){
                    case "true":
                    case "false":
                        result = Type.BOOL;
                        break;
                    default:
                        throw new InternalParserException("can not get type of service word or delimiter");
                }
                break;
            case InternalProgramPresentation.delimiterTableId:
                throw new InternalParserException("can not get type of service word or delimiter");
            case InternalProgramPresentation.numberTableId:
                if (parserQueueItem.getLexId() >= numberTable.size())
                    throw new InternalParserException("can not find number with id " + parserQueueItem.getLexId());
                result = numberTable.get(parserQueueItem.getLexId()).getType();
                break;
            case InternalProgramPresentation.identifierTableId:
                if (parserQueueItem.getLexId() >= identifierTable.size())
                    throw new InternalParserException("can not find number with id " + parserQueueItem.getLexId());
                result = identifierTable.get(parserQueueItem.getLexId()).getType();
                if (result == null)
                    throw new InternalParserException("variable '" + parserQueueItem.getLexeme() + "' not defined");
                break;
            default:
                throw new InternalParserException("can not find table with id " + parserQueueItem.getTableId());
        }
        if (result == null)
            throw new InternalParserException("can not get type of " + parserQueueItem.getTableId() + " " + parserQueueItem.getLexId());
        return result;
    }

    Type getExprType(Stack<String> stack) {
        while (stack.size() >= 3) {
            String operand2 = stack.pop();
            String operator = stack.pop();
            String operand1 = stack.pop();
            Type result = null;
            for (int i = 0; i < binOperationTable.size(); i++) {
                if (binOperationTable.get(i).getOperation().equals(operator)
                && binOperationTable.get(i).getOperand1().name().equals(operand1)
                && binOperationTable.get(i).getOperand2().name().equals(operand2)) {
                    result = binOperationTable.get(i).getResult();
                    break;
                }
            }
            if (result == null)
                throw new InternalParserException("can not get type of " + operand1 + " " + operator + " " + operand2);
            stack.push(result.name());
        }
        return Type.valueOf(stack.pop());
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
