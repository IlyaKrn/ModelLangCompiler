package com.ilyakrn.entities.items;

public class NumberItem {
    private final String lexeme;
    private Type type;
    private final int dimensionCount;

    public NumberItem(String lexeme, int dimensionCount, Type type) {
        this.lexeme = lexeme;
        this.dimensionCount = dimensionCount;
        this.type = type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getLexeme() {
        return lexeme;
    }

    public Type getType() {
        return type;
    }

    public int getDimensionCount() {
        return dimensionCount;
    }
}
