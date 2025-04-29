package com.ilyakrn.interpreter;

import com.ilyakrn.entities.InternalProgramPresentation;
import com.ilyakrn.entities.items.PolizItem;
import com.ilyakrn.exceptions.external.InterpretationException;
import com.ilyakrn.exceptions.internal.InternalInterpreterException;

import java.util.ArrayList;
import java.util.Stack;

public class Interpreter {

    public void interpret(InternalProgramPresentation internalProgramPresentation) {
        System.out.println(internalProgramPresentation);
//        Stack<PolizItem> execStack = new Stack<>();
//        for (int i = 0; i < internalProgramPresentation.getPolizTable().size(); i++) {
//            PolizItem currentItem = internalProgramPresentation.getPolizTable().get(i);
//            if (i == internalProgramPresentation.getPolizTable().size() - 1){
//                if (!execStack.isEmpty())
//                    throw new InternalInterpreterException("execution stack is not empty int the end of program");
//                if (!currentItem.getLexeme().equals("}"))
//                    throw new InterpretationException("program must be terminated by '}'");
//            }
//
//            if (currentItem.isOperator()){
//                if (execStack.isEmpty())
//                    throw new InternalInterpreterException("execution stack is empty");
//
//                int operandsCount = 0;
//                switch (currentItem.getLexeme()){
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
//                        throw new InternalInterpreterException("Invalid operator " + currentItem.getLexeme());
//                }
//
//                if (operandsCount == 0)
//                    throw new InternalInterpreterException("operator with 0 operands are not allowed");
//                else if (operandsCount == 1) {
//
//                    switch (currentItem.getLexeme()){
//                        case "!": break;
//                        case "R": break;
//                        case "W": break;
//                        case "not": break;
//                        default:
//                            throw new InternalInterpreterException("Invalid operator " + currentItem.getLexeme());
//                    }
//                } else if (operandsCount == 2) {
//                    switch (currentItem.getLexeme()){
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
//                            throw new InternalInterpreterException("Invalid operator " + currentItem.getLexeme());
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
