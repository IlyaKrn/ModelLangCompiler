package com.ilyakrn;

import com.ilyakrn.lexer.Lexer;
import com.ilyakrn.lexer.LexerOutput;
import com.ilyakrn.parser.Parser;

import java.io.File;
import java.io.FileReader;
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
        LexerOutput lexerOutput = lexer.analyze(prog);
        parser.analyze(lexerOutput);

    }
}