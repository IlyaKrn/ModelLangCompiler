package com.ilyakrn.interpreter;

import com.ilyakrn.entities.InternalProgramPresentation;
import com.ilyakrn.entities.items.*;
import com.ilyakrn.exceptions.external.InterpretationException;
import com.ilyakrn.exceptions.internal.InternalInterpreterException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;

public class Interpreter {

    private HashMap<String, MemoryItem> memory;
    private InternalProgramPresentation internalProgramPresentation;

    public void interpret(InternalProgramPresentation input) {
        memory = new HashMap<>();
        internalProgramPresentation = input;

        for (int i = 0; i < internalProgramPresentation.getIdentifierTable().size(); i++) {
            IdentifierItem item = internalProgramPresentation.getIdentifierTable().get(i);
            String defaultValue = "";
            switch (item.getType()) {
                case INT:
                    defaultValue="0";
                    break;
                case FLOAT:
                    defaultValue="0.0";
                    break;
                case BOOL:
                    defaultValue="false";
                    break;
                default:
                    throw new InternalInterpreterException("Unknown item type: '" + item.getType() + "'");

            }
            memory.put(getMemoryAddress(InternalProgramPresentation.identifierTableId, i), new MemoryItem(item.getType(), defaultValue));
        }
        for (int i = 0; i < internalProgramPresentation.getNumberTable().size(); i++) {
            NumberItem item = internalProgramPresentation.getNumberTable().get(i);
            try {
                if (item.getType() == Type.INT){
                    String originValue = item.getLexeme();
                    if(originValue.charAt(originValue.length() - 1) == 'b' ||
                            originValue.charAt(originValue.length() - 1) == 'o' ||
                            originValue.charAt(originValue.length() - 1) == 'd' ||
                            originValue.charAt(originValue.length() - 1) == 'h'
                    ){
                        originValue = originValue.substring(0, originValue.length() - 1);
                    }
                    String value = "" + Integer.parseInt(originValue, item.getDimensionCount());
                    memory.put(getMemoryAddress(InternalProgramPresentation.numberTableId, i), new MemoryItem(Type.INT, value));
                }
                if (item.getType() == Type.FLOAT){
                    throw new InternalInterpreterException("FLOAT UNDER DEVELOPMENT");
                }
            } catch (NumberFormatException e) {
                throw new InterpretationException("can not convert '" + item.getLexeme() + "' to decimal int");
            }
        }
        for (int i = 0; i < internalProgramPresentation.getServiceTable().size(); i++) {
            ServiceItem item = internalProgramPresentation.getServiceTable().get(i);
            if (item.getLexeme() == "true" || item.getLexeme() == "false") {
                memory.put(getMemoryAddress(InternalProgramPresentation.serviceTableId, i), new MemoryItem(Type.BOOL, item.getLexeme()));
            }
        }
        for (int i = 0; i < internalProgramPresentation.getPolizPointerTable().size(); i++) {
            PolizPointerItem item = internalProgramPresentation.getPolizPointerTable().get(i);
            memory.put(getMemoryAddress(InternalProgramPresentation.polizPointerTableId, i), new MemoryItem(Type.INT, String.valueOf(item.getPolizIndex())));
        }





        Stack<InterpreterStackItem> execStack = new Stack<>();
        for (int i = 0; i < internalProgramPresentation.getPolizTable().size(); i++) {
            PolizItem currentPolizItem = internalProgramPresentation.getPolizTable().get(i);

            if (i == internalProgramPresentation.getPolizTable().size() - 1){
                if (!execStack.isEmpty())
                    throw new InternalInterpreterException("unexpected end of program");
                if (currentPolizItem.getTableId() != InternalProgramPresentation.delimiterTableId || !internalProgramPresentation.getDelimiterTable().get(currentPolizItem.getLexId()).getLexeme().equals("}"))
                    throw new InterpretationException("program must be terminated by '}'");
                return;
            }

            if (currentPolizItem.getTableId() == InternalProgramPresentation.delimiterTableId) {

                String operator = internalProgramPresentation.getDelimiterTable().get(currentPolizItem.getLexId()).getLexeme();
                int operandsCount = getOperandsCount(operator);

                if (operandsCount == 1) {
                    if (execStack.size() < 1)
                        throw new InterpretationException("execution stack size < 1 for 1-placed operator '" + internalProgramPresentation.getDelimiterTable().get(currentPolizItem.getLexId()).getLexeme() + "'");
                    InterpreterStackItem stackOperand = execStack.pop();

                    switch (operator){
                        case "!":
                            try {
                                i = Integer.parseInt(stackOperand.getValue()) - 1;
                                if (i < 0 || i >= internalProgramPresentation.getPolizTable().size())
                                    throw new InterpretationException("poliz pointer out of bounds");
                            } catch (NumberFormatException e){
                                throw new InterpretationException("invalid poliz pointer value");
                            }
                            break;
                        case "R":
                            if (stackOperand.getMemoryAddress() == null)
                                throw new InterpretationException("memory to read not allocated");
                            Scanner scanner = new Scanner(System.in);
                            String value = scanner.nextLine();
                            switch (stackOperand.getType()){
                                case INT:
                                    try{
                                        int integerValue = Integer.parseInt(value);
                                        MemoryItem memoryItem = memory.get(stackOperand.getMemoryAddress());
                                        memoryItem.setValue(String.valueOf(integerValue));
                                        memory.put(stackOperand.getMemoryAddress(), memoryItem);
                                    }catch (NumberFormatException e){
                                        throw new InterpretationException("invalid integer value");
                                    }
                                    break;
                                case FLOAT:
                                    throw new InternalInterpreterException("FLOAT UNDER DEVELOPMENT");
                                case BOOL:
                                    if (!value.equals("true") && !value.equals("false"))
                                        throw new InterpretationException("can not convert '" + value + "' to bool");
                                    MemoryItem memoryItem = memory.get(stackOperand.getMemoryAddress());
                                    memoryItem.setValue(value);
                                    memory.put(stackOperand.getMemoryAddress(), memoryItem);
                                    break;
                                default:
                                    throw new InternalInterpreterException("unknown type '" + stackOperand.getType().name() + "'");
                            }
                            break;
                        case "W":
                            System.out.println(stackOperand.getValue());
                            break;
                        case "not":
                            execStack.push(new InterpreterStackItem(stackOperand.getValue().equals("true") ? "false" : "true", Type.BOOL, null));
                            break;
                        default:
                            throw new InternalInterpreterException("Invalid poliz operator '" + internalProgramPresentation.getDelimiterTable().get(currentPolizItem.getLexId()).getLexeme() + "'");
                    }
                } else if (operandsCount == 2) {
                    if (execStack.size() < 2)
                        throw new InterpretationException("execution stack size < 2 for 2-placed operator '" + internalProgramPresentation.getDelimiterTable().get(currentPolizItem.getLexId()).getLexeme() + "'");

                    InterpreterStackItem stackOperand2 = execStack.pop();
                    InterpreterStackItem stackOperand1 = execStack.pop();
                    switch (operator){
                        case ">":
                            try{
                                float floatValue1 = Float.parseFloat(stackOperand1.getValue());
                                float floatValue2 = Float.parseFloat(stackOperand2.getValue());
                                execStack.push(new InterpreterStackItem(floatValue1 > floatValue2 ? "true" : "false", getBinOpResultType(operator, stackOperand1.getType(), stackOperand2.getType()), null));
                            }catch (NumberFormatException e){
                                throw new InterpretationException("can apply '>' to '" + stackOperand1.getValue() + "' and '" + stackOperand2.getValue() + "'");
                            }
                            break;
                        case "<":
                            try{
                                float floatValue1 = Float.parseFloat(stackOperand1.getValue());
                                float floatValue2 = Float.parseFloat(stackOperand2.getValue());
                                execStack.push(new InterpreterStackItem(floatValue1 < floatValue2 ? "true" : "false", getBinOpResultType(operator, stackOperand1.getType(), stackOperand2.getType()), null));
                            }catch (NumberFormatException e){
                                throw new InterpretationException("can apply '<' to '" + stackOperand1.getValue() + "' and '" + stackOperand2.getValue() + "'");
                            }
                            break;
                        case "<>":
                            try{
                                String value1;
                                String value2;
                                if (stackOperand1.getType() == Type.BOOL && stackOperand2.getType() == Type.BOOL){
                                    value1 = String.valueOf(Boolean.parseBoolean(stackOperand1.getValue()));
                                    value2 = String.valueOf(Boolean.parseBoolean(stackOperand2.getValue()));
                                }
                                else {
                                    value1 = String.valueOf(Float.parseFloat(stackOperand1.getValue()));
                                    value2 = String.valueOf(Float.parseFloat(stackOperand2.getValue()));
                                }
                                execStack.push(new InterpreterStackItem(!value1.equals(value2) ? "true" : "false", getBinOpResultType(operator, stackOperand1.getType(), stackOperand2.getType()), null));
                            }catch (NumberFormatException e){
                                throw new InterpretationException("can apply '<>' to '" + stackOperand1.getValue() + "' and '" + stackOperand2.getValue() + "'");
                            }
                            break;
                        case ">=":
                            try{
                                float floatValue1 = Float.parseFloat(stackOperand1.getValue());
                                float floatValue2 = Float.parseFloat(stackOperand2.getValue());
                                execStack.push(new InterpreterStackItem(floatValue1 >= floatValue2 ? "true" : "false", getBinOpResultType(operator, stackOperand1.getType(), stackOperand2.getType()), null));
                            }catch (NumberFormatException e){
                                throw new InterpretationException("can apply '>=' to '" + stackOperand1.getValue() + "' and '" + stackOperand2.getValue() + "'");
                            }
                            break;
                        case "<=":
                            try{
                                float floatValue1 = Float.parseFloat(stackOperand1.getValue());
                                float floatValue2 = Float.parseFloat(stackOperand2.getValue());
                                execStack.push(new InterpreterStackItem(floatValue1 <= floatValue2 ? "true" : "false", getBinOpResultType(operator, stackOperand1.getType(), stackOperand2.getType()), null));
                            }catch (NumberFormatException e){
                                throw new InterpretationException("can apply '<=' to '" + stackOperand1.getValue() + "' and '" + stackOperand2.getValue() + "'");
                            }
                            break;
                        case "=":
                            try{
                                String value1;
                                String value2;
                                if (stackOperand1.getType() == Type.BOOL && stackOperand2.getType() == Type.BOOL){
                                    value1 = String.valueOf(Boolean.parseBoolean(stackOperand1.getValue()));
                                    value2 = String.valueOf(Boolean.parseBoolean(stackOperand2.getValue()));
                                }
                                else {
                                    value1 = String.valueOf(Float.parseFloat(stackOperand1.getValue()));
                                    value2 = String.valueOf(Float.parseFloat(stackOperand2.getValue()));
                                }
                                execStack.push(new InterpreterStackItem(value1.equals(value2) ? "true" : "false", getBinOpResultType(operator, stackOperand1.getType(), stackOperand2.getType()), null));
                            }catch (NumberFormatException e){
                                throw new InterpretationException("can apply '=' to '" + stackOperand1.getValue() + "' and '" + stackOperand2.getValue() + "'");
                            }
                            break;
                        case "+":
                            try{
                                float floatValue1 = Float.parseFloat(stackOperand1.getValue());
                                float floatValue2 = Float.parseFloat(stackOperand2.getValue());
                                String res = String.valueOf(floatValue1 + floatValue2);
                                if (getBinOpResultType(operator, stackOperand1.getType(), stackOperand2.getType()) == Type.INT) {
                                    res = res.substring(0, res.length() - 2);
                                }
                                execStack.push(new InterpreterStackItem(res, getBinOpResultType(operator, stackOperand1.getType(), stackOperand2.getType()), null));
                            }catch (NumberFormatException e){
                                throw new InterpretationException("can apply '+' to '" + stackOperand1.getValue() + "' and '" + stackOperand2.getValue() + "'");
                            }
                            break;
                        case "-":
                            try{
                                float floatValue1 = Float.parseFloat(stackOperand1.getValue());
                                float floatValue2 = Float.parseFloat(stackOperand2.getValue());
                                String res = String.valueOf(floatValue1 - floatValue2);
                                if (getBinOpResultType(operator, stackOperand1.getType(), stackOperand2.getType()) == Type.INT) {
                                    res = res.substring(0, res.length() - 2);
                                }
                                execStack.push(new InterpreterStackItem(res, getBinOpResultType(operator, stackOperand1.getType(), stackOperand2.getType()), null));
                            }catch (NumberFormatException e){
                                throw new InterpretationException("can apply '-' to '" + stackOperand1.getValue() + "' and '" + stackOperand2.getValue() + "'");
                            }
                            break;
                        case "*":
                            try{
                                float floatValue1 = Float.parseFloat(stackOperand1.getValue());
                                float floatValue2 = Float.parseFloat(stackOperand2.getValue());
                                String res = String.valueOf(floatValue1 * floatValue2);
                                if (getBinOpResultType(operator, stackOperand1.getType(), stackOperand2.getType()) == Type.INT) {
                                    res = res.substring(0, res.length() - 2);
                                }
                                execStack.push(new InterpreterStackItem(res, getBinOpResultType(operator, stackOperand1.getType(), stackOperand2.getType()), null));
                            }catch (NumberFormatException e){
                                throw new InterpretationException("can apply '*' to '" + stackOperand1.getValue() + "' and '" + stackOperand2.getValue() + "'");
                            }
                            break;
                        case "/":
                            try{
                                float floatValue1 = Float.parseFloat(stackOperand1.getValue());
                                float floatValue2 = Float.parseFloat(stackOperand2.getValue());
                                String res = String.valueOf(floatValue1 / floatValue2);
                                execStack.push(new InterpreterStackItem(res, getBinOpResultType(operator, stackOperand1.getType(), stackOperand2.getType()), null));
                            }catch (NumberFormatException e){
                                throw new InterpretationException("can apply '/' to '" + stackOperand1.getValue() + "' and '" + stackOperand2.getValue() + "'");
                            }
                            break;
                        case "or":
                            try{
                                boolean booleanValue1 = Boolean.parseBoolean(stackOperand1.getValue());
                                boolean booleanValue2 = Boolean.parseBoolean(stackOperand2.getValue());
                                String res = String.valueOf(booleanValue1 || booleanValue2);
                                execStack.push(new InterpreterStackItem(res, getBinOpResultType(operator, stackOperand1.getType(), stackOperand2.getType()), null));
                            }catch (NumberFormatException e){
                                throw new InterpretationException("can apply 'or' to '" + stackOperand1.getValue() + "' and '" + stackOperand2.getValue() + "'");
                            }
                            break;
                        case "and":
                            try{
                                boolean booleanValue1 = Boolean.parseBoolean(stackOperand1.getValue());
                                boolean booleanValue2 = Boolean.parseBoolean(stackOperand2.getValue());
                                String res = String.valueOf(booleanValue1 && booleanValue2);
                                execStack.push(new InterpreterStackItem(res, getBinOpResultType(operator, stackOperand1.getType(), stackOperand2.getType()), null));
                            }catch (NumberFormatException e){
                                throw new InterpretationException("can apply 'and' to '" + stackOperand1.getValue() + "' and '" + stackOperand2.getValue() + "'");
                            }
                            break;
                        case "ass":
                            if (stackOperand1.getMemoryAddress() == null)
                                throw new InterpretationException("memory to read not allocated");
                            MemoryItem memoryItem = memory.get(stackOperand1.getMemoryAddress());
                            memoryItem.setValue(String.valueOf(stackOperand2.getValue()));
                            memory.put(stackOperand1.getMemoryAddress(), memoryItem);
                            break;
                        case "!F":
                            try {
                                if (stackOperand1.getType() == Type.BOOL){
                                    if (stackOperand1.getValue().equals("false")){
                                        i = Integer.parseInt(stackOperand2.getValue()) - 1;
                                        if (i < 0 || i >= internalProgramPresentation.getPolizTable().size())
                                            throw new InterpretationException("poliz pointer out of bounds");
                                    }
                                } else
                                    throw new InterpretationException("'!F' can not apply to " + stackOperand1.getValue() + "' and '" + stackOperand2.getValue() + "'");

                            } catch (NumberFormatException e){
                                throw new InterpretationException("invalid poliz pointer value");
                            }
                            break;
                        default:
                            throw new InternalInterpreterException("Invalid poliz operator " + internalProgramPresentation.getDelimiterTable().get(currentPolizItem.getLexId()).getLexeme());
                    }
                } else {
                    throw new InternalInterpreterException("operator with '" + operandsCount + "' operands are not allowed. operator '" + internalProgramPresentation.getDelimiterTable().get(currentPolizItem.getLexId()).getLexeme() + "'");
                }
            } else {
                execStack.push(new InterpreterStackItem(getValueById(currentPolizItem.getTableId(), currentPolizItem.getLexId()), getTypeById(currentPolizItem.getTableId(), currentPolizItem.getLexId()), getMemoryAddress(currentPolizItem.getTableId(), currentPolizItem.getLexId())));
            }
        }
    }

    Type getTypeById(int tableId, int lexId){
        switch (tableId){
            case InternalProgramPresentation.identifierTableId:
                if (lexId >= internalProgramPresentation.getIdentifierTable().size())
                    throw new InternalInterpreterException("identifier table id out of bounds");
                return internalProgramPresentation.getIdentifierTable().get(lexId).getType();
            case InternalProgramPresentation.numberTableId:
                if (lexId >= internalProgramPresentation.getNumberTable().size())
                    throw new InternalInterpreterException("number table id out of bounds");
                return internalProgramPresentation.getNumberTable().get(lexId).getType();
            case InternalProgramPresentation.serviceTableId:
                if (lexId >= internalProgramPresentation.getServiceTable().size())
                    throw new InternalInterpreterException("servide table id out of bounds");
                if (!internalProgramPresentation.getServiceTable().get(lexId).getLexeme().equals("true") && !internalProgramPresentation.getServiceTable().get(lexId).getLexeme().equals("false"))
                    throw new InternalInterpreterException("can not ger type of '" + internalProgramPresentation.getServiceTable().get(lexId).getLexeme() + "'");
                return Type.BOOL;
            case InternalProgramPresentation.polizPointerTableId:
                return Type.INT;
            default:
                throw new InternalInterpreterException("can not get type of lexeme");
        }
    }

    String getValueById(int tableId, int lexId){
        return memory.get(getMemoryAddress(tableId, lexId)).getValue();
    }

    String getMemoryAddress(int tableId, int lexemeId) {
        return tableId + ":" + lexemeId;
    }

    int getOperandsCount(String operator) {
        switch (operator) {
            case ">":
            case "<":
            case "<>":
            case ">=":
            case "<=":
            case "=":
            case "+":
            case "-":
            case "*":
            case "/":
            case "or":
            case "and":
            case "ass":
            case "!F":
                return 2;
            case "!":
            case "R":
            case "W":
            case "not":
                return 1;
            default:
                throw new InternalInterpreterException("Invalid poliz operator '" + operator + "'");
        }
    }

    Type getBinOpResultType(String operator, Type type1, Type type2) {
        for (BinOperationItem binOperationItem : internalProgramPresentation.getBinOperationTable()) {
            if (binOperationItem.getOperation().equals(operator) && binOperationItem.getOperand1() == type1 && binOperationItem.getOperand2() == type2)
                return binOperationItem.getResult();
        }
        throw new InternalInterpreterException("can not get result type for '" + type1 + "' '" + operator + "' '" + type2 + "'");
    }
}
