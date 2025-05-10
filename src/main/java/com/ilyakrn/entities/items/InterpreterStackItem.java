package com.ilyakrn.entities.items;

public class InterpreterStackItem {
    private final String value;
    private final Type type;
    private final String memoryAddress;

    public InterpreterStackItem(String value, Type type, String memoryAddress) {
        this.value = value;
        this.type = type;
        this.memoryAddress = memoryAddress;
    }

    public String getValue() {
        return value;
    }

    public Type getType() {
        return type;
    }

    public String getMemoryAddress() {
        return memoryAddress;
    }
}
