package com.ilyakrn;

import com.ilyakrn.lexer.Lexer;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws Exception {
        String prog = "";

        Lexer lexer = new Lexer();
        lexer.analyze(prog);

    }
}