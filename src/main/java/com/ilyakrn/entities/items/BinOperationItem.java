package com.ilyakrn.entities.items;

public class BinOperationItem {
    private final String operation;
    private final Type operand1;
    private final Type operand2;
    private final Type result;

    public BinOperationItem(String operation, Type operand1, Type operand2, Type result) {
        this.operation = operation;
        this.operand1 = operand1;
        this.operand2 = operand2;
        this.result = result;
    }

    public String getOperation() {
        return operation;
    }

    public Type getOperand1() {
        return operand1;
    }

    public Type getOperand2() {
        return operand2;
    }

    public Type getResult() {
        return result;
    }
}
