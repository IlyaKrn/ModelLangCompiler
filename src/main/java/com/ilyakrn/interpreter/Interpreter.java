package com.ilyakrn.interpreter;

import com.ilyakrn.entities.InternalProgramPresentation;
import com.ilyakrn.entities.items.PolizItem;
import com.ilyakrn.exceptions.external.InterpretationException;
import com.ilyakrn.exceptions.internal.InternalInterpreterException;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class Interpreter {

    public void interpret(InternalProgramPresentation internalProgramPresentation) {
        System.out.println(internalProgramPresentation);
//        Stack<PolizItem> execStack = new Stack<>();
//        for (int i = 0; i < internalProgramPresentation.getPolizTable().size(); i++) {
//            PolizItem currentItem = internalProgramPresentation.getPolizTable().get(i);
//
//            if (i == internalProgramPresentation.getPolizTable().size() - 1){
//                if (!execStack.isEmpty())
//                    throw new InternalInterpreterException("execution stack is not empty int the end of program");
//                if (currentItem.getTableId() != InternalProgramPresentation.delimiterTableId || !internalProgramPresentation.getDelimiterTable().get(currentItem.getLexId()).getLexeme().equals("}"))
//                    throw new InterpretationException("program must be terminated by '}'");
//            }
//
//            if (currentItem.getTableId() == InternalProgramPresentation.delimiterTableId) {
//                if (execStack.isEmpty())
//                    throw new InternalInterpreterException("execution stack is empty");
//
//                int operandsCount = 0;
//                switch (internalProgramPresentation.getDelimiterTable().get(currentItem.getLexId()).getLexeme()){
//                    case ">":
//                    case "<":
//                    case "<>":
//                    case ">=":
//                    case "<=":
//                    case "=":
//                    case "+":
//                    case "-":
//                    case "*":
//                    case "/":
//                    case "or":
//                    case "and":
//                    case "ass":
//                    case "!F":
//                        operandsCount = 2;
//                        break;
//                    case "!":
//                    case "R":
//                    case "W":
//                    case "not":
//                        operandsCount = 1;
//                        break;
//                    default:
//                        throw new InternalInterpreterException("Invalid poliz operator " + internalProgramPresentation.getDelimiterTable().get(currentItem.getLexId()).getLexeme());
//                }
//
//                if (operandsCount == 0)
//                    throw new InternalInterpreterException("operator with 0 operands are not allowed");
//                else if (operandsCount == 1) {
//                    if (execStack.size() < 1)
//                        throw new InterpretationException("execution stack size < 1 for single-operator");
//                    PolizItem operand = execStack.pop();
//                    switch (internalProgramPresentation.getDelimiterTable().get(currentItem.getLexId()).getLexeme()){
//                        case "!":
//                            if (operand.getTableId() != InternalProgramPresentation.polizPointerTableId)
//                                throw new InterpretationException("goto can be applied to poliz pointer only");
//                            try {
//                                i = internalProgramPresentation.getPolizPointerTable().get(currentItem.getLexId()).getPolizIndex() - 1;
//                            } catch (NumberFormatException e){
//                                throw new InterpretationException("invalid poliz pointer value");
//                            }
//                            break;
//                        case "R":
//                            if (operand.getTableId() == InternalProgramPresentation.polizPointerTableId)
//                                throw new InterpretationException("R can not be applied to poliz pointer");
//                            break;
//                        case "W":
//                            if (operand.getTableId() == InternalProgramPresentation.polizPointerTableId)
//                                throw new InterpretationException("W can not be applied to poliz pointer");
//                            System.out.println(operand);
//                            break;
//                        case "not":
//                            if (operand.getTableId() != InternalProgramPresentation.identifierTableId ||
//                                    !(operand.getTableId() == InternalProgramPresentation.serviceTableId &&
//                                            (
//                                                    internalProgramPresentation.getServiceTable().get(operand.getLexId()).getLexeme().equals("true") ||
//                                                            internalProgramPresentation.getServiceTable().get(operand.getLexId()).getLexeme().equals("false")
//                                            )
//                                    )
//                            )
//                                throw new InterpretationException("'not' can be applied to identifier or logical constant only");
//                            break;
//                        default:
//                            throw new InternalInterpreterException("Invalid poliz operator " + internalProgramPresentation.getDelimiterTable().get(currentItem.getLexId()).getLexeme());
//                    }
//                } else if (operandsCount == 2) {
//                    if (execStack.size() < 2)
//                        throw new InterpretationException("execution stack size < 2 for single-operator");
//                    PolizItem operand2 = execStack.pop();
//                    PolizItem operand1 = execStack.pop();
//                    switch (internalProgramPresentation.getDelimiterTable().get(currentItem.getLexId()).getLexeme()){
//                        case ">": break;
//                        case "<": break;
//                        case "<>": break;
//                        case ">=": break;
//                        case "<=": break;
//                        case "=": break;
//                        case "+": break;
//                        case "-": break;
//                        case "*": break;
//                        case "/": break;
//                        case "or": break;
//                        case "and": break;
//                        case "ass": break;
//                        case "!F": break;
//                        default:
//                            throw new InternalInterpreterException("Invalid poliz operator " + internalProgramPresentation.getDelimiterTable().get(currentItem.getLexId()).getLexeme());
//                    }
//                }
//                else {
//                    throw new InternalInterpreterException("operator with more then 2 operands are not allowed");
//                }
//            }
//            else {
//                execStack.push(currentItem);
//            }
//        }
    }
}
