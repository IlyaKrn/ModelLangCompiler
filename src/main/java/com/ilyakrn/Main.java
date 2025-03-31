package com.ilyakrn;

import com.ilyakrn.lexer.Lexer;
import com.ilyakrn.lexer.LexerOutput;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws Exception {
        String prog = "a125756h\nfor--41o ";

        Lexer lexer = new Lexer();
        System.out.println("==================================");
        LexerOutput output = lexer.analyze(prog);
        System.out.println(output);
        System.out.println("==================================");

    }
}