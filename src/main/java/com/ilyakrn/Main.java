package com.ilyakrn;

import com.ilyakrn.lexer.Lexer;
import com.ilyakrn.entities.InternalProgramPresentation;
import com.ilyakrn.parser.Parser;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws Exception {
        String prog = new String(Files.readAllBytes(Paths.get("/home/ilyakrn/IdeaProjects/ModelLangCompiler/main.prog")), StandardCharsets.UTF_8);

        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        InternalProgramPresentation internalProgramPresentation = lexer.analyze(prog);
        if(internalProgramPresentation.isError()){
            System.out.println("LEXER COMPLETE FAILED: " + internalProgramPresentation.getMessage());
            return;
        }
        System.out.println("LEXER COMPLETE SUCCESSFUL");

        boolean parserOutput = parser.analyze(internalProgramPresentation);
        if(!parserOutput){
            System.out.println("PARSER FAILED: NO MESSAGE");
            return;
        }
        System.out.println("PARSER COMPLETE SUCCESSFUL");

    }
}