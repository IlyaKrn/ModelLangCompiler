package com.ilyakrn;

import com.ilyakrn.exceptions.external.CompilerExternalException;
import com.ilyakrn.exceptions.internal.CompilerInternalException;
import com.ilyakrn.exceptions.internal.InternalLexerException;
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
        String prog = new String(Files.readAllBytes(Paths.get("/home/ilyakrn/IdeaProjects/ModelLangCompiler/main.prog")), StandardCharsets.UTF_8);

        Lexer lexer = new Lexer();
        Parser parser = new Parser();

        InternalProgramPresentation internalProgramPresentation = null;
        try {
            System.out.println("==================COMPILER==================");
            internalProgramPresentation = lexer.analyze(prog);
            System.out.println("LEXER COMPLETE SUCCESSFUL");

            internalProgramPresentation = parser.analyze(internalProgramPresentation);
            System.out.println("PARSER COMPLETE SUCCESSFUL");

            for (int i = 0; i < internalProgramPresentation.getPolizTable().size(); i++) {
                System.out.println(i + "\t" + internalProgramPresentation.getPolizTable().get(i).getLexeme());
            }

//            System.out.println(internalProgramPresentation);

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