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

public class Main {
    public static void main(String[] args) throws Exception {
        String prog = new String(Files.readAllBytes(Paths.get("/home/ilyakrn/IdeaProjects/ModelLangCompiler/main.prog")), StandardCharsets.UTF_8);

        Lexer lexer = new Lexer();
        Parser parser = new Parser();

        try {
            InternalProgramPresentation internalProgramPresentation = lexer.analyze(prog);
            System.out.println("LEXER COMPLETE SUCCESSFUL");

            parser.analyze(internalProgramPresentation);
            System.out.println("PARSER COMPLETE SUCCESSFUL");

        } catch (CompilerInternalException e){
            System.out.println("COMPILER INTERNAL ERROR:\n" + e.getMessage());
        } catch (CompilerExternalException e){
            System.out.println("COMPILATION FAILED:\n" + e.getMessage());
        } catch (Exception e){
            System.out.println("COMPILER FATAL ERROR:\n" + e.getMessage());
            e.printStackTrace();
        }
    }
}