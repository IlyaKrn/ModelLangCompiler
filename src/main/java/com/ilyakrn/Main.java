package com.ilyakrn;

import com.ilyakrn.lexer.Lexer;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws Exception {
        String prog = "125756h;for--41o ";

        Lexer lexer = new Lexer();
        System.out.println("==================================");
        lexer.analyze(prog);
        System.out.println("==================================");

    }
}