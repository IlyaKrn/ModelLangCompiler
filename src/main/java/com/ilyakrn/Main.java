package com.ilyakrn;

import com.ilyakrn.exceptions.external.CompilerExternalException;
import com.ilyakrn.exceptions.internal.CompilerInternalException;
import com.ilyakrn.exceptions.internal.InternalLexerException;
import com.ilyakrn.interpreter.Interpreter;
import com.ilyakrn.lexer.Lexer;
import com.ilyakrn.entities.InternalProgramPresentation;
import com.ilyakrn.parser.Parser;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws Exception {
        String filePath = "main.prog";
        String compileFlag = "-i";
        if (args.length > 0) {
            switch (args[0]){
                case "-i":
                    compileFlag = "-i";
                    break;
                case "-l":
                    compileFlag = "-l";
                    break;
                case "-p":
                    compileFlag = "-p";
                    break;
                default:
                    System.err.println("Invalid compiler flag '" + args[0] + "'\nallowed flags:\n'-i' - compile and interpret\n'-l' - print compiled program presentation\n'-p' - print POLIZ of program presentation\nalso you can set file to compile as second argument");
                    return;

            }
        }
        if (args.length > 1) {
            filePath = args[1];
        }
        String prog = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);

        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        Interpreter interpreter = new Interpreter();

        InternalProgramPresentation internalProgramPresentation = null;
        try {
            System.out.println("==================COMPILER==================");
            internalProgramPresentation = lexer.analyze(prog);
            System.out.println("LEXER COMPLETE SUCCESSFUL");

            internalProgramPresentation = parser.analyze(internalProgramPresentation);
            System.out.println("PARSER COMPLETE SUCCESSFUL");

            if (compileFlag.equals("-l")) {
                System.out.println(internalProgramPresentation);
            }
            if (compileFlag.equals("-p")) {
                System.out.println("===================POLIZ===================");
                for (int i = 0; i < internalProgramPresentation.getPolizTable().size(); i++) {
                    String lexemeName = "";
                    String tableName = "";
                    switch (internalProgramPresentation.getPolizTable().get(i).getTableId()){
                        case InternalProgramPresentation.serviceTableId:
                            lexemeName = internalProgramPresentation.getServiceTable().get(internalProgramPresentation.getPolizTable().get(i).getLexId()).getLexeme();
                            tableName = "serv";
                            break;
                        case InternalProgramPresentation.delimiterTableId:
                            lexemeName = internalProgramPresentation.getDelimiterTable().get(internalProgramPresentation.getPolizTable().get(i).getLexId()).getLexeme();
                            tableName = "delim";
                            break;
                        case InternalProgramPresentation.identifierTableId:
                            lexemeName = internalProgramPresentation.getIdentifierTable().get(internalProgramPresentation.getPolizTable().get(i).getLexId()).getLexeme();
                            tableName = "ident";
                            break;
                        case InternalProgramPresentation.numberTableId:
                            lexemeName = internalProgramPresentation.getNumberTable().get(internalProgramPresentation.getPolizTable().get(i).getLexId()).getLexeme();
                            tableName = "num ";
                            break;
                        case InternalProgramPresentation.polizPointerTableId:
                            lexemeName = String.valueOf(internalProgramPresentation.getPolizPointerTable().get(internalProgramPresentation.getPolizTable().get(i).getLexId()).getPolizIndex());
                            tableName = "point";
                            break;
                    }
                    System.out.println(String.format("%s\t%s\t\t%s", i, tableName, lexemeName));
                }
            }
            if (compileFlag.equals("-i")) {
                System.out.println("INTERPRETING PROGRAM...");
                interpreter.interpret(internalProgramPresentation);
                System.out.println("INTERPRETATION COMPLETE SUCCESSFUL");
            }


            System.out.println("====================END=====================");
        } catch (CompilerExternalException e){
            System.out.println("COMPILATION FAILED:\n" + e.getMessage());
            System.out.println("====================END=====================");
        } catch (CompilerInternalException e){
            System.out.println("COMPILER INTERNAL ERROR:\n" + e.getMessage());
            System.out.println("====================END=====================");
        } catch (Exception e){
            System.out.println("COMPILER FATAL ERROR:\n" + e.getMessage());
            System.out.println("====================END=====================");
            e.printStackTrace();
        }
    }
}