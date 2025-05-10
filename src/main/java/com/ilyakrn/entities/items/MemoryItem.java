package com.ilyakrn.entities.items;

public class MemoryItem {
    private final Type type;
    String value;

    public MemoryItem(Type type, String value) {
        this.type = type;
        this.value = value;
    }

    public Type getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
