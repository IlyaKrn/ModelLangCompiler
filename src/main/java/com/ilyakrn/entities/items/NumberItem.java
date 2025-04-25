package com.ilyakrn.entities.items;

public class NumberItem {
    private String lexeme;
    private Type type;

    public String getLexeme() {
        return lexeme;
    }

    public void setLexeme(String lexeme) {
        this.lexeme = lexeme;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public NumberItem(String lexeme, Type type) {
        this.lexeme = lexeme;
        this.type = type;
    }
}
